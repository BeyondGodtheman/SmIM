package com.zhangye.im.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.zhangye.im.R
import com.zhangye.im.model.Emoj
import com.zhangye.im.model.EmojDatas
import com.zhangye.im.model.EmojGroupEntity
import com.zhangye.im.utils.SmileUtils

/**
 *
 * Created by zhangye on 2017/12/11.
 */
class ChatInputMenu : LinearLayout {


    var inputContainer: FrameLayout
    var emojContainer: FrameLayout
    lateinit var chatPrimaryMenu: ChatPrimaryMenuBase
    lateinit var emojMenu: EmojMenuBase
    var chatExtendMenu: ChatExtendMenu
    var chatExtendMenuContainer: FrameLayout


    var listener: ChatInputMenuListener? = null
    var inited: Boolean = false


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)


    init {
        LayoutInflater.from(context).inflate(R.layout.layout_chat_input, this)
        inputContainer = findViewById(R.id.input_container)
        emojContainer = findViewById(R.id.emoj_container)
        chatExtendMenuContainer = findViewById(R.id.extend_container)

        // extend menu
        chatExtendMenu = findViewById(R.id.extend)
    }


    /**
     * init view
     *
     * This method should be called after registerExtendMenuItem(), setCustomEmojiconMenu() and setCustomPrimaryMenu().
     * @param emojGroupList --will use default if null
     */
    @SuppressLint("InflateParams")
    fun initMenu(emojGroupList: MutableList<EmojGroupEntity>?) {
        var mEmojGroupList = emojGroupList
        if (inited) {
            return
        }
//        //默认输入框
        chatPrimaryMenu = LayoutInflater.from(context).inflate(R.layout.layout_chat_primary_menu, null) as ChatPrimaryMenu
        inputContainer.addView(chatPrimaryMenu)

        // emoj输入键盘
        emojMenu = LayoutInflater.from(context).inflate(R.layout.layout_emojicon_menu, null) as EmojMenu
        if (mEmojGroupList == null) {
            mEmojGroupList = arrayListOf()
            mEmojGroupList.add(EmojGroupEntity(R.mipmap.ee_1, EmojDatas.DATA))
        }
        emojContainer.addView(emojMenu)

        (emojMenu as EmojMenu).init(mEmojGroupList)

        processChatMenu()
        chatExtendMenu.init()

        inited = true
    }


    /**
     * set custom emojicon menu
     * @param customEmojiconMenu
     */
    fun setCustomEmojiconMenu(customEmojiconMenu: EmojMenuBase) {
        this.emojMenu = customEmojiconMenu
    }

    /**
     * set custom primary menu
     * @param customPrimaryMenu
     */
    fun setCustomPrimaryMenu(customPrimaryMenu: ChatPrimaryMenuBase) {
        this.chatPrimaryMenu = customPrimaryMenu
    }

    fun getPrimaryMenu(): ChatPrimaryMenuBase? {
        return chatPrimaryMenu
    }

    fun getExtendMenu(): ChatExtendMenu {
        return chatExtendMenu
    }

    fun getEmojiconMenu(): EmojMenuBase? {
        return emojMenu
    }


    /**
     * register menu item
     *
     * @param name
     * item name
     * @param drawableRes
     * background of item
     * @param itemId
     * id
     * @param listener
     * on click event of item
     */
    fun registerExtendMenuItem(name: String, drawableRes: Int, itemId: Int,
                               listener: ChatExtendMenu.ChatExtendMenuItemClickListener) {
        chatExtendMenu.registerMenuItem(name, drawableRes, itemId, listener)
    }

    /**
     * register menu item
     *
     * @param name
     * resource id of item name
     * @param drawableRes
     * background of item
     * @param itemId
     * id
     * @param listener
     * on click event of item
     */
    fun registerExtendMenuItem(nameRes: Int, drawableRes: Int, itemId: Int,
                               listener: ChatExtendMenu.ChatExtendMenuItemClickListener) {
        chatExtendMenu.registerMenuItem(nameRes, drawableRes, itemId, listener)
    }


    private fun processChatMenu() {
        // send message button
        chatPrimaryMenu.setChatPrimaryMenuListener(object : ChatPrimaryMenuBase.ChatPrimaryMenuListener {

            override fun onSendBtnClicked(content: String) {
                listener?.onSendMessage(content)
            }


            override fun onToggleVoiceBtnClicked() {
                hideExtendMenuContainer()
            }

            override fun onToggleExtendClicked() {
                toggleMore()
            }

            override fun onToggleEmojClicked() {
                toggleEmoj()
            }

            override fun onEditTextClicked() {
                hideExtendMenuContainer()
            }


            override fun onPressToSpeakBtnTouch(v: View, event: MotionEvent): Boolean {
                return if (listener != null) {
                    listener!!.onPressToSpeakBtnTouch(v, event)
                } else false
            }
        })

        // emojicon menu
        emojMenu.setEmojiconMenuListener(object : EmojMenuBase.EmojiconMenuListener {

            override fun onExpressionClicked(emojicon: Emoj) {
                if (emojicon.getType() != Emoj.Type.BIG_EXPRESSION) {
                    if (emojicon.getEmojiText() != null) {
                        chatPrimaryMenu.onEmojiconInputEvent(SmileUtils.getSmiledText(context, emojicon.getEmojiText()!!))
                    }
                } else {
                    listener?.onBigExpressionClicked(emojicon)
                }
            }

            override fun onDeleteImageClicked() {
                chatPrimaryMenu.onEmojiconDeleteEvent()
            }
        })

    }


    /**
     * insert text
     * @param text
     */
    fun insertText(text: String) {
        getPrimaryMenu()!!.onTextInsert(text)
    }

    /**
     * show or hide extend menu
     *
     */
    private fun toggleMore() {
        if (chatExtendMenuContainer.visibility == View.GONE) {
            hideKeyboard()
            handler.postDelayed({
                chatExtendMenuContainer.visibility = View.VISIBLE
                chatExtendMenu.visibility = View.VISIBLE
                emojMenu.visibility = View.GONE
            }, 50)
        } else {
            if (emojMenu.visibility == View.VISIBLE) {
                emojMenu.visibility = View.GONE
                chatExtendMenu.visibility = View.VISIBLE
            } else {
                chatExtendMenuContainer.visibility = View.GONE
            }
        }
    }

    /**
     * show or hide emojicon
     */
    private fun toggleEmoj() {
        if (chatExtendMenuContainer.visibility == View.GONE) {
            hideKeyboard()
            handler.postDelayed({
                chatExtendMenuContainer.visibility = View.VISIBLE
                chatExtendMenu.visibility = View.GONE
                emojMenu.visibility = View.VISIBLE
            }, 50)
        } else {
            if (emojMenu.visibility == View.VISIBLE) {
                chatExtendMenuContainer.visibility = View.GONE
                emojMenu.visibility = View.GONE
            } else {
                chatExtendMenu.visibility = View.GONE
                emojMenu.visibility = View.VISIBLE
            }

        }
    }

    /**
     * hide keyboard
     */
    private fun hideKeyboard() {
        chatPrimaryMenu.hideKeyboard()
    }

    /**
     * hide extend menu
     */
    fun hideExtendMenuContainer() {
        chatExtendMenu.visibility = View.GONE
        emojMenu.visibility = View.GONE
        chatExtendMenuContainer.visibility = View.GONE
    }

    /**
     * when back key pressed
     *
     * @return false--extend menu is on, will hide it first
     * true --extend menu is off
     */
    fun onBackPressed(): Boolean {
        if (chatExtendMenuContainer.visibility == View.VISIBLE) {
            hideExtendMenuContainer()
            return false
        } else {
            return true
        }

    }


    fun setChatInputMenuListener(listener: ChatInputMenuListener) {
        this.listener = listener
    }

    interface ChatInputMenuListener {
        /**
         * when send message button pressed
         *
         * @param content
         * message content
         */
        fun onSendMessage(content: String)

        /**
         * when big icon pressed
         * @param emojicon
         */
        fun onBigExpressionClicked(emojicon: Emoj)

        /**
         * when speak button is touched
         * @param v
         * @param event
         * @return
         */
        fun onPressToSpeakBtnTouch(v: View, event: MotionEvent): Boolean
    }
}

