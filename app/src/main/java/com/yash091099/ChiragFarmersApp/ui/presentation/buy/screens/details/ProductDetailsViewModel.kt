package com.yash091099.ChiragFarmersApp.ui.presentation.buy.screens.details

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.data.remote.dto.ProductDetailedData
import com.yash091099.ChiragFarmersApp.data.remote.dto.ProductReviewsData
import com.yash091099.ChiragFarmersApp.data.remote.dto.ReviewReactionRequest
import com.yash091099.ChiragFarmersApp.domain.repository.ProductRepository
import com.yash091099.ChiragFarmersApp.domain.usecase.AddToCartUseCase
import com.yash091099.ChiragFarmersApp.domain.usecase.GetProductReviewsUseCase
import com.yash091099.ChiragFarmersApp.domain.usecase.ReactToReviewUseCase
import com.yash091099.ChiragFarmersApp.R
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val productRepository: ProductRepository,
    private val addToCartUseCase: AddToCartUseCase,
    private val getProductReviewsUseCase: GetProductReviewsUseCase,
    private val reactToReviewUseCase: ReactToReviewUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductDetailsUiState>(ProductDetailsUiState.Loading)
    val uiState: StateFlow<ProductDetailsUiState> = _uiState.asStateFlow()

    private val _cartState = MutableStateFlow<CartActionState>(CartActionState.Idle)
    val cartState: StateFlow<CartActionState> = _cartState.asStateFlow()

    private val _isInCart = MutableStateFlow(false)
    val isInCart: StateFlow<Boolean> = _isInCart.asStateFlow()

    private val _reviewsState = MutableStateFlow<ProductReviewsUiState>(ProductReviewsUiState.Loading)
    val reviewsState: StateFlow<ProductReviewsUiState> = _reviewsState.asStateFlow()

    private val _reviewReactionState = MutableStateFlow<ReviewReactionUiState>(ReviewReactionUiState.Idle)
    val reviewReactionState: StateFlow<ReviewReactionUiState> = _reviewReactionState.asStateFlow()

    private val productId: String = checkNotNull(savedStateHandle["productId"])

//    init {
//        loadProductDetails()
//    }

    fun loadProductDetails() {
        viewModelScope.launch {
            _uiState.value = ProductDetailsUiState.Loading
            _reviewsState.value = ProductReviewsUiState.Loading

            productRepository.getProductDetailsDetailed(productId).fold(
                onSuccess = { productDetails ->
                    _isInCart.value = productDetails.isInCart
                    _uiState.value = ProductDetailsUiState.Success(productDetails)
                    loadProductReviews()
                },
                onFailure = { exception ->
                    _uiState.value = ProductDetailsUiState.Error(
                        exception.message ?: context.getString(R.string.error_failed_load_detail)
                    )
                }
            )
        }
    }

    private suspend fun loadProductReviews() {
        getProductReviewsUseCase(productId).fold(
            onSuccess = { reviews ->
                _reviewsState.value = ProductReviewsUiState.Success(reviews)
            },
            onFailure = { exception ->
                _reviewsState.value = ProductReviewsUiState.Error(
                    exception.message ?: context.getString(R.string.error_failed_load_reviews)
                )
            }
        )
    }

    fun addToCart() {
        viewModelScope.launch {
            _cartState.value = CartActionState.Loading
            addToCartUseCase(productId).fold(
                onSuccess = { cartItemsCount ->
                    // Don't reload product details, just update the local isInCart state
                    _isInCart.value = true
                    _cartState.value = CartActionState.Success(cartItemsCount)
                },
                onFailure = { exception ->
                    _cartState.value = CartActionState.Error(
                        exception.message ?: context.getString(R.string.error_failed_add_to_cart)
                    )
                }
            )
        }
    }

    fun reactToReview(reviewId: String?, action: String) {
        val trimmedReviewId = reviewId?.trim().orEmpty()
        if (trimmedReviewId.isBlank()) {
            _reviewReactionState.value = ReviewReactionUiState.Error(context.getString(R.string.error_review_id_missing))
            return
        }

        val normalizedAction = action.trim().lowercase(Locale.getDefault())
        if (normalizedAction != "like" && normalizedAction != "dislike") {
            _reviewReactionState.value = ReviewReactionUiState.Error(context.getString(R.string.error_invalid_review_action))
            return
        }

        if ((_reviewReactionState.value as? ReviewReactionUiState.Loading)?.reviewId == trimmedReviewId) return

        viewModelScope.launch {
            _reviewReactionState.value = ReviewReactionUiState.Loading(trimmedReviewId)

            reactToReviewUseCase(
                ReviewReactionRequest(
                    ratingId = trimmedReviewId,
                    action = normalizedAction
                )
            ).fold(
                onSuccess = { response ->
                    if (response.success) {
                        updateReviewLocally(trimmedReviewId, normalizedAction)
                        _reviewReactionState.value = ReviewReactionUiState.Success(response.message)
                    } else {
                        _reviewReactionState.value = ReviewReactionUiState.Error(response.message)
                    }
                },
                onFailure = { exception ->
                    _reviewReactionState.value = ReviewReactionUiState.Error(
                        exception.message ?: context.getString(R.string.error_failed_update_review)
                    )
                }
            )
        }
    }

    private fun updateReviewLocally(reviewId: String, action: String) {
        val currentState = _reviewsState.value as? ProductReviewsUiState.Success ?: return
        val updatedReviews = currentState.reviews.recentReviews.map { review ->
            if (review.reviewId == reviewId) {
                when (action) {
                    "like" -> review.copy(likes = review.likes + 1)
                    "dislike" -> review.copy(dislikes = review.dislikes + 1)
                    else -> review
                }
            } else {
                review
            }
        }

        _reviewsState.value = ProductReviewsUiState.Success(
            currentState.reviews.copy(recentReviews = updatedReviews)
        )
    }

    fun retry() {
        loadProductDetails()
    }

    fun resetCartState() {
        _cartState.value = CartActionState.Idle
    }

    fun resetReviewReactionState() {
        _reviewReactionState.value = ReviewReactionUiState.Idle
    }
}

sealed class ProductReviewsUiState {
    object Loading : ProductReviewsUiState()
    data class Success(val reviews: ProductReviewsData) : ProductReviewsUiState()
    data class Error(val message: String) : ProductReviewsUiState()
}

sealed class ReviewReactionUiState {
    object Idle : ReviewReactionUiState()
    data class Loading(val reviewId: String) : ReviewReactionUiState()
    data class Success(val message: String) : ReviewReactionUiState()
    data class Error(val message: String) : ReviewReactionUiState()
}

sealed class ProductDetailsUiState {
    object Loading : ProductDetailsUiState()
    data class Success(val productDetails: ProductDetailedData) : ProductDetailsUiState()
    data class Error(val message: String) : ProductDetailsUiState()
}

sealed class CartActionState {
    object Idle : CartActionState()
    object Loading : CartActionState()
    data class Success(val cartItemsCount: Int) : CartActionState()
    data class Error(val message: String) : CartActionState()
}


