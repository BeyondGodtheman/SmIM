package com.zhangye.im.widget

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import com.zhangye.im.R
import com.zhangye.im.adapter.EmojGridAdapter
import com.zhangye.im.adapter.EmojPagerAdapter
import com.zhangye.im.model.Emoj
import com.zhangye.im.model.EmojGroupEntity
import com.zhangye.im.utils.SmileUtils
import java.util.ArrayList

/**
 *
 * Created by zhangye on 2017/12/11.
 */
class EmojPagerView : ViewPager {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    private var groupEntities: List<EmojGroupEntity>? = null

    private var pagerAdapter: PagerAdapter? = null

    private val emojiconRows = 3
    private var emojiconColumns = 7

    private val bigEmojiconRows = 2
    private var bigEmojiconColumns = 4

    private var firstGroupPageSize: Int = 0

    private var maxPageCount: Int = 0
    private var previousPagerPosition: Int = 0
    private var pagerViewListener: EmojPagerViewListener? = null
    private var viewpages: MutableList<View>? = null


    fun init(emojiconGroupList: List<EmojGroupEntity>?, emijiconColumns: Int, bigEmojiconColumns: Int) {
        if (emojiconGroupList == null) {
            throw RuntimeException("emojiconGroupList is null")
        }

        this.groupEntities = emojiconGroupList
        this.emojiconColumns = emijiconColumns
        this.bigEmojiconColumns = bigEmojiconColumns

        viewpages = ArrayList()
        for (i in groupEntities!!.indices) {
            val group = groupEntities!![i]
            val groupEmojicons = group.getEmojiconList()
            val gridViews = getGroupGridViews(group)
            if (i == 0) {
                firstGroupPageSize = gridViews.size
            }
            maxPageCount = Math.max(gridViews.size, maxPageCount)
            viewpages!!.addAll(gridViews)
        }

        pagerAdapter = EmojPagerAdapter(viewpages!!)
        adapter = pagerAdapter
        setOnPageChangeListener(EmojiPagerChangeListener())

        if (pagerViewListener != null) {
            pagerViewListener!!.onPagerViewInited(maxPageCount, firstGroupPageSize)
        }
    }

    fun setPagerViewListener(pagerViewListener: EmojPagerViewListener) {
        this.pagerViewListener = pagerViewListener
    }


    /**
     * set emojicon group position
     * @param position
     */
    fun setGroupPostion(position: Int) {
        if (adapter != null && position >= 0 && position < groupEntities!!.size) {
            var count = 0
            for (i in 0 until position) {
                count += getPageSize(groupEntities!![i])
            }
            currentItem = count
        }
    }

    /**
     * get emojicon group gridview list
     * @param groupEntity
     * @return
     */
    fun getGroupGridViews(groupEntity: EmojGroupEntity): List<View> {
        val emojiconList = groupEntity.getEmojiconList()
        var itemSize = emojiconColumns * emojiconRows - 1
        val totalSize = emojiconList?.size
        val emojiType = groupEntity.getType()
        if (emojiType === Emoj.Type.BIG_EXPRESSION) {
            itemSize = bigEmojiconColumns * bigEmojiconRows
        }
        val pageSize = if (totalSize!! % itemSize == 0) totalSize / itemSize else totalSize / itemSize + 1
        val views = ArrayList<View>()
        for (i in 0 until pageSize) {
            val view = View.inflate(context, R.layout.layout_expression_gridview, null)
            val gv = view.findViewById<GridView>(R.id.gridview)
            if (emojiType == Emoj.Type.BIG_EXPRESSION) {
                gv.numColumns = bigEmojiconColumns
            } else {
                gv.numColumns = emojiconColumns
            }
            val list = ArrayList<Emoj>()
            if (i != pageSize - 1) {
                list.addAll(emojiconList.subList(i * itemSize, (i + 1) * itemSize))
            } else {
                list.addAll(emojiconList.subList(i * itemSize, totalSize))
            }
            if (emojiType !== Emoj.Type.BIG_EXPRESSION) {
                val deleteIcon = Emoj()
                deleteIcon.setEmojiText(SmileUtils.DELETE_KEY)
                list.add(deleteIcon)
            }
            val gridAdapter = EmojGridAdapter(context, 1, list, emojiType!!)
            gv.adapter = gridAdapter
            gv.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val emojicon = gridAdapter.getItem(position)
                if (pagerViewListener != null) {
                    val emojiText = emojicon.getEmojiText()
                    if (emojiText != null && emojiText == SmileUtils.DELETE_KEY) {
                        pagerViewListener?.onDeleteImageClicked()
                    } else {
                        pagerViewListener?.onExpressionClicked(emojicon)
                    }

                }
            }

            views.add(view)
        }
        return views
    }


    /**
     * add emojicon group
     * @param groupEntity
     */
    fun addEmojiconGroup(groupEntity: EmojGroupEntity, notifyDataChange: Boolean) {
        val pageSize = getPageSize(groupEntity)
        if (pageSize > maxPageCount) {
            maxPageCount = pageSize
            if (pagerViewListener != null && pagerAdapter != null) {
                pagerViewListener?.onGroupMaxPageSizeChanged(maxPageCount)
            }
        }
        viewpages!!.addAll(getGroupGridViews(groupEntity))
        if (pagerAdapter != null && notifyDataChange) {
            pagerAdapter?.notifyDataSetChanged()
        }
    }

    /**
     * remove emojicon group
     * @param position
     */
    fun removeEmojiconGroup(position: Int) {
        if (position > groupEntities!!.size - 1) {
            return
        }
        if (pagerAdapter != null) {
            pagerAdapter!!.notifyDataSetChanged()
        }
    }

    /**
     * get size of pages
     * @return
     */
    private fun getPageSize(groupEntity: EmojGroupEntity): Int {
        val emojiconList = groupEntity.getEmojiconList()
        var itemSize = emojiconColumns * emojiconRows - 1
        val totalSize = emojiconList?.size
        val emojiType = groupEntity.getType()
        if (emojiType === Emoj.Type.BIG_EXPRESSION) {
            itemSize = bigEmojiconColumns * bigEmojiconRows
        }
        return if (totalSize!! % itemSize == 0) totalSize / itemSize else totalSize / itemSize + 1
    }

    private inner class EmojiPagerChangeListener : ViewPager.OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            var endSize = 0
            var groupPosition = 0
            for (groupEntity in groupEntities!!) {
                val groupPageSize = getPageSize(groupEntity)
                //if the position is in current group
                if (endSize + groupPageSize > position) {
                    //this is means user swipe to here from previous page
                    if (previousPagerPosition - endSize < 0) {
                        if (pagerViewListener != null) {
                            pagerViewListener?.onGroupPositionChanged(groupPosition, groupPageSize)
                            pagerViewListener?.onGroupPagePostionChangedTo(0)
                        }
                        break
                    }
                    //this is means user swipe to here from back page
                    if (previousPagerPosition - endSize >= groupPageSize) {
                        if (pagerViewListener != null) {
                            pagerViewListener?.onGroupPositionChanged(groupPosition, groupPageSize)
                            pagerViewListener?.onGroupPagePostionChangedTo(position - endSize)
                        }
                        break
                    }

                    //page changed
                    if (pagerViewListener != null) {
                        pagerViewListener?.onGroupInnerPagePostionChanged(previousPagerPosition - endSize, position - endSize)
                    }
                    break

                }
                groupPosition++
                endSize += groupPageSize
            }

            previousPagerPosition = position
        }

        override fun onPageScrollStateChanged(arg0: Int) {}
        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
    }


    interface EmojPagerViewListener {
        /**
         * pagerview initialized
         * @param groupMaxPageSize --max pages size
         * @param firstGroupPageSize-- size of first group pages
         */
        fun onPagerViewInited(groupMaxPageSize: Int, firstGroupPageSize: Int)

        /**
         * group position changed
         * @param groupPosition--group position
         * @param pagerSizeOfGroup--page size of group
         */
        fun onGroupPositionChanged(groupPosition: Int, pagerSizeOfGroup: Int)

        /**
         * page position changed
         * @param oldPosition
         * @param newPosition
         */
        fun onGroupInnerPagePostionChanged(oldPosition: Int, newPosition: Int)

        /**
         * group page position changed
         * @param position
         */
        fun onGroupPagePostionChangedTo(position: Int)

        /**
         * max page size changed
         * @param maxCount
         */
        fun onGroupMaxPageSizeChanged(maxCount: Int)

        fun onDeleteImageClicked()
        fun onExpressionClicked(emojicon: Emoj)

    }

}