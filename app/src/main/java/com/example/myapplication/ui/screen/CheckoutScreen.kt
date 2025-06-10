package com.example.myapplication.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.data.model.OrderEntity
import com.example.myapplication.ui.viewmodel.OrderViewModel
import androidx.compose.material.icons.rounded.Remove

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    viewModel: OrderViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onPaymentSuccess: () -> Unit = {}
) {
    val orders by viewModel.orders.observeAsState(emptyList())
    var showConfirmDialog by remember { mutableStateOf(false) }

    val subtotal = remember(orders) { orders.sumOf { it.price * it.quantity } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Column(modifier = Modifier.padding(16.dp)) {
                // Bagian 2: Subtotal
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Subtotal", style = MaterialTheme.typography.titleMedium)
                    Text("Rp$subtotal", style = MaterialTheme.typography.titleMedium)
                }

                // Tombol bayar dan cancel
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onBackClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = { showConfirmDialog = true },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Bayar")
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxSize()
        ) {
            if (orders.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Tidak ada item untuk checkout.")
                }
            } else {
                // Bagian 1: Daftar item
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(orders) { order ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(Modifier.weight(1f)) {
                                    Text(order.menuName, style = MaterialTheme.typography.titleMedium)
                                    Text("Harga: Rp${order.price}")
                                    Text("Subtotal: Rp${order.price * order.quantity}")
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = {
                                        if (order.quantity > 1) viewModel.addOrder(order.copy(quantity = order.quantity - 1))
                                    }) {
                                        Icon(Icons.Rounded.Remove, contentDescription = "Kurang")
                                    }
                                    Text(order.quantity.toString())
                                    IconButton(onClick = {
                                        viewModel.addOrder(order.copy(quantity = order.quantity + 1))
                                    }) {
                                        Icon(Icons.Rounded.Add, contentDescription = "Tambah")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmDialog = false },
                title = { Text("Konfirmasi Pembayaran") },
                text = { Text("Apakah Anda yakin untuk membayar pesanan ini?") },
                confirmButton = {
                    TextButton(onClick = {
                        showConfirmDialog = false
                        viewModel.clearOrders()
                        onPaymentSuccess()
                    }) {
                        Text("Bayar")
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { showConfirmDialog = false }) {
                        Text("Batal")
                    }
                }
            )
        }
    }
}
