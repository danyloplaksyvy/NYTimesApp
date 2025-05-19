package pro.danyloplaksyvyi.nytimesapp.features.navigation.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import pro.danyloplaksyvyi.nytimesapp.features.navigation.domain.model.Screens
import pro.danyloplaksyvyi.nytimesapp.features.signin.presentation.view.SignInScreen
import pro.danyloplaksyvyi.nytimesapp.features.signin.presentation.viewmodel.AuthViewModel

fun NavGraphBuilder.authNavGraph(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    navigation(route = Graph.AUTH, startDestination = Screens.SignInScreen.name) {
        composable(route = Screens.SignInScreen.name) {
            SignInScreen(navController, authViewModel)
        }
    }
}