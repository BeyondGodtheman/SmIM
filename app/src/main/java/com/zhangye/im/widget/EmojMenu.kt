package com.zhangye.im.widget

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.zhangye.im.R
import com.zhangye.im.model.Emoj
import com.zhangye.im.model.EmojGroupEntity
import java.util.*

/**
 * s
 * Created by zhangye on 2017/12/11.
 */
class EmojMenu : EmojMenuBase {

    private var emojiconColumns: Int = 0
    private var bigEmojiconColumns: Int = 0
    private lateinit var tabBar: EmojScrollTabBar
    private var indicatorView: EmojIndicatorView? = null
    private var pagerView: EmojPagerView? = null

    private val emojiconGroupList = ArrayList<EmojGroupEntity>()

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.layout_widget_emoj, this)
        val ta = context.obtainStyledAttributes(attrs, R.styleable.EmojMenu)
        val defaultColumns = 7
        emojiconColumns = ta.getInt(R.styleable.EmojMenu_emojColumns, defaultColumns)
        val defaultBigColumns = 4
        bigEmojiconColumns = ta.getInt(R.styleable.EmojMenu_bigEmojRows, defaultBigColumns)
        ta.recycle()

        pagerView = findViewById(R.id.pager_view)
        indicatorView = findViewById(R.id.indicator_view)
        tabBar = findViewById(R.id.emoj_tab_type)

    }

    fun init(groupEntities: List<EmojGroupEntity>?) {
        if (groupEntities == null || groupEntities.isEmpty()) {
            return
        }
        for (groupEntity in groupEntities) {
            emojiconGroupList.add(groupEntity)
            tabBar.addTab(groupEntity.getIcon())
        }

        pagerView?.setPagerViewListener(PagerViewListener())
        pagerView?.init(emojiconGroupList, emojiconColumns, bigEmojiconColumns)

        tabBar.setTabBarItemClickListener(object : EmojScrollTabBar.ScrollTabBarItemClickListener {
            override
            fun onItemClick(position: Int) {
                pagerView!!.setGroupPostion(position)
            }
        })

    }


    /**
     * add emojicon group
     * @param groupEntity
     */
    fun addEmojiconGroup(groupEntity: EmojGroupEntity) {
        emojiconGroupList.add(groupEntity)
        pagerView?.addEmojiconGroup(groupEntity, true)
        tabBar.addTab(groupEntity.getIcon())
    }

    /**
     * add emojicon group list
     * @param groupEntitieList
     */
    fun addEmojiconGroup(groupEntitieList: List<EmojGroupEntity>) {
        for (i in groupEntitieList.indices) {
            val groupEntity = groupEntitieList[i]
            emojiconGroupList.add(groupEntity)
            pagerView?.addEmojiconGroup(groupEntity, i == groupEntitieList.size - 1)
            tabBar.addTab(groupEntity.getIcon())
        }

    }

    /**
     * remove emojicon group
     * @param position
     */
    fun removeEmojiconGroup(position: Int) {
        emojiconGroupList.removeAt(position)
        pagerView?.removeEmojiconGroup(position)
        tabBar.removeTab(position)
    }

    fun setTabBarVisibility(isVisible: Boolean) {
        if (!isVisible) {
            tabBar.visibility = View.GONE
        } else {
            tabBar.visibility = View.VISIBLE
        }
    }


    private inner class PagerViewListener : EmojPagerView.EmojPagerViewListener {
        override fun onPagerViewInited(groupMaxPageSize: Int, firstGroupPageSize: Int) {
            indicatorView?.init(groupMaxPageSize)
            indicatorView?.updateIndicator(firstGroupPageSize)
            tabBar.selectedTo(0)
        }

        override fun onGroupPositionChanged(groupPosition: Int, pagerSizeOfGroup: Int) {
            indicatorView?.updateIndicator(pagerSizeOfGroup)
            tabBar.selectedTo(groupPosition)
        }

        override fun onGroupInnerPagePostionChanged(oldPosition: Int, newPosition: Int) {
            indicatorView?.selectTo(oldPosition, newPosition)
        }

        override fun onGroupPagePostionChangedTo(position: Int) {
        }

        override fun onGroupMaxPageSizeChanged(maxCount: Int) {
            indicatorView?.updateIndicator(maxCount)
        }

        override fun onDeleteImageClicked() {
            listener?.onDeleteImageClicked()
        }

        override fun onExpressionClicked(emojicon: Emoj) {
            listener?.onExpressionClicked(emojicon)
        }


    }

}