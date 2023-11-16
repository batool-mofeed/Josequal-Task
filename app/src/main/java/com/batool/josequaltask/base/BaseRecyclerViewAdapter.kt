@file:SuppressLint("NotifyDataSetChanged")

package com.batool.josequaltask.base

import android.annotation.SuppressLint
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<T> :
  RecyclerView.Adapter<BaseRecyclerViewAdapter.BaseViewHolder>() {

  var items = ArrayList<T>()

  fun addItems(comingItems: List<T>) {
    if (comingItems.isNotEmpty()) {
      items.clear()
      items.addAll(comingItems)
      notifyDataSetChanged()
    } else {
      items.clear()
      notifyItemRangeRemoved(0, itemCount - 1)
    }
  }

  override fun getItemCount() = items.size

  override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
    holder.onBind(holder.adapterPosition)
  }

  abstract class BaseViewHolder(
    binding: ViewDataBinding
  ) : RecyclerView.ViewHolder(binding.root) {
    abstract fun onBind(position: Int)
  }
}