package com.morozione.clicker.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import com.morozione.clicker.RecordFeedback
import com.morozione.clicker.RecordManager

@Composable
fun RecordDialog(
    record: Int,
    newRecord: Int,
    onDismiss: () -> Unit
) {
    val feedback = RecordManager.getRecordFeedback(record, newRecord)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = feedback.message,
                color = feedback.color,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                text = newRecord.toString(),
                color = feedback.color,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
} 