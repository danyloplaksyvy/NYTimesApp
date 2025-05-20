package pro.danyloplaksyvyi.nytimesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel
import pro.danyloplaksyvyi.nytimesapp.features.main.presentation.viewmodel.OverviewViewModel
import pro.danyloplaksyvyi.nytimesapp.features.navigation.presentation.RootNavigationGraph
import pro.danyloplaksyvyi.nytimesapp.features.signin.presentation.viewmodel.AuthViewModel
import pro.danyloplaksyvyi.nytimesapp.ui.theme.NYTimesAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val authViewModel: AuthViewModel = koinViewModel()
            val overviewViewModel: OverviewViewModel = koinViewModel()
            NYTimesAppTheme {
                RootNavigationGraph(
                    modifier = Modifier.padding(),
                    authViewModel,
                    overviewViewModel
                )
            }
        }
    }
}
