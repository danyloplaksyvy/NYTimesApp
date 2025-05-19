package pro.danyloplaksyvyi.nytimesapp.features.signin.presentation.view

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
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

            else -> { /* Do nothing */
            }
        }
    }

    val isVisible = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isVisible.value = true
    }
    val alpha by animateFloatAsState(
        targetValue = if (isVisible.value) 1f else 0f,
        animationSpec = tween(durationMillis = 2000)
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                            append("Welcome to the ")
                        }
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                            append("Bookio!")
                        }
                    },
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .alpha(alpha)
                        .padding(16.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.signinimage),
                    contentDescription = "Sign In Image",
                    modifier = Modifier
                        .size(312.dp)
                        .alpha(alpha)
                )
            }
            Button(
                onClick = { authViewModel.signIn() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .alpha(alpha)
                    .fillMaxWidth(0.8f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                            ,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.sign_in_with_google))
                    Spacer(modifier = Modifier.width(8.dp))
                    Image(
                        painter = painterResource(id = R.drawable.googleicon),
                        contentDescription = "Google icon",
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f))
                            .padding(4.dp)
                            .size(24.dp)
                            .clip(CircleShape)
                    )
                }
            }
        }
    }
}