package pro.danyloplaksyvyi.nytimesapp.features.navigation.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import pro.danyloplaksyvyi.nytimesapp.features.main.presentation.view.OverviewScreen
import pro.danyloplaksyvyi.nytimesapp.features.main.presentation.view.details.BooksByCategoryScreen
import pro.danyloplaksyvyi.nytimesapp.features.main.presentation.viewmodel.booksbylist.BooksByListViewModel
import pro.danyloplaksyvyi.nytimesapp.features.main.presentation.viewmodel.overview.OverviewViewModel
import pro.danyloplaksyvyi.nytimesapp.features.navigation.domain.model.Screens
import pro.danyloplaksyvyi.nytimesapp.features.signin.presentation.viewmodel.AuthViewModel

fun NavGraphBuilder.mainNavGraph(
    navController: NavController,
    authViewModel: AuthViewModel,
    overviewViewModel: OverviewViewModel,
    booksByListViewModel: BooksByListViewModel
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
        composable(route = "${Screens.CategoryDetailsScreen.name}/{encoded_name}",
            arguments = listOf(
                navArgument("encoded_name") { type = NavType.StringType }
            )) { navBackStackEntry ->
            val encodedName = navBackStackEntry.arguments?.getString("encoded_name") ?: ""
            BooksByCategoryScreen(navController, encodedName, booksByListViewModel)
        }
    }
}