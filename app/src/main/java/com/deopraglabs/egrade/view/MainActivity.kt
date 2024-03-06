package com.deopraglabs.egrade.view

import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.ImageView
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.VideoView
import com.deopraglabs.egrade.R
import com.deopraglabs.egrade.databinding.ActivityMainBinding
import com.google.android.material.resources.TextAppearance

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val videoView = binding.videoStart
        val constraintLayout = binding.constraintLayout

        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {
                val uri = Uri.parse("android.resource://$packageName/" + R.raw.egrade_night)
                videoView.setVideoURI(uri)
                constraintLayout.setBackgroundColor(Color.parseColor("#161616"))
            }
            Configuration.UI_MODE_NIGHT_NO,
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                val uri = Uri.parse("android.resource://$packageName/" + R.raw.egrade)
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

    private suspend fun isDatabaseWorking(): Boolean {

        return false;
    }
}