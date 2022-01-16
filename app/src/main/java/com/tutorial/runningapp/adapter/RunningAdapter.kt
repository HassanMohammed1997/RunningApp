package com.tutorial.runningapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tutorial.runningapp.data.db.RunEntity
import com.tutorial.runningapp.databinding.ListItemRunBinding

class RunningAdapter : ListAdapter<RunEntity, RunningAdapter.RunViewHolder>(RunDiffUtil()) {

    class RunViewHolder(private val binding: ListItemRunBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun getViewHolder(parent: ViewGroup): RunViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val itemView = ListItemRunBinding.inflate(inflater, parent, false)
                return RunViewHolder(itemView)
            }
        }

        fun bind(item: RunEntity) {
            binding.run = item
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder =
        RunViewHolder.getViewHolder(parent)

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) =
        holder.bind(getItem(position))
}


