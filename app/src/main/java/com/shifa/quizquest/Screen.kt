package com.shifa.quizquest

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome_screen")
    object Dashboard : Screen("dashboard_screen")
    object Login : Screen("Login_Screen")
    object Profile : Screen("profile_screen")
    object Settings : Screen("setting_screen")
    object Signup : Screen("Signup_screen")
    object Quiz : Screen("quiz_screen")
}