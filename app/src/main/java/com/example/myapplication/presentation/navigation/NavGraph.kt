package com.example.myapplication.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myapplication.domain.model.User
import com.example.myapplication.domain.model.UserRole
import com.example.myapplication.presentation.admin.AdminPanelScreen
import com.example.myapplication.presentation.admin.AddEditEntryScreen
import com.example.myapplication.presentation.auth.LoginScreen
import com.example.myapplication.presentation.auth.RegisterScreen
import com.example.myapplication.presentation.bookmarks.BookmarksScreen
import com.example.myapplication.presentation.category.CategoryBrowseScreen
import com.example.myapplication.presentation.detail.EntryDetailScreen
import com.example.myapplication.presentation.home.HomeScreen
import com.example.myapplication.presentation.notifications.NotificationsScreen
import com.example.myapplication.presentation.profile.ProfileScreen
import com.example.myapplication.presentation.search.SearchScreen
import com.example.myapplication.presentation.splash.SplashScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    currentUser: User?,
    onUserLoggedIn: (User) -> Unit,
    onLogout: () -> Unit
) {
    NavHost(navController = navController, startDestination = Screen.Splash.route) {

        composable(Screen.Splash.route) {
            SplashScreen(
                onComplete = {
                    val dest = if (currentUser != null) Screen.Home.route else Screen.Login.route
                    navController.navigate(dest) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { user ->
                    onUserLoggedIn(user)           // обновляем AppContent
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = { user ->
                    onUserLoggedIn(user)           // обновляем AppContent
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable(Screen.Home.route) {
            val role = currentUser?.role ?: UserRole.USER
            key(role) {
                HomeScreen(
                    userRole = role,
                    onEntryClick = { id -> navController.navigate(Screen.EntryDetail.createRoute(id)) },
                    onCategoryClick = { name -> navController.navigate(Screen.CategoryBrowse.createRoute(name)) },
                    onSearchClick = { navController.navigate(Screen.Search.route) },
                    onNotificationsClick = { navController.navigate(Screen.Notifications.route) },
                    onAddClick = { navController.navigate(Screen.AddEditEntry.createRoute()) }
                )
            }
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onEntryClick = { id -> navController.navigate(Screen.EntryDetail.createRoute(id)) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Bookmarks.route) {
            BookmarksScreen(
                onEntryClick = { id -> navController.navigate(Screen.EntryDetail.createRoute(id)) },
                onSearchClick = { navController.navigate(Screen.Search.route) }
            )
        }

        composable(Screen.Profile.route) {
            key(currentUser?.role) {
                ProfileScreen(
                    user = currentUser,
                    onLogout = {
                        onLogout()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onAdminPanelClick = { navController.navigate(Screen.AdminPanel.route) }
                )
            }
        }

        composable(Screen.Notifications.route) {
            NotificationsScreen(onBackClick = { navController.popBackStack() })
        }

        composable(
            route = Screen.EntryDetail.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            EntryDetailScreen(
                entryId = id,
                onBackClick = { navController.popBackStack() },
                onRelatedTermClick = { navController.navigate(Screen.Search.route) }
            )
        }

        composable(
            route = Screen.CategoryBrowse.route,
            arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            CategoryBrowseScreen(
                categoryName = name,
                onEntryClick = { id -> navController.navigate(Screen.EntryDetail.createRoute(id)) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.AdminPanel.route) {
            AdminPanelScreen(
                onBackClick = { navController.popBackStack() },
                onAddClick = { navController.navigate(Screen.AddEditEntry.createRoute()) },
                onEditClick = { id -> navController.navigate(Screen.AddEditEntry.createRoute(id)) }
            )
        }

        composable(
            route = Screen.AddEditEntry.route,
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            AddEditEntryScreen(
                entryId = id,
                onBackClick = { navController.popBackStack() },
                onSaved = { navController.popBackStack() }
            )
        }
    }
}
