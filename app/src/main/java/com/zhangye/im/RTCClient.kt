package com.zhangye.im

import android.content.Context
import android.opengl.EGLContext
import com.cocosh.shmstore.utils.LogUtils
import org.webrtc.*

/**
 *
 * Created by zhangye on 2017/12/19.
 */

//RtcListener listener, String host, PeerConnectionParameters params, EGLContext mEGLcontext
class RTCClient(context: Context, mEGLcontext: EGLContext?) {
    var isInit: Boolean = false
    var peerConnection: PeerConnectionFactory
    var videoSource: VideoSource
    var audioSource: AudioSource
    var mediaStream: MediaStream
    var localVideoTrack:VideoTrack
    var localAudioTrack:AudioTrack

    init {
        isInit = PeerConnectionFactory.initializeAndroidGlobals(context, true, true
                , true, mEGLcontext)
        peerConnection = PeerConnectionFactory()
        val videoConstraints = MediaConstraints()
        //视频流
        videoSource = peerConnection.createVideoSource(VideoCapturerAndroid.create(VideoCapturerAndroid.getNameOfFrontFacingDevice()), videoConstraints)
        mediaStream = peerConnection.createLocalMediaStream("video_stream")
        localVideoTrack = peerConnection.createVideoTrack("video_track", videoSource)
        mediaStream.addTrack(localVideoTrack)

        //音频流
        audioSource = peerConnection.createAudioSource(MediaConstraints())
        localAudioTrack = peerConnection.createAudioTrack("audio_track", audioSource)
        mediaStream.addTrack(localAudioTrack)

    }


    fun startVideo() {

    }


    /**
     * Call this method in Activity.onPause()
     */
    fun onPause() {
        videoSource.stop()
    }

    /**
     * Call this method in Activity.onResume()
     */
    fun onResume() {
        videoSource.restart()
    }

    /**
     * Call this method in Activity.onDestroy()
     */
    fun onDestroy() {
        videoSource.dispose()
    }

}