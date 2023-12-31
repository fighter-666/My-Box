package com.example.mybox.recharge.adapter

import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.mybox.R
import com.example.mybox.databinding.AdapterRechargeManyImageGridBinding
import com.example.mybox.recharge.data.GetFeedListData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ManyImageGridAdapter(
    @LayoutRes layoutResId: Int,
    data: MutableList<GetFeedListData.FeedListBean.PicListBean>,
) :
    BaseQuickAdapter<GetFeedListData.FeedListBean.PicListBean, BaseViewHolder>(layoutResId, data) {


    override fun getItemCount(): Int {
        val picListSize = data.size
        return when {
            picListSize < 2 -> 0
            picListSize >= 4 -> 4
            else -> 2
        }
    }

    override fun convert(holder: BaseViewHolder, item: GetFeedListData.FeedListBean.PicListBean) {
        val binding = AdapterRechargeManyImageGridBinding.bind(holder.itemView)
        //卡片锁宽等比缩放（imageRatio用来计算高度）
        val layoutParams = binding.ivImageUrl.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.dimensionRatio = item.imageRatio // 例如，设置宽高比为16:9

        binding.ivImageUrl.layoutParams = layoutParams

        // 在协程中加载网络图片或在后台线程中加载大量图片。
        // 确保在使用 Glide 加载图片时选择正确的 Dispatchers，以避免阻塞主线程
        CoroutineScope(Dispatchers.Main).launch {
            // 设置圆角半径
            val requestOptions = RequestOptions().transform(RoundedCorners(20))
            Glide.with(mContext)
                .load(item.imageUrl)//使用 load() 方法传入 URL 字符串 imageUrl 来指定要加载的图片资源
                //使用 transition() 方法可以设置过渡效果，例如交叉淡入淡出效果
                //.transform( GranularRoundedCorners(20f,20f,0f,0f))//四个角单独指定角度
                //.transition(DrawableTransitionOptions.withCrossFade())
                .apply(requestOptions)
                .error(R.drawable.ic_launcher_foreground)
                .into(binding.ivImageUrl)
        }

    }
}