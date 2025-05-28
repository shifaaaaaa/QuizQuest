package com.shifa.quizquest

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
    var selectedImage by remember { mutableStateOf(R.drawable.profile1) }

    val profileImages = listOf(
        R.drawable.profile1,
        R.drawable.profile2,
        R.drawable.profile3,
        R.drawable.profile4,
        R.drawable.profile5
    )

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
            Spacer(modifier = Modifier.height(40.dp))
            TitleSection()
            Spacer(modifier = Modifier.height(20.dp))
            ProfileImage(selectedImage)
            Spacer(modifier = Modifier.height(24.dp))
            NameSection()
            Spacer(modifier = Modifier.height(24.dp))
            FormCard(
                profileImages = profileImages,
                onImageSelected = { selectedImage = it },
                nickname = nickname,
                onNicknameChange = { if (it.length <= 16) nickname = it },
                description = description,
                onDescriptionChange = { if (it.length <= 40) description = it },
                onSave = {
                    Log.d("PROFILE_SAVE", "Nickname: $nickname")
                    Log.d("PROFILE_SAVE", "Deskripsi: $description")
                    navController.navigate("dashboard")
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            ReturnButton()
        }
    }
}

@Composable
fun TitleSection() {
    Text(
        text = "Welcome",
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = poppins,
        color = Color.Black
    )
}

@Composable
fun ProfileImage(imageRes: Int) {
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = "Foto Profil",
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
    )
}

@Composable
fun NameSection() {
    Box(
        modifier = Modifier
            .background(Color(0xFFB6D3F1), RoundedCornerShape(12.dp))
            .padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Nama Karakter",
            fontFamily = poppins,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormCard(
    profileImages: List<Int>,
    onImageSelected: (Int) -> Unit,
    nickname: String,
    onNicknameChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    onSave: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0x88BFE9EC)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            TextLabel("Foto Profil")
            Spacer(modifier = Modifier.height(8.dp))
            ProfileImageRow(profileImages, onImageSelected)
            Spacer(modifier = Modifier.height(16.dp))
            TextLabel("Nickname")
            Spacer(modifier = Modifier.height(4.dp))
            TextField(
                value = nickname,
                onValueChange = onNicknameChange,
                placeholder = { Text("Maks 16 karakter") },
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFFDFF5F3)),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextLabel("Deskripsi")
            Spacer(modifier = Modifier.height(4.dp))
            TextField(
                value = description,
                onValueChange = onDescriptionChange,
                placeholder = { Text("Deskripsi diri 40 karakter") },
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.textFieldColors(containerColor = Color(0xFFDFF5F3)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onSave,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1EAFAF),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.6f)
                    .height(40.dp)
            ) {
                Text(
                    text = "Save",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun TextLabel(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        fontFamily = poppins
    )
}

@Composable
fun ProfileImageRow(profileImages: List<Int>, onImageSelected: (Int) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        profileImages.forEach { imageRes ->
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Foto Pilihan",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable { onImageSelected(imageRes) }
            )
        }
    }
}

@Composable
fun ReturnButton() {
    Button(
        onClick = {
            // TODO: Navigate to DashboardActivity
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF42AF6B),
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(navController = rememberNavController())
}