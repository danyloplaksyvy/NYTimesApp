package pro.danyloplaksyvyi.nytimesapp.features.main.presentation.view.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import pro.danyloplaksyvyi.nytimesapp.R
import pro.danyloplaksyvyi.nytimesapp.features.signin.presentation.viewmodel.AuthViewModel

data class Book(
    val title: String,
    val author: String,
    val publisher: String,
    val rank: Int,
    val description: String,
    val buyUrl: String,
    val imageUrl: String?
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BooksByCategoryScreen(
    navController: NavController,
    listNameEncoded: String,
    authViewModel: AuthViewModel
) {
    // TODO: load from  VM
    val books = remember { sampleBooks() }
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { books.size })

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                ,
                title = {
                    Text(
                        stringResource(R.string.books_in_category, listNameEncoded),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            stringResource(R.string.back),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    Text(
                        "${pagerState.currentPage + 1}/${pagerState.pageCount}",
                        modifier = Modifier.padding(horizontal = 4.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent.copy(alpha = 0.4f),
                )
            )
        },
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(
                    start = paddingValues.calculateLeftPadding(LayoutDirection.Ltr),
                    end = paddingValues.calculateRightPadding(LayoutDirection.Ltr),
                    bottom = paddingValues.calculateBottomPadding()
                )
                .systemBarsPadding()
        ) {
            VerticalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                key = { it },
                flingBehavior = PagerDefaults.flingBehavior(state = pagerState)
            ) { page ->
                BookPagerItem(
                    book = books[page],
                    onBuyClick = { /* navController… */ }
                )
            }
        }
    }
}

@Composable
fun BookPagerItem(book: Book, onBuyClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Full-screen cover
        AsyncImage(
            model = book.imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Overlay gradient
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )

        // Bottom info card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = book.title,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "${book.author} • ${book.publisher}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onBuyClick) {
                        Text(stringResource(R.string.buy).uppercase())
                    }
                    Row {
                        IconButton(onClick = { /* favorite */ }) {
                            Icon(Icons.Filled.FavoriteBorder, "Like")
                        }
                        IconButton(onClick = { /* share */ }) {
                            Icon(Icons.Filled.Share, "Share")
                        }
                        IconButton(onClick = { /* bookmark */ }) {
                            Icon(Icons.Filled.Bookmark, "Bookmark")
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(64.dp))
        // Rank badge
        Box(
            modifier = Modifier
                .padding(vertical = 64.dp)
                .size(80.dp)
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .background(
                    color = if (book.rank == 1) MaterialTheme.colorScheme.secondary
                    else MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (book.rank == 1) {
                Icon(Icons.Filled.EmojiEvents, "Top Rank", tint = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text(
                    "#${book.rank}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

// Sample data helper
fun sampleBooks() = listOf(
    Book(
        title = "The Haunted Mansion",
        author = "Boo Author",
        publisher = "Spooky Press",
        rank = 1,
        description = "A thrilling ghost-hunting adventure across creaky corridors and shadowy rooms.",
        buyUrl = "https://…",
        imageUrl = "https://picsum.photos/600/400"
    ),
    Book(
        title = "Winds of Winter",
        author = "Icy Author",
        publisher = "Northern Tales",
        rank = 2,
        description = "A chilling saga set in the frost-bitten realms of the far North.",
        buyUrl = "https://…",
        imageUrl = "https://picsum.photos/600/400"
    ),
    Book(
        title = "Winds of Winter",
        author = "Icy Author",
        publisher = "Northern Tales",
        rank = 2,
        description = "A chilling saga set in the frost-bitten realms of the far North.",
        buyUrl = "https://…",
        imageUrl = "https://picsum.photos/600/400"
    ),
    Book(
        title = "Winds of Winter",
        author = "Icy Author",
        publisher = "Northern Tales",
        rank = 2,
        description = "A chilling saga set in the frost-bitten realms of the far North.",
        buyUrl = "https://…",
        imageUrl = "https://picsum.photos/600/400"
    )
)