package com.zhangye.im.utils

import android.content.Context
import android.net.Uri
import android.text.Spannable
import android.text.style.ImageSpan
import com.zhangye.im.model.EmojDatas
import java.io.File
import java.util.*
import java.util.regex.Pattern

/**
 * Created by zhangye on 2017/12/11.
 */
object SmileUtils {

    val DELETE_KEY = "em_delete_delete_expression"

    val ee_1 = "[):]"
    val ee_2 = "[:D]"
    val ee_3 = "[;)]"
    val ee_4 = "[:-o]"
    val ee_5 = "[:p]"
    val ee_6 = "[(H)]"
    val ee_7 = "[:@]"
    val ee_8 = "[:s]"
    val ee_9 = "[:$]"
    val ee_10 = "[:(]"
    val ee_11 = "[:'(]"
    val ee_12 = "[:|]"
    val ee_13 = "[(a)]"
    val ee_14 = "[8o|]"
    val ee_15 = "[8-|]"
    val ee_16 = "[+o(]"
    val ee_17 = "[<o)]"
    val ee_18 = "[|-)]"
    val ee_19 = "[*-)]"
    val ee_20 = "[:-#]"
    val ee_21 = "[:-*]"
    val ee_22 = "[^o)]"
    val ee_23 = "[8-)]"
    val ee_24 = "[(|)]"
    val ee_25 = "[(u)]"
    val ee_26 = "[(S)]"
    val ee_27 = "[(*)]"
    val ee_28 = "[(#)]"
    val ee_29 = "[(R)]"
    val ee_30 = "[({)]"
    val ee_31 = "[(})]"
    val ee_32 = "[(k)]"
    val ee_33 = "[(F)]"
    val ee_34 = "[(W)]"
    val ee_35 = "[(D)]"

    private val spannableFactory = Spannable.Factory
            .getInstance()

    private val emoticons = HashMap<Pattern, Any>()



    fun initData() {
        EmojDatas.DATA.forEach {
            addPattern(it.getEmojiText(), it.getIcon())
        }
//        EaseEmojiconInfoProvider emojiconInfoProvider = EaseUI . getInstance ().getEmojiconInfoProvider();
//        if (emojiconInfoProvider != null && emojiconInfoProvider.getTextEmojiconMapping() != null) {
//            for (Entry< String, Object> entry : emojiconInfoProvider.getTextEmojiconMapping().entrySet()){
//                addPattern(entry.getKey(), entry.getValue());
//            }
//        }
    }

    /**
     * add text and icon to the map
     * @param emojiText-- text of emoji
     * @param icon -- resource id or local path
     */
    fun addPattern(emojiText: String?, icon: Any?) {
        emoticons.put(Pattern.compile(Pattern.quote(emojiText)), icon!!)
    }


    /**
     * replace existing spannable with smiles
     * @param context
     * @param spannable
     * @return
     */
    fun addSmiles(context: Context, spannable: Spannable): Boolean {

        if(emoticons.size == 0){
            initData()
        }

        var hasChanges = false
        for ((key, value) in emoticons) {
            val matcher = key.matcher(spannable)
            while (matcher.find()) {
                var set = true
                for (span in spannable.getSpans(matcher.start(),
                        matcher.end(), ImageSpan::class.java))
                    if (spannable.getSpanStart(span) >= matcher.start() && spannable.getSpanEnd(span) <= matcher.end())
                        spannable.removeSpan(span)
                    else {
                        set = false
                        break
                    }
                if (set) {
                    hasChanges = true
                    if (value is String && !value.startsWith("http")) {
                        val file = File(value)
                        if (!file.exists() || file.isDirectory) {
                            return false
                        }
                        spannable.setSpan(ImageSpan(context, Uri.fromFile(file)),
                                matcher.start(), matcher.end(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    } else {
                        spannable.setSpan(ImageSpan(context, value as Int),
                                matcher.start(), matcher.end(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                }
            }
        }

        return hasChanges
    }

    fun getSmiledText(context: Context, text: CharSequence): Spannable {
        val spannable = spannableFactory.newSpannable(text)
        addSmiles(context, spannable)
        return spannable
    }

    fun containsKey(key: String): Boolean {
        var b = false
        for ((key1) in emoticons) {
            val matcher = key1.matcher(key)
            if (matcher.find()) {
                b = true
                break
            }
        }

        return b
    }

    fun getSmilesSize(): Int {
        return emoticons.size
    }
}