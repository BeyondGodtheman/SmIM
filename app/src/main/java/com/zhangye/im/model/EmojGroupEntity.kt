package com.zhangye.im.model

import com.zhangye.im.model.Emoj.Type

/**
 * 一组表情所对应的实体类
 * Created by zhangye on 2017/12/11.
 */
class EmojGroupEntity {
    /**
     * 表情数据
     */
    private var emojiconList: ArrayList<Emoj>? = null
    /**
     * 图片
     */
    private var icon: Int = 0
    /**
     * 组名
     */
    private var name: String? = null
    /**
     * 表情类型
     */
    private var type: Type? = null

    constructor() {}

    constructor(icon: Int, emojiconList: ArrayList<Emoj>) {
        this.icon = icon
        this.emojiconList = emojiconList
        type = Type.NORMAL
    }

    fun EaseEmojiconGroupEntity(icon: Int, emojiconList: ArrayList<Emoj>, type: Type) {
        this.icon = icon
        this.emojiconList = emojiconList
        this.type = type
    }

    fun getEmojiconList(): ArrayList<Emoj>? {
        return emojiconList
    }

    fun setEmojiconList(emojiconList: ArrayList<Emoj>) {
        this.emojiconList = emojiconList
    }

    fun getIcon(): Int {
        return icon
    }

    fun setIcon(icon: Int) {
        this.icon = icon
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getType(): Type? {
        return type
    }

    fun setType(type: Type) {
        this.type = type
    }
}