package pro.danyloplaksyvyi.nytimesapp.features.navigation.presentation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ComponentActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.android.ext.android.inject
import pro.danyloplaksyvyi.nytimesapp.features.main.presentation.viewmodel.OverviewViewModel

import pro.danyloplaksyvyi.nytimesapp.features.signin.presentation.view.SplashScreen
import pro.danyloplaksyvyi.nytimesapp.features.signin.presentation.viewmodel.AuthViewModel

@Composable
fun RootNavigationGraph(modifier: Modifier = Modifier, authViewModel: AuthViewModel, overviewViewModel: OverviewViewModel) {
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
        mainNavGraph(rootNavController, authViewModel, overviewViewModel)
    }
}

object Graph {
    const val SPLASH = "splash_graph"
    const val ROOT = "root_graph"
    const val AUTH = "auth_graph"
    const val MAIN = "main_graph"
}