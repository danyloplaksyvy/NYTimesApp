package pro.danyloplaksyvyi.nytimesapp.features.main.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pro.danyloplaksyvyi.nytimesapp.R
import pro.danyloplaksyvyi.nytimesapp.features.main.domain.model.overview.BookList
import pro.danyloplaksyvyi.nytimesapp.features.main.domain.model.overview.Results
import pro.danyloplaksyvyi.nytimesapp.features.main.presentation.viewmodel.overview.OverviewUiState
import pro.danyloplaksyvyi.nytimesapp.features.main.presentation.viewmodel.overview.OverviewViewModel
import pro.danyloplaksyvyi.nytimesapp.features.navigation.presentation.Graph
import pro.danyloplaksyvyi.nytimesapp.features.signin.domain.model.SignInState
import pro.danyloplaksyvyi.nytimesapp.features.signin.presentation.viewmodel.AuthViewModel
import pro.danyloplaksyvyi.nytimesapp.utils.RetryButton
import pro.danyloplaksyvyi.nytimesapp.utils.formatReadableDate
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverviewScreen(
    navController: NavController,
    onCategoryClick: (String, String) -> Unit,
    authViewModel: AuthViewModel,
    overviewViewModel: OverviewViewModel,
) {
    val signInState by authViewModel.signInState.collectAsState()
    val uiState by overviewViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
    // Load Overview
    LaunchedEffect(Unit) {
        overviewViewModel.loadOverview(today)
    }
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

    LaunchedEffect(uiState) {
        if (uiState is OverviewUiState.Error) {
            snackbarHostState.showSnackbar((uiState as OverviewUiState.Error).message)
        }
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
                        Icon(
                            Icons.AutoMirrored.Filled.Logout,
                            "Logout",
                            tint = MaterialTheme.colorScheme.primary
                        )
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
        when (uiState) {
            is OverviewUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is OverviewUiState.Success -> {
                val successState = uiState as OverviewUiState.Success
                OverviewList(
                    modifier = Modifier.padding(padding),
                    results = successState.results,
                    onCategoryClick = { name -> onCategoryClick(name, successState.results.published_date) })
            }

            is OverviewUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                    RetryButton { overviewViewModel.loadOverview(today) }
                }
            }
        }
    }
}

@Composable
fun OverviewList(
    modifier: Modifier = Modifier,
    results: Results,
    onCategoryClick: (String) -> Unit
) {
    val bookLists = results.lists
    Column(
        modifier
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(
                R.string.current_lists_as_of,
                formatReadableDate(results.published_date)
            ),
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
            items(bookLists) { category ->
                CategoryGridItem(category) {
                    onCategoryClick(category.list_name_encoded)
                }
            }
        }
    }
}

@Composable
fun CategoryGridItem(book: BookList, onClick: () -> Unit) {
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
                imageVector = Icons.Filled.CollectionsBookmark,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )

            // Title & date
            Column {
                Text(
                    text = book.list_name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Go to",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}