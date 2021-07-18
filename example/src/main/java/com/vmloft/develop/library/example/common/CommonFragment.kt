package com.vmloft.develop.library.example.common

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.common.base.BaseFragment
import com.vmloft.develop.library.tools.utils.logger.VMLog

import kotlinx.android.synthetic.main.fragment_demo_tab.*


class CommonFragment : BaseFragment() {

    private lateinit var tabName: String

    companion object {
        private const val fragmentTitleKey = "fragmentTitleKey"

        /**
         * Fragment 的工厂方法，方便创建并设置参数
         */
        fun newInstance(arg1: String): CommonFragment {
            val fragment = CommonFragment()
            val args = Bundle()
            args.putString(fragmentTitleKey, arg1)
            fragment.arguments = args
            return fragment
        }
    }


    override fun layoutId() = R.layout.fragment_demo_tab

    override fun initData() {
        tabName = arguments!!.getString(fragmentTitleKey) ?: ""

        val list = mutableListOf<String>()
        for (index in 1..100) {
            list.add("$tabName-$index")
        }

        smartRefreshLayout.setOnRefreshListener {
            VMLog.d("触发刷新")
        }
        smartRefreshLayout.setOnLoadMoreListener {
            VMLog.d("触发加载更多")
        }
        tableRowRV.layoutManager = LinearLayoutManager(context)
        tableRowRV.adapter = TestRecycleViewAdapter(context!!, list)
    }

    class TestRecycleViewAdapter(context: Context, list: List<String>) : RecyclerView.Adapter<TestRecycleViewAdapter.ViewHolder>() {
        private val mContext: Context = context
        private val mList: List<String> = list
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view: View = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.mTextView.text = mList[position]
            holder.mTextView.setOnClickListener { VMLog.d("hello") }
        }

        override fun getItemCount(): Int {
            return mList.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var mTextView: TextView = itemView as TextView
        }

    }
}