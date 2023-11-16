package com.batool.josequaltask.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import com.batool.josequaltask.base.BaseRecyclerViewAdapter
import com.batool.josequaltask.databinding.ItemLocationPlaceItemBinding
import com.batool.josequaltask.model.PlaceModel

class PlacesAdapter(
    private val onPlaceClicked: (PlaceModel) -> Unit
) : BaseRecyclerViewAdapter<PlaceModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PlacesVH(
        ItemLocationPlaceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    inner class PlacesVH(
        private val binding: ItemLocationPlaceItemBinding
    ) : BaseViewHolder(binding) {
        override fun onBind(position: Int) {
            with(binding) {
                val item = items[position]
                place = item
                executePendingBindings()
                mainContainer.setOnClickListener {
                    onPlaceClicked(item)
                }
            }
        }
    }
}