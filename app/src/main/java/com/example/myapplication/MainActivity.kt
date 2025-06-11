package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.ui.screen.*
import com.example.myapplication.ui.viewmodel.TableViewModel
//import com.example.myapplication.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint // ⬅ Tambahkan ini


@AndroidEntryPoint // ⬅ Tambahkan ini
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
//    val userViewModel: UserViewModel = hiltViewModel()
    // ✅ Ambil shared TableViewModel di sini
    val tableViewModel: TableViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = "login") {
        composable("scanqr") {
            ScanQRScreen(
                onScanComplete = { navController.navigate("login") },
                viewModel = tableViewModel // ✅ tambahkan ini
            )
        }
        composable("login") {
            LoginScreen(
                navController = navController,
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

        composable(
            route = "menu?userId={userId}",
            arguments = listOf(navArgument("userId") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: -1
            var selectedTab by rememberSaveable { mutableStateOf(BottomTab.Menu) }

            MenuScreen(
                onNavigateToProfile = { navController.navigate("profile/$userId") },
                onNavigateToCart = { navController.navigate("order") },
                onCheckoutClick = { navController.navigate("checkout") },
                onNavigateToScanQR = { navController.navigate("scanqr") },
                selectedTab = selectedTab,
                onTabSelected = {
                    selectedTab = it
                    when (it) {
                        BottomTab.Menu -> navController.navigate("menu?userId=$userId")
                        BottomTab.Orders -> navController.navigate("order")
                        BottomTab.Cart -> navController.navigate("checkout")
                        BottomTab.History -> navController.navigate("history")
                        BottomTab.Profile -> navController.navigate("profile/$userId")
                    }
                },
                tableViewModel = tableViewModel
            )
        }
        composable("order") {
            OrderScreen(onBackClick = { navController.popBackStack() })
        }
        composable(
            route = "profile/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: -1
            Log.d("NavDebug", "Navigated with userId: $userId")
            ProfileScreen(userId = userId)
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