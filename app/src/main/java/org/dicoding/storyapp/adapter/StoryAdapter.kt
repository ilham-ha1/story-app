package org.dicoding.storyapp.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.dicoding.storyapp.databinding.ItemStoryBinding
import org.dicoding.storyapp.model.response.ListStoryItem
import org.dicoding.storyapp.ui.detail.DetailActivity

class StoryAdapter:PagingDataAdapter<ListStoryItem,StoryAdapter.ListViewHolder>(DIFF_CALLBACK){

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int):ListViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            viewHolder.bind(data)
        }
    }

    class ListViewHolder(var binding:ItemStoryBinding) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(data: ListStoryItem){
            binding.tvUsername.text = data.name
            Glide.with(binding.root.context)
                .load(data.photoUrl)
                .into(binding.imgStoryPhoto)
            binding.tvDescription.text = data.description
            binding.tvDate.text = data.createdAt
            val positionText = if (data.lat != null && data.lon != null) {
                "${data.lat}, ${data.lon}"
            } else {
                ""
            }
            binding.tvPosition.text = positionText
            itemView.setOnClickListener {
                val intentDetail = Intent(itemView.context, DetailActivity::class.java)
                intentDetail.putExtra(DetailActivity.EXTRA_ID, data.id)
                itemView.context.startActivity(intentDetail)
            }

        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}