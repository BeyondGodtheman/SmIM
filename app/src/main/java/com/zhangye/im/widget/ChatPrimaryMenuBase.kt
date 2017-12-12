package com.zhangye.im.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout

/**
 *
 * Created by zhangye on 2017/12/11.
 */
abstract class ChatPrimaryMenuBase : RelativeLayout {
    protected var listener: ChatPrimaryMenuListener?= null
    protected var inputManager: InputMethodManager? = null

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    private fun init(context: Context) {
        inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    /**
     * set primary menu listener
     * @param listener
     */
    fun setChatPrimaryMenuListener(listener: ChatPrimaryMenuListener) {
        this.listener = listener
    }

    /**
     * emoji icon input event
     * @param emojiContent
     */
    abstract fun onEmojiconInputEvent(emojiContent: CharSequence)

    /**
     * emoji icon delete event
     */
    abstract fun onEmojiconDeleteEvent()

    /**
     * hide extend menu
     */


    /**
     * insert text
     * @param text
     */
    abstract fun onTextInsert(text: CharSequence)

    /**
     * hide keyboard
     */
    fun hideKeyboard() {
        if ((context as Activity).window.attributes.softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if ((context as Activity).currentFocus != null)
                inputManager?.hideSoftInputFromWindow((context as Activity).currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }


    interface ChatPrimaryMenuListener {
        /**
         * when send button clicked
         * @param content
         */
        fun onSendBtnClicked(content: String)

        /**
         * when speak button is touched
         * @return
         */
        fun onPressToSpeakBtnTouch(v: View, event: MotionEvent): Boolean


        /**
         * toggle on/off voice button
         */
        fun onToggleVoiceBtnClicked()

        /**
         * toggle on/off extend menu
         */
        fun onToggleExtendClicked()

        /**
         * toggle on/off emoji icon
         */
        fun onToggleEmojClicked()

        /**
         * on text input is clicked
         */
        fun onEditTextClicked()
    }

}