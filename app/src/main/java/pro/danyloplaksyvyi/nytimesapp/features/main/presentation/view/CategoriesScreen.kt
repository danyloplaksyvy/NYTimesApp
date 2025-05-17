package pro.danyloplaksyvyi.nytimesapp.features.main.presentation.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CategoriesScreen(onCategoryClick: () -> Unit, onSignOutClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = onCategoryClick) {
                Text("Go to category")
            }
            Button(onClick = onSignOutClick) {
                Text("SignOut")
            }
        }
    }
}