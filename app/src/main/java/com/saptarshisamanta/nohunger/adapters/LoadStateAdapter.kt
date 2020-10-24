package com.saptarshisamanta.nohunger.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.saptarshisamanta.nohunger.databinding.LoadStateBinding

class LoadStateAdapter(private var retryListener: RetryListener) :
    androidx.paging.LoadStateAdapter<LoadStateAdapter.LoadStateViewHolder>() {

    class LoadStateViewHolder(private val loadStateBinding: LoadStateBinding) :
        RecyclerView.ViewHolder(loadStateBinding.root) {
        fun bind(loadState: LoadState, retryListener: RetryListener) {
            loadStateBinding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                buttonRetry.isVisible = loadState !is LoadState.Loading
                textViewError.isVisible = loadState !is LoadState.Loading
                clickListener = retryListener
            }
        }

        companion object {
            fun from(parent: ViewGroup): LoadStateViewHolder {
                val loadStateBinding =
                    LoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return LoadStateViewHolder(loadStateBinding)
            }
        }
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState, retryListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder.from(parent)
    }
}

class RetryListener(var retryClickListener: () -> Unit) {
    fun onClick() = retryClickListener()
}