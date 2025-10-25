package com.example.recetadecomidalogin

import Story
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recetadecomidalogin.databinding.StoryItemBinding
class StoryAdapter(private val stories: List<Story>) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: StoryItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = stories[position]
        holder.binding.storyName.text = story.name
        Glide.with(holder.itemView.context).load(story.imageUrl).into(holder.binding.storyImage)
    }

    override fun getItemCount() = stories.size
}