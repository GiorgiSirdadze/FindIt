package com.example.findit.presentation.screen.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findit.domain.model.ChatMessage
import com.example.findit.domain.resource.Resource
import com.example.findit.domain.usecase.chat.GetMessagesUseCase
import com.example.findit.domain.usecase.user.GetProfilePictureUseCase
import com.example.findit.domain.usecase.user.GetUserNameUseCase
import com.example.findit.domain.usecase.chat.SendMessageUseCase
import com.example.findit.presentation.mappper.toPresentation
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
import java.util.UUID
import javax.inject.Inject
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val getProfilePictureUseCase: GetProfilePictureUseCase,
    private val getUserNameUseCase: GetUserNameUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(ChatState())
    val state: StateFlow<ChatState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<ChatEffect>()
    val effects: SharedFlow<ChatEffect> = _effects.asSharedFlow()

    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
    private val receiverId: String = savedStateHandle["receiverId"] ?: ""

    private val chatId = listOf(currentUserId, receiverId).sorted().joinToString("_")

    init {
        loadMessages()
        loadUserName()
        loadProfileImage()
    }

    fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.SendMessage -> sendMessage(event.text)
        }
    }

    private fun loadMessages() {
        viewModelScope.launch {
            getMessagesUseCase(chatId).collectLatest { messages ->
                val presentationMessages = messages.map { it.toPresentation() }
                _state.update { it.copy(messages = presentationMessages) }
            }
        }
    }

    private fun loadUserName() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val name = getUserNameUseCase(receiverId)
                _state.update {
                    it.copy(
                        userName = name,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun loadProfileImage() {
        viewModelScope.launch {
            getProfilePictureUseCase(receiverId).collect { result ->
                when (result) {
                    is Resource.Loader -> _state.update { it.copy(isLoading = true) }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                profileImageUrl = result.data,
                                isLoading = false
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update { it.copy(isLoading = false) }
                    }
                }
            }
        }
    }

    private fun sendMessage(text: String) {
        viewModelScope.launch {
            val message = ChatMessage(
                id = UUID.randomUUID().toString(),
                senderId = currentUserId,
                message = text,
                timestamp = System.currentTimeMillis()
            )
            try {
                sendMessageUseCase(chatId, message)
                _effects.emit(ChatEffect.MessageSent)
            } catch (e: Exception) {
                _effects.emit(ChatEffect.ShowError("Failed to send message"))
            }
        }
    }
}


