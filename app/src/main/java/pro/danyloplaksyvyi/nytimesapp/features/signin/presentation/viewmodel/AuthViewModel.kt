package pro.danyloplaksyvyi.nytimesapp.features.signin.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pro.danyloplaksyvyi.nytimesapp.R
import pro.danyloplaksyvyi.nytimesapp.features.signin.data.GoogleAuthClient
import pro.danyloplaksyvyi.nytimesapp.features.signin.domain.model.SignInState

class AuthViewModel(
    private val googleAuthClient: GoogleAuthClient,
    private val context: Context
) : ViewModel() {
    private val _signInState = MutableStateFlow<SignInState>(SignInState.Idle)
    val signInState: StateFlow<SignInState> = _signInState

    init {
        checkSignedIn()
    }

    private fun checkSignedIn() {
        if (googleAuthClient.isSignedIn()) {
            _signInState.value = SignInState.Success
        } else {
            _signInState.value = SignInState.Idle
        }
    }

    fun signIn() {
        viewModelScope.launch {
            try {
                _signInState.value = SignInState.Loading
                val success = googleAuthClient.signIn()
                if (success) {
                    _signInState.value = SignInState.Success
                } else {
                    _signInState.value = SignInState.Error(context.getString(R.string.sign_in_failed))
                }
            } catch (e: Exception) {
                _signInState.value = SignInState.Error(e.message ?: context.getString(R.string.unknown_error))
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                _signInState.value = SignInState.Loading
                val success = googleAuthClient.signOut()
                if (success) {
                    _signInState.value = SignInState.Idle
                } else {
                    _signInState.value = SignInState.Error(context.getString(R.string.sign_out_failed))
                }
            } catch (e: Exception) {
             _signInState.value = SignInState.Error(e.message ?: context.getString(R.string.unknown_error))
            }

        }
    }
}