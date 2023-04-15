package org.dicoding.storyapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.dicoding.storyapp.databinding.ItemStoryBinding
import org.dicoding.storyapp.model.response.ListStoryItem
import org.dicoding.storyapp.ui.detail.DetailActivity

class StoryAdapter(private val listStoryItem: List<ListStoryItem>):RecyclerView.Adapter<StoryAdapter.ListViewHolder>(){
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int):ListViewHolder {
        val binding =
            ItemStoryBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    class ListViewHolder(var binding:ItemStoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(viewHolder: ListViewHolder, position: Int) {
        listStoryItem[position].let { story ->
           viewHolder.binding.tvUsername.text = story.name
            Glide.with(viewHolder.binding.root.context)
                .load(story.photoUrl)
                .into(viewHolder.binding.imgStoryPhoto)
            viewHolder.binding.tvDescription.text = story.description
            viewHolder.binding.tvDate.text = story.createdAt
            viewHolder.itemView.setOnClickListener {
                val intentDetail = Intent(viewHolder.itemView.context, DetailActivity::class.java)
                intentDetail.putExtra(DetailActivity.EXTRA_ID, listStoryItem[viewHolder.adapterPosition].id)
                viewHolder.itemView.context.startActivity(intentDetail)
            }
        }

    }

    override fun getItemCount() = listStoryItem.size
}