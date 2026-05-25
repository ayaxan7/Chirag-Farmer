package com.yash091099.ChiragFarmersApp.ui.presentation.assist.screens.assistResult

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash091099.ChiragFarmersApp.data.remote.dto.CropAnalysisDataDto
import com.yash091099.ChiragFarmersApp.data.remote.dto.CropInsecticideDto
import com.yash091099.ChiragFarmersApp.domain.repository.CropAnalysisRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val cropAnalysisRepository: CropAnalysisRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val imageUri: String = savedStateHandle.get<String>("imageUri").orEmpty()

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
            _uiState.value = AssistResultUiState.Error("Selected image is missing")
            return
        }

        viewModelScope.launch {
            _uiState.value = AssistResultUiState.Loading
            cropAnalysisRepository.analyzeCrop(imageUri).fold(
                onSuccess = { data ->
                    _uiState.value = AssistResultUiState.Success(data.toUiModel())
                },
                onFailure = { error ->
                    _uiState.value = AssistResultUiState.Error(
                        error.message ?: "Failed to analyze crop image"
                    )
                }
            )
        }
    }

    private fun CropAnalysisDataDto.toUiModel(): CropAnalysisUiModel {
        return CropAnalysisUiModel(
            cropName = cropName.orEmpty().ifBlank { "Unknown Crop" },
            diseaseName = diseaseIdentified?.name.orEmpty().ifBlank { "Unknown Disease" },
            confidence = diseaseIdentified?.confidence,
            about = diseaseIdentified?.about.orEmpty().ifBlank { "No description available." },
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


