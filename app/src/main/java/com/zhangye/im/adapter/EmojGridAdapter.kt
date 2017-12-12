package com.zhangye.im.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.zhangye.im.R
import com.zhangye.im.model.Emoj
import com.zhangye.im.utils.SmileUtils

/**
 *
 * Created by zhangye on 2017/12/11.
 */
class EmojGridAdapter(context: Context, textViewResourceId: Int, objects: List<Emoj>, var emojiconType: Emoj.Type) : ArrayAdapter<Emoj>(context, textViewResourceId, objects) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var mConvertView = convertView
        if (mConvertView == null) {
            mConvertView = if (emojiconType === Emoj.Type.BIG_EXPRESSION) {
                View.inflate(context, R.layout.layout_row_big_expression, null)
            } else {
                View.inflate(context, R.layout.layout_row_expression, null)
            }
        }

        val imageView = mConvertView?.findViewById<ImageView>(R.id.iv_expression)
        val textView = mConvertView?.findViewById<TextView>(R.id.tv_name)
        val emojicon = getItem(position)
        if (emojicon.getName() != null) {
            textView?.text = emojicon.getName()
        }

        if (SmileUtils.DELETE_KEY == (emojicon.getEmojiText())) {
            imageView?.setImageResource(R.mipmap.icon_delete_expression)
        } else {
            if (emojicon.getIcon() != 0) {
                imageView?.setImageResource(emojicon.getIcon())
            } else if (emojicon.getIconPath() != null) {
                Glide.with(context).load(emojicon.getIconPath()).placeholder(R.mipmap.icon_default_expression).into(imageView)
            }
        }


        return mConvertView
    }

}