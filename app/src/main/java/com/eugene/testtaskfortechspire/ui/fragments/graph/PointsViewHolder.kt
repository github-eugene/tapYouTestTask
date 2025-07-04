package com.eugene.testtaskfortechspire.ui.fragments.graph

import androidx.recyclerview.widget.RecyclerView
import com.eugene.testtaskfortechspire.databinding.ItemPointBinding
import com.eugene.testtaskfortechspire.model.PointUiModel

class PointViewHolder(private val binding: ItemPointBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(point: PointUiModel) = with(binding) {
        tvXValue.text = point.x.toString()
        tvYValue.text = point.y.toString()
    }
}