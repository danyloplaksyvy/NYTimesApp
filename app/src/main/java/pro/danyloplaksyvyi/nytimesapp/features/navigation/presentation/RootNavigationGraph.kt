package pro.danyloplaksyvyi.nytimesapp.features.navigation.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun RootNavigationGraph(modifier: Modifier = Modifier) {
    val rootNavController = rememberNavController()

    NavHost(modifier = modifier, route = Graph.ROOT, startDestination = Graph.AUTH, navController = rootNavController) {
        authNavGraph(rootNavController)
        mainNavGraph(rootNavController)
    }
}

object Graph {
    const val ROOT = "root_graph"
    const val AUTH = "auth_graph"
    const val MAIN = "main_graph"
}