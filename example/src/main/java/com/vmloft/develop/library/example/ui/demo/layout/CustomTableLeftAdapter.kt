package com.vmloft.develop.library.example.ui.demo.layout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.vmloft.develop.library.example.R

/**
 * Create by lzan13 2021/07/14
 * 描述：表格左侧固定列适配器
 */
class CustomTableLeftAdapter(private val context: Context) : RecyclerView.Adapter<CustomTableLeftAdapter.TableRowViewHolder>() {

    private val dataList = mutableListOf<CustomTableBean>()

    fun setDataList(list: List<CustomTableBean>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    fun addDataList(list: List<CustomTableBean>) {
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): TableRowViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_table_left_row_layout, viewGroup, false)
        return TableRowViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: TableRowViewHolder, position: Int) {
        val bean = dataList[position]
        viewHolder.tableRowNameTV.text = bean.ccName

        viewHolder.itemView.isSelected = position > 1 && position % 2 == 0
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    /**
     * Create by lzan13 on 2021/7/15
     * 描述：表格 ItemViewHolder
     */
    inner class TableRowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tableRowNameTV: TextView = itemView.findViewById(R.id.tableRowNameTV)
    }
}