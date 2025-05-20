package pro.danyloplaksyvyi.nytimesapp.features.navigation.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pro.danyloplaksyvyi.nytimesapp.features.main.presentation.viewmodel.booksbylist.BooksByListViewModel
import pro.danyloplaksyvyi.nytimesapp.features.main.presentation.viewmodel.overview.OverviewViewModel

import pro.danyloplaksyvyi.nytimesapp.features.signin.presentation.view.SplashScreen
import pro.danyloplaksyvyi.nytimesapp.features.signin.presentation.viewmodel.AuthViewModel

@Composable
fun RootNavigationGraph(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    overviewViewModel: OverviewViewModel,
    booksByListViewModel: BooksByListViewModel
) {
    val rootNavController = rememberNavController()
    NavHost(
//        modifier = modifier,
        route = Graph.ROOT,
        startDestination = Graph.SPLASH,
        navController = rootNavController
    ) {
        composable(route = Graph.SPLASH) {
            SplashScreen(authViewModel, rootNavController)
        }
        authNavGraph(rootNavController, authViewModel)
        mainNavGraph(rootNavController, authViewModel, overviewViewModel, booksByListViewModel)
    }
}

object Graph {
    const val SPLASH = "splash_graph"
    const val ROOT = "root_graph"
    const val AUTH = "auth_graph"
    const val MAIN = "main_graph"
}