package com.tutorial.runningapp.adapters

import androidx.recyclerview.widget.DiffUtil
import com.tutorial.runningapp.data.db.RunEntity

class RunDiffUtil : DiffUtil.ItemCallback<RunEntity>() {
    override fun areItemsTheSame(oldItem: RunEntity, newItem: RunEntity): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: RunEntity, newItem: RunEntity): Boolean =
        oldItem == newItem
}