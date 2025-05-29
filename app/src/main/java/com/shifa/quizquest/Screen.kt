package com.shifa.quizquest

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome_screen")
    object Dashboard : Screen("dashboard_screen")
    object Login : Screen("Login_Screen")
    object Profile : Screen("profile_screen")
    object Setting : Screen("setting_screen")
    // Tambahkan layar lain di sini jika ada (misal: Profile, Settings, dll.)
}