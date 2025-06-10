// File: MainActivity.kt
package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.screen.*
import dagger.hilt.android.AndroidEntryPoint // ⬅️ Tambahkan ini


@AndroidEntryPoint // ⬅️ Tambahkan ini
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier, color = MaterialTheme.colorScheme.background) {
                    AppNavigator()
                }
            }
        }
    }
}

@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { navController.navigate("menu") },
                onRegisterClick = { navController.navigate("register") }
            )
        }
        composable(route = "register") {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate("menu") },
                onNavigateToLogin = { navController.navigate("login") }
            )
        }

        composable("menu") {
            var selectedTab by rememberSaveable { mutableStateOf(BottomTab.Menu) }

            MenuScreen(
                onNavigateToProfile = { navController.navigate("profile") },
                onNavigateToCart = { navController.navigate("order") },
                onCheckoutClick = { navController.navigate("checkout") },
                selectedTab = selectedTab,
                onTabSelected = {
                    selectedTab = it
                    when (it) {
                        BottomTab.Menu -> navController.navigate("menu")
                        BottomTab.Orders -> navController.navigate("order")
                        BottomTab.History -> navController.navigate("history")
                        BottomTab.Profile -> navController.navigate("profile")
                    }
                }
            )
        }
        composable("order") {
            OrderScreen(onBackClick = { navController.popBackStack() })
        }
        composable("profile") {
            ProfileScreen(onBackClick = { navController.popBackStack() })
        }

        composable("checkout") {
            CheckoutScreen(
                onBackClick = { navController.popBackStack() },
                onPaymentSuccess = {
                    navController.navigate("order") // misal kembali ke order
                }
            )
        }
    }
}
