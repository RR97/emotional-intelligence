package com.github.rr97.emotionalintelligence

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import org.jetbrains.annotations.NotNull


class MyStartUpActivity : StartupActivity {
    private val LOG = Logger.getInstance(MyStartUpActivity::class.java)

    override fun runActivity(@NotNull project: Project) {
        LOG.info("Project startup activity")
        InfoDialog(MoodType.NEW_DAY, null).showAndGet()
    }
}