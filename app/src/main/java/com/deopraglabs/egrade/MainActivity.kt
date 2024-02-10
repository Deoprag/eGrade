package com.deopraglabs.egrade

import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.MediaController
import android.widget.VideoView
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val videoView = findViewById<VideoView>(R.id.video_start)
        val constraintLayout = findViewById<ConstraintLayout>(R.id.constraint_layout)

        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {
                val uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.egrade_night)
                videoView.setVideoURI(uri)
                constraintLayout.setBackgroundColor(Color.parseColor("#161616"))
            }
            Configuration.UI_MODE_NIGHT_NO,
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                val uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.egrade)
                videoView.setVideoURI(uri)
            }
        }

        val mediaController = MediaController(this)
        videoView.setMediaController(null)
        mediaController.setAnchorView(videoView)
        videoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = false
            mediaPlayer.start()
        }
    }
}