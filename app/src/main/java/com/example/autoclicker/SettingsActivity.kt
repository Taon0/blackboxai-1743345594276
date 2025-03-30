package com.example.autoclicker

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    private lateinit var wordListEditText: EditText
    private lateinit var clickSpeedSeekBar: SeekBar
    private lateinit var clickSpeedTextView: TextView
    private lateinit var saveButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        wordListEditText = findViewById(R.id.wordListEditText)
        clickSpeedSeekBar = findViewById(R.id.clickSpeedSeekBar)
        clickSpeedTextView = findViewById(R.id.clickSpeedTextView)
        saveButton = findViewById(R.id.saveButton)

        loadSettings()

        clickSpeedSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                clickSpeedTextView.text = getString(R.string.click_speed, progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        saveButton.setOnClickListener {
            saveSettings()
            finish()
        }
    }

    private fun loadSettings() {
        val words = sharedPreferences.getString("word_list", "play, proceed, exit")
        val speed = sharedPreferences.getInt("click_speed", 1)

        wordListEditText.setText(words)
        clickSpeedSeekBar.progress = speed
        clickSpeedTextView.text = getString(R.string.click_speed, speed)
    }

    private fun saveSettings() {
        val editor = sharedPreferences.edit()
        editor.putString("word_list", wordListEditText.text.toString())
        editor.putInt("click_speed", clickSpeedSeekBar.progress)
        editor.apply()
    }
}