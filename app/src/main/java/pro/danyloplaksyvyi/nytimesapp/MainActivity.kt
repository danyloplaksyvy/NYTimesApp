package pro.danyloplaksyvyi.nytimesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import pro.danyloplaksyvyi.nytimesapp.features.navigation.presentation.RootNavigationGraph
import pro.danyloplaksyvyi.nytimesapp.ui.theme.NYTimesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NYTimesAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RootNavigationGraph(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
