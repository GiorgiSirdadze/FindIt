package com.example.findit.presentation.screen.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.findit.databinding.ItemMessageReceivedBinding
import com.example.findit.databinding.ItemMessageSentBinding
import com.example.findit.domain.model.ChatMessage
import com.example.findit.presentation.model.ChatMessagePresentation

class MessageAdapter(private val currentUserId: String) :
    ListAdapter<ChatMessagePresentation, RecyclerView.ViewHolder>(DiffCallback()) {

    companion object {
        private const val TYPE_SENT = 1
        private const val TYPE_RECEIVED = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).senderId == currentUserId) TYPE_SENT else TYPE_RECEIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_SENT) {
            val binding = ItemMessageSentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            SentMessageViewHolder(binding)
        } else {
            val binding = ItemMessageReceivedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ReceivedMessageViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position)
        when (holder) {
            is SentMessageViewHolder -> holder.bind(message)
            is ReceivedMessageViewHolder -> holder.bind(message)
        }
    }

    inner class SentMessageViewHolder(private val binding: ItemMessageSentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: ChatMessagePresentation) {
            binding.sentText.text = message.message
            binding.sentTime.text = message.timestamp
        }
    }

    inner class ReceivedMessageViewHolder(private val binding: ItemMessageReceivedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: ChatMessagePresentation) {
            binding.receivedText.text = message.message
            binding.receivedTime.text = message.timestamp
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ChatMessagePresentation>() {
        override fun areItemsTheSame(oldItem: ChatMessagePresentation, newItem: ChatMessagePresentation): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ChatMessagePresentation, newItem: ChatMessagePresentation): Boolean =
            oldItem == newItem
    }
}
