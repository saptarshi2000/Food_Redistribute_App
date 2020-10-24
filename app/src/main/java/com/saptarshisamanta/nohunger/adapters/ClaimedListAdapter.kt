package com.saptarshisamanta.nohunger.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.saptarshisamanta.nohunger.data.Food
import com.saptarshisamanta.nohunger.databinding.ItemFoodBinding

class ClaimedListAdapter :
    ListAdapter<Food, ClaimedListAdapter.ClaimedListViewHolder>(ClaimedFoodDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClaimedListViewHolder {
        return ClaimedListViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: ClaimedListViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

    }

    class ClaimedListViewHolder(var binding: ItemFoodBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Food) {
            binding.apply {
                food = item
                binding.button.text = "Claimed"
                binding.button.isEnabled = false
                executePendingBindings()
            }
        }
        companion object{
            fun from(parent: ViewGroup):ClaimedListViewHolder{
                val binding = ItemFoodBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                return ClaimedListViewHolder(binding)
            }
        }
    }
}

class ClaimedFoodDiffCallBack : DiffUtil.ItemCallback<Food>() {
    override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
        return oldItem._id == newItem._id
    }

    override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
        return oldItem == newItem
    }

}