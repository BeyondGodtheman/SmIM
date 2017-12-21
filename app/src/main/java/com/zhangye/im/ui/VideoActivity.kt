package com.zhangye.im.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Point
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.zhangye.im.R
import com.zhangye.im.RTCClient
import kotlinx.android.synthetic.main.activity_video.*
import org.webrtc.VideoRenderer
import org.webrtc.VideoRendererGui

/**
 * 视频页面
 * Created by zhangye on 2017/12/19.
 */
class VideoActivity : AppCompatActivity() {
    var client: RTCClient? = null
    lateinit var localRender: VideoRenderer
    lateinit var remoteRender: VideoRenderer.Callbacks
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
                    1)
        }


        glview_call.preserveEGLContextOnPause = true
        glview_call.keepScreenOn = true //屏幕常亮
        VideoRendererGui.setView(glview_call, null)

        init()
    }

    private fun init() {
        //初始化RTC客户端
        client = RTCClient(this, null)
        //宽高为屏幕的百分比
        localRender = VideoRendererGui.createGui(0, 0, 100, 50, VideoRendererGui.ScalingType.SCALE_ASPECT_FILL, true)
        client?.localVideoTrack?.addRenderer(localRender)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 获取视频权限了
                client?.startVideo()

            } else {
                Toast.makeText(this, "请在应用管理中打开“相机”访问权限！", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        client?.onResume()
    }

    override fun onPause() {
        super.onPause()
        client?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        client?.onDestroy()
    }
}