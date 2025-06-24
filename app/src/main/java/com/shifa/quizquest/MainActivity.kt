package com.shifa.quizquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Screen.Splash.route
            )
                {
                composable(Screen.Welcome.route) {
                    QuizQuestApp(navController = navController)
                }
                composable(Screen.Login.route) {
                    LoginScreen(navController = navController)
                }
                composable(Screen.Dashboard.route) {
                    DashboardScreen(navController = navController)
                }
                composable(Screen.Profile.route) {
                    ProfileScreen(navController = navController)
                }
                composable(Screen.Settings.route) {
                    SettingsScreen(navController = navController)
                }
                composable(Screen.Signup.route) {
                    SignUpScreen(navController = navController)
                }
                composable(Screen.Quiz.route) {
                    QuizScreen(navController = navController)
                }
                composable(Screen.QuizList.route) {
                    QuizListScreen(navController = navController)
                }
                    composable(Screen.Splash.route) {
                        SplashScreen(navController = navController)
                    }
                    composable("quiz_play_screen/{quizId}") { backStackEntry ->
                    val quizId = backStackEntry.arguments?.getString("quizId")?.toIntOrNull() ?: 1
                    QuizPlayScreen(navController = navController, quizId = quizId)
                }
            }
        }
    }
}

@Composable
fun QuizQuestApp(navController: NavController) {
    val isDark by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF85E4DC),
            Color(0xFF3FA1B7),
            Color(0xFF2E8B9B)
        )
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
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(24.dp)
                        ),
                    shape = RoundedCornerShape(40.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Welcome to QuizQuest",
                            fontFamily = poppins,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center,
                            color = Color(0xFF2E8B9B)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Challenge yourself with interactive quizzes and track your progress!",
                            fontFamily = poppins,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            lineHeight = 24.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(32.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Button(
                                onClick = {
                                    Toast.makeText(context, "Navigasi ke Login Page!",
                                        Toast.LENGTH_SHORT).show()
                                    navController.navigate(Screen.Login.route)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF3FA1B7)
                                ),
                                shape = RoundedCornerShape(12.dp),
                                elevation = ButtonDefaults.buttonElevation(
                                    defaultElevation = 4.dp,
                                    pressedElevation = 2.dp
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp)
                            ) {
                                Text(
                                    text = "Login",
                                    fontFamily = poppins,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }

                            Button(
                                onClick = {
                                    Toast.makeText(context, "Navigasi ke Signup Page!",
                                        Toast.LENGTH_SHORT).show()
                                    navController.navigate(Screen.Signup.route)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF3FA1B7)
                                ),
                                shape = RoundedCornerShape(12.dp),
                                elevation = ButtonDefaults.buttonElevation(
                                    defaultElevation = 4.dp,
                                    pressedElevation = 2.dp
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp)
                            ) {
                                Text(
                                    text = "Sign Up",
                                    fontFamily = poppins,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Bottom decorative text
                Text(
                    text = "Start your learning journey today!",
                    fontFamily = poppins,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun TopNavigationBar() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "QuizQuest",
                fontSize = 35.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF2E8B9B),
                fontFamily = poppins
            )
        }
    }

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun QuizQuestAppPreview() {
    QuizQuestApp(navController = rememberNavController())
}