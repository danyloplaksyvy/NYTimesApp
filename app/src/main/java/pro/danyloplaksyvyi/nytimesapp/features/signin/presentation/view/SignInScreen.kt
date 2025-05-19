package pro.danyloplaksyvyi.nytimesapp.features.signin.presentation.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import pro.danyloplaksyvyi.nytimesapp.R
import pro.danyloplaksyvyi.nytimesapp.features.navigation.presentation.Graph
import pro.danyloplaksyvyi.nytimesapp.features.signin.presentation.model.SignInState
import pro.danyloplaksyvyi.nytimesapp.features.signin.presentation.viewmodel.AuthViewModel

@Composable
fun SignInScreen(navController: NavController, authViewModel: AuthViewModel) {
    val signInState by authViewModel.signInState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(signInState) {
        when (signInState) {
            is SignInState.Success -> {
                navController.navigate(Graph.MAIN) {
                    popUpTo(Graph.AUTH) { inclusive = true }
                }
            }
            is SignInState.Error -> {
                snackbarHostState.showSnackbar((signInState as SignInState.Error).message)
            }
            else -> { /* Do nothing */ }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = { authViewModel.signIn() }) {
                Text(stringResource(R.string.sign_in_with_google))
            }
        }
    }
}