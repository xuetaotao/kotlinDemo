package com.jlpay.kotlindemo.utils

import android.content.res.Resources
import android.os.Build
import androidx.annotation.RequiresApi
import com.jlpay.kotlindemo.bean.LocaleBean
import java.util.*

object LocaleUtils {

    const val LANGUAGE_KEY = "language_value"
    const val DEFAULT_LANGUAGE_VALUE = "English"
    const val SPANISH = "Spanish"
    const val English = "English"

    @RequiresApi(Build.VERSION_CODES.N)
    fun getSystemLocale(): Locale {
        val configuration = Resources.getSystem().configuration
        return configuration.locales[0]
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getDefaultLocaleBean(): LocaleBean {
        var defaultLocaleBean: LocaleBean = LocaleBean(English, Locale.ENGLISH)
        val systemLocale = getSystemLocale()
        val systemLanguage = systemLocale.language
        for (supportLocaleBean in getSupportLocaleBeans()) {
            if (systemLanguage.equals(supportLocaleBean.locale.language)) {
                defaultLocaleBean = supportLocaleBean
                break
            }
        }
        return defaultLocaleBean
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getCurrentLocaleBean(): LocaleBean {
        return if (SpUtils.getPrefObj(LANGUAGE_KEY, LocaleBean::class.java) != null) {
            SpUtils.getPrefObj(LANGUAGE_KEY, LocaleBean::class.java)
        } else {
            getDefaultLocaleBean()
        }
    }

    private fun getSupportLocaleBeans(): List<LocaleBean> {
        return listOf(
            LocaleBean(SPANISH, Locale("es")), LocaleBean(English, Locale.ENGLISH)
        )
    }
}