package com.example.spellsbook.app.ui.compose.fragments

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.spellsbook.R

@Composable
fun ExportToast(exportResult: Boolean?) {
    val context = LocalContext.current
    if (exportResult != null) {
        if (exportResult == true) {
            Toast.makeText(
                context,
                stringResource(R.string.export_complete),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                context,
                stringResource(R.string.exception_of_export),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}