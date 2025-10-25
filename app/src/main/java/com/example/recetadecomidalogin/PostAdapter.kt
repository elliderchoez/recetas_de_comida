package com.example.recetadecomidalogin

import Post
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recetadecomidalogin.databinding.PostItemBinding

class PostAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        Glide.with(holder.itemView.context).load(post.imageUrl).into(holder.binding.postImage)
        holder.binding.likesText.text = "${post.likes} likes"
        holder.binding.commentsText.text = post.comments
        holder.binding.likeButton.setOnClickListener {
            post.likes++
            notifyItemChanged(position)
        }
    }

    override fun getItemCount() = posts.size
}