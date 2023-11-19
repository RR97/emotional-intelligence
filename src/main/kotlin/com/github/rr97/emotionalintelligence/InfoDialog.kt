package com.github.rr97.emotionalintelligence
import com.github.rr97.emotionalintelligence.MoodType
import com.intellij.openapi.ui.DialogWrapper
import org.jetbrains.annotations.Nullable
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Image
import java.net.URL
import javax.swing.Action
import javax.swing.ImageIcon
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel


class InfoDialog(private val mood: MoodType, val sourceCode: String?) : DialogWrapper(true) {
    init {
        title = ""
        init()
    }

    override fun createActions(): Array<Action> {
        return arrayOf<Action>()
    }

    override fun createLeftSideActions(): Array<Action> {
        return arrayOf<Action>()
    }

    @Nullable
    override fun createCenterPanel(): JComponent {
        // val messageGenerator = MessageGenerator() --> Code throwing Error due to errors in the 3rd party library
        val jsonRes = getOpenAIRes(moodType = mood)
        val text = jsonRes?.get("text") as String //runBlocking { messageGenerator.wordsOfAffirmation(mood, sourceCode)}
        val dialogPanel = JPanel(BorderLayout())

        // Adding text
        val label = JLabel(text)
        label.preferredSize = Dimension(100, 27)
        dialogPanel.add(label, BorderLayout.PAGE_START)

        if (mood != MoodType.NEW_DAY) {
            // Generating image
            val imageURL = jsonRes["image_url"] as String // runBlocking { messageGenerator.image(text) }
            val data = URL(imageURL).readBytes()

            // Scaling image and adding to popup
            var imageIcon = ImageIcon(data)
            val ratio = imageIcon.iconWidth.toDouble() / imageIcon.iconHeight.toDouble()
            val finalHeight = 350
            val finalWidth = (finalHeight * ratio).toInt()
            imageIcon = ImageIcon(imageIcon.image.getScaledInstance(finalWidth, finalHeight, Image.SCALE_SMOOTH))
            val imageLabel = JLabel(imageIcon)

            dialogPanel.add(imageLabel, BorderLayout.CENTER)
        }
        return dialogPanel
    }
}