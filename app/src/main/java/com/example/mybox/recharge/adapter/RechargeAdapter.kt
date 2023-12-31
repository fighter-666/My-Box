package com.example.mybox.recharge.adapter

import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.mybox.databinding.AdapterRechargeBinding
import com.example.mybox.recharge.data.GetFeedListData

class RechargeAdapter(
    @LayoutRes layoutResId: Int,
    data: MutableList<GetFeedListData.FeedListBean.QuickRechargeBean.DenominationBean>,
) :
    BaseQuickAdapter<GetFeedListData.FeedListBean.QuickRechargeBean.DenominationBean, BaseViewHolder>(
        layoutResId,
        data
    ) {

    override fun convert(
        holder: BaseViewHolder,
        item: GetFeedListData.FeedListBean.QuickRechargeBean.DenominationBean,
    ) {
        val binding = AdapterRechargeBinding.bind(holder.itemView)
        binding.tvMainTitle.text = item.mainTitle
        binding.tvSubtitle.text = item.subtitle
    }
}