package com.yash091099.ChiragFarmersApp.ui.presentation.home.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.data.remote.dto.SearchProductItem
import com.yash091099.ChiragFarmersApp.domain.usecase.SearchProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchProductsUseCase: SearchProductsUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<SearchProductItem>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private var searchJob: Job? = null

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
        searchJob?.cancel()
        
        if (newQuery.length >= 2) {
            searchJob = viewModelScope.launch {
                delay(500)
                _isSearching.value = true
                searchProductsUseCase(newQuery).onSuccess {
                    _searchResults.value = it
                }.onFailure {
                    _searchResults.value = emptyList()
                }
                _isSearching.value = false
            }
        } else {
            _searchResults.value = emptyList()
            _isSearching.value = false
        }
    }
}
