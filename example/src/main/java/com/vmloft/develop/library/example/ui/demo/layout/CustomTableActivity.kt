package com.vmloft.develop.library.example.ui.demo.layout


import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.vmloft.develop.library.common.base.BaseActivity

import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.router.AppRouter
import com.vmloft.develop.library.tools.utils.logger.VMLog
import com.vmloft.develop.library.tools.widget.layout.VMTableLayoutManager
import kotlinx.android.synthetic.main.activity_demo_custom_table.*


/**
 * Created by lzan13 on 2021/7/14.
 * 描述：测试自定义 LayoutManager 相关界面
 */
@Route(path = AppRouter.appCustomTable)
class CustomTableActivity : BaseActivity() {


    override fun layoutId() = R.layout.activity_demo_custom_table

    override fun initUI() {
        super.initUI()
        setTopTitle("自定义表格")

        initRecyclerView()
        setupRecyclerView()
    }

    override fun initData() {
    }

    private fun initRecyclerView() {
        val dataList = mutableListOf<CustomTableBean>()
        // 第一行为标题
        var bean = CustomTableBean()
        dataList.add(bean)

        // 顶部固定部分，顶部就一条数据
        topRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        topRecyclerView.setHasFixedSize(true)
        val topAdapter = CustomTableAdapter(this)
        topAdapter.setDataList(dataList)
        topRecyclerView.adapter = topAdapter
        topRecyclerView.setOnTouchListener { v, event ->
            recyclerView.dispatchTouchEvent(event)
            false
        }

        // 填充数据
        for (i in 1..10) {
            bean = CustomTableBean("子账户$i", "2021-7-$i", "11:11:$i", "23:11:$i", "08:23:$i", "09:32:$i", "12:33:$i")
            dataList.add(bean)
        }

        // 左侧固定部分
        leftRecyclerView.layoutManager = LinearLayoutManager(this)
        leftRecyclerView.setHasFixedSize(true)
        val leftAdapter = CustomTableLeftAdapter(this)
        leftAdapter.setDataList(dataList)
        leftRecyclerView.adapter = leftAdapter
        leftRecyclerView.setOnTouchListener { v, event ->
            recyclerView.dispatchTouchEvent(event)
            false
        }

        val layoutManager = VMTableLayoutManager()
        layoutManager.totalColumnCount = 1
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        val adapter = CustomTableAdapter(this)
        adapter.setDataList(dataList)
//        val adapter = SimpleAdapter()
//        adapter.itemCount = 50
        recyclerView.adapter = adapter

    }

    private fun setupRecyclerView() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                VMLog.d("表格滚动信息 x-$dx y-$dy")

                val layoutManager = recyclerView.layoutManager as VMTableLayoutManager
                val positionCol = layoutManager.firstVisibleColumn
                val positionRow = layoutManager.firstVisibleRow

                VMLog.d("表格滚动位置 col-$positionCol row-$positionRow")

                val visibilityView = layoutManager.getChildAt(positionCol)
                if (visibilityView != null) {
                    val offsetTop = layoutManager.getDecoratedTop(visibilityView)
                    val offsetLeft = layoutManager.getDecoratedLeft(visibilityView)

                    // 同步固定行和列的滚动位置
                    val topLayoutManager = topRecyclerView.layoutManager as LinearLayoutManager
                    topLayoutManager.scrollToPositionWithOffset(positionCol, offsetLeft)

                    val leftLayoutManager = leftRecyclerView.layoutManager as LinearLayoutManager
                    leftLayoutManager.scrollToPositionWithOffset(positionRow, offsetTop)


                    // 是否显示顶部固定行
                    if (positionRow > 0 || offsetTop < 0) {
                        topRecyclerView.visibility = View.VISIBLE
                    } else {
                        topRecyclerView.visibility = View.GONE
                    }
                    // 是否显示左侧固定列
                    if (offsetLeft < 0) {
                        leftRecyclerView.visibility = View.VISIBLE
                        leftRecyclerViewShadow.visibility = View.VISIBLE
                    } else {
                        leftRecyclerView.visibility = View.GONE
                        leftRecyclerViewShadow.visibility = View.GONE
                    }
                    VMLog.d("表格滚动位置 col-$positionCol row-$positionRow offsetTop-$offsetTop offsetLeft-$offsetLeft")
                }
            }
        })
    }


}