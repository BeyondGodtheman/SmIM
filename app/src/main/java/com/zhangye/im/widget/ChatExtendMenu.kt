package com.zhangye.im.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.zhangye.im.R
import com.zhangye.im.utils.DensityUtil
import java.util.ArrayList

/**
 * Created by zhangye on 2017/12/11.
 */
class ChatExtendMenu : GridView {

    private val itemModels = ArrayList<ChatMenuItemModel>()

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : this(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ChatExtendMenu)
        val numColumns = ta.getInt(R.styleable.ChatExtendMenu_numColumns, 4)
        ta.recycle()

        setNumColumns(numColumns)
        stretchMode = GridView.STRETCH_COLUMN_WIDTH
        gravity = Gravity.CENTER_VERTICAL
        verticalSpacing = DensityUtil.dip2px(context, 8f)
    }

    /**
     * init
     */
    fun init() {
        adapter = ItemAdapter(itemModels)
    }



    fun registerMenuItem(name: String, drawableRes: Int, itemId: Int, listener: ChatExtendMenuItemClickListener) {
        val item = ChatMenuItemModel()
        item.name = name
        item.image = drawableRes
        item.id = itemId
        item.clickListener = listener
        itemModels.add(item)
    }

    /**
     * register menu item
     *
     * @param nameRes
     * resource id of item name
     * @param drawableRes
     * background of item
     * @param itemId
     * id
     * @param listener
     * on click event of item
     */
    fun registerMenuItem(nameRes: Int, drawableRes: Int, itemId: Int, listener: ChatExtendMenuItemClickListener) {
        registerMenuItem(context.getString(nameRes), drawableRes, itemId, listener)
    }


    private inner class ItemAdapter(objects: List<ChatMenuItemModel>) : ArrayAdapter<ChatMenuItemModel>(context, 1, objects) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var mConvertView = convertView
            var menuItem: ChatMenuItem? = null
            if (mConvertView == null) {
                mConvertView = ChatMenuItem(context)
            }
            menuItem = mConvertView as ChatMenuItem?
            menuItem!!.setImage(getItem(position)!!.image)
            menuItem.setText(getItem(position)!!.name)
            menuItem.setOnClickListener { v ->
                if (getItem(position)!!.clickListener != null) {
                    getItem(position)!!.clickListener!!.onClick(getItem(position)!!.id, v)
                }
            }
            return mConvertView
        }


    }


    interface ChatExtendMenuItemClickListener {
        fun onClick(itemId: Int, view: View)
    }


    internal inner class ChatMenuItemModel {
        var name: String? = null
        var image: Int = 0
        var id: Int = 0
        var clickListener: ChatExtendMenuItemClickListener? = null
    }

    internal inner class ChatMenuItem : LinearLayout {
        private lateinit var imageView: ImageView
        private lateinit var textView: TextView

        constructor(context: Context, attrs: AttributeSet, defStyle: Int) : this(context, attrs) {}

        constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
            init(context, attrs)
        }

        constructor(context: Context) : super(context) {
            init(context, null)
        }

        private fun init(context: Context, attrs: AttributeSet?) {
            LayoutInflater.from(context).inflate(R.layout.item_chat_menu, this)
            imageView = findViewById(R.id.image)
            textView = findViewById(R.id.text)
        }

        fun setImage(resid: Int) {
            imageView.setBackgroundResource(resid)
        }

        fun setText(resid: Int) {
            textView.setText(resid)
        }

        fun setText(text: String?) {
            textView.text = text
        }
    }
}
