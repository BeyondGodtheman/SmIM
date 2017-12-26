package com.zhangye.im.widget

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.cocosh.shmstore.utils.LogUtils
import com.zhangye.im.R
import kotlinx.android.synthetic.main.widget_chat_primary_menu.view.*

/**
 *
 * Created by zhangye on 2017/12/11.
 */
open class ChatPrimaryMenu : ChatPrimaryMenuBase, View.OnClickListener {

    private var ctrlPress = false

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
        LayoutInflater.from(context).inflate(R.layout.widget_chat_primary_menu, this)
        edt_msg.setOnClickListener(this)
        edt_msg.requestFocus()
        //发送按钮事件
        tv_send.setOnClickListener(this)


        //表情键盘切换
        cb_face.setOnCheckedChangeListener({ _: CompoundButton, b: Boolean ->

            if (b) {
                cb_add.isChecked = false
                cb_type.isChecked = false
            }
            listener?.onToggleEmojClicked(b)
        })

        //录音
        cb_type.setOnCheckedChangeListener({ _: CompoundButton, b: Boolean ->


            LogUtils.i("点击消息类型：$b")

            if (b) {
                cb_face.isChecked = false
                cb_add.isChecked = false

                tv_send.visibility = View.GONE
                cb_add.visibility = View.VISIBLE

                edt_msg.visibility = View.GONE
                btn_record.visibility = View.VISIBLE

                hideKeyboard()


            } else {
                edt_msg.visibility = View.VISIBLE
                btn_record.visibility = View.GONE

                if (edt_msg.text.isNotEmpty()) {
                    tv_send.visibility = View.VISIBLE
                    cb_add.visibility = View.GONE
                }

                edt_msg.requestFocus()
            }
        })

        //扩展类型事件
        cb_add.setOnCheckedChangeListener({ _: CompoundButton, b: Boolean ->


            if (b) {
                cb_face.isChecked = false
                cb_type.isChecked = false
            }

            listener?.onToggleExtendClicked(b)
        })


        //拦截键盘事件
        edt_msg.setOnKeyListener { _, keyCode, event ->
            LogUtils.d("keyCode:" + keyCode + " action:" + event.action)

            // test on Mac virtual machine: ctrl map to KEYCODE_UNKNOWN
            if (keyCode == KeyEvent.KEYCODE_UNKNOWN) {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    ctrlPress = true
                } else if (event.action == KeyEvent.ACTION_UP) {
                    ctrlPress = false
                }
            }
            false
        }

        //拦截键盘事件
        edt_msg.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND || event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN && ctrlPress) {
                val s = edt_msg.text.toString()
                edt_msg.setText("")
                listener?.onSendBtnClicked(s)
                true
            } else {
                false
            }
        }

        //根据输入切换按钮
        edt_msg.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!TextUtils.isEmpty(s)) {
                    cb_add.visibility = View.GONE
                    tv_send.visibility = View.VISIBLE
                } else {
                    cb_add.visibility = View.VISIBLE
                    tv_send.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable) {

            }
        })


        btn_record.setOnTouchListener { v, event ->
            return@setOnTouchListener listener?.onPressToSpeakBtnTouch(v, event) ?: false
        }
    }

    /**
     * set recorder view when speak icon is touched
     */
//    fun setPressToSpeakRecorderView(voiceRecorderView: VoiceRecorderView) {
//        val voiceRecorderView1 = voiceRecorderView
//    }

    /**
     * append emoji icon to editText
     * @param emojiContent
     */
    override fun onEmojiconInputEvent(emojiContent: CharSequence) {
        edt_msg.append(emojiContent)
    }

    /**
     * delete emojicon
     */
    override fun onEmojiconDeleteEvent() {
        if (!TextUtils.isEmpty(edt_msg.text)) {
            val event = KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL)
            edt_msg.dispatchKeyEvent(event)
        }
    }

    /**
     * on clicked event
     * @param view
     */
    override fun onClick(view: View) {

        when (view.id) {
            edt_msg.id -> {
                listener?.onEditTextClicked()
            }

            tv_send.id -> {
                if (listener != null) {
                    val s = edt_msg.text.toString()
                    edt_msg.setText("")
                    listener?.onSendBtnClicked(s)
                }
            }

        }

//        val id = view.id
//        if (id == R.id.btn_send) {
//            if (listener != null) {
//                val s = editText!!.text.toString()
//                editText!!.setText("")
//                listener?.onSendBtnClicked(s)
//            }
//        } else if (id == R.id.btn_set_mode_voice) {
//            setModeVoice()
//            showNormalFaceImage()
//            if (listener != null)
//                listener?.onToggleVoiceBtnClicked()
//        } else if (id == R.id.btn_set_mode_keyboard) {
//            setModeKeyboard()
//            showNormalFaceImage()
//            if (listener != null)
//                listener?.onToggleVoiceBtnClicked()
//        } else if (id == R.id.btn_more) {
//            buttonSetModeVoice!!.visibility = View.VISIBLE
//            buttonSetModeKeyboard!!.visibility = View.GONE
//            edittext_layout!!.visibility = View.VISIBLE
//            buttonPressToSpeak!!.visibility = View.GONE
//            showNormalFaceImage()
//            if (listener != null)
//                listener?.onToggleExtendClicked()
//        } else if (id == R.id.et_sendmessage) {
//            edittext_layout!!.setBackgroundResource(R.drawable.ease_input_bar_bg_active)
//            faceNormal!!.visibility = View.VISIBLE
//            faceChecked!!.visibility = View.INVISIBLE
//            if (listener != null)
//                listener?.onEditTextClicked()
//        }
//        else if (id == R.id.rl_face) {
//            toggleFaceImage()
//            if (listener != null) {
//                listener?.onToggleEmojClicked()
//            }
//        } else {
//        }
    }


    /**
     * show voice icon when speak bar is touched
     *
     */
    protected fun setModeVoice() {
        hideKeyboard()
//            edittext_layout!!.visibility = View.GONE
//        buttonSetModeVoice!!.visibility = View.GONE
//            buttonSetModeKeyboard!!.visibility = View.VISIBLE
//        buttonSend!!.visibility = View.GONE
//        buttonMore!!.visibility = View.VISIBLE
//        buttonPressToSpeak!!.visibility = View.VISIBLE
    }

    /**
     * show keyboard
     */
    fun setModeKeyboard() {
//            edittext_layout!!.visibility = View.VISIBLE
//            buttonSetModeKeyboard!!.visibility = View.GONE
//        buttonSetModeVoice!!.visibility = View.VISIBLE
        // mEditTextContent.setVisibility(View.VISIBLE);
        edt_msg.requestFocus()
        // buttonSend.setVisibility(View.VISIBLE);
//        buttonPressToSpeak!!.visibility = View.GONE
//        if (TextUtils.isEmpty(edt_msg.text)) {
//            buttonMore!!.visibility = View.VISIBLE
//            buttonSend!!.visibility = View.GONE
//        } else {
//            buttonMore!!.visibility = View.GONE
//            buttonSend!!.visibility = View.VISIBLE
//        }

    }


    override fun onTextInsert(text: CharSequence) {
        val start = edt_msg.selectionStart
        val editable = edt_msg.editableText
        editable.insert(start, text)
        setModeKeyboard()
    }

}