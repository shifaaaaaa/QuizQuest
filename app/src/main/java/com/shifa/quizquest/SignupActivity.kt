package com.shifa.quizquest

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shifa.quizquest.ui.theme.poppins

class SignupActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SignUpScreen(navController = rememberNavController())
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavController,
    signupViewModel: SignupViewModel = viewModel()
) {
    val context = LocalContext.current
    val username = signupViewModel.username
    val email = signupViewModel.email
    val password = signupViewModel.password
    val confirmPassword = signupViewModel.confirmPassword
    val errorMessage = signupViewModel.errorMessage
    val isLoading = signupViewModel.isLoading
    val backgroundGradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF85E4DC), Color(0xFF3FA1B7))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .width(320.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Create Your QuizQuest Account",
                    fontFamily = poppins,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A2B4C),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                SignUpTextField("Username", username) { signupViewModel.onUsernameChange(it) }
                SignUpTextField("Email", email) { signupViewModel.onEmailChange(it) }
                SignUpTextField("Password", password, true) { signupViewModel.onPasswordChange(it) }
                SignUpTextField("Confirm Password", confirmPassword, true) { signupViewModel.onConfirmPasswordChange(it) }

                // Error message
                errorMessage?.let { message ->
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error,
                        fontFamily = poppins,
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Button(
                    onClick = {
                        signupViewModel.performSignup(
                            onSuccess = {
                                Toast.makeText(context, "Signup Berhasil!", Toast.LENGTH_SHORT).show()
                                navController.navigate(Screen.Login.route)
                            },
                            onFailure = { errorMsg ->
                                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3FA1B7)),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text(
                            text = "Sign Up",
                            fontFamily = poppins,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Already have an account? ",
                        fontFamily = poppins,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Login",
                        fontFamily = poppins,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3FA1B7),
                        modifier = Modifier.clickable {
                            navController.navigate(Screen.Login.route)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpTextField(
    label: String,
    value: String,
    isPassword: Boolean = false,
    onValueChange: (String) -> Unit
) {
    Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontFamily = poppins,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text("Enter your $label", fontSize = 14.sp) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color(0xFF3FA1B7),
                unfocusedIndicatorColor = Color.LightGray
            ),
            shape = RoundedCornerShape(8.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(navController = rememberNavController())
}