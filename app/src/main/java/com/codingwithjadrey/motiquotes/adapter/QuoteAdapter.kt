package com.codingwithjadrey.motiquotes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codingwithjadrey.motiquotes.data.Motive
import com.codingwithjadrey.motiquotes.databinding.MotiveItemBinding

class QuoteAdapter(private val onItemClicked: (Motive) -> Unit) :
    ListAdapter<Motive, QuoteAdapter.QuoteViewHolder>(DiffCallBack) {

    class QuoteViewHolder(private val binding: MotiveItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(motive: Motive) {
            binding.apply {
                dateCreated.text = motive.createdOn
                quoteSource.text = motive.quoteSource
                motiveItem.text = motive.motiveQuote
            }
        }
    }

    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<Motive>() {
            override fun areItemsTheSame(oldItem: Motive, newItem: Motive): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Motive, newItem: Motive): Boolean {
                return oldItem.motiveQuote == newItem.motiveQuote
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        return QuoteViewHolder(
            MotiveItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.itemView.setOnClickListener { onItemClicked(currentItem) }
        holder.bind(currentItem)
    }
}