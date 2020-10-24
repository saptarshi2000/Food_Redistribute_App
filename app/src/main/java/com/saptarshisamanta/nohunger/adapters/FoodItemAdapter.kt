package com.saptarshisamanta.nohunger.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.saptarshisamanta.nohunger.data.Food
import com.saptarshisamanta.nohunger.databinding.ItemFoodBinding

class FoodItemAdapter(var foodClickListener: FoodClickListener) :
    PagingDataAdapter<Food, FoodItemAdapter.FoodItemViewHolder>(FoodDiffCallback()) {

    override fun onBindViewHolder(holder: FoodItemViewHolder, position: Int) {
        val currentFood = getItem(position)!!
//        getItem(position)?.claimed = false
        holder.bind(currentFood, foodClickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemViewHolder {
        return FoodItemViewHolder.from(parent)
    }

    class FoodItemViewHolder private constructor(private val binding: ItemFoodBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(foodItem: Food, foodClickListener: FoodClickListener) {
            binding.apply {
                food = foodItem
                clickListener = foodClickListener
                if (food?.claimed == false) {
                    button.text = "Redistribute"
                    button.isEnabled = true
                } else {
                    button.text = "Claimed"
                    button.isEnabled = false
                }
                executePendingBindings()
            }
        }

        companion object {
            fun from(parent: ViewGroup): FoodItemViewHolder {
                val binding =
                    ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return FoodItemViewHolder(binding)
            }
        }
    }
}

class FoodDiffCallback : DiffUtil.ItemCallback<Food>() {
    override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
        return oldItem._id == newItem._id
    }

    override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
        return oldItem == newItem
    }
}

class FoodClickListener(val clickListener: (view: View, id: String) -> Unit) {
    fun onClick(view: View, foodItem: Food) = clickListener(view, foodItem._id!!)
}