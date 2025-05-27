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
import androidx.compose.foundation.clickable

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
                .padding(top = 48.dp, bottom = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                TopNavigationBar(navController = navController)
                Spacer(modifier = Modifier.height(100.dp))
                Text(
                    text = "Welcome to QuizQuest",
                    fontFamily = poppins,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold
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
            }
        }
    }
}

@Composable
fun TopNavigationBar(navController: NavController) {
    val context = LocalContext.current

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
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TopNavItem("Login") {
                Toast.makeText(context, "Navigasi ke Login Page!", Toast.LENGTH_SHORT).show()
                navController.navigate(Screen.Login.route) {
                    // Opsi: popUpTo Welcome.route jika ingin WelcomeScreen dihapus dari back stack
                    // Contoh: agar tidak bisa kembali ke Welcome setelah klik login
                    // popUpTo(Screen.Welcome.route) { inclusive = false } // false agar Welcome tetap ada di back stack
                }
            }
        }
    }
}

@Composable
fun TopNavItem(title: String, onClick: () -> Unit) {
    Text(
        text = title,
        fontSize = 14.sp,
        color = Color.White,
        modifier = Modifier.clickable { onClick() }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun QuizQuestAppPreview() {
    QuizQuestApp(navController = rememberNavController())
}