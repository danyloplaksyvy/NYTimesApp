package pro.danyloplaksyvyi.nytimesapp.features.signin.presentation.model

sealed class SignInState {
    object Idle : SignInState()
    object Loading : SignInState()
    object Success : SignInState()
    data class Error(val message: String) : SignInState()
}