package com.zhangye.im.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Message
import android.os.PowerManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.zhangye.im.R
import com.zhangye.im.utils.ChatRowVoicePlayer
import com.zhangye.im.utils.Constants
import com.zhangye.im.utils.VoiceRecorder

/**
 * 录音view
 * Created by zhangye on 2017/12/21.
 */
class VoiceRecorderView : RelativeLayout {
    lateinit var wakeLock: PowerManager.WakeLock

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    lateinit var voiceRecorder: VoiceRecorder
    lateinit var micImages: Array<Drawable>
    lateinit var micImage: ImageView
    lateinit var recordingHint: TextView
    //
    var micImageHandler: Handler = Handler(Handler.Callback { msg ->
        micImage.setImageDrawable(micImages[msg.what])
        return@Callback false
    })


    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.layout_voice_recorder, this)
        micImage = findViewById(R.id.mic_image)
        recordingHint = findViewById(R.id.recording_hint)

        voiceRecorder = VoiceRecorder(micImageHandler)

        // animation resources, used for recording
        micImages = arrayOf(resources.getDrawable(R.drawable.record_animate_01),
                resources.getDrawable(R.drawable.record_animate_02),
                resources.getDrawable(R.drawable.record_animate_03),
                resources.getDrawable(R.drawable.record_animate_04),
                resources.getDrawable(R.drawable.record_animate_05),
                resources.getDrawable(R.drawable.record_animate_06),
                resources.getDrawable(R.drawable.record_animate_07),
                resources.getDrawable(R.drawable.record_animate_08),
                resources.getDrawable(R.drawable.record_animate_09),
                resources.getDrawable(R.drawable.record_animate_10),
                resources.getDrawable(R.drawable.record_animate_11),
                resources.getDrawable(R.drawable.record_animate_12),
                resources.getDrawable(R.drawable.record_animate_13),
                resources.getDrawable(R.drawable.record_animate_14))

        wakeLock = (context.getSystemService(Context.POWER_SERVICE) as PowerManager).newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK, "SMim")
    }

    /**
     * on speak button touched
     *
     * @param v
     * @param event
     */
    fun onPressToSpeakBtnTouch(v: View, event: MotionEvent, recorderCallback: VoiceRecorderCallback?): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                try {
                    val voicePlayer = ChatRowVoicePlayer.getInstance(context)
                    if (voicePlayer?.isPlaying!!)
                        voicePlayer.stop()
                    v.isPressed = true
                    startRecording()
                } catch (e: Exception) {
                    v.isPressed = false
                }

                return true
            }
            MotionEvent.ACTION_MOVE -> {
                if (event.y < 0) {
                    showReleaseToCancelHint()
                } else {
                    showMoveUpToCancelHint()
                }
                return true
            }
            MotionEvent.ACTION_UP -> {
                v.isPressed = false
                if (event.y < 0) {
                    // discard the recorded audio.
                    discardRecording()
                } else {
                    // stop recording and send voice file
                    try {
                        val length = stopRecoding()
                        when {
                            length > 0 -> recorderCallback?.onVoiceRecordComplete(getVoiceFilePath(), length)
                            length == Constants.FILE_INVALID -> Toast.makeText(context, R.string.Recording_without_permission, Toast.LENGTH_SHORT).show()
                            else -> Toast.makeText(context, R.string.The_recording_time_is_too_short, Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(context, R.string.send_failure_please, Toast.LENGTH_SHORT).show()
                    }

                }
                return true
            }
            else -> {
                discardRecording()
                return false
            }
        }
    }

    interface VoiceRecorderCallback {
        /**
         * on voice record complete
         *
         * @param voiceFilePath
         * 录音完毕后的文件路径
         * @param voiceTimeLength
         * 录音时长
         */
        fun onVoiceRecordComplete(voiceFilePath: String, voiceTimeLength: Int)
    }

    @SuppressLint("WakelockTimeout")
    fun startRecording() {
        if (android.os.Environment.getExternalStorageState() != android.os.Environment.MEDIA_MOUNTED) {
            Toast.makeText(context, R.string.Send_voice_need_sdcard_support, Toast.LENGTH_SHORT).show()
            return
        }
        try {
            wakeLock.acquire()
            this.visibility = View.VISIBLE
            recordingHint.text = context.getString(R.string.move_up_to_cancel)
            recordingHint.setBackgroundColor(Color.TRANSPARENT)
            voiceRecorder.startRecording(context)
        } catch (e: Exception) {
            e.printStackTrace()
            if (wakeLock.isHeld)
                wakeLock.release()
            voiceRecorder.discardRecording()
            this.visibility = View.INVISIBLE
            Toast.makeText(context, R.string.recoding_fail, Toast.LENGTH_SHORT).show()
            return
        }

    }

    //
    fun showReleaseToCancelHint() {
        recordingHint.text = context.getString(R.string.release_to_cancel)
        recordingHint.setBackgroundResource(R.drawable.recording_text_hint_bg)
    }

    fun showMoveUpToCancelHint() {
        recordingHint.text = context.getString(R.string.move_up_to_cancel)
        recordingHint.setBackgroundColor(Color.TRANSPARENT)
    }

    fun discardRecording() {
        if (wakeLock.isHeld)
            wakeLock.release()
        try {
            // stop recording
            if (voiceRecorder.isRecording) {
                voiceRecorder.discardRecording()
                this.visibility = View.INVISIBLE
            }
        } catch (e: Exception) {
        }

    }

    fun stopRecoding(): Int {
        this.visibility = View.INVISIBLE
        if (wakeLock.isHeld)
            wakeLock.release()
        return voiceRecorder.stopRecoding()
    }

    fun getVoiceFilePath(): String {
        return voiceRecorder.voiceFilePath
    }

    fun getVoiceFileName(): String {
        return voiceRecorder.voiceFileName
    }

    fun isRecording(): Boolean {
        return voiceRecorder.isRecording
    }
}