package com.example.mybox.adapter

import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.myapplication.recharge.view.property.Piggy
import com.example.mybox.R

class ComponentsAdapter(@LayoutRes layoutResId: Int, data: MutableList<Piggy>) :
    BaseQuickAdapter<Piggy, BaseViewHolder>(layoutResId, data) {
    override fun convert(holder: BaseViewHolder, item: Piggy) {
        holder.setImageResource(R.id.ivComponentsAdapter, item.image)
        holder.setText(R.id.tvComponentsAdapter, item.name)
    }
}
