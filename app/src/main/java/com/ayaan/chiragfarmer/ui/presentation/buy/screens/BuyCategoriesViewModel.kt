package com.ayaan.chiragfarmer.ui.presentation.buy.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ayaan.chiragfarmer.domain.model.Product
import com.ayaan.chiragfarmer.domain.usecase.GetAllProductsByCategoryUseCase
import com.ayaan.chiragfarmer.domain.usecase.GetSmartFarmingProductsUseCase
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

    /**
     * Maps display category names to API category parameters
     */
    private fun mapCategoryNameToApiParameter(categoryName: String): String? {
        return when {
            categoryName.equals("Seeds", ignoreCase = true) -> "seeds"
            categoryName.equals("Sprayers", ignoreCase = true) -> "sprayers"
            categoryName.equals("Agriculture Drone", ignoreCase = true) -> "drones"
            categoryName.equals("Tractors", ignoreCase = true) -> "tractors"
            else -> null
        }
    }

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
                val apiCategory = mapCategoryNameToApiParameter(categoryName)
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




