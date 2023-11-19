package com.github.rr97.emotionalintelligence.listeners

import com.github.rr97.emotionalintelligence.InfoDialog
import com.github.rr97.emotionalintelligence.MoodType
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener

const val timeThreshold = 150*60*60 // ms (2.5h)
const val minCountOfChanges = 15
var initTime: Long = 0

class FileHistoryListener : DocumentListener {
    private var firstLine: Int? = null
    private var lastLine: Int? = null
    private var previousLineTouched: Int? = null
    private val changesOnLineCount = mutableMapOf<Int, Int>()

    private fun checkMood(): MoodType {
        var devMood = MoodType.NONE
        if (changesOnLineCount.count() > 70) {
            devMood = MoodType.EXCELLENT
        }
        var consecutiveStruggles = 0
        for (entry in changesOnLineCount) {
            if (entry.value >= minCountOfChanges) {
                consecutiveStruggles++
            } else {
                consecutiveStruggles = 0
            }
            if (consecutiveStruggles > 3) {
                devMood = MoodType.BAD
                break
            }
        }
        changesOnLineCount.clear()
        return devMood
    }

    override fun documentChanged(event: DocumentEvent) {
        val currentLine = event.document.getLineNumber(event.offset)
        if (firstLine == null) {
            firstLine = currentLine
        } else if (currentLine < firstLine!!) {
            firstLine = currentLine
        }

        if (lastLine == null) {
            lastLine = currentLine
        } else if (currentLine > lastLine!!) {
            lastLine = currentLine
        }

        if (previousLineTouched != currentLine) {
            changesOnLineCount[currentLine] = (changesOnLineCount[currentLine] ?: 0) + 1
            previousLineTouched = currentLine
        }

        if (event.oldTimeStamp.minus(initTime) >= timeThreshold) {
            val mood: MoodType = checkMood()
            if (mood != MoodType.NONE) {
                InfoDialog(mood, event.document.text).showAndGet()
            }
            initTime = event.oldTimeStamp
        }
    }
}