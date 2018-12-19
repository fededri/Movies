package com.fedetorres.movies

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.fedetorres.movies.main.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import com.fedetorres.movies.dagger.ContextModule
import com.fedetorres.movies.dagger.DaggerAppComponent
import com.fedetorres.movies.dagger.MoviesModule
import com.fedetorres.movies.dagger.RoomModule
import com.fedetorres.movies.main.CATEGORY
import com.fedetorres.movies.main.MainState
import org.hamcrest.CoreMatchers.not
import org.junit.Before

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {


    @get:Rule
    var activityRule = android.support.test.rule.ActivityTestRule(MainActivity::class.java)


    @Test
    fun click_btPopular_ShowsRecyclerView() {
        activityRule.activity.viewModel.onCategoryClick(CATEGORY.POPULAR)
        onView(withId(R.id.bt_popular)).check(matches(not(isDisplayed())))
        onView(withId(R.id.bt_top_rated)).check(matches(not(isDisplayed())))
        onView(withId(R.id.bt_upcoming)).check(matches(not(isDisplayed())))
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
    }
}