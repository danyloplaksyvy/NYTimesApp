package pro.danyloplaksyvyi.nytimesapp.features.main.presentation.view.details

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import pro.danyloplaksyvyi.nytimesapp.R
import pro.danyloplaksyvyi.nytimesapp.features.main.domain.model.booksbylist.ListResults
import pro.danyloplaksyvyi.nytimesapp.features.main.domain.model.overview.Book
import pro.danyloplaksyvyi.nytimesapp.features.main.presentation.viewmodel.booksbylist.BookListUiState
import pro.danyloplaksyvyi.nytimesapp.features.main.presentation.viewmodel.booksbylist.BooksByListViewModel
import pro.danyloplaksyvyi.nytimesapp.ui.theme.SurfaceLight
import pro.danyloplaksyvyi.nytimesapp.utils.RetryButton
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun BooksByCategoryScreen(
    navController: NavController,
    listNameEncoded: String,
    booksByListViewModel: BooksByListViewModel
) {
    val uiState by booksByListViewModel.uiState.collectAsState()
    val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
    LaunchedEffect(today, listNameEncoded) {
        booksByListViewModel.loadList(today, listNameEncoded)
    }

    when (uiState) {
        is BookListUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is BookListUiState.Success -> {
            val results = (uiState as BookListUiState.Success).data
            val pagerState = rememberPagerState(initialPage = 0, pageCount = { results.books.size })
            BookByCategoryPage(pagerState, navController, results)
        }

        is BookListUiState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                RetryButton { booksByListViewModel.loadList(today, listNameEncoded) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookByCategoryPage(pagerState: PagerState, navController: NavController, results: ListResults) {
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                title = {
                    Text(
                        stringResource(R.string.books_in_category, results.list_name),
                        color = SurfaceLight,
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            stringResource(R.string.back),
                            tint = SurfaceLight
                        )
                    }
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
                    book = results.books[page]
                )
            }
        }
    }
}

@Composable
fun BookPagerItem(book: Book) {
    val context = LocalContext.current
    // Build once
    val customTabsIntent = remember {
        CustomTabsIntent.Builder()
            .build()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Blurred background
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(book.book_image)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .blur(20.dp)
        )

        // Foreground image
        AsyncImage(
            model = book.book_image,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        // Gradient overlay
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

        // Info Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${book.author} • ${book.publisher}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Expandable Description
                book.description?.takeIf { it.isNotBlank() }?.let { description ->
                    ExpandableText(text = description)
                }

                // Actions
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = {
                        customTabsIntent.launchUrl(context, Uri.parse(book.amazon_product_url))
                    }) {
                        Text(stringResource(R.string.buy))
                    }

                }
            }
        }

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
                Icon(
                    Icons.Filled.EmojiEvents,
                    "Top Rank",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
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

@Composable
fun ExpandableText(
    text: String,
    modifier: Modifier = Modifier,
    collapsedMaxLines: Int = 3,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall
) {
    var isExpanded by remember { mutableStateOf(false) }
    var fullLineCount by remember { mutableIntStateOf(0) }

    // Measure full text (invisible) to get real line count
    Text(
        text = text,
        style = textStyle,
        onTextLayout = { fullLineCount = it.lineCount },
        maxLines = Int.MAX_VALUE,
        overflow = TextOverflow.Clip,
        modifier = Modifier
            .fillMaxWidth()
            .alpha(0f)
            .height(0.dp)
    )

    // Show collapsed or expanded text
    Column(modifier = modifier) {
        Text(
            text = text,
            style = textStyle,
            maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLines,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.animateContentSize()
        )

        // Only show toggle if the real line count exceeds collapsedMaxLines
        if (fullLineCount > collapsedMaxLines) {
            Text(
                text = if (isExpanded)
                    stringResource(R.string.show_less)
                else
                    stringResource(R.string.show_more),
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .clickable { isExpanded = !isExpanded }
                    .padding(top = 4.dp)
            )
        }
    }
}
