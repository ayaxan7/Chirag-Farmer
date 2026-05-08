package com.yash091099.ChiragFarmersApp.ui.presentation.buy.screens.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yash091099.ChiragFarmersApp.domain.model.Product
import com.yash091099.ChiragFarmersApp.domain.usecase.GetAllProductsByCategoryUseCase
import com.yash091099.ChiragFarmersApp.domain.usecase.GetSmartFarmingProductsUseCase
import com.yash091099.ChiragFarmersApp.ui.presentation.sell.data.Categories
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
    private val getAllProductsByCategoryUseCase: GetAllProductsByCategoryUseCase,
    private val getSmartFarmingProductsUseCase: GetSmartFarmingProductsUseCase
) : ViewModel() {

    private val _categoryName = MutableStateFlow("")
    private val _selectedSubcategory = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val products: Flow<PagingData<Product>> = combine(_categoryName, _selectedSubcategory) { category, subcategory ->
        category to subcategory
    }.flatMapLatest { (categoryName, subcategory) ->
        when {
            categoryName.equals("Direct From Farmers", ignoreCase = true) -> {
                // Use the direct-from-farmers endpoint
                getAllProductsByCategoryUseCase(
                    category = "direct-from-farmers",
                    subcategory = subcategory
                )
            }
            categoryName.isNotEmpty() && !categoryName.equals("All Produces", ignoreCase = true) -> {
                // Use the smart farming endpoint for other categories
                val apiCategory = when {
                    categoryName.equals("Smart Farming", ignoreCase = true) -> "smart farming"
                    categoryName.equals("Popular Products", ignoreCase = true) -> "popular products"
                    else -> Categories.getBuyApiCategory(categoryName)
                }
                if (apiCategory != null) {
                    getSmartFarmingProductsUseCase(
                        category = apiCategory,
                        subcategory = subcategory
                    )
                } else {
                    flowOf(PagingData.empty())
                }
            }
            else -> {
                flowOf(PagingData.empty())
            }
        }
    }.cachedIn(viewModelScope)

    fun setCategoryName(categoryName: String) {
        _categoryName.value = categoryName
        _selectedSubcategory.value = null
    }

    fun onCategoryChipSelected(apiValue: String?) {
        _selectedSubcategory.value = apiValue
    }
}




