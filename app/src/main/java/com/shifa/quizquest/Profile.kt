package com.shifa.quizquest

import androidx.compose.foundation.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.shifa.quizquest.ui.theme.poppins

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    var nickname by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val backgroundGradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF85E4DC), Color(0xFF3FA1B7))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(12.dp)
        ) {

            Spacer(modifier = Modifier.height(100.dp))
            // Foto utama
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4F7F7B))
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Nama Karakter
            Box(
                modifier = Modifier
                    .background(Color(0xFFBFE9EC), RoundedCornerShape(12.dp))
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Nama Karakter",
                    fontFamily = poppins,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x88BFE9EC)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Label Foto Profil
                    Text(
                        text = "Foto Profil",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        fontFamily = poppins
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Circle icon pilihan foto
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        repeat(5) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.LightGray)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Nickname
                    Text(
                        text = "Nickname",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        fontFamily = poppins
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    TextField(
                        value = nickname,
                        onValueChange = {
                            if (it.length <= 16) nickname = it
                        },
                        placeholder = {
                            Text("Maks 16 karakter")
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color(0xFFD8EEF1)
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Deskripsi
                    Text(
                        text = "Deskripsi",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        fontFamily = poppins
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    TextField(
                        value = description,
                        onValueChange = {
                            if (it.length <= 40) description = it
                        },
                        placeholder = {
                            Text("Deskripsi diri 40 karakter")
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color(0xFFD8EEF1)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            navController.navigate("dashboard")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Blue,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .padding(horizontal = 80.dp)
                            .fillMaxWidth(0.9f)
                            .height(40.dp)
                    ) {
                        Text(
                            text = "Save",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    navController.navigate("dashboard")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp)
            ) {
                Text(
                    text = "Return to Dashboard",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(navController = rememberNavController())
}