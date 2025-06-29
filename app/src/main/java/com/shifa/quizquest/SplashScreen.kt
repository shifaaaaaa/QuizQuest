package com.shifa.quizquest

import android.window.SplashScreen
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.shifa.quizquest.ui.theme.poppins
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    // Animation state
    var startAnimation by remember { mutableStateOf(false) }

    // Main animations
    val logoAnimation = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "logo"
    )

    val textAnimation = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 800,
            delayMillis = 300
        ),
        label = "text"
    )

    // Loading dots animation
    val infiniteTransition = rememberInfiniteTransition(label = "loading")
    val loadingAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "loadingAlpha"
    )

    // Background gradient
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF667eea),
            Color(0xFF764ba2),
            Color(0xFF85E4DC),
            Color(0xFF3FA1B7)
        )
    )

    // Start animation and navigate
    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(2500) // 2.5 seconds - lebih cepat
        navController.navigate(Screen.Welcome.route) {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Logo
            Card(
                modifier = Modifier
                    .size(100.dp)
                    .scale(logoAnimation.value)
                    .alpha(logoAnimation.value),
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🧠",
                        fontSize = 40.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // App Name
            Text(
                text = "QuizQuest",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppins,
                color = Color.White,
                modifier = Modifier
                    .alpha(textAnimation.value)
                    .scale(textAnimation.value),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tagline
            Text(
                text = "Uji Pengetahuanmu!",
                fontSize = 14.sp,
                fontFamily = poppins,
                color = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.alpha(textAnimation.value),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Simple Loading Indicator
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.alpha(textAnimation.value)
            ) {
                repeat(3) { index ->
                    val delay = index * 150
                    val dotAlpha by infiniteTransition.animateFloat(
                        initialValue = 0.3f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(
                                durationMillis = 600,
                                delayMillis = delay
                            ),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "dot$index"
                    )

                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .alpha(dotAlpha)
                            .background(
                                color = Color.White,
                                shape = CircleShape
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Loading Text
            Text(
                text = "Memuat...",
                fontSize = 12.sp,
                fontFamily = poppins,
                color = Color.White.copy(alpha = loadingAlpha),
                modifier = Modifier.alpha(textAnimation.value)
            )
        }

        // Bottom Version
        Text(
            text = "v1.0.0",
            fontSize = 10.sp,
            fontFamily = poppins,
            color = Color.White.copy(alpha = 0.6f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
                .alpha(textAnimation.value)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(navController = rememberNavController())
}
