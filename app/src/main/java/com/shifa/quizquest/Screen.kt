package com.shifa.quizquest

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome_screen")
    object Dashboard : Screen("dashboard_screen")
    object Login : Screen("login_Screen")
    object Splash : Screen("splash")
    object Profile : Screen("profile_screen")
    object Settings : Screen("setting_screen")
    object Signup : Screen("signup_screen")
    object Quiz : Screen("quiz_screen")
    object QuizList : Screen("quiz_list_screen")
    object QuizPlay : Screen("quiz_play_screen/{quizId}") {
        fun createRoute(quizId: Int): String = "quiz_play_screen/$quizId"
    }

}