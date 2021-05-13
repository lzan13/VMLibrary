package com.vmloft.develop.library.example.ui.main.mine

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


class TabFragment : BaseFragment() {

    private lateinit var tabName: String

    override fun layoutId() = R.layout.fragment_demo_tab

    override fun initData() {
        tabName = arguments!!.getString(tabNameKey) ?: ""

        val list = mutableListOf<String>()
        for (index in 1..100) {
            list.add("$tabName-$index")
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = TestRecycleViewAdapter(context!!, list)
    }

    companion object {
        private val tabNameKey = "tabNameKey"

        /**
         * Fragment 的工厂方法，方便创建并设置参数
         */
        fun newInstance(arg1: String): TabFragment {
            val fragment = TabFragment()
            val args = Bundle()
            args.putString(tabNameKey, arg1)
            fragment.arguments = args
            return fragment
        }
    }

    class TestRecycleViewAdapter(context: Context, list: List<String>) : RecyclerView.Adapter<TestRecycleViewAdapter.ViewHolder>() {
        private val mContext: Context
        private val mList: List<String>
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view: View = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.mTextView.text = mList[position]
            holder.mTextView.setOnClickListener { VMLog.d("hello")}
        }

        override fun getItemCount(): Int {
            return mList.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var mTextView: TextView
            init {
                mTextView = itemView as TextView
            }
        }

        init {
            mContext = context
            mList = list
        }
    }
}