package com.example.filesapp.helper

import android.content.Context
import android.content.res.Configuration
import java.util.*

object SetLocale {
    fun setLocale(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)

        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
    }
}