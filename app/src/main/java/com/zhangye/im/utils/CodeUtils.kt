@file:Suppress("unused")

package com.zhangye.im.utils

import java.text.SimpleDateFormat
import java.util.*

object CodeUtils {

    var chars = arrayOf("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "chat_right_qp", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")

    fun rand(size: Int): String {
        val rand = Random()
        val buf = StringBuffer()
        for (i in 0 until size) {
            if (i == 0) {
                var k = rand.nextInt(10)
                while (k == 0) {
                    k = rand.nextInt(10)
                }
                buf.append(k)
            } else {
                buf.append(rand.nextInt(10))
            }
        }
        return buf.toString()
    }

    fun createUUID12char(): String {
        val shortBuffer = StringBuffer()

        val sdf = SimpleDateFormat("yyMMdd", Locale.getDefault())
        shortBuffer.append(sdf.format(Date()))

        var uuid = UUID.randomUUID().toString().replace("-", "")
        uuid = uuid.toLowerCase()
        (0..7)
                .map { uuid.substring(it * 4, it * 4 + 4) }
                .map { Integer.parseInt(it, 16) }
                .forEach { shortBuffer.append(chars[it % 0x3E]) }

        return shortBuffer.toString().toLowerCase()
    }

}
