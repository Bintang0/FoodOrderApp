package com.example.myapplication.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.data.model.OrderEntity
import com.example.myapplication.ui.viewmodel.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    viewModel: OrderViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onCheckoutClick: () -> Unit = {}
) {
    val orders by viewModel.orders.observeAsState(emptyList())
    var showCheckoutSuccess by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pesanan Anda") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.clearOrders() }) {
                        Icon(Icons.Default.Delete, contentDescription = "Hapus Semua")
                    }
                }
            )
        },
        bottomBar = {
            if (orders.isNotEmpty()) {
                Button(
                    onClick = {
                        viewModel.clearOrders()
                        showCheckoutSuccess = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Checkout")
                }
            }
        },
        snackbarHost = {
            if (showCheckoutSuccess) {
                Snackbar(
                    modifier = Modifier.padding(8.dp),
                    action = {
                        TextButton(onClick = { showCheckoutSuccess = false }) { Text("Tutup") }
                    }
                ) { Text("Checkout berhasil! Pesanan Anda sedang diproses.") }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (orders.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Belum ada pesanan")
                }
            } else {
                LazyColumn {
                    items(orders) { order ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(order.menuName, style = MaterialTheme.typography.titleMedium)
                                    Text("Qty: ${order.quantity}")
                                    Text("Harga: Rp${order.price}")
                                }
                                IconButton(onClick = { viewModel.deleteOrder(order) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Hapus")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
