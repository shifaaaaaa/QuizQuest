package com.shifa.quizquest

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.shifa.quizquest.ui.theme.poppins

@Composable
fun SettingsScreen() {
    var darkMode by remember { mutableStateOf(false) }
    var showNickname by remember { mutableStateOf(true) }
    var brightness by remember { mutableFloatStateOf(0.3f) }
    var fontSize by remember { mutableFloatStateOf(0.5f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFDCDCDC))
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Header
        Text(
            text = "Settings",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = poppins,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Section: UI & Accessibility
        SectionHeader("UI & Accessibility", Color(0xFF64A0CE))

        SettingSlider(
            iconResId = R.drawable.text,
            label = "Fonts",
            value = fontSize,
            onValueChange = { fontSize = it }
        )

        SettingToggle(
            iconResId = R.drawable.dark,
            label = "Dark Mode",
            isChecked = darkMode,
            onCheckedChange = { darkMode = it }
        )

        SettingSlider(
            iconResId = R.drawable.bright,
            label = "Brightness",
            value = brightness,
            onValueChange = { brightness = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Section: User & Account
        SectionHeader("User & Account", Color(0xFFD77575))

        SettingItem(iconResId = R.drawable.secret, label = "Privacy :")

        SettingToggle(
            iconResId = R.drawable.name,
            label = "Show Nickname",
            isChecked = showNickname,
            onCheckedChange = { showNickname = it }
        )

        SettingItem(iconResId = R.drawable.name, label = "Credentials :")

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* TODO: change email */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF40A9A0),
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
        ) {
            Text("Change Email", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { /* TODO: change password */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFC98522),
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
        ) {
            Text("Change Password", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { /* TODO: Navigate to LoginActivity */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD74F4F),
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
        ) {
            Text("Logout", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { /* TODO: Navigate to DashboardActivity */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF42AF6B),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Return to Dashboard", fontWeight = FontWeight.Bold)
        }
    }
}

// Reusable section header
@Composable
fun SectionHeader(title: String, backgroundColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontFamily = poppins,
            fontSize = 18.sp,
            color = Color.Black,
            modifier = Modifier.padding(12.dp)
        )
    }
}

// Reusable setting with icon and label (no action)
@Composable
fun SettingItem(iconResId: Int, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = label,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
    }
}

// Reusable toggle switch item
@Composable
fun SettingToggle(
    iconResId: Int,
    label: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = label,
            modifier = Modifier
                .size(24.dp)
                .padding(bottom = 2.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF54A0D2)
            )
        )
    }
}

// Reusable slider item
@Composable
fun SettingSlider(
    iconResId: Int,
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = label,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                fontFamily = poppins,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..1f,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}
