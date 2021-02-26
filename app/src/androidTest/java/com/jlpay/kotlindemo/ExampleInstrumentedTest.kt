package com.jlpay.kotlindemo

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 *
 * src/androidTest 是用于涉及Android仪器的单元测试
 *
 * src/test是纯单元测试，不涉及android框架。您可以在这里运行测试，而无需在真实设备或仿真器上运行
 *
 * 可以使用这两个文件夹。使用src/androidTest测试使用Android框架的代码。使用src/test测试纯java类的代码。写测试的方法几乎是一样的
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.jlpay.kotlindemo", appContext.packageName)
    }
}