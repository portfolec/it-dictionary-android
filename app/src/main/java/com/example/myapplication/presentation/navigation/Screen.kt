package com.example.myapplication.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Search : Screen("search")
    object Bookmarks : Screen("bookmarks")
    object Profile : Screen("profile")
    object Notifications : Screen("notifications")
    object AdminPanel : Screen("admin_panel")

    object EntryDetail : Screen("entry/{id}") {
        fun createRoute(id: String) = "entry/$id"
    }

    object CategoryBrowse : Screen("category/{name}") {
        fun createRoute(name: String) = "category/$name"
    }

    object AddEditEntry : Screen("entry_edit?id={id}") {
        fun createRoute(id: String? = null) = if (id != null) "entry_edit?id=$id" else "entry_edit"
    }
}
