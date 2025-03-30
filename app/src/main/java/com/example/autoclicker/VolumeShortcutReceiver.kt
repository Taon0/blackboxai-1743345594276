package com.example.autoclicker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager

class VolumeShortcutReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.media.VOLUME_CHANGED_ACTION") {
            val audioManager = context?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

            // Check if the volume button was pressed
            if (currentVolume == 0) {
                // Start or stop the auto-clicking service
                val serviceIntent = Intent(context, ClickService::class.java)
                context.startService(serviceIntent)
            }
        }
    }
}