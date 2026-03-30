package com.ayaan.chiragfarmer.ui.presentation.buy.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ayaan.chiragfarmer.domain.model.Product
import com.ayaan.chiragfarmer.domain.usecase.GetAllProductsByCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class BuyCategoriesViewModel @Inject constructor(
    private val getAllProductsByCategoryUseCase: GetAllProductsByCategoryUseCase
) : ViewModel() {

    private val _categoryName = MutableStateFlow("")
    private val _selectedSubcategory = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val products: Flow<PagingData<Product>> = combine(_categoryName, _selectedSubcategory) { category, subcategory ->
        category to subcategory
    }.flatMapLatest { (categoryName, subcategory) ->
        if (categoryName.equals("Direct From Farmers", ignoreCase = true)) {
            getAllProductsByCategoryUseCase(
                category = "direct-from-farmers",
                subcategory = subcategory
            )
        } else {
            flowOf(PagingData.empty())
        }
    }.cachedIn(viewModelScope)

    fun setCategoryName(categoryName: String) {
        _categoryName.value = categoryName
    }

    fun onCategoryChipSelected(chipName: String) {
        val normalizedChip = chipName
            .replace("\n", " ")
            .trim()

        _selectedSubcategory.value = if (normalizedChip.equals("All Produces", ignoreCase = true)) {
            null
        } else {
            normalizedChip.replace("&", "and").lowercase()
        }
    }
}

