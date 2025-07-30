package com.example.findit.presentation.screen.chatlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.findit.R
import com.example.findit.databinding.ItemChatBinding
import com.example.findit.domain.model.Chat
import com.example.findit.presentation.extension.loadImage

class ChatListAdapter(
    private val currentUserId: String,
    private val onChatClick: (String) -> Unit
) : ListAdapter<Chat, ChatListAdapter.ChatViewHolder>(ChatDiffCallback())
 {

     private var userNames: Map<String, Pair<String, String?>> = emptyMap()

    fun updateUserNames(userNames: Map<String, Pair<String, String?>>) {
        this.userNames = userNames
    }

    inner class ChatViewHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(chat: Chat) {
            val otherUserId = chat.participantIds.first { it != currentUserId }
            val displayName = userNames[otherUserId]?.first ?: "Unknown User"
            val profileImageUrl = userNames[otherUserId]?.second

            binding.usernameText.text = displayName
            binding.lastMessageText.text = chat.lastMessage

            profileImageUrl?.let {
                binding.imageProfile.loadImage(
                    url = it,
                    placeHolder = R.drawable.user
                )
            }

            binding.root.setOnClickListener {
                onChatClick(otherUserId)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemChatBinding.inflate(inflater, parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ChatDiffCallback : DiffUtil.ItemCallback<Chat>() {
    override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
        return oldItem == newItem
    }
}
