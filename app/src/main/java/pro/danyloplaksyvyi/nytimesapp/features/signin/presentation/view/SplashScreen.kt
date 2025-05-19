package pro.danyloplaksyvyi.nytimesapp.features.signin.presentation.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import pro.danyloplaksyvyi.nytimesapp.features.navigation.presentation.Graph
import pro.danyloplaksyvyi.nytimesapp.features.signin.presentation.model.SignInState
import pro.danyloplaksyvyi.nytimesapp.features.signin.presentation.viewmodel.AuthViewModel

@Composable
fun SplashScreen(authViewModel: AuthViewModel, navController: NavController) {
    val signInState by authViewModel.signInState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(signInState) {
        when (signInState) {
            is SignInState.Success -> {
                navController.navigate(Graph.MAIN) {
                    popUpTo(Graph.SPLASH) { inclusive = true }
                }
            }
            is SignInState.Idle -> {
                navController.navigate(Graph.AUTH) {
                    popUpTo(Graph.SPLASH) { inclusive = true }
                }
            }
            is SignInState.Loading -> {
                // UI is shown during loading
            }

            is SignInState.Error -> {
                snackbarHostState.showSnackbar((signInState as SignInState.Error).message)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}