package com.morozione.clicker.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.morozione.clicker.R
import com.morozione.clicker.ui.components.RecordDialog
import com.morozione.clicker.ui.theme.ClickerTheme
import com.morozione.clicker.ui.theme.CoinTextColor
import com.morozione.clicker.ui.theme.TextPrimaryColor
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.view.WindowCompat
import com.morozione.clicker.ui.theme.BackgroundColor

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val startClickerActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.getIntExtra("new_record", 0)?.let { newRecord ->
                // Handle new record if needed
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ClickerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BackgroundColor
                ) {
                    MainScreen(
                        onStartClick = { startClickerActivity() }
                    )
                }
            }
        }
    }

    private fun startClickerActivity() {
        startClickerActivity.launch(Intent(this, ClickerActivity::class.java))
    }
}

@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(),
    onStartClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .systemBarsPadding()
            .padding(16.dp)
    ) {
        // Coins section at the top
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 8.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${uiState.coins}",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 24.sp,
                fontFamily = FontFamily.Cursive
            )
            Spacer(modifier = Modifier.width(4.dp))
            Image(
                painter = painterResource(id = R.drawable.coin),
                contentDescription = "Coins",
                modifier = Modifier.size(30.dp)
            )
        }

        // Record text below coins
        Text(
            text = "Record: ${uiState.record}",
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 34.sp,
            fontFamily = FontFamily.Cursive,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 48.dp)
        )

        // Start button in the center
        Button(
            onClick = onStartClick,
            enabled = !uiState.isPlaying,
            modifier = Modifier
                .align(Alignment.Center)
                .size(width = 300.dp, height = 150.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = MaterialTheme.shapes.large
        ) {
            Text(
                text = "Start",
                fontSize = 32.sp,
                fontFamily = FontFamily.Cursive
            )
        }
    }

    if (uiState.showRecordDialog) {
        RecordDialog(
            record = uiState.record,
            newRecord = uiState.newRecord,
            onDismiss = { viewModel.onRecordDialogDismiss() }
        )
    }
} 