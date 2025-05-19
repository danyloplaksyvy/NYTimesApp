package pro.danyloplaksyvyi.nytimesapp.features.navigation.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import pro.danyloplaksyvyi.nytimesapp.features.main.presentation.view.CategoriesScreen
import pro.danyloplaksyvyi.nytimesapp.features.main.presentation.view.details.CategoryDetailsScreen
import pro.danyloplaksyvyi.nytimesapp.features.navigation.domain.model.Screens
import pro.danyloplaksyvyi.nytimesapp.features.signin.presentation.viewmodel.AuthViewModel

fun NavGraphBuilder.mainNavGraph(navController: NavController, authViewModel: AuthViewModel) {
    navigation(route = Graph.MAIN, startDestination = Screens.CategoriesScreen.name) {
        composable(route = Screens.CategoriesScreen.name) {
            CategoriesScreen(navController, authViewModel)
        }
        composable(route = Screens.CategoryDetailsScreen.name) {
            CategoryDetailsScreen {
                navController.navigate(Screens.CategoriesScreen.name)
            }
        }
    }
}