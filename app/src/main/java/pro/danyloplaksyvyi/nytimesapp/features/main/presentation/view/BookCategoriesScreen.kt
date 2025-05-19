package pro.danyloplaksyvyi.nytimesapp.features.main.presentation.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pro.danyloplaksyvyi.nytimesapp.R
import pro.danyloplaksyvyi.nytimesapp.features.navigation.domain.model.Screens
import pro.danyloplaksyvyi.nytimesapp.features.navigation.presentation.Graph
import pro.danyloplaksyvyi.nytimesapp.features.signin.presentation.model.SignInState
import pro.danyloplaksyvyi.nytimesapp.features.signin.presentation.viewmodel.AuthViewModel
import pro.danyloplaksyvyi.nytimesapp.utils.formatReadableDate

data class Category(
    val displayName: String,
    val listNameEncoded: String,
    val publishedDate: String
)

data class CategoriesScreenState(
    val publishedDate: String,
    val categories: List<Category>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookCategoriesScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val signInState by authViewModel.signInState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle sign-in state
    LaunchedEffect(signInState) {
        when (signInState) {
            is SignInState.Error -> snackbarHostState.showSnackbar((signInState as SignInState.Error).message)
            is SignInState.Idle -> navController.navigate(Graph.AUTH) {
                popUpTo(Graph.MAIN) {
                    inclusive = true
                }
            }

            else -> {}
        }
    }

    // TODO: replace with real ViewModel state
    val screenState = remember {
        CategoriesScreenState(
            publishedDate = "2025-05-14",
            categories = listOf(
                Category("Hardcover Fiction", "hardcover-fiction", "2025-05-14"),
                Category(
                    "Combined Print & E-Book Fiction",
                    "combined-print-and-e-book-fiction",
                    "2025-05-14"
                ),
                Category("Paperback Nonfiction", "paperback-nonfiction", "2025-05-14"),
                Category("Advice How-to & Misc.", "advice-how-to-and-miscellaneous", "2025-05-14"),
                Category("Hardcover Fiction", "hardcover-fiction", "2025-05-14"),
                Category(
                    "Combined Print & E-Book Fiction",
                    "combined-print-and-e-book-fiction",
                    "2025-05-14"
                ),
                Category("Paperback Nonfiction", "paperback-nonfiction", "2025-05-14"),
                Category("Advice How-to & Misc.", "advice-how-to-and-miscellaneous", "2025-05-14"),
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.book_categories)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                actions = {
                    IconButton(onClick = {
                        authViewModel.signOut()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.Logout, "Logout", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                    modifier = Modifier.padding(16.dp)
                ) { Text(data.visuals.message) }
            }
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text(
                text = stringResource(R.string.current_lists_as_of,  formatReadableDate(screenState.publishedDate)),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(screenState.categories) { category ->
                    CategoryGridItem(category) {
                        navController.navigate("${Screens.CategoryDetailsScreen.name}/${category.listNameEncoded}")
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryGridItem(category: Category, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .aspectRatio(1f) // make it square
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top icon
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )

            // Title & date
            Column {
                Text(
                    text = category.displayName,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = formatReadableDate(category.publishedDate),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Chevron for affordance
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Go to ${category.displayName}",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}