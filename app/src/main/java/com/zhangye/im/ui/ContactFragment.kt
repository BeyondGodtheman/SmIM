package com.zhangye.im.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhangye.im.R
import com.zhangye.im.SMClient
import com.zhangye.im.`interface`.OnContactListener
import com.zhangye.im.adapter.ContactAdapter
import com.zhangye.im.model.Contacts
import kotlinx.android.synthetic.main.fragment_contact.view.*

/**
 *
 * Created by 张野 on 2017/11/3.
 */
class ContactFragment : Fragment(), View.OnClickListener {
    lateinit var convertView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        convertView = inflater.inflate(R.layout.fragment_contact, container, false)
        SMClient.getInstance().webSocketManager.queryContact(object : OnContactListener {
            override fun onContact(contacts: Contacts) {
                activity.runOnUiThread {
                    convertView.recycleView.layoutManager = LinearLayoutManager(activity)
                    convertView.recycleView.adapter = ContactAdapter(contacts.payload.addList)
                }
            }
        })



        return convertView
    }


    override fun onClick(v: View) {

    }

    override fun onDestroy() {
        super.onDestroy()

    }

}