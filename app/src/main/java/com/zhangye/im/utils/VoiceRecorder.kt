package com.zhangye.im.utils

import android.content.Context
import android.media.MediaRecorder
import android.os.Handler
import android.os.SystemClock
import android.text.format.Time
import com.cocosh.shmstore.utils.LogUtils
import com.zhangye.im.SMClient
import java.io.File
import java.io.IOException
import java.util.*

/**
 * 录音
 * Created by zhangye on 2017/12/21.
 */
open class VoiceRecorder(private val handler: Handler) {
    internal var recorder: MediaRecorder? = null

    var isRecording = false
    private var startTime: Long = 0
    lateinit var voiceFilePath: String
    lateinit var voiceFileName: String
    private var file: File? = null

    /**
     * start recording to the file
     */
    fun startRecording(appContext: Context): String? {
        file = null
        try {
            // need to create recorder every time, otherwise, will got exception
            // from setOutputFile when try to reuse
            if (recorder != null) {
                recorder?.release()
                recorder = null
            }
            recorder = MediaRecorder()
            recorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            recorder?.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB)
            recorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            recorder?.setAudioChannels(1) // MONO
            recorder?.setAudioSamplingRate(8000) // 8000Hz
            recorder?.setAudioEncodingBitRate(64) // seems if change this to
            // 128, still got same file
            // size.
            // one easy way is to use temp file
            // file = File.createTempFile(PREFIX + userId, EXTENSION,
            // User.getVoicePath());
            voiceFileName = getVoiceFileName(SMClient.getInstance().userManager.getSenderUsername())
            voiceFilePath = SMClient.getInstance().pathUtil.voicePath.path + "/" + voiceFileName
            file = File(voiceFilePath)
            recorder?.setOutputFile(file!!.absolutePath)
            recorder?.prepare()
            isRecording = true
            recorder?.start()
        } catch (e: IOException) {
            LogUtils.e("prepare() failed")
        }

        Thread(Runnable {
            try {
                while (isRecording) {
                    val msg = android.os.Message()
                    msg.what = recorder!!.maxAmplitude * 13 / 0x7FFF
                    handler.sendMessage(msg)
                    SystemClock.sleep(100)
                }
            } catch (e: Exception) {
                // from the crash report website, found one NPE crash from
                // one android 4.0.4 htc phone
                // maybe handler is null for some reason
                LogUtils.e(e.toString())
            }
        }).start()
        startTime = Date().time
        LogUtils.d("start voice recording to file:" + file!!.absolutePath)
        return if (file == null) null else file!!.absolutePath
    }

    /**
     * stop the recoding
     *
     * @return seconds of the voice recorded
     */

    fun discardRecording() {
        if (recorder != null) {
            try {
                recorder!!.stop()
                recorder!!.release()
                recorder = null
                if (file != null && file!!.exists() && !file!!.isDirectory) {
                    file!!.delete()
                }
            } catch (e: IllegalStateException) {
            } catch (e: RuntimeException) {
            }

            isRecording = false
        }
    }

    fun stopRecoding(): Int {
        if (recorder != null) {
            isRecording = false
            recorder?.stop()
            recorder?.release()
            recorder = null

            if (file == null || !file!!.exists() || !file!!.isFile) {
                return Constants.FILE_INVALID
            }
            if (file?.length() == 0L) {
                file?.delete()
                return Constants.FILE_INVALID
            }
            val seconds = (Date().time - startTime).toInt() / 1000
            LogUtils.d("voice recording finished. seconds:" + seconds + " file length:" + file?.length())
            return seconds
        }
        return 0
    }

    protected fun finalize() {
        if (recorder != null) {
            recorder?.release()
        }
    }

    private fun getVoiceFileName(uid: String): String {
        val now = Time()
        now.setToNow()
        return uid + now.toString().substring(0, 15) + EXTENSION
    }

    companion object {

        internal val PREFIX = "voice"
        internal val EXTENSION = ".amr"
    }
}