package pro.danyloplaksyvyi.nytimesapp.features.navigation.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import pro.danyloplaksyvyi.nytimesapp.features.main.presentation.view.OverviewScreen
import pro.danyloplaksyvyi.nytimesapp.features.main.presentation.view.details.BooksByCategoryScreen
import pro.danyloplaksyvyi.nytimesapp.features.main.presentation.viewmodel.OverviewViewModel
import pro.danyloplaksyvyi.nytimesapp.features.navigation.domain.model.Screens
import pro.danyloplaksyvyi.nytimesapp.features.signin.presentation.viewmodel.AuthViewModel

fun NavGraphBuilder.mainNavGraph(
    navController: NavController,
    authViewModel: AuthViewModel,
    overviewViewModel: OverviewViewModel
) {
    navigation(route = Graph.MAIN, startDestination = Screens.CategoriesScreen.name) {
        composable(route = Screens.CategoriesScreen.name) {
            OverviewScreen(
                navController = navController,
                onCategoryClick = { name -> navController.navigate("${Screens.CategoryDetailsScreen.name}/$name") },
                authViewModel,
                overviewViewModel
            )
        }
        composable(route = "${Screens.CategoryDetailsScreen.name}/{encodedName}",
            arguments = listOf(
                navArgument("encodedName") { type = NavType.StringType }
            )) {
            BooksByCategoryScreen(navController, "", authViewModel)
        }
    }
}