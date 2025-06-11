package com.example.myapplication.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.ui.viewmodel.ProfileViewModel
import com.example.myapplication.ui.viewmodel.TableViewModel
import com.example.myapplication.models.UserResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userId: Int,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val user by viewModel.user

    // Load user saat screen dibuka
    LaunchedEffect(userId) {
        if (userId > 0) {
            viewModel.loadUser(userId)
        } else {
            Log.e("ProfileDebug", "Invalid userId: $userId")
        }
    }




    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Profil Saya") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Informasi Pengguna", style = MaterialTheme.typography.titleLarge)

            ProfileInfoCard(label = "Nama", value = user?.name ?: "-")
            ProfileInfoCard(label = "Email", value = user?.email ?: "-")
            ProfileInfoCard(label = "Nomor Meja", value = user?.table_number ?: "-")
        }
    }
}




@Composable
fun ProfileInfoCard(label: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}