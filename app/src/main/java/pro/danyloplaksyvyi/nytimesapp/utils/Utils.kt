package pro.danyloplaksyvyi.nytimesapp.utils

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pro.danyloplaksyvyi.nytimesapp.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatReadableDate(dateString: String): String {
    return try {
        val parsedDate = LocalDate.parse(dateString) // parses ISO-8601 (yyyy-MM-dd)
        val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM", Locale.getDefault())
        parsedDate.format(formatter)
    } catch (e: Exception) {
        dateString // fallback to original if parsing fails
    }
}

@Composable
fun RetryButton(onClick: () -> Unit) {
    Button(onClick = { onClick() }, shape = RoundedCornerShape(8.dp)) {
        Text(stringResource(R.string.retry))
    }
}