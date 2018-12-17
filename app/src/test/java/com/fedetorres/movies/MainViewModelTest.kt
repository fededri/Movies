package com.fedetorres.movies

import android.app.Application
import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.res.Resources
import com.fedetorres.movies.database.MovieDao
import com.fedetorres.movies.database.entities.Movie
import com.fedetorres.movies.main.*
import com.fedetorres.movies.network.ApiErrorParser
import com.fedetorres.movies.network.MoviesApi
import com.fedetorres.movies.network.responses.CreatedBy
import com.fedetorres.movies.network.responses.GetMoviesResponse
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.*

class MainViewModelTest {

    @get:Rule
    var instantExecuterRule = InstantTaskExecutorRule()
    @Mock
    lateinit var movieDao: MovieDao


    private lateinit var moviesApiModel: MoviesApiModel


    @Mock
    lateinit var observer: Observer<MainState?>


    @Mock
    private lateinit var moviesRepository: MoviesRepository

    @Mock
    private lateinit var context: Application


    private lateinit var moviesViewModel: MainViewModel


    @Mock
    private lateinit var lifecycleOwner: LifecycleOwner

    @Mock
    private lateinit var lifecycle: Lifecycle

    @Mock
    private lateinit var apiErrorParser: ApiErrorParser

    var currentState: MainState = MainState()
    val movies = mutableListOf<Movie>()

    @Before
    fun setupMoviesViewModel() {
        MockitoAnnotations.initMocks(this)

        val movie1 = Movie()
        movie1.id = 1
        movie1.adult = false
        movie1.title = "Harry Potter"
        movie1.overview = "A great film"

        movies.add(movie1)

        setupContext()
        setupMovieDao()

        moviesViewModel = MainViewModel(moviesRepository, apiErrorParser, TestScheduler(), TestScheduler())
        moviesViewModel.getData().observeForever(observer)
    }

    private fun setupContext() {
        Mockito.`when`<Context>(context.applicationContext).thenReturn(context)

        Mockito.`when`(context.resources).thenReturn(Mockito.mock(Resources::class.java))

        whenever(lifecycle.currentState).thenReturn(Lifecycle.State.RESUMED)
        whenever(lifecycleOwner.lifecycle).thenReturn(lifecycle)

    }


    fun setupMovieDao() {
        whenever(movieDao.getMovies()).thenReturn(Single.just(listOf<Movie>()))
        whenever(movieDao.getMovie(any())).thenReturn(Movie())
    }

    private inline fun <reified T> lambdaMock(): T = Mockito.mock(T::class.java)

    @Test
    fun categoryClick_LoadingAndCategory() {
        doReturn(Single.just(movies)).`when`(moviesRepository).getMovies(anyOrNull())


        moviesViewModel.onCategoryClick(CATEGORY.POPULAR)


        verify(observer).onChanged(MainState(selectedCategory = CATEGORY.POPULAR, loading = true))
        verify(moviesRepository).getMovies(anyOrNull())

    }

    @Test
    fun goBackFromMovies_ShowButtons() {

        val state = moviesViewModel.getData().value

        moviesViewModel.postState(state?.copy(movies = movies, selectedCategory = CATEGORY.POPULAR))
        assert(moviesViewModel.getData().value?.movies != null)
        moviesViewModel.goBack()

        assert(moviesViewModel.getData().value?.movies == null)
        assert(moviesViewModel.getData().value?.loading == false)
        assert(moviesViewModel.getData().value?.selectedCategory == null)
    }

    @Test
    fun searchInput_ChangesKeywordAndSearchDb() {
        doReturn(Observable.just(movies)).`when`(moviesRepository).getMoviesFromDb()

        with(moviesViewModel) {
            val state = getData().value
            postState(state?.copy(keyword = null, loading = false))

            search("avengers")
            assert(getData().value?.keyword == "avengers")
            assert(getData().value?.loading == true)
            verify(moviesRepository).getMoviesFromDb()
        }

    }


}