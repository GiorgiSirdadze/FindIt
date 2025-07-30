package com.example.findit.presentation.screen.home.helper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.findit.R
import com.example.findit.databinding.PostItemBinding
import com.example.findit.presentation.extension.loadImage
import com.example.findit.presentation.model.PostPresentation

class PostAdapter(val onPostClicked : (String) -> Unit) : ListAdapter<PostPresentation, PostAdapter.PostViewHolder>(DiffCallback()) {

    inner class PostViewHolder(private val binding: PostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(post: PostPresentation) = with(binding) {
            tvPostType.text = post.postType.toString()
            tvDescription.text = post.description
            tvUserId.text = post.userFullName
            tvTimestamp.text = post.timestamp
            ivPostImage.loadImage(post.imageUrl, R.drawable.postdefault)
            imgProfile.loadImage(post.userProfilePicture, R.drawable.ic_my_profile)
            root.setOnClickListener {
                onPostClicked(post.postId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<PostPresentation>() {
        override fun areItemsTheSame(oldItem: PostPresentation, newItem: PostPresentation): Boolean {
            return oldItem.postId == newItem.postId
        }

        override fun areContentsTheSame(oldItem: PostPresentation, newItem: PostPresentation): Boolean {
            return oldItem == newItem
        }
    }
}
