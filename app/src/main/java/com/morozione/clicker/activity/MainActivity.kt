package com.morozione.clicker.activity

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.morozione.clicker.R
import com.morozione.clicker.ui.components.RecordDialog
import com.morozione.clicker.ui.theme.BackgroundColor
import com.morozione.clicker.ui.theme.ClickerTheme
import com.morozione.clicker.ui.theme.CoinTextColor
import com.morozione.clicker.ui.theme.TextPrimaryColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= 28) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }

        setContent {
            ClickerTheme {
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BackgroundColor
                ) {
                    val mainViewModel: MainViewModel = hiltViewModel()

                    NavHost(
                        navController = navController,
                        startDestination = "main"
                    ) {
                        composable("main") {
                            MainScreen(
                                viewModel = mainViewModel,
                                onStartClick = {
                                    mainViewModel.onStartClick()
                                    navController.navigate("clicker")
                                }
                            )
                        }

                        composable("clicker") {
                            ClickerScreen(
                                onGameFinished = { clicks ->
                                    navController.popBackStack()
                                    mainViewModel.onGameFinished(clicks)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onStartClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val view = LocalView.current

    val insets = if (Build.VERSION.SDK_INT >= 28) {
        ViewCompat.getRootWindowInsets(view) ?: return
    } else {
        null
    }

    val statusBarHeight = insets?.getInsets(WindowInsetsCompat.Type.statusBars())?.top ?: 0
    val navigationBarHeight =
        insets?.getInsets(WindowInsetsCompat.Type.navigationBars())?.bottom ?: 0

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(
                top = if (Build.VERSION.SDK_INT >= 28) statusBarHeight.dp else 0.dp,
                bottom = if (Build.VERSION.SDK_INT >= 28) navigationBarHeight.dp else 0.dp
            )
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

@Composable
fun ClickerScreen(
    viewModel: ClickerViewModel = hiltViewModel(),
    onGameFinished: (clicks: Int) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val view = LocalView.current

    val insets = if (Build.VERSION.SDK_INT >= 28) {
        ViewCompat.getRootWindowInsets(view) ?: return
    } else {
        null
    }

    val statusBarHeight = insets?.getInsets(WindowInsetsCompat.Type.statusBars())?.top ?: 0
    val navigationBarHeight =
        insets?.getInsets(WindowInsetsCompat.Type.navigationBars())?.bottom ?: 0

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(
                top = if (Build.VERSION.SDK_INT >= 28) statusBarHeight.dp else 0.dp,
                bottom = if (Build.VERSION.SDK_INT >= 28) navigationBarHeight.dp else 0.dp
            )
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "to next: ${uiState.nextLevelTarget}",
                color = TextPrimaryColor,
                fontSize = 36.sp,
                fontFamily = FontFamily.Cursive,
                modifier = Modifier.padding(top = 15.dp)
            )

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

            Text(
                text = uiState.clicks.toString(),
                fontSize = 90.sp,
                fontFamily = FontFamily.Cursive,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }

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
            onGameFinished(uiState.clicks)
        }
    }
} 