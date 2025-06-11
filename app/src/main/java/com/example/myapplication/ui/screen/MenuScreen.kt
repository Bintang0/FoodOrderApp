package com.example.myapplication.ui.screen

import android.os.Build
import java.text.NumberFormat
import java.util.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import com.example.myapplication.data.model.MenuItemEntity
import com.example.myapplication.data.model.OrderEntity
import com.example.myapplication.ui.viewmodel.MenuItemViewModel
import com.example.myapplication.ui.viewmodel.OrderViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import com.example.myapplication.ui.util.extractDominantColor
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.viewinterop.AndroidView
import android.widget.FrameLayout

import android.graphics.RenderEffect
import android.graphics.Shader

import android.view.View

import androidx.compose.runtime.Composable

import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.viewmodel.TableViewModel
import java.text.SimpleDateFormat



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    viewModel: MenuItemViewModel = hiltViewModel(),
    onNavigateToProfile: () -> Unit,
    onNavigateToScanQR: () -> Unit,
    onNavigateToCart: () -> Unit,
    onCheckoutClick: () -> Unit,
    selectedTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit,
    tableViewModel: TableViewModel // tambahkan ini
) {
    val allMenuItems by viewModel.menuItems.observeAsState(emptyList())
    var selectedItem by remember { mutableStateOf<MenuItemEntity?>(null) }
    var selectedCategory by remember { mutableStateOf("Semua") }
    val orderViewModel: OrderViewModel = hiltViewModel()
    val orders by orderViewModel.orders.observeAsState(emptyList())
    var showAdded by remember { mutableStateOf<String?>(null) }

    //Slide down
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val filteredItems = if (selectedCategory == "Semua") {
        allMenuItems
    } else {
        allMenuItems.filter { it.kategori == selectedCategory }
    }

    val tableNumber by tableViewModel.tableNumber.collectAsState()

//    val categories = listOf("Semua") + allMenuItems.map { it.kategori }.distinct()


    val categories = listOf(
        "Semua" to R.drawable.ic_all,
        "Makanan" to R.drawable.ic_food,
        "Minuman" to R.drawable.ic_drink,
        "Dessert" to R.drawable.ic_dessert,
        "Snack" to R.drawable.ic_snack
    )




    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            FloatingBottomNavBar(
                selectedTab = selectedTab,
                onTabSelected = onTabSelected
            )
        },
        topBar = {
            Surface(
                tonalElevation = 0.dp,
                color = Color.Transparent, // 💡 ini penting agar tidak render warna background
                shadowElevation = 0.dp,    // hilangkan bayangan jika tidak diinginkan

            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = onNavigateToCart) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search), // pastikan drawable ada
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("location:", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Text("New York", style = MaterialTheme.typography.bodyMedium)
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .clickable { onNavigateToScanQR() } // pindah ke halaman scan QR
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_table), // ikon meja di drawable
                            contentDescription = "Meja",
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Meja: ${tableNumber ?: "-"}", // nilai default jika null
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                }
            }
        }
        ,
        snackbarHost = {
            showAdded?.let {
                Snackbar(
                    modifier = Modifier.padding(8.dp),
                    action = {
                        TextButton(onClick = { showAdded = null }) { Text("Tutup") }
                    }
                ) { Text("$it ditambahkan ke keranjang") }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                CategoryFilterBar(
                    categories = categories,
                    selected = selectedCategory,
                    onSelect = { selectedCategory = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

//                CarouselSection(items = allMenuItems.take(5))

                PromoBannerCard()

//                Spacer(modifier = Modifier.height(12.dp))

//                CategoryFilter(categories, selectedCategory) {
//                    selectedCategory = it
//                }

                Spacer(modifier = Modifier.height(12.dp))

                if (filteredItems.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Belum ada menu untuk kategori ini", style = MaterialTheme.typography.bodyLarge)
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(filteredItems) { item ->
                            MenuGridItemCard(
                                item = item,
                                onClick = { selectedItem = it }
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = selectedItem != null,
                enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(300)),
                exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(300)),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {

                LaunchedEffect(selectedItem) {
                    if (selectedItem != null && !sheetState.isVisible) {
                        sheetState.show()
                    } else if (selectedItem == null && sheetState.isVisible) {
                        sheetState.hide()
                    }
                }

                if (selectedItem != null) {
                    ModalBottomSheet(
                        onDismissRequest = { selectedItem = null },
                        sheetState = sheetState,
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                        tonalElevation = 8.dp,
                        scrimColor = Color.Transparent // 👈 hindari efek gelap default
                    ) {
                        val item = selectedItem!!

                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Purchase Confirmation", style = MaterialTheme.typography.titleLarge)
                                IconButton(onClick = {
                                    coroutineScope.launch {
                                        sheetState.hide()
                                        selectedItem = null
                                    }
                                }) {
                                    Icon(Icons.Default.Close, contentDescription = "Tutup")
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Image(
                                painter = painterResource(id = item.imageResId),
                                contentDescription = item.nama,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("You will buy", style = MaterialTheme.typography.labelMedium)
                            Text(item.nama, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(vertical = 4.dp))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Kategori: ${item.kategori}")
                            Text("Harga: Rp${item.harga}")
                            Spacer(modifier = Modifier.height(12.dp))
                            Surface(
                                color = Color(0xFFF3F3F3),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("Important Notes", style = MaterialTheme.typography.labelMedium)
                                    Text("Deskripsi: Menu favorit khas rumah yang disajikan dengan nasi putih dan topping spesial.", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        orderViewModel.addOrder(
                                            OrderEntity(
                                                menuName = item.nama,
                                                quantity = 1,
                                                price = item.harga,
                                                status = "Preparing", // default status
                                                date = getCurrentFormattedDate() // ganti dengan tanggal saat ini
                                            )
                                        )
                                        coroutineScope.launch {
                                            sheetState.hide()
                                            selectedItem = null
                                        }
                                        onCheckoutClick() // ✅ navigasi ke halaman checkout
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                                ) {
                                    Text("Yes, Buy Package")
                                }
                                OutlinedButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            sheetState.hide()
                                            selectedItem = null
                                        }
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Cancel")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun getCurrentFormattedDate(): String {
    val formatter = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    return formatter.format(Date())
}

@Composable
fun MenuGridItemCard(
    item: MenuItemEntity,
    onClick: (MenuItemEntity) -> Unit
) {
    val formattedHarga = NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(item.harga)

    Card(
        onClick = { onClick(item) },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = item.imageResId),
                contentDescription = item.nama,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                Text(
                    text = item.nama,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1
                )
                Text(
                    text = item.kategori,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formattedHarga,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


@Composable
fun PromoBannerCard(
    modifier: Modifier = Modifier,
    imageResId: Int = R.drawable.banner_sushi
) {
    val context = LocalContext.current
    var dominantColor by remember { mutableStateOf(Color.Gray) }
    val fallbackColor = MaterialTheme.colorScheme.surface.toArgb()

    LaunchedEffect(imageResId) {
        dominantColor = extractDominantColor(context, imageResId, fallbackColor)
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(20.dp))
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                // Gambar tetap utuh
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = "Promo Banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )

                // Gradasi hanya pada sisi kanan gambar (30-40% area)
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fraction = 0.5f) // hanya sisi kanan
                        .align(Alignment.CenterEnd)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    dominantColor
                                )
                            )
                        )
                )
            }


            // 🔹 Box teks dengan warna flat
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(dominantColor)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "Discount 50%",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                    Text(
                        "learn more...",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}


@Composable
fun CategoryFilter(
    categories: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.horizontalScroll(rememberScrollState())
    ) {
        categories.forEach { category ->
            AssistChip(
                onClick = { onSelect(category) },
                label = { Text(category) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (selected == category) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    labelColor = if (selected == category) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}

@Composable
fun CategoryFilterBar(
    categories: List<Pair<String, Int>>,
    selected: String,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        categories.forEach { (category, iconResId) ->
            Surface(
                shape = RoundedCornerShape(50),
                color = if (selected == category)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.surfaceVariant,
                shadowElevation = 1.dp,
                modifier = Modifier
                    .clickable { onSelect(category) }
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = iconResId),
                        contentDescription = category,
                        modifier = Modifier
                            .size(20.dp)
                            .clip(RoundedCornerShape(50))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        category,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (selected == category)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

enum class BottomTab(val label: String, val icon: ImageVector) {
    Menu("Menu", Icons.Default.Restaurant),
    Orders("Pesanan", Icons.Default.List),
    Cart("Keranjang", Icons.Default.ShoppingCart),
    History("Riwayat", Icons.Default.History),
    Profile("Profil", Icons.Default.Person)
}

@Composable
fun FloatingBottomNavBar(
    selectedTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            shape = RoundedCornerShape(50),
            color = Color.White.copy(alpha = 0.9f), // transparan lembut
            shadowElevation = 8.dp // bayangan agar melayang
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomTab.values().forEach { tab ->
                    IconButton(
                        onClick = { onTabSelected(tab) },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.label,
                            tint = if (tab == selectedTab)
                                MaterialTheme.colorScheme.primary
                            else
                                Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}
