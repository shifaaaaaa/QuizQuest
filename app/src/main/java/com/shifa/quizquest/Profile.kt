package com.shifa.quizquest

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shifa.quizquest.datastore.ProfileDataStore
import com.shifa.quizquest.ui.theme.poppins
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    // Dapatkan UID pengguna saat ini dengan aman.
    val currentUser = Firebase.auth.currentUser
    val uid = currentUser?.uid

    // State untuk input pengguna
    var nickname by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedImageId by remember { mutableStateOf(R.drawable.profile1) }

    val coroutineScope = rememberCoroutineScope()

    // Hanya buat DataStore jika UID valid, jika tidak, jangan lakukan apa-apa.
    val profileStore = remember(uid) {
        if (uid != null) {
            ProfileDataStore(context, uid)
        } else {
            null
        }
    }

    // Daftar gambar yang valid
    val profileImages = listOf(
        R.drawable.profile1,
        R.drawable.profile2,
        R.drawable.profile3,
        R.drawable.profile4,
        R.drawable.profile5
    )

    // Gunakan LaunchedEffect untuk memuat data profil saat pertama kali layar dibuka
    // Hanya berjalan jika profileStore berhasil dibuat (UID tidak null)
    LaunchedEffect(key1 = profileStore) {
        profileStore?.let { store ->
            store.nickname.collect { savedNickname -> nickname = savedNickname }
        }
        profileStore?.let { store ->
            store.description.collect { savedDesc -> description = savedDesc }
        }
        profileStore?.let { store ->
            store.imageId.collect { savedImageId ->
                if (profileImages.contains(savedImageId)) {
                    selectedImageId = savedImageId
                }
            }
        }
    }

    val backgroundGradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF85E4DC), Color(0xFF3FA1B7))
    )

    Box(
        modifier = Modifier.fillMaxSize().background(brush = backgroundGradient),
        contentAlignment = Alignment.TopCenter
    ) {
        if (uid == null || profileStore == null) {
            // Tampilan jika pengguna tidak login atau UID tidak ditemukan
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Gagal memuat profil: Pengguna tidak ditemukan.", color = Color.White)
            }
        } else {
            // Tampilan utama jika semua aman
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(12.dp).fillMaxSize(),
                contentPadding = PaddingValues(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(40.dp))
                    TitleSection()
                }
                item {
                    ProfileImage(imageRes = selectedImageId)
                }
                item {
                    NameSection(nickname = nickname)
                }
                item {
                    FormCard(
                        profileImages = profileImages,
                        onImageSelected = { selectedImageId = it },
                        nickname = nickname,
                        onNicknameChange = { if (it.length <= 16) nickname = it },
                        description = description,
                        onDescriptionChange = { if (it.length <= 40) description = it },
                        onSave = {
                            coroutineScope.launch {
                                // Panggil saveProfile dari instance dataStore yang aman
                                profileStore.saveProfile(nickname, description, selectedImageId)
                                navController.navigate(Screen.Dashboard.route) {
                                    popUpTo(Screen.Dashboard.route) { inclusive = true }
                                }
                            }
                        }
                    )
                }
                item {
                    ReturnButton(onClick = { navController.popBackStack() })
                }
            }
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
fun NameSection(nickname: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFFB6D3F1), RoundedCornerShape(12.dp))
            .padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        Text(
            text = nickname.ifEmpty { "Nama Karakter" },
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
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFDFF5F3),unfocusedContainerColor = Color(0xFFDFF5F3)),
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
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFDFF5F3),unfocusedContainerColor = Color(0xFFDFF5F3)),
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
fun ReturnButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
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
