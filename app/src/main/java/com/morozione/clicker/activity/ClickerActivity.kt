package com.morozione.clicker.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.morozione.clicker.ui.theme.ClickerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClickerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClickerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ClickerScreen(
                        finish = { clicks ->
                            setResult(RESULT_OK, android.content.Intent().apply {
                                putExtra("new_record", clicks)
                            })
                            finish()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ClickerScreen(
    viewModel: ClickerViewModel = viewModel(),
    finish: (clicks: Int) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "to next: ${uiState.nextLevelTarget}",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Time: ${uiState.timeLeft}",
                style = MaterialTheme.typography.headlineLarge,
                color = when {
                    uiState.timeLeft >= 5 -> Color(0xFF159731)
                    uiState.timeLeft == 3 -> Color(0xFFB4AD1C)
                    uiState.timeLeft == 2 -> Color(0xFFBA1D2C)
                    else -> LocalContentColor.current
                }
            )
        }
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { viewModel.onClickButton() },
                modifier = Modifier.size(120.dp)
            ) {
                Text(
                    text = uiState.clicks.toString(),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            if (uiState.canBuyExtraTime) {
                Button(
                    onClick = { viewModel.onBuyExtraTime() }
                ) {
                    Text("Buy Extra Time (30 coins)")
                }
            }
        }
    }

    LaunchedEffect(uiState.isGameFinished) {
        if (uiState.isGameFinished) {
            finish(uiState.clicks)
        }
    }
} 