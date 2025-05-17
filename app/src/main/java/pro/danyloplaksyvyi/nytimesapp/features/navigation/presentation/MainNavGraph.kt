package pro.danyloplaksyvyi.nytimesapp.features.navigation.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import pro.danyloplaksyvyi.nytimesapp.features.main.presentation.view.CategoriesScreen
import pro.danyloplaksyvyi.nytimesapp.features.main.presentation.view.details.CategoryDetailsScreen
import pro.danyloplaksyvyi.nytimesapp.features.navigation.domain.Screens

fun NavGraphBuilder.mainNavGraph(navController: NavController) {
    navigation(route = Graph.MAIN, startDestination = Screens.CategoriesScreen.name) {
        composable(route = Screens.CategoriesScreen.name) {
            CategoriesScreen(onSignOutClick = {navController.navigate(Graph.AUTH)}, onCategoryClick =  {
                navController.navigate(Screens.CategoryDetailsScreen.name)
            })
        }
        composable(route = Screens.CategoryDetailsScreen.name) {
            CategoryDetailsScreen {
                navController.navigate(Screens.CategoriesScreen.name)
            }
        }
    }
}