package com.example.findit.presentation.screen.chatlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findit.domain.resource.Resource
import com.example.findit.domain.usecase.chat.GetChatsUseCase
import com.example.findit.domain.usecase.user.GetProfilePictureUseCase
import com.example.findit.domain.usecase.user.GetUserNameUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val getChatsUseCase: GetChatsUseCase,
    private val getUserNameUseCase: GetUserNameUseCase,
    private val getProfilePictureUseCase: GetProfilePictureUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ChatListState())
    val state: StateFlow<ChatListState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<ChatListEffect>()
    val effects: SharedFlow<ChatListEffect> = _effects.asSharedFlow()

    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

    init {
        onEvent(ChatListEvent.LoadChats)
    }

    fun onEvent(event: ChatListEvent) {
        when (event) {
            is ChatListEvent.LoadChats -> loadChats()
            is ChatListEvent.ChatClicked -> onChatClicked(event.otherUserId)
        }
    }

    private fun loadChats() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            getChatsUseCase(currentUserId).collectLatest { chats ->
                val userNames = mutableMapOf<String, Pair<String, String?>>()

                chats.forEach { chat ->
                    val otherUserId = chat.participantIds.first { it != currentUserId }

                    val username = getUserNameUseCase(otherUserId)

                    val profileImageFlow = launch {
                        getProfilePictureUseCase(otherUserId).collect { resource ->
                            if (resource is Resource.Success) {
                                userNames[otherUserId] = username to resource.data
                            }
                        }
                    }

                    profileImageFlow.join() // Wait until profile image flow is completed
                }

                _state.update {
                    it.copy(
                        chats = chats,
                        userNames = userNames,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun onChatClicked(otherUserId: String) {
        viewModelScope.launch {
            _effects.emit(ChatListEffect.NavigateToChat(otherUserId))
        }
    }
}



