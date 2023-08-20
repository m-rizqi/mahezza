package com.mahezza.mahezza.util

import android.content.Context
import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource

sealed class StringResource {
    data class DynamicString(val  value: String): StringResource()
    class StringResWithParams(
        @StringRes val resId : Int,
        vararg val args: Any
    ): StringResource()

    @Composable
    fun asString(): String {
        return when(this){
            is DynamicString -> value
            is StringResWithParams -> {
                stringResource(resId, processParams(this.args).toTypedArray())
            }
        }
    }

    @Composable
    private fun processParams(vararg params : Any) =
        params.map {
            when(it){
                is Int -> {
                    val context = LocalContext.current
                    try {
                        context.getString(it)
                    } catch (ex: Resources.NotFoundException) {
                        it
                    }
                }
                is StringResWithParams -> it.asString()
                else -> it
            }
        }
}