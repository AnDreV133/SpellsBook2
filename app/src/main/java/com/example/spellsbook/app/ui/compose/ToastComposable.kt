package com.example.spellsbook.app.ui.compose

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
private fun Toast(message: String, durationEnum: Int) {
    Toast.makeText(LocalContext.current, message, durationEnum).show()
}

@Composable
fun ToastShort(message: String) {
    Toast(message = message, durationEnum = Toast.LENGTH_SHORT)
}

@Composable
fun ToastLong(message: String) {
    Toast(message = message, durationEnum = Toast.LENGTH_LONG)
}