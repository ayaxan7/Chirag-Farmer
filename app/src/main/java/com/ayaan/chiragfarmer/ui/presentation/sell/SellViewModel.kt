package com.ayaan.chiragfarmer.ui.presentation.sell

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ayaan.chiragfarmer.domain.model.Product
import com.ayaan.chiragfarmer.domain.usecase.GetFarmerProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class SellViewModel @Inject constructor(
    private val getFarmerProductsUseCase: GetFarmerProductsUseCase
) : ViewModel() {

    private val _activeSearchQuery = MutableStateFlow("")
    private val _soldOutSearchQuery = MutableStateFlow("")

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val activeProducts: Flow<PagingData<Product>> = _activeSearchQuery
        .flatMapLatest { query ->
            getFarmerProductsUseCase(type = "active", search = query.ifEmpty { null })
        }
        .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val soldOutProducts: Flow<PagingData<Product>> = _soldOutSearchQuery
        .flatMapLatest { query ->
            getFarmerProductsUseCase(type = "sold_out", search = query.ifEmpty { null })
        }
        .cachedIn(viewModelScope)

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun fetchActive(query: String) {
        _activeSearchQuery.value = query
    }

    fun fetchSoldOut(query: String) {
        _soldOutSearchQuery.value = query
    }
}
