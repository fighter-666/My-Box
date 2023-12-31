package com.example.myapplication.recharge.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mybox.R
import com.example.mybox.databinding.FragmentRechargeWaterfallBinding
import com.example.mybox.recharge.adapter.WaterfallAdapter
import com.example.mybox.recharge.data.GetFeedListData
import com.example.mybox.recharge.fragment.BaseLazyFragment
import com.example.mybox.recharge.widget.LoadMoreManager
import com.google.gson.Gson


class WaterfallFragment : BaseLazyFragment() {
    private var _binding: FragmentRechargeWaterfallBinding? = null
    val binding get() = _binding!!
    private lateinit var myAdapter: WaterfallAdapter
    private var contactNumber: String? = null
    private var mIntent: Intent? = null
    private lateinit var feedList: GetFeedListData

    companion object {
        private const val ARG_TAB_NAME = "tabName"
        fun newInstance(tabName: Int): WaterfallFragment {
            val args = Bundle()
            args.putString(ARG_TAB_NAME, tabName.toString())
            val fragment = WaterfallFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRechargeWaterfallBinding.inflate(inflater, container, false)
        return binding.root
    }

    //用于请求读取联系人权限并执行相应的操作。
    private fun requestReadContactsPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // 如果已经有权限，直接执行读取联系人数据的操作
            val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            pickContactLauncher.launch(intent)
        } else {
            // 请求权限
            requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    // 处理权限请求结果
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // 用户同意了权限，跳转到通讯录界面，选择充值号码
            val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            pickContactLauncher.launch(intent)
        } else {
            // 用户拒绝了权限，可以给出相应的提示或处理逻辑
            Toast.makeText(context, "请打开读取联系人权限", Toast.LENGTH_SHORT).show()
            //requestReadContactsPermission()
        }
    }

    //使用registerForActivityResult来注册一个用于选择联系人的Activity结果回调
    private val pickContactLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                mIntent = result.data
                val contactUri = mIntent?.data
                contactNumber = getContactNumberByUri(contactUri)
                //刷新指定item
                //获取要刷新的position
                //Log.d("aaa",contactNumber.toString())
                val updatedItem = myAdapter.getItem(1)
                if (updatedItem?.quickRecharge != null) {
                    updatedItem.quickRecharge.title = contactNumber
                    // 更新适配器中的数据集
                    feedList.feedList[1] = updatedItem // 将索引为1的项替换为更新后的项
                    myAdapter.notifyItemChanged(1)
                }
            }
        }


    override fun loadData() {
        //从应用程序的资产文件夹中读取名为"getFeedListData.json"的JSON文件并将其内容作为字符串进行处理
        val json: String = requireContext().assets.open("getFeedListData.json").bufferedReader()
            .use { it.readText() }
        //使用了Gson库来将JSON数据转换为GetFeedTabData对象
        val gson = Gson()
        feedList = gson.fromJson(json, GetFeedListData::class.java)
        myAdapter = WaterfallAdapter(feedList.feedList)
        binding.rvComponentsWaterfall.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = myAdapter
        }

        //监听条目子组件的点击事件
        myAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.btnSelect -> {
                    requestReadContactsPermission()
                }
            }

            //Toast.makeText(context, "onItemChildClick $position", Toast.LENGTH_SHORT).show()
        }

       /* myAdapter.setOnItemClickListener { _, _, position ->
            Toast.makeText(context, "onItemClick $position", Toast.LENGTH_SHORT).show()
        }*/

        // 设置回调监听器
        LoadMoreManager.setOnLoadMoreListener(object : LoadMoreManager.OnLoadMoreListener {
            override fun onLoadMore() {
                // 在这里触发加载更多数据的操作
                val data: MutableList<GetFeedListData.FeedListBean> = mutableListOf()
             /*   data.add(
                    feedList.feedList[5]
                )
                data.add(
                    feedList.feedList[6]
                )*/
                data.add(
                    feedList.feedList[16]
                )
                data.add(
                    feedList.feedList[15]
                )
                myAdapter.addMoreValue(feedList.feedList,data)
                val startPosition = feedList.feedList.size - 2 // 开始位置是已有数据的最后两个位置
                val itemCount = data.size // 添加的数据项数
                myAdapter.notifyItemRangeInserted(startPosition, itemCount)
            }
        })
    }

    private fun getContactNumberByUri(data: Uri?): String? {

        var phoneNumber: String? = null
        //来获取一个光标（Cursor）对象。这里使用 contentResolver 来查询联系人的数据
        val cursor =
            data?.let { requireActivity().contentResolver.query(it, null, null, null, null) }
        //使用 Kotlin 的 use 函数，确保在使用完光标后关闭它
        cursor?.use { it ->
            //这行代码检查 Cursor 是否有数据，并将 Cursor 定位到第一行
            if (it.moveToFirst()) {
                //这行代码获取存储联系人是否有电话号码的列的索引。
                // HAS_PHONE_NUMBER 是联系人表中的一个列，它表示该联系人是否有电话号码。
                val hasPhoneIndex = it.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                //这行代码获取联系人 ID 列的索引。_ID 是联系人表中的一个列，它表示每个联系人的唯一标识符。
                val idIndex = it.getColumnIndex(ContactsContract.Contacts._ID)

                //这行代码从 Cursor 中获取存储联系人是否有电话号码的值，并将其存储在名为 hasPhone 的变量中。
                var hasPhone = it.getString(hasPhoneIndex)
                val id = it.getString(idIndex)

                // 这行代码将 hasPhone 的值进行比较。如果它等于字符串 "1"（忽略大小写），
                // 则将 hasPhone 设置为 "true"，否则设置为 "false"。
                // 这样做是为了将数据库中存储的 1/0 值转换为更易读的布尔值。
                hasPhone = if (hasPhone.equals("1", ignoreCase = true)) {
                    "true"
                } else {
                    "false"
                }

                if (hasPhone.toBoolean()) {
                    //这行代码使用 contentResolver 查询联系人的电话号码。
                    // requireActivity() 返回与当前 Fragment 相关联的 Activity。
                    val phonesCursor = requireActivity().contentResolver.query(
                        //表示查询电话号码的内容 URI，指定了查询电话号码的数据表。
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        //表示查询条件，限制查询结果必须与给定的联系人 ID 相匹配。
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null
                    )

                    // 这行代码使用了安全调用操作符 ?. 以确保 phonesCursor 不为 null，并在使用后自动关闭 Cursor
                    phonesCursor?.use {
                        //这行代码检查 Cursor 是否有数据，并将 Cursor 定位到第一行
                        if (it.moveToFirst()) {
                            //获取电话号码列的索引，将其赋值给 phoneNumberIndex。
                            val phoneNumberIndex =
                                it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                            //通过索引从光标中获取电话号码，并将其赋值给 phoneNumber
                            phoneNumber = it.getString(phoneNumberIndex)

                        }
                    }
                }
            }
        }
        return phoneNumber
    }
}


