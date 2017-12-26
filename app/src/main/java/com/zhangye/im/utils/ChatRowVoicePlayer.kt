package com.zhangye.im.utils

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import com.zhangye.im.model.Chat
import java.io.IOException

/**
 * 语音播放
 * Created by zhangye on 2017/12/21.
 */
class ChatRowVoicePlayer private constructor(cxt: Context) {

    private val audioManager: AudioManager
    val player: MediaPlayer
    /**
     * May null, please consider.
     *
     * @return
     */
    var currentPlayingId: String? = null
        private set

    private var onCompletionListener: MediaPlayer.OnCompletionListener? = null

    val isPlaying: Boolean
        get() = player.isPlaying

    fun play(msg: Chat, listener: MediaPlayer.OnCompletionListener) {

        if (player.isPlaying) {
            stop()
        }

        currentPlayingId = msg.messageId
        onCompletionListener = listener

        try {
            setSpeaker()
            player.setDataSource(msg.payload.content)
            player.prepare()
            player.setOnCompletionListener {
                stop()

                currentPlayingId = null
                onCompletionListener = null
            }
            player.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun stop() {
        player.stop()
        player.reset()

        /**
         * This listener is to stop the voice play animation currently, considered the following 3 conditions:
         *
         * 1.A new voice item is clicked to play, to stop the previous playing voice item animation.
         * 2.The voice is play complete, to stop it's voice play animation.
         * 3.Press the voice record button will stop the voice play and must stop voice play animation.
         *
         */
        if (onCompletionListener != null) {
            onCompletionListener!!.onCompletion(player)
        }
    }

    init {
        val baseContext = cxt.applicationContext
        audioManager = baseContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        player = MediaPlayer()
    }

    private fun setSpeaker() {
        val speakerOn = false
        if (speakerOn) {
            audioManager.mode = AudioManager.MODE_NORMAL
            audioManager.isSpeakerphoneOn = true
            player.setAudioStreamType(AudioManager.STREAM_RING)
        } else {
            audioManager.isSpeakerphoneOn = false// 关闭扬声器
            // 把声音设定成Earpiece（听筒）出来，设定为正在通话中
            audioManager.mode = AudioManager.MODE_IN_CALL
            player.setAudioStreamType(AudioManager.STREAM_VOICE_CALL)
        }
    }

    companion object {
        private val TAG = "ConcurrentMediaPlayer"

        private var instance: ChatRowVoicePlayer? = null

        fun getInstance(context: Context): ChatRowVoicePlayer? {
            if (instance == null) {
                synchronized(ChatRowVoicePlayer::class.java) {
                    if (instance == null) {
                        instance = ChatRowVoicePlayer(context)
                    }
                }
            }
            return instance
        }
    }
}
