package com.example.bit240164_foodmanager

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.bit240164_foodmanager.ui.navigation.AppNavigation
import com.example.bit240164_foodmanager.ui.theme.BIT240164_FoodManagerTheme
import com.example.bit240164_foodmanager.utils.LanguageState
import com.example.bit240164_foodmanager.utils.LocalAppLanguageState
import java.util.Locale

class MainActivity : ComponentActivity() {

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val initialLanguage = LanguageState.loadInitialLanguage(this)
        val locale = Locale(initialLanguage.code)
        Locale.setDefault(locale)
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        setContent {
            val languageState = remember { LanguageState(initialLanguage) }

            CompositionLocalProvider(LocalAppLanguageState provides languageState) {
                BIT240164_FoodManagerTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AppNavigation()
                    }
                }
            }
        }
    }
}
