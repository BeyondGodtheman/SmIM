package com.zhangye.im.utils

import android.content.Context
import android.os.Environment
import com.zhangye.im.SMClient
import java.io.File

/**
 * 存储路径管理类
 * Created by zhangye on 2017/12/21.
 */
class PathUtil {
    lateinit var voicePath: File
    lateinit var imagePath: File
    lateinit var videoPath: File
    lateinit var filePath: File

    lateinit var pathPrefix: String
    private var storageDir: File? = null


    fun initDirs(context: Context) {
        pathPrefix = "/Android/data/${context.packageName}/"

        voicePath = generateVoicePath(context)
        if (!voicePath.exists()) {
            voicePath.mkdirs()
        }

        this.imagePath = generateImagePath(context)
        if (!this.imagePath.exists()) {
            this.imagePath.mkdirs()
        }


        this.videoPath = generateVideoPath(context)
        if (!this.videoPath.exists()) {
            this.videoPath.mkdirs()
        }

        this.filePath = generateFiePath(context)
        if (!this.filePath.exists()) {
            this.filePath.mkdirs()
        }

    }


    /**
     * 获取SD卡路径
     */
    private fun getStorageDir(context: Context): File? {
        if (storageDir == null) {
            val file = Environment.getExternalStorageDirectory()
            if (file.exists()) {
                return file
            }

            storageDir = context.filesDir
        }

        return storageDir
    }

    private fun generateImagePath(context: Context): File {
        return File(getStorageDir(context), pathPrefix + SMClient.getInstance().userManager.getSenderUsername() + "/image/")
    }

    private fun generateVoicePath(context: Context): File {
        return File(getStorageDir(context), pathPrefix + SMClient.getInstance().userManager.getSenderUsername() + "/voice/")
    }

    private fun generateFiePath(context: Context): File {
        return File(getStorageDir(context), pathPrefix + SMClient.getInstance().userManager.getSenderUsername() + "/file/")
    }

    private fun generateVideoPath(context: Context): File {
        return File(getStorageDir(context), pathPrefix + SMClient.getInstance().userManager.getSenderUsername() + "/video/")
    }

}