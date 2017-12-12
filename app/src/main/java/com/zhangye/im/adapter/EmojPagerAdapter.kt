package com.zhangye.im.adapter

import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View

/**
 *
 * Created by zhangye on 2017/12/11.
 */
class EmojPagerAdapter(private val views: List<View>) : PagerAdapter() {

    override fun getCount(): Int {
        return views.size
    }

    override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
        return arg0 === arg1
    }

    override fun instantiateItem(arg0: View?, arg1: Int): Any {
        (arg0 as ViewPager).addView(views[arg1])
        return views[arg1]
    }

    override fun destroyItem(arg0: View?, arg1: Int, arg2: Any?) {
        (arg0 as ViewPager).removeView(views[arg1])

    }

}