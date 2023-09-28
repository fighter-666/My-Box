package com.example.mybox.recharge.fragment

import androidx.fragment.app.Fragment


open class BaseLazyFragment : Fragment() {
    override fun onResume() {
        super.onResume()
        loadData()
    }

     open fun loadData() {
        // 你的加载逻辑
    }
}


