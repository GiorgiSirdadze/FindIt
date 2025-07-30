package com.example.findit.presentation.screen.home

sealed class HomeScreenEvent {
    data class OnPostClicked(val postId: String) : HomeScreenEvent()
    data class OnSearchQueryChanged(val query: String) : HomeScreenEvent()
    data class OnFiltersSelected(val selectedFilters: List<String>) : HomeScreenEvent()
    data object ClearError : HomeScreenEvent()
}