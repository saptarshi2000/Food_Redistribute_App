package com.saptarshisamanta.nohunger.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saptarshisamanta.nohunger.R
import kotlinx.android.synthetic.main.item_view_pager.view.*

class ViewPagerAdapter(private val photos: List<Int>) :
    RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        return ViewPagerViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val image = photos[position]
        holder.bind(image)
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    class ViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(imageView: Int) {
            itemView.imageView2.setImageResource(imageView)
        }

        companion object {
            fun from(parent: ViewGroup): ViewPagerViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_view_pager, parent, false)
                return ViewPagerViewHolder(view)
            }
        }
    }
}