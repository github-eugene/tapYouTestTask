package com.eugene.testtaskfortechspire.ui.fragments.graph

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.eugene.testtaskfortechspire.R
import com.eugene.testtaskfortechspire.databinding.ItemPointBinding
import com.eugene.testtaskfortechspire.model.PointUiModel

class PointsAdapter() : RecyclerView.Adapter<PointViewHolder>() {

    private var items: List<PointUiModel> = listOf()

    fun setItems(items: List<PointUiModel>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPointBinding.inflate(inflater, parent, false)
        return PointViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: PointViewHolder, position: Int) {
        holder.bind(items[position])

        val backgroundColorRes = if (position % 2 == 0) {
            R.color.table_color_even
        } else {
            R.color.table_color_odd
        }
        holder.itemView.setBackgroundColor(
            ContextCompat.getColor(holder.itemView.context, backgroundColorRes)
        )
    }
}