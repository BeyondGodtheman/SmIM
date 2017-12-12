package com.zhangye.im.widget

import android.content.Context
import android.text.TextUtils
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
    private var buttonSetModeVoice: View? = null
    private var buttonSend: View? = null
    private var buttonPressToSpeak: View? = null
    private var buttonMore: Button? = null
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
//        buttonSetModeKeyboard = findViewById(R.id.btn_set_mode_keyboard)
//        edittext_layout = findViewById(R.id.edittext_layout) as RelativeLayout
//        buttonSetModeVoice = findViewById(R.id.btn_set_mode_voice)
//        buttonSend = findViewById(R.id.btn_send)
//        buttonPressToSpeak = findViewById(R.id.btn_press_to_speak)
//        faceNormal = findViewById(R.id.iv_face_normal) as ImageView
        edt_msg.setOnClickListener(this)
        //表情键盘切换
        cb_face.setOnCheckedChangeListener({ _: CompoundButton, b: Boolean ->

            if (cb_type.isChecked) {
                cb_type.isChecked = false
            }

            listener?.onToggleEmojClicked()
        })

        //录音
        cb_type.setOnCheckedChangeListener({ _: CompoundButton, b: Boolean ->
            if (cb_face.isChecked) {
                cb_face.isChecked = false
            }
        })

//        val faceLayout = findViewById(R.id.rl_face) as RelativeLayout
//        buttonMore = findViewById(R.id.btn_more) as Button
//        edittext_layout?.setBackgroundResource(R.drawable.ease_input_bar_bg_normal)

//        buttonSend!!.setOnClickListener(this)
//        buttonSetModeKeyboard!!.setOnClickListener(this)
//        buttonSetModeVoice!!.setOnClickListener(this)
//        buttonMore?.setOnClickListener(this)
//        faceLayout.setOnClickListener(this)
        edt_msg.setOnClickListener(this)
        edt_msg.requestFocus()



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

        edt_msg.setOnEditorActionListener { v, actionId, event ->
            //            LogUtils.d("keyCode:" + event.keyCode + " action" + event.action + " ctrl:" + ctrlPress)
            if (actionId == EditorInfo.IME_ACTION_SEND || event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN && ctrlPress) {
                val s = edt_msg.text.toString()
                edt_msg.setText("")
                listener?.onSendBtnClicked(s)
                true
            } else {
                false
            }
        }


        buttonPressToSpeak?.setOnTouchListener { v, event ->
            if (listener != null) {
                listener?.onPressToSpeakBtnTouch(v, event)!!
            } else false
        }
    }

    /**
     * set recorder view when speak icon is touched
     */
//    fun setPressToSpeakRecorderView(voiceRecorderView: EaseVoiceRecorderView) {
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

        when(view.id){
            edt_msg.id ->{
                cb_face.isChecked = false
                listener?.onEditTextClicked()
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
        buttonSetModeVoice!!.visibility = View.GONE
//            buttonSetModeKeyboard!!.visibility = View.VISIBLE
        buttonSend!!.visibility = View.GONE
        buttonMore!!.visibility = View.VISIBLE
        buttonPressToSpeak!!.visibility = View.VISIBLE
    }

    /**
     * show keyboard
     */
    fun setModeKeyboard() {
//            edittext_layout!!.visibility = View.VISIBLE
//            buttonSetModeKeyboard!!.visibility = View.GONE
        buttonSetModeVoice!!.visibility = View.VISIBLE
        // mEditTextContent.setVisibility(View.VISIBLE);
        edt_msg.requestFocus()
        // buttonSend.setVisibility(View.VISIBLE);
        buttonPressToSpeak!!.visibility = View.GONE
        if (TextUtils.isEmpty(edt_msg.text)) {
            buttonMore!!.visibility = View.VISIBLE
            buttonSend!!.visibility = View.GONE
        } else {
            buttonMore!!.visibility = View.GONE
            buttonSend!!.visibility = View.VISIBLE
        }

    }


    override fun onTextInsert(text: CharSequence) {
        val start = edt_msg.selectionStart
        val editable = edt_msg.editableText
        editable.insert(start, text)
        setModeKeyboard()
    }

}