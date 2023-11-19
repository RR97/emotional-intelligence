package com.github.rr97.emotionalintelligence

import com.intellij.testFramework.TestDataPath
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import junit.framework.TestCase

@TestDataPath("\$CONTENT_ROOT/src/test/testData")
class MyPluginTest : BasePlatformTestCase() {

    fun testXMLFile() {
        TestCase.assertEquals(2,2)
    }
    override fun getTestDataPath() = "src/test/testData/fileHistory"
}
