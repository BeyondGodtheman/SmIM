package com.zhangye.im.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.zhangye.im.model.Emoj

/**
 * Created by zhangye on 2017/12/11.
 */
open class EmojMenuBase : LinearLayout {
    var listener: EmojiconMenuListener? = null
    constructor(context: Context) : super(context)

    @SuppressLint("NewApi")
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)


    /**
     * set emojicon menu listener
     * @param listener
     */
    fun setEmojiconMenuListener(listener: EmojiconMenuListener) {
        this.listener = listener
    }

    interface EmojiconMenuListener {
        /**
         * on emojicon clicked
         * @param emojicon
         */
        fun onExpressionClicked(emojicon: Emoj)

        /**
         * on delete image clicked
         */
        fun onDeleteImageClicked()
    }
}