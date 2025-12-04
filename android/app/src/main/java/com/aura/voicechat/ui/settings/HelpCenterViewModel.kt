package com.aura.voicechat.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.voicechat.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Help Center Screen
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class HelpCenterViewModel @Inject constructor(
    private val repository: SettingsRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(HelpCenterState())
    val state: StateFlow<HelpCenterState> = _state.asStateFlow()
    
    private var allFaqs: List<FaqItem> = emptyList()
    
    fun loadFaqs() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            
            repository.getFaqs().fold(
                onSuccess = { response ->
                    allFaqs = response.faqs.map { dto ->
                        FaqItem(
                            id = dto.id,
                            question = dto.question,
                            answer = dto.answer,
                            category = dto.category,
                            order = dto.order
                        )
                    }
                    
                    val categories = allFaqs.map { it.category }.distinct().sorted()
                    
                    _state.value = _state.value.copy(
                        faqs = allFaqs,
                        categories = categories,
                        isLoading = false
                    )
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = error.message ?: "Failed to load FAQs"
                    )
                }
            )
        }
    }
    
    fun searchFaqs(query: String) {
        if (query.isEmpty()) {
            _state.value = _state.value.copy(faqs = allFaqs)
            return
        }
        
        val filtered = allFaqs.filter {
            it.question.contains(query, ignoreCase = true) ||
            it.answer.contains(query, ignoreCase = true)
        }
        
        _state.value = _state.value.copy(faqs = filtered)
    }
    
    fun filterByCategory(category: String?) {
        if (category == null) {
            _state.value = _state.value.copy(faqs = allFaqs)
            return
        }
        
        val filtered = allFaqs.filter { it.category == category }
        _state.value = _state.value.copy(faqs = filtered)
    }
}

data class HelpCenterState(
    val faqs: List<FaqItem> = emptyList(),
    val categories: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
