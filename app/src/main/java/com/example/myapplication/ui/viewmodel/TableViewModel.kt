package com.example.myapplication.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class TableViewModel @Inject constructor() : ViewModel() {
    private val _tableNumber = MutableStateFlow<String?>(null)
    val tableNumber: StateFlow<String?> = _tableNumber

    fun setTableNumber(number: String) {
        _tableNumber.value = number
    }
}

