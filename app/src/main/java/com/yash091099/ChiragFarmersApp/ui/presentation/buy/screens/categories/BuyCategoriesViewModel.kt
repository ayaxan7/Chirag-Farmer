package com.yash091099.ChiragFarmersApp.ui.presentation.buy.screens.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yash091099.ChiragFarmersApp.data.location.LocationManager
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BuyCategoriesViewModel @Inject constructor(
    private val getAllProductsByCategoryUseCase: GetAllProductsByCategoryUseCase,
    private val getSmartFarmingProductsUseCase: GetSmartFarmingProductsUseCase,
    private val locationManager: LocationManager
) : ViewModel() {

    private val _categoryName = MutableStateFlow("")
    private val _selectedSubcategory = MutableStateFlow<String?>(null)
    private val _filterState = MutableStateFlow(FilterState())
    private val _currentLocation = MutableStateFlow<Pair<Double, Double>?>(null)

    fun setFilterState(state: FilterState) {
        _filterState.value = state
        if (state.nearBy) {
            fetchCurrentLocation()
        } else {
            _currentLocation.value = null
        }
    }

    private fun fetchCurrentLocation() {
        viewModelScope.launch {
            _currentLocation.value = locationManager.getCurrentLocation()
        }
    }

    private data class FilterQuery(
        val categoryName: String,
        val subcategory: String?,
        val filter: FilterState,
        val currentLocation: Pair<Double, Double>?
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val products: Flow<PagingData<Product>> = combine(_categoryName, _selectedSubcategory, _filterState, _currentLocation) { category, subcategory, filter, currentLocation ->
        FilterQuery(category, subcategory, filter, currentLocation)
    }.flatMapLatest { (categoryName, subcategory, filter, currentLocation) ->

        val minPrice = filter.minBudget.takeIf { v -> v.isNotEmpty() }
        val maxPrice = filter.maxBudget.takeIf { v -> v.isNotEmpty() }
        val sort = when {
            filter.sortByLowToHigh -> "price_low_to_high"
            filter.sortByHighToLow -> "price_high_to_low"
            else -> null
        }
        val rating = if (filter.moreThan3Rating) "true" else null
        val location = if (filter.nearBy && currentLocation != null) {
            "${currentLocation.second},${currentLocation.first}"
        } else null

        when {
            categoryName.equals("Direct From Farmers", ignoreCase = true) -> {
                getAllProductsByCategoryUseCase(
                    category = "direct-from-farmers",
                    subcategory = subcategory,
                    minPrice = minPrice,
                    maxPrice = maxPrice,
                    sort = sort,
                    rating = rating,
                    location = location
                )
            }
            categoryName.isNotEmpty() && !categoryName.equals("All Produces", ignoreCase = true) -> {
                val apiCategory = when {
                    categoryName.equals("Smart Farming", ignoreCase = true) -> "smart farming"
                    categoryName.equals("Popular Products", ignoreCase = true) -> "popular products"
                    else -> Categories.getBuyApiCategory(categoryName)
                }
                if (apiCategory != null) {
                    getSmartFarmingProductsUseCase(
                        category = apiCategory,
                        subcategory = subcategory,
                        minPrice = minPrice,
                        maxPrice = maxPrice,
                        sort = sort,
                        rating = rating,
                        location = location
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




