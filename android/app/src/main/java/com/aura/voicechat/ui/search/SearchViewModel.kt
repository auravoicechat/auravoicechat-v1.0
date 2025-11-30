package com.aura.voicechat.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Search ViewModel
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()
    
    init {
        loadRecentSearches()
    }
    
    private fun loadRecentSearches() {
        _uiState.value = _uiState.value.copy(
            recentSearches = listOf("StarLight", "Dragon Room", "12345678", "Phoenix")
        )
    }
    
    fun search(query: String, tabIndex: Int) {
        _uiState.value = _uiState.value.copy(
            searchQuery = query,
            isLoading = query.isNotEmpty()
        )
        
        if (query.isEmpty()) return
        
        viewModelScope.launch {
            // Simulate API call
            kotlinx.coroutines.delay(500)
            
            when (tabIndex) {
                0 -> searchUsers(query)
                1 -> searchRooms(query)
                2 -> searchById(query)
            }
        }
    }
    
    private fun searchUsers(query: String) {
        // Sample results
        val results = listOf(
            SearchUser("12345678", "StarLight", null, 25, true),
            SearchUser("12345679", "StarDust", null, 18, false),
            SearchUser("12345680", "StarGazer", null, 42, true)
        ).filter { it.name.contains(query, ignoreCase = true) }
        
        _uiState.value = _uiState.value.copy(
            userResults = results,
            isLoading = false
        )
    }
    
    private fun searchRooms(query: String) {
        // Sample results
        val results = listOf(
            SearchRoom("room1", "Dragon Warriors", null, "DragonKing", 45),
            SearchRoom("room2", "Music Paradise", null, "DJ Storm", 128),
            SearchRoom("room3", "Chill Zone", null, "ChillMaster", 23)
        ).filter { it.name.contains(query, ignoreCase = true) }
        
        _uiState.value = _uiState.value.copy(
            roomResults = results,
            isLoading = false
        )
    }
    
    private fun searchById(query: String) {
        // Check if ID exists
        val result = when {
            query == "12345678" -> IdSearchResult("12345678", "StarLight", "user")
            query == "room1" -> IdSearchResult("room1", "Dragon Warriors", "room")
            else -> null
        }
        
        _uiState.value = _uiState.value.copy(
            idSearchResult = result,
            isLoading = false
        )
    }
    
    fun clearSearch() {
        _uiState.value = _uiState.value.copy(
            searchQuery = "",
            userResults = emptyList(),
            roomResults = emptyList(),
            idSearchResult = null
        )
    }
    
    fun clearRecentSearches() {
        _uiState.value = _uiState.value.copy(recentSearches = emptyList())
    }
}

data class SearchUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val recentSearches: List<String> = emptyList(),
    val userResults: List<SearchUser> = emptyList(),
    val roomResults: List<SearchRoom> = emptyList(),
    val idSearchResult: IdSearchResult? = null
)
