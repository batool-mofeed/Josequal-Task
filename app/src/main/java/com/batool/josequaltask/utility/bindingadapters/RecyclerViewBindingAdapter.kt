@file:Suppress("UNCHECKED_CAST")

package com.batool.josequaltask.utility.bindingadapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.batool.josequaltask.base.BaseRecyclerViewAdapter


object RecyclerViewBindingAdapter {

    /**bind list of items with the base recycler view adapter**/
    @BindingAdapter("items")
    @JvmStatic
    fun <Any> RecyclerView?.addItems(items: List<Any>?) {
        if (this != null && items != null) {
            with((this.adapter as? BaseRecyclerViewAdapter<Any>)) {
                this?.addItems(items)
            }
        }
    }

}