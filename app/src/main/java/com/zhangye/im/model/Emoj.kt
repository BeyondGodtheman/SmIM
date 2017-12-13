package com.zhangye.im.model

/**
 * EMOJ表情的数据类
 * Created by zhangye on 2017/12/11.
 */
class Emoj {

    /**
     * constructor
     * @param icon- resource id of the icon
     * @param emojiText- text of emoji icon
     */
   constructor(icon: Int, emojiText: String){
        this.icon = icon
        this.emojiText = emojiText
        this.type = Type.NORMAL
    }

    /**
     * constructor
     * @param icon - resource id of the icon
     * @param emojiText - text of emoji icon
     * @param type - normal or big
     */
    constructor(icon: Int, emojiText: String, type: Type){
        this.icon = icon
        this.emojiText = emojiText
        this.type = type
    }


    constructor()


    /**
     * identity code
     */
    private var identityCode: String? = null

    /**
     * static icon resource id
     */
    private var icon: Int = 0

    /**
     * dynamic icon resource id
     */
    private var bigIcon: Int = 0

    /**
     * text of emoji, could be null for big icon
     */
    private var emojiText: String? = null

    /**
     * name of emoji icon
     */
    private var name: String? = null

    /**
     * normal or big
     */
    private var type: Type? = null

    /**
     * path of icon
     */
    private var iconPath: String? = null

    /**
     * path of big icon
     */
    private var bigIconPath: String? = null


    /**
     * get the resource id of the icon
     * @return
     */
    fun getIcon(): Int {
        return icon
    }


    /**
     * set the resource id of the icon
     * @param icon
     */
    fun setIcon(icon: Int) {
        this.icon = icon
    }


    /**
     * get the resource id of the big icon
     * @return
     */
    fun getBigIcon(): Int {
        return bigIcon
    }


    /**
     * set the resource id of the big icon
     * @return
     */
    fun setBigIcon(dynamicIcon: Int) {
        this.bigIcon = dynamicIcon
    }


    /**
     * get text of emoji icon
     * @return
     */
    fun getEmojiText(): String? {
        return emojiText
    }


    /**
     * set text of emoji icon
     * @param emojiText
     */
    fun setEmojiText(emojiText: String) {
        this.emojiText = emojiText
    }

    /**
     * get name of emoji icon
     * @return
     */
    fun getName(): String? {
        return name
    }

    /**
     * set name of emoji icon
     * @param name
     */
    fun setName(name: String) {
        this.name = name
    }

    /**
     * get type
     * @return
     */
    fun getType(): Type? {
        return type
    }


    /**
     * set type
     * @param type
     */
    fun setType(type: Type) {
        this.type = type
    }


    /**
     * get icon path
     * @return
     */
    fun getIconPath(): String? {
        return iconPath
    }


    /**
     * set icon path
     * @param iconPath
     */
    fun setIconPath(iconPath: String) {
        this.iconPath = iconPath
    }


    /**
     * get path of big icon
     * @return
     */
    fun getBigIconPath(): String? {
        return bigIconPath
    }


    /**
     * set path of big icon
     * @param bigIconPath
     */
    fun setBigIconPath(bigIconPath: String) {
        this.bigIconPath = bigIconPath
    }

    /**
     * get identity code
     * @return
     */
    fun getIdentityCode(): String? {
        return identityCode
    }

    /**
     * set identity code
     * @param identityId
     */
    fun setIdentityCode(identityCode: String) {
        this.identityCode = identityCode
    }

    fun newEmojiText(codePoint: Int): String {
        return if (Character.charCount(codePoint) == 1) {
            codePoint.toString()
        } else {
            String(Character.toChars(codePoint))
        }
    }


    enum class Type {
        /**
         * normal icon, can be input one or more in edit view
         */
        NORMAL,
        /**
         * big icon, send out directly when your press it
         */
        BIG_EXPRESSION
    }
}