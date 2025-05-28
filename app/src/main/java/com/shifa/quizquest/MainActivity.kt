package com.shifa.quizquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shifa.quizquest.ui.theme.poppins
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = Screen.Welcome.route
            ) {
                composable(Screen.Welcome.route) {
                    QuizQuestApp(navController = navController)
                }
                composable(Screen.Login.route) {
                    LoginScreen(navController = navController)
                }
                composable(Screen.Dashboard.route) {
                    DashboardScreen()
                }
            }
        }
    }
}

@Composable
fun QuizQuestApp(navController: NavController) {
    val isDark by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val backgroundGradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF85E4DC), Color(0xFF3FA1B7))
    )

    MaterialTheme(
        colorScheme = if (isDark) darkColorScheme() else lightColorScheme()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = backgroundGradient)
                .padding(
                    top = 48.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                TopNavigationBar()
                Spacer(modifier = Modifier.height(50.dp))
                Text(
                    text = "Welcome to QuizQuest",
                    fontFamily = poppins,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Challenge yourself with interactive quizzes and track your progress!",
                    fontFamily = poppins,
                    fontWeight = FontWeight.Thin,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(32.dp))

                // Tombol Login
                Button(
                    onClick = {
                        Toast.makeText(context, "Navigasi ke Login Page!", Toast.LENGTH_SHORT).show()
                        navController.navigate(Screen.Login.route)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3FA1B7)),
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(50.dp)
                ) {
                    Text(
                        text = "Login",
                        fontFamily = poppins,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun TopNavigationBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "QuizQuest",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun QuizQuestAppPreview() {
    QuizQuestApp(navController = rememberNavController())
}