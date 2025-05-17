package pro.danyloplaksyvyi.nytimesapp.features.navigation.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import pro.danyloplaksyvyi.nytimesapp.features.navigation.domain.Screens
import pro.danyloplaksyvyi.nytimesapp.features.signin.presentation.SignInScreen

fun NavGraphBuilder.authNavGraph(
    navController: NavController,
) {
    navigation(route = Graph.AUTH, startDestination = Screens.SignInScreen.name) {
        composable(route = Screens.SignInScreen.name) {
            SignInScreen {
                navController.navigate(Graph.MAIN) {
                    popUpTo(Graph.AUTH) { inclusive = true }
                }
            }
        }
    }
}