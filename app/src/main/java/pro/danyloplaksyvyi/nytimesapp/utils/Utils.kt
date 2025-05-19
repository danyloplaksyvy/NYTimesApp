package pro.danyloplaksyvyi.nytimesapp.utils

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