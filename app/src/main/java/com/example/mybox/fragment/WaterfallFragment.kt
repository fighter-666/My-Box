package com.example.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.myapplication.adapter.WaterfallAdapter
import com.example.myapplication.recharge.view.property.Card
import com.example.mybox.R
import com.example.mybox.databinding.FragmentWaterfallBinding
import com.gyf.immersionbar.ImmersionBar


class WaterfallFragment : Fragment() {
    private var _binding: FragmentWaterfallBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWaterfallBinding.inflate(inflater, container, false)
        val view = binding.root
        ImmersionBar.with(this)
            .transparentStatusBar()  //透明状态栏，不写默认透明色
            .titleBar(binding.toolbar)    //解决状态栏和布局重叠问题，任选其一
            .init()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      /*  val piggies = mutableListOf(
            Pair(R.mipmap.icon_grid_color_helper, "QMUIColorHelper"),
            Pair(
                R.drawable.icon_grid_color_helper,
                "哇哈哈哇哈哈哇哈哈哇哈哈哇哈哈哇哈哈哇哈哈哇哈哈哇哈哈哇哈哈哇哈哈哇哈哈哇哈哈哇哈哈QMUIDeviceHelper"
            ),
            Pair(R.mipmap.icon_grid_drawable_helper, "QWUIDrawableHelper"),
            Pair(R.mipmap.icon_grid_tip_dialog, "QMUIStatusBarHelper"),
            Pair(R.mipmap.icon_grid_view_helper, "QMUIViewHelper"),
            Pair(R.mipmap.icon_grid_tip_dialog, "QMUINotchHelper")
        ).map { (imageResId, helperText) ->
            Card(imageResId, helperText, 0, 0)
        }.toMutableList()*/

        val piggies = mutableListOf<Card>()
        piggies.add(Card(R.mipmap.icon_grid_color_helper,   "1",0,0))
        piggies.add(Card(R.mipmap.icon_grid_device_helper,  "2",0,0))
        piggies.add(Card(R.mipmap.icon_grid_drawable_helper, "3",0,0) )
        piggies.add(Card(R.mipmap.icon_grid_tip_dialog,     "4",0,0))
        piggies.add(Card(R.mipmap.icon_grid_view_helper,    "5",0,0))
        piggies.add(Card(R.mipmap.icon_grid_tip_dialog,    "6",0,0))
        val myAdapter = WaterfallAdapter(R.layout.adapter_waterfall, piggies)
        binding.rvComponentsWaterfall.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = myAdapter
        }

       /* myAdapter.setOnItemClickListener { piggy ->
            // 处理列表项点击事件
            Toast.makeText(context, piggy.name, Toast.LENGTH_SHORT).show()
            when (piggy.name) {
                "QMUIColorHelper" -> {
                    val intent = Intent(context, RechargePageActivity::class.java)
                    intent.putExtra("piggyName", piggy.name)
                    startActivity(intent)
                }

                "QMUIDeviceHelper" -> {
                    val intent = Intent(context, VariousTextviewActivity::class.java)
                    intent.putExtra("piggyName", piggy.name)
                    startActivity(intent)
                }

                "QWUIDrawableHelper" -> {
                    val intent = Intent(context, ThirdActivity::class.java)
                    intent.putExtra("piggyName", piggy.name)
                    startActivity(intent)
                }

                "QMUIStatusBarHelper" -> {
                    val intent = Intent(context, FourthActivity::class.java)
                    intent.putExtra("piggyName", piggy.name)
                    startActivity(intent)
                }

                "QMUIViewHelper" -> {
                    val intent = Intent(context, WebViewActivity::class.java)
                    intent.putExtra("piggyName", piggy.name)
                    startActivity(intent)
                }

                "QMUINotchHelper" -> {
                    val intent = Intent(context, CustomActivity::class.java)
                    intent.putExtra("piggyName", piggy.name)
                    startActivity(intent)
                }
                // 其他Piggy对象的处理逻辑...

                else -> {
                    // 默认的页面跳转逻辑
                    val intent = Intent(context, RechargePageActivity::class.java)
                    intent.putExtra("piggyName", piggy.name)
                    startActivity(intent)
                }
            }
        }*/

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}





