package com.tsunderead.tsundoku.parser

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import org.koitharu.kotatsu.parsers.config.ConfigKey
import org.koitharu.kotatsu.parsers.config.MangaSourceConfig
import org.koitharu.kotatsu.parsers.model.MangaSource
import org.koitharu.kotatsu.parsers.model.SortOrder

private const val KEY_SORT_ORDER = "sort_order"

class SourceSettings(context: Context, source: MangaSource) : MangaSourceConfig {

    private val prefs = context.getSharedPreferences(source.name, Context.MODE_PRIVATE)

    var defaultSortOrder: SortOrder?
        get() = prefs.getEnumValue(KEY_SORT_ORDER, SortOrder::class.java)
        set(value) = prefs.edit { putEnumValue(KEY_SORT_ORDER, value) }

    @Suppress("UNCHECKED_CAST")
    override fun <T> get(key: ConfigKey<T>): T {
        return when (key) {
            is ConfigKey.Domain -> prefs.getString(key.key, key.defaultValue)
                .ifNullOrEmpty { key.defaultValue }
        } as T
    }

    private fun <E : Enum<E>> SharedPreferences.getEnumValue(key: String, enumClass: Class<E>): E? {
        val stringValue = getString(key, null) ?: return null
        return enumClass.enumConstants?.find {
            it.name == stringValue
        }
    }

    private inline fun String?.ifNullOrEmpty(defaultValue: () -> String): String {
        return if (this.isNullOrEmpty()) defaultValue() else this
    }

    private fun <E : Enum<E>> SharedPreferences.Editor.putEnumValue(key: String, value: E?) {
        putString(key, value?.name)
    }
}