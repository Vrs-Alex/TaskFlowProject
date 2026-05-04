package com.vrsalex.taskflow.presentation.common.bottom_sheet.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

abstract class ItemBottomSheetViewModel: ViewModel() {


    abstract suspend fun onTitleUpdate(title: String)
    private val _titleFlow = MutableStateFlow("")
    fun onTitleChanged(title: String) {
        _titleFlow.value = title
    }

    abstract suspend fun onDescriptionUpdate(description: String?)
    private val _descriptionFlow = MutableStateFlow<String?>(null)
    fun onDescriptionChanged(description: String?) {
        _descriptionFlow.value = description
    }


    init {
        viewModelScope.launch {
            launch {
                _titleFlow
                    .drop(1)
                    .debounce { 500L }
                    .collectLatest {
                        onTitleUpdate(it)
                    }
            }
            launch {
                _descriptionFlow
                    .drop(1)
                    .debounce { 500L }
                    .collectLatest {
                        onDescriptionUpdate(it)
                    }
            }
        }
    }

}