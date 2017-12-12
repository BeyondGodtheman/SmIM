package com.zhangye.im.widget

import android.content.Context
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.zhangye.im.R
import com.zhangye.im.utils.DensityUtil
import java.util.*

/**
 *
 * Created by zhangye on 2017/12/11.
 */
class EmojScrollTabBar : RelativeLayout {

    private var scrollView: HorizontalScrollView? = null
    private var tabContainer: LinearLayout? = null

    private val tabList = ArrayList<ImageView>()
    private var itemClickListener: ScrollTabBarItemClickListener? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    init {
        init(context)
    }


    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.layout_emoji_tab_bar, this)

        scrollView = findViewById(R.id.scroll_view)
        tabContainer = findViewById(R.id.tab_container)
    }

    /**
     * add tab
     * @param icon
     */
    fun addTab(icon: Int) {
        val tabView = View.inflate(context, R.layout.scroll_tab_item, null)
        val imageView = tabView.findViewById<ImageView>(R.id.iv_icon)
        imageView.setImageResource(icon)
        val tabWidth = 60f
        val imgParams = LinearLayout.LayoutParams(DensityUtil.dip2px(context, tabWidth), RelativeLayout.LayoutParams.MATCH_PARENT)
        imageView.layoutParams = imgParams
        tabContainer!!.addView(tabView)
        tabList.add(imageView)
        val position = tabList.size - 1
        imageView.setOnClickListener {
            if (itemClickListener != null) {
                itemClickListener!!.onItemClick(position)
            }
        }
    }

    /**
     * remove tab
     * @param position
     */
    fun removeTab(position: Int) {
        tabContainer!!.removeViewAt(position)
        tabList.removeAt(position)
    }

    fun selectedTo(position: Int) {
        scrollTo(position)
        for (i in tabList.indices) {
            if (position == i) {
                tabList[i].setBackgroundColor(resources.getColor(R.color.tab_selected))
            } else {
                tabList[i].setBackgroundColor(resources.getColor(R.color.tab_nomal))
            }
        }
    }

    private fun scrollTo(position: Int) {
        val childCount = tabContainer!!.childCount
        if (position < childCount) {
            scrollView!!.post(Runnable {
                val mScrollX = tabContainer!!.scrollX
                val childX = ViewCompat.getX(tabContainer!!.getChildAt(position)).toInt()

                if (childX < mScrollX) {
                    scrollView!!.scrollTo(childX, 0)
                    return@Runnable
                }

                val childWidth = tabContainer!!.getChildAt(position).width.toInt()
                val hsvWidth = scrollView!!.width
                val childRight = childX + childWidth
                val scrollRight = mScrollX + hsvWidth

                if (childRight > scrollRight) {
                    scrollView!!.scrollTo(childRight - scrollRight, 0)
                    return@Runnable
                }
            })
        }
    }


    fun setTabBarItemClickListener(itemClickListener: ScrollTabBarItemClickListener) {
        this.itemClickListener = itemClickListener
    }


    interface ScrollTabBarItemClickListener {
        fun onItemClick(position: Int)
    }

}