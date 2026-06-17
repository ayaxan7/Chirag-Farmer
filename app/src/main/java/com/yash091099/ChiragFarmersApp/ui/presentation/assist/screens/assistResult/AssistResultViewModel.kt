package com.yash091099.ChiragFarmersApp.ui.presentation.assist.screens.assistResult

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.R
import com.yash091099.ChiragFarmersApp.data.remote.dto.CropAnalysisDataDto
import com.yash091099.ChiragFarmersApp.data.remote.dto.CropInsecticideDto
import com.yash091099.ChiragFarmersApp.domain.repository.CropAnalysisRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CropInsecticideUiModel(
    val pesticideName: String,
    val dosageUse: String,
    val target: String
)

data class CropAnalysisUiModel(
    val cropName: String,
    val diseaseName: String,
    val confidence: Int?,
    val about: String,
    val symptoms: List<String>,
    val avoid: List<String>,
    val insecticides: List<CropInsecticideUiModel>
)

sealed class AssistResultUiState {
    data object Loading : AssistResultUiState()
    data class Success(val data: CropAnalysisUiModel) : AssistResultUiState()
    data class Error(val message: String) : AssistResultUiState()
}

@HiltViewModel
class AssistResultViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cropAnalysisRepository: CropAnalysisRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val imageUri: String = savedStateHandle.get<String>("imageUri").orEmpty()
    private val language: String = savedStateHandle.get<String>("language") ?: "hindi"

    private val _uiState = MutableStateFlow<AssistResultUiState>(AssistResultUiState.Loading)
    val uiState: StateFlow<AssistResultUiState> = _uiState.asStateFlow()

    init {
        loadAnalysis()
    }

    fun retry() {
        loadAnalysis()
    }

    fun getImageUri(): String = imageUri

    private fun loadAnalysis() {
        if (imageUri.isBlank()) {
            _uiState.value = AssistResultUiState.Error(context.getString(R.string.error_selected_image_missing))
            return
        }

        viewModelScope.launch {
            _uiState.value = AssistResultUiState.Loading
            cropAnalysisRepository.analyzeCrop(imageUri, language).fold(
                onSuccess = { data ->
                    _uiState.value = AssistResultUiState.Success(data.toUiModel())
                },
                onFailure = { error ->
                    _uiState.value = AssistResultUiState.Error(
                        error.message ?: context.getString(R.string.error_analysis_failed)
                    )
                }
            )
        }
    }

    private fun CropAnalysisDataDto.toUiModel(): CropAnalysisUiModel {
        return CropAnalysisUiModel(
            cropName = cropName.orEmpty().ifBlank { context.getString(R.string.error_unknown_crop) },
            diseaseName = diseaseIdentified?.name.orEmpty().ifBlank { context.getString(R.string.error_unknown_disease) },
            confidence = diseaseIdentified?.confidence,
            about = diseaseIdentified?.about.orEmpty().ifBlank { context.getString(R.string.error_no_description) },
            symptoms = symptomsIdentified,
            avoid = avoid,
            insecticides = insecticides.map { it.toUiModel() }
        )
    }

    private fun CropInsecticideDto.toUiModel(): CropInsecticideUiModel {
        return CropInsecticideUiModel(
            pesticideName = pesticideName.orEmpty(),
            dosageUse = dosageUse.orEmpty(),
            target = target.orEmpty()
        )
    }
}


