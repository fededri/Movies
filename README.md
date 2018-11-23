# Movies


Ejemplo de una aplicacion que obtiene peliculas de una API publica (THE MOVIE DB)
La arquitectura es MVVM, con Dagger2 como DI y Room como base de datos.

Se divide en el activity principal, su viewmodel (MainViewModel) y el model (Movies repository) que ademas se sirve de MoviesApiModel

El objetivo del activity es cambiar la vista en base a las actualizaciones del estado.
El objetivo del ViewModel es simplemente recibir inputs y realizar transformaciones de estado obteniendo datos del repositorio y agregando estos ultimos al estado
MoviesRepository es el modelo encargado de brindar los datos que requiere la aplicacion, se encarga de obtenerlos tanto desde la web como de la base de datos local,
MoviesApiModel implementa las requests a la api de TMDB, los datos recibidos son transformados antes de ser presentados en la aplicacion por otros mas utiles.

Se busca seguir el principio de responsabilidad unica definiendo una unica responsabilidad a cada objeto y encapsulando los detalles de implementacion.
Ademas se busca un codigo limpio evitando las repeticiones de codigo, delegando  responsabilidades y teniendo en cuenta posibles memory leaks y cambios de configuracion como rotaciones de pantalla. Por ejemplo en caso de una rotacion el estado del activity no se pierde porque es guardado por el ViewModel que no es destruido, cuando el activity sea recreado recibira la misma instancia del ViewModel

La api de TMDB pide una numero de lista (si no se envia devuelve un error), asi que esta hardcodeado con el numero de lista 1. A su vez todos los videos devueltos por esta api estan en "false" por lo que no se han podido agregar

Le falta a la app manejar el paginado e implementacion de algunos tests
