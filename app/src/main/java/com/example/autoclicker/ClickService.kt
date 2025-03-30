package com.example.autoclicker

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.google.mlkit.vision.text.Text
import java.util.concurrent.Executors

class ClickService : AccessibilityService() {
    private val handler = Handler(Looper.getMainLooper())
    private val executor = Executors.newSingleThreadExecutor()
    private var isRunning = false
    private var clickDelay = 500L // Default 500ms
    private val targetWords = mutableSetOf("play", "proceed", "exit")

    override fun onServiceConnected() {
        super.onServiceConnected()
        // Initialisierung hier
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (!isRunning) return
        
        executor.execute {
            val rootNode = rootInActiveWindow ?: return@execute
            findAndClickNodes(rootNode)
            rootNode.recycle()
        }
    }

    private fun findAndClickNodes(node: AccessibilityNodeInfo) {
        // Text extraction und Filterung
        val nodeText = node.text?.toString()?.lowercase() ?: ""
        if (targetWords.any { nodeText.contains(it) }) {
            performClick(node)
            return
        }

        // Rekursiver Durchlauf durch Kindknoten
        for (i in 0 until node.childCount) {
            node.getChild(i)?.let { child ->
                findAndClickNodes(child)
                child.recycle()
            }
        }
    }

    private fun performClick(node: AccessibilityNodeInfo) {
        val bounds = Rect()
        node.getBoundsInScreen(bounds)
        val clickX = bounds.centerX()
        val clickY = bounds.centerY()

        val path = Path().apply {
            moveTo(clickX.toFloat(), clickY.toFloat())
        }

        val gestureDescription = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(
                path, 0, 50))
            .build()

        dispatchGesture(gestureDescription, null, null)
    }

    fun startClicking() {
        isRunning = true
    }

    fun stopClicking() {
        isRunning = false
    }

    fun updateSettings(words: String, delay: Long) {
        targetWords.clear()
        targetWords.addAll(words.split(",").map { it.trim() })
        clickDelay = delay
    }

    override fun onInterrupt() {}
}