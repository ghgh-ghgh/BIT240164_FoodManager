package com.example.bit240164_foodmanager.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import java.util.Locale

enum class AppLanguage(val code: String, val displayName: String) {
    ENGLISH("en", "EN"),
    VIETNAMESE("vi", "VI")
}

class LanguageState(initialLang: AppLanguage = AppLanguage.ENGLISH) {
    var currentLanguage by mutableStateOf(initialLang)
        private set

    @Suppress("DEPRECATION")
    fun switchLanguage(context: Context, newLang: AppLanguage) {
        currentLanguage = newLang
        val prefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        prefs.edit().putString("language_code", newLang.code).apply()

        val locale = Locale(newLang.code)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        (context as? Activity)?.recreate()
    }

    companion object {
        fun loadInitialLanguage(context: Context): AppLanguage {
            val prefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
            val code = prefs.getString("language_code", AppLanguage.ENGLISH.code) ?: "en"
            return if (code == "vi") AppLanguage.VIETNAMESE else AppLanguage.ENGLISH
        }
    }
}

val LocalAppLanguageState = staticCompositionLocalOf<LanguageState> {
    error("No LanguageState provided")
}
