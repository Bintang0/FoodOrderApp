package com.example.myapplication.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.R
import com.example.myapplication.data.model.OrderEntity
import com.example.myapplication.ui.viewmodel.OrderViewModel



fun getStatusColor(status: String): Color {
    return when (status) {
        "Preparing" -> Color(0xFFFFA000) // Kuning/Oranye
        "Completed" -> Color(0xFF4CAF50) // Hijau
        "Cancelled" -> Color(0xFFF44336) // Merah
        else -> Color.Gray
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    viewModel: OrderViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onCheckoutClick: () -> Unit = {}
) {
    val orders by viewModel.orders.observeAsState(emptyList())
    var selectedStatus by remember { mutableStateOf<String?>(null) }
    var showCheckoutSuccess by remember { mutableStateOf(false) }

    // Ambil status unik dari daftar pesanan
    val statusList = orders.map { it.status }.distinct()

    // Filter pesanan berdasarkan status yang dipilih
    val filteredOrders = selectedStatus?.let { status ->
        orders.filter { it.status == status }
    } ?: orders

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
                    IconButton(onClick = {
                        viewModel.checkoutOrders {
                            showCheckoutSuccess = true
                        }
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Hapus Semua")
                    }

                }
            )
        },
        bottomBar = {
            if (orders.isNotEmpty()) {
                Button(
                    onClick = {
                        viewModel.checkoutOrders {
                            showCheckoutSuccess = true
                        }
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
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (orders.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Belum ada pesanan")
                }
            } else {
                Text("Pesanan Terbaru", style = MaterialTheme.typography.titleMedium)

                LazyRow(
                    contentPadding = PaddingValues(end = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(orders) { order ->
                        Card(
                            modifier = Modifier
                                .width(200.dp)
                                .height(220.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val imageRes = when (order.menuName) {
                                    "Nasi Goreng" -> R.drawable.nasi_goreng
                                    "Mie Ayam" -> R.drawable.mie_ayam
                                    "Es Teh" -> R.drawable.es_teh
                                    else -> R.drawable.banner_sushi
                                }
                                Image(
                                    painter = painterResource(id = imageRes),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp)
                                        .clip(MaterialTheme.shapes.medium),
                                    contentScale = ContentScale.Crop
                                )
                                Text(order.menuName, style = MaterialTheme.typography.titleSmall)
                                Text("Rp${order.price}", style = MaterialTheme.typography.labelMedium)
                            }
                        }
                    }
                }

                // Tampilkan status filter
                Text("Filter Status", style = MaterialTheme.typography.titleMedium)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(statusList) { status ->
                        FilterChip(
                            selected = selectedStatus == status,
                            onClick = {
                                selectedStatus = if (selectedStatus == status) null else status
                            },
                            label = { Text(status) }
                        )
                    }
                }

                // Tampilkan hasil yang difilter
                Text("Riwayat Pesanan", style = MaterialTheme.typography.titleMedium)
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(filteredOrders) { index, order ->
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("${String.format("%02d", index + 1)}. ${order.menuName}", style = MaterialTheme.typography.bodyLarge)
                                    Text(order.date, style = MaterialTheme.typography.labelMedium)
                                }
                                Text(
                                    text = order.status,
                                    color = getStatusColor(order.status),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}




