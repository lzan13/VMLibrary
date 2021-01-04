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
import com.vmloft.develop.library.example.base.BaseFragment
import com.vmloft.develop.library.tools.utils.logger.VMLog

import kotlinx.android.synthetic.main.fragment_tab.*


class TabFragment : BaseFragment() {

    override fun layoutId() = R.layout.fragment_tab

    override fun initData() {
        val data = listOf<String>(
                "0",
                "1",
                "2",
                "3",
                "4",
                "5",
                "6",
                "7",
                "8",
                "9",
                "0",
                "1",
                "2",
                "3",
                "4",
                "5",
                "6",
                "7",
                "8",
                "9",
                "0",
                "1",
                "2",
                "3",
                "4",
                "5",
                "6",
                "7",
                "8",
                "9")
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = TestRecycleViewAdapter(context!!, data)
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