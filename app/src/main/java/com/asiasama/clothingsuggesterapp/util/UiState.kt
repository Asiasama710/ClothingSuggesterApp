package com.asiasama.clothingsuggesterapp.util

sealed class UiState{
    object Loading : UiState()
    class Success(val data: Any) : UiState()
    class Error(val error: String) : UiState()
}
