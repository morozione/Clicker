package com.morozione.clicker.activity

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.morozione.clicker.R
import com.morozione.clicker.ui.theme.BackgroundColor
import com.morozione.clicker.ui.theme.ClickerTheme
import com.morozione.clicker.ui.theme.CoinTextColor
import com.morozione.clicker.ui.theme.TextPrimaryColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClickerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable edge-to-edge
        enableEdgeToEdge()
        
        // Make the app full screen
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        setContent {
            ClickerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BackgroundColor
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
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .systemBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Next level target text
            Text(
                text = "to next: ${uiState.nextLevelTarget}",
                color = TextPrimaryColor,
                fontSize = 36.sp,
                fontFamily = FontFamily.Cursive,
                modifier = Modifier.padding(top = 15.dp)
            )
            
            // Timer text
            Text(
                text = "Time: ${uiState.timeLeft}",
                color = TextPrimaryColor,
                fontSize = 24.sp,
                fontFamily = FontFamily.Cursive,
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(250.dp)
        ) {
            // Click button background
            Image(
                painter = painterResource(
                    id = if (isPressed) R.drawable.click_down else R.drawable.click_up
                ),
                contentDescription = "Click button background",
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) { viewModel.onClickButton() }
            )
            
            // Click count text
            Text(
                text = uiState.clicks.toString(),
                fontSize = 90.sp,
                fontFamily = FontFamily.Cursive,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Buy extra time button
        if (uiState.canBuyExtraTime) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.add_time),
                    contentDescription = "Buy extra time",
                    modifier = Modifier
                        .size(80.dp)
                        .clickable { viewModel.onBuyExtraTime() }
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = "-30",
                        color = CoinTextColor,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Cursive
                    )
                    Image(
                        painter = painterResource(id = R.drawable.coin),
                        contentDescription = "Coins",
                        modifier = Modifier.size(18.dp)
                    )
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