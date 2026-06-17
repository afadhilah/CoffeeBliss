@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.aflabs.coffeebliss.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.aflabs.coffeebliss.ui.CoffeeBlissViewModel
import com.aflabs.coffeebliss.ui.theme.CoffeePrimary
import com.aflabs.coffeebliss.ui.theme.CoffeeSecondary
import com.aflabs.coffeebliss.ui.theme.CoffeeTertiary
import com.aflabs.coffeebliss.util.QrCodeGenerator
import java.text.NumberFormat
import java.util.Locale

// Rupiah Formatter Utility
fun formatRupiah(amount: Double): String {
    return try {
        val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        formatter.format(amount).replace("Rp", "Rp ").replace(",00", "")
    } catch (e: Exception) {
        "Rp ${String.format("%,.0f", amount)}"
    }
}

// 1. SPLASH SCREEN
@Composable
fun SplashScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(CoffeePrimary, Color(0xFF2E1C16))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.LocalCafe,
                contentDescription = "Logo Coffee Bliss",
                tint = CoffeeTertiary,
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "COFFEE BLISS",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 2.sp
            )
            Text(
                text = "Digital Membership Card",
                fontSize = 16.sp,
                color = Color.LightGray,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(48.dp))
            Button(
                onClick = { navController.navigate("member_list") },
                colors = ButtonDefaults.buttonColors(containerColor = CoffeeTertiary),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "Mulai",
                    color = CoffeePrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun MemberListScreen(navController: NavController, viewModel: CoffeeBlissViewModel) {
    val members by viewModel.allMembers.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedTier by remember { mutableStateOf("Semua") }
    val tiers = listOf("Semua", "Bronze", "Silver", "Gold")

    Scaffold(
        containerColor = Color(0xFFF9FAFC),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_member") },
                containerColor = CoffeePrimary,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Member")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Modern Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Halo, Coffee Lover!", fontSize = 14.sp, color = Color.Gray)
                    Text("Coffee Bliss Members", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = CoffeePrimary)
                }
                Card(
                    shape = RoundedCornerShape(50.dp),
                    colors = CardDefaults.cardColors(containerColor = CoffeeSecondary.copy(alpha = 0.2f)),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.AccountCircle, contentDescription = null, tint = CoffeePrimary, modifier = Modifier.size(32.dp))
                    }
                }
            }

            // Search and Filter Tune Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Cari member...", color = Color.Gray, fontSize = 14.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = CoffeePrimary.copy(alpha = 0.5f),
                        unfocusedBorderColor = Color.LightGray.copy(alpha = 0.3f),
                        focusedPlaceholderColor = Color.Gray,
                        unfocusedPlaceholderColor = Color.Gray
                    ),
                    singleLine = true
                )
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(CoffeePrimary, shape = RoundedCornerShape(50.dp))
                        .clickable { /* Optional filter action */ },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Tune, contentDescription = "Filter", tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }

            // Category Tabs (Pills)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tiers.forEach { tier ->
                    val isSelected = selectedTier == tier
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (isSelected) CoffeePrimary else Color.White,
                                shape = RoundedCornerShape(50.dp)
                            )
                            .clickable { selectedTier = tier }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = tier,
                            color = if (isSelected) Color.White else CoffeePrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val filteredMembers = members.filter { member ->
                val matchesSearch = member.name.contains(searchQuery, ignoreCase = true) || 
                                     member.formattedId.contains(searchQuery, ignoreCase = true)
                val matchesTier = selectedTier == "Semua" || member.statusTier.equals(selectedTier, ignoreCase = true)
                matchesSearch && matchesTier
            }

            if (filteredMembers.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "Tidak ada member ditemukan.",
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(filteredMembers) { member ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.selectMember(member.id)
                                    navController.navigate("dashboard")
                                },
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(2.dp),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .background(CoffeePrimary.copy(alpha = 0.1f), shape = RoundedCornerShape(12.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AccountCircle,
                                            contentDescription = null,
                                            tint = CoffeePrimary,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(member.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = CoffeePrimary)
                                        Text(member.formattedId, fontSize = 12.sp, color = Color.Gray)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Card(
                                            colors = CardDefaults.cardColors(
                                                containerColor = when (member.statusTier.lowercase()) {
                                                    "gold" -> Color(0xFFFFF9C4)
                                                    "silver" -> Color(0xFFF5F5F5)
                                                    else -> Color(0xFFD7CCC8)
                                                }
                                            ),
                                            shape = RoundedCornerShape(50.dp)
                                        ) {
                                            Text(
                                                text = member.statusTier,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = when (member.statusTier.lowercase()) {
                                                    "gold" -> Color(0xFFF57F17)
                                                    "silver" -> Color(0xFF616161)
                                                    else -> CoffeePrimary
                                                },
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text("${member.points}", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = CoffeePrimary)
                                        Text("Poin", fontSize = 10.sp, color = Color.Gray)
                                    }
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(CoffeePrimary, shape = RoundedCornerShape(50.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddMemberScreen(navController: NavController, viewModel: CoffeeBlissViewModel) {
    val name by viewModel.regName.collectAsState()
    val email by viewModel.regEmail.collectAsState()
    val phone by viewModel.regPhone.collectAsState()
    val context = LocalContext.current

    Scaffold(
        containerColor = Color(0xFFF9FAFC)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Modern Custom Header with Floating Back Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White, shape = RoundedCornerShape(50.dp))
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = CoffeePrimary)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text("Registrasi Member", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = CoffeePrimary)
            }

            Text(
                "Masukkan detail informasi member untuk pendaftaran kartu digital.", 
                fontSize = 14.sp, 
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { viewModel.regName.value = it },
                label = { Text("Nama Lengkap") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = CoffeePrimary) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = CoffeePrimary,
                    unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
                    focusedLabelColor = CoffeePrimary,
                    unfocusedLabelColor = Color.Gray
                )
            )

            OutlinedTextField(
                value = email,
                onValueChange = { viewModel.regEmail.value = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = CoffeePrimary) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = CoffeePrimary,
                    unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
                    focusedLabelColor = CoffeePrimary,
                    unfocusedLabelColor = Color.Gray
                )
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { viewModel.regPhone.value = it },
                label = { Text("Nomor HP") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = CoffeePrimary) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = CoffeePrimary,
                    unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
                    focusedLabelColor = CoffeePrimary,
                    unfocusedLabelColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.registerMember(
                        onSuccess = {
                            Toast.makeText(context, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
                            navController.navigate("dashboard") {
                                popUpTo("member_list")
                            }
                        },
                        onError = { message ->
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        }
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = CoffeePrimary),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                Text("Simpan Member", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

// 4. MEMBER DASHBOARD SCREEN
@Composable
fun DashboardScreen(navController: NavController, viewModel: CoffeeBlissViewModel) {
    val member by viewModel.currentMember.collectAsState()

    if (member == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = CoffeePrimary)
        }
        return
    }

    Scaffold(
        containerColor = Color(0xFFF9FAFC)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header Welcome
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Halo, Selamat Datang", fontSize = 14.sp, color = Color.Gray)
                    Text(member!!.name, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = CoffeePrimary)
                }
                IconButton(
                    onClick = {
                        viewModel.selectMember(null)
                        navController.navigate("member_list") {
                            popUpTo("member_list") { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White, shape = RoundedCornerShape(50.dp))
                ) {
                    Icon(Icons.Default.ExitToApp, contentDescription = "Keluar/Ganti Member", tint = CoffeePrimary)
                }
            }

            // Preview Member Card (Gradient Card)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(CoffeePrimary, Color(0xFF704C42))
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("COFFEE BLISS", color = CoffeeTertiary, fontWeight = FontWeight.Bold, fontSize = 16.sp, letterSpacing = 1.5.sp)
                            
                            // Badge tier di dalam kartu
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = when (member!!.statusTier.lowercase()) {
                                        "gold" -> Color(0xFFFFD54F)
                                        "silver" -> Color(0xFFE0E0E0)
                                        else -> Color(0xFFBCAAA4)
                                    }
                                ),
                                shape = RoundedCornerShape(50.dp)
                            ) {
                                Text(
                                    text = member!!.statusTier.uppercase(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = when (member!!.statusTier.lowercase()) {
                                        "gold" -> Color(0xFF5D4037)
                                        "silver" -> Color(0xFF424242)
                                        else -> Color(0xFF3E2723)
                                    },
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Column {
                            Text(member!!.name.uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp, letterSpacing = 1.sp)
                            Text("No. Member: ${member!!.formattedId}", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Column {
                                Text("VALID THRU", color = Color.White.copy(alpha = 0.5f), fontSize = 8.sp)
                                Text("PERMANENT", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("TOTAL POIN", color = Color.White.copy(alpha = 0.5f), fontSize = 9.sp)
                                Text("${member!!.points} pts", color = CoffeeTertiary, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                            }
                        }
                    }
                }
            }

            Text("Menu Utama", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = CoffeePrimary)

            // Navigation Grid Options
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    // Card Option
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(110.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { navController.navigate("card") },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(CoffeePrimary.copy(alpha = 0.08f), shape = RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.QrCode, contentDescription = null, tint = CoffeePrimary, modifier = Modifier.size(26.dp))
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Kartu Member", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = CoffeePrimary)
                        }
                    }

                    // Transaction History Option
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(110.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { navController.navigate("transactions") },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(CoffeePrimary.copy(alpha = 0.08f), shape = RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.History, contentDescription = null, tint = CoffeePrimary, modifier = Modifier.size(26.dp))
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Transaksi", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = CoffeePrimary)
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    // Rewards Option
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(110.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { navController.navigate("rewards") },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(CoffeePrimary.copy(alpha = 0.08f), shape = RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = CoffeePrimary, modifier = Modifier.size(26.dp))
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Tukar Poin", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = CoffeePrimary)
                        }
                    }

                    // Profile Option
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(110.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { navController.navigate("profile") },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(CoffeePrimary.copy(alpha = 0.08f), shape = RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = CoffeePrimary, modifier = Modifier.size(26.dp))
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Profil Saya", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = CoffeePrimary)
                        }
                    }
                }
            }
        }
    }
}

// 5. DIGITAL MEMBERSHIP CARD SCREEN
@Composable
fun MemberCardScreen(navController: NavController, viewModel: CoffeeBlissViewModel) {
    val member by viewModel.currentMember.collectAsState()

    if (member == null) return

    Scaffold(
        containerColor = Color(0xFFF9FAFC)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Modern Custom Header with Floating Back Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White, shape = RoundedCornerShape(50.dp))
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = CoffeePrimary)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text("Kartu Member", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = CoffeePrimary)
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Elegant membership card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(CoffeePrimary, Color(0xFF704C42))
                                )
                            )
                            .padding(24.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("COFFEE BLISS", color = CoffeeTertiary, fontWeight = FontWeight.Bold, fontSize = 16.sp, letterSpacing = 2.sp)
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = when (member!!.statusTier.lowercase()) {
                                            "gold" -> Color(0xFFFFD54F)
                                            "silver" -> Color(0xFFE0E0E0)
                                            else -> Color(0xFFBCAAA4)
                                        }
                                    ),
                                    shape = RoundedCornerShape(50.dp)
                                ) {
                                    Text(
                                        text = member!!.statusTier.uppercase(),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = when (member!!.statusTier.lowercase()) {
                                            "gold" -> Color(0xFF5D4037)
                                            "silver" -> Color(0xFF424242)
                                            else -> Color(0xFF3E2723)
                                        },
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // QR Code Box
                            val qrBitmap = QrCodeGenerator.generate(member!!.formattedId, 400)
                            if (qrBitmap != null) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    modifier = Modifier
                                        .size(190.dp)
                                        .padding(4.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                ) {
                                    Image(
                                        bitmap = qrBitmap.asImageBitmap(),
                                        contentDescription = "Member QR Code",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(12.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(member!!.name.uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp, letterSpacing = 1.sp)
                            Text("ID: ${member!!.formattedId}", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp, fontWeight = FontWeight.Medium)

                            Spacer(modifier = Modifier.height(24.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("STATUS", color = Color.White.copy(alpha = 0.5f), fontSize = 8.sp)
                                    Text(member!!.statusTier.uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text("TOTAL POIN", color = Color.White.copy(alpha = 0.5f), fontSize = 8.sp)
                                    Text("${member!!.points} pts", color = CoffeeTertiary, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    "Tunjukkan QR Code ini ke barista saat bertransaksi untuk mengumpulkan poin atau melakukan penukaran reward.",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

// 6// 6. TRANSACTION HISTORY SCREEN
@Composable
fun TransactionHistoryScreen(navController: NavController, viewModel: CoffeeBlissViewModel) {
    val transactions by viewModel.transactionHistory.collectAsState()
    val member by viewModel.currentMember.collectAsState()

    if (member == null) return

    Scaffold(
        containerColor = Color(0xFFF9FAFC),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_transaction") },
                containerColor = CoffeePrimary,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                elevation = FloatingActionButtonDefaults.elevation(4.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Transaksi")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Modern Custom Header with Floating Back Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White, shape = RoundedCornerShape(50.dp))
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = CoffeePrimary)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text("Riwayat Transaksi", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = CoffeePrimary)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                if (transactions.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Belum ada riwayat transaksi.", color = Color.Gray)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(transactions) { tx ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(2.dp),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .background(CoffeePrimary.copy(alpha = 0.08f), shape = RoundedCornerShape(12.dp)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Payments,
                                                contentDescription = null,
                                                tint = CoffeePrimary,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(tx.date, fontSize = 11.sp, color = Color.Gray)
                                            Text(formatRupiah(tx.amount), fontWeight = FontWeight.Bold, fontSize = 16.sp, color = CoffeePrimary)
                                        }
                                    }
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = CoffeeTertiary.copy(alpha = 0.15f)),
                                        shape = RoundedCornerShape(50.dp)
                                    ) {
                                        Text(
                                            "+${tx.pointEarned} Pts",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 12.sp,
                                            color = CoffeePrimary,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// 7. ADD TRANSACTION SCREEN
@Composable
fun AddTransactionScreen(navController: NavController, viewModel: CoffeeBlissViewModel) {
    val amountStr by viewModel.txAmount.collectAsState()
    val context = LocalContext.current

    // Real-time calculation of points (Rp 10.000 = 1 Point)
    val pointsCalculated = remember(amountStr) {
        val amt = amountStr.toDoubleOrNull() ?: 0.0
        (amt / 10000).toInt()
    }

    // Modal state for point updated dialog
    var showSuccessDialog by remember { mutableStateOf(false) }
    var pointsAdded by remember { mutableStateOf(0) }

    Scaffold(
        containerColor = Color(0xFFF9FAFC)
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Modern Custom Header with Floating Back Button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White, shape = RoundedCornerShape(50.dp))
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = CoffeePrimary)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Tambah Transaksi", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = CoffeePrimary)
                }

                Text(
                    "Simulasikan pencatatan pembelian kasir untuk memberikan poin kepada member.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = amountStr,
                    onValueChange = { viewModel.txAmount.value = it },
                    label = { Text("Nominal Pembelian (Rp)") },
                    leadingIcon = { Icon(Icons.Default.Payments, contentDescription = null, tint = CoffeePrimary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = CoffeePrimary,
                        unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
                        focusedLabelColor = CoffeePrimary,
                        unfocusedLabelColor = Color.Gray
                    )
                )

                // Live Points Calculator Preview
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CoffeePrimary.copy(alpha = 0.05f)),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Aturan Point", fontSize = 11.sp, color = Color.Gray)
                            Text("Rp 10.000 = 1 Point", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = CoffeePrimary)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Poin Didapat", fontSize = 11.sp, color = Color.Gray)
                            Text("$pointsCalculated Poin", fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = CoffeePrimary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.addTransaction(
                            onSuccess = { added ->
                                pointsAdded = added
                                showSuccessDialog = true
                            },
                            onError = { message ->
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CoffeePrimary),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Text("Simpan Transaksi", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            // POINT UPDATED SUCCESS DIALOG
            if (showSuccessDialog) {
                AlertDialog(
                    onDismissRequest = {},
                    confirmButton = {
                        Button(
                            onClick = {
                                showSuccessDialog = false
                                navController.popBackStack()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CoffeePrimary),
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                        ) {
                            Text("Selesai", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    },
                    icon = {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(64.dp)
                        )
                    },
                    title = {
                        Text(
                            "Transaksi Berhasil!",
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            color = CoffeePrimary
                        )
                    },
                    text = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "+$pointsAdded Poin",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = CoffeeTertiary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Poin berhasil ditambahkan ke kartu member digital.",
                                textAlign = TextAlign.Center,
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    },
                    shape = RoundedCornerShape(28.dp),
                    containerColor = Color.White
                )
            }
        }
    }
}

// 8. REWARDS / REDEEM POINT SCREEN
@Composable
fun RewardsScreen(navController: NavController, viewModel: CoffeeBlissViewModel) {
    val member by viewModel.currentMember.collectAsState()
    val redeems by viewModel.redeemHistory.collectAsState()
    val context = LocalContext.current

    if (member == null) return

    // List of available static rewards
    val rewardsList = listOf(
        RewardItem("Espresso", 50, "Nikmati secangkir espresso murni yang pekat dan harum."),
        RewardItem("Cappuccino", 100, "Perpaduan pas antara espresso, susu hangat, dan busa susu."),
        RewardItem("Latte Gratis", 150, "Secangkir caffe latte lembut dengan seni susu indah di atasnya.")
    )

    // Alert dialog states
    var selectedReward by remember { mutableStateOf<RewardItem?>(null) }
    var showRedeemConfirm by remember { mutableStateOf(false) }
    var showRedeemSuccess by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color(0xFFF9FAFC)
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Modern Custom Header with Floating Back Button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White, shape = RoundedCornerShape(50.dp))
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = CoffeePrimary)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Tukar Poin / Rewards", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = CoffeePrimary)
                }

                // Point counter display (Gradient Card)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(CoffeePrimary, Color(0xFF704C42))
                                )
                            )
                            .fillMaxWidth()
                            .padding(24.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Poin Anda Saat Ini", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("${member!!.points} Poin", color = CoffeeTertiary, fontWeight = FontWeight.ExtraBold, fontSize = 28.sp)
                            }
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(Color.White.copy(alpha = 0.15f), shape = RoundedCornerShape(14.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Stars, contentDescription = null, tint = CoffeeTertiary, modifier = Modifier.size(28.dp))
                            }
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    item {
                        Text("Pilihan Menu Reward", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = CoffeePrimary, modifier = Modifier.padding(vertical = 4.dp))
                    }

                    items(rewardsList) { reward ->
                        val isAffordable = member!!.points >= reward.pointsCost
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isAffordable) Color.White else Color(0xFFF1EFEA).copy(alpha = 0.7f)
                            ),
                            elevation = CardDefaults.cardElevation(if (isAffordable) 2.dp else 0.dp),
                            shape = RoundedCornerShape(20.dp),
                            border = if (isAffordable) BorderStroke(1.dp, CoffeePrimary.copy(alpha = 0.05f)) else null
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .background(
                                                    if (isAffordable) CoffeePrimary.copy(alpha = 0.08f) else Color.LightGray.copy(alpha = 0.3f),
                                                    shape = RoundedCornerShape(10.dp)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.LocalCafe,
                                                contentDescription = null,
                                                tint = if (isAffordable) CoffeePrimary else Color.Gray,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            reward.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = if (isAffordable) CoffeePrimary else Color.Gray
                                        )
                                    }
                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (isAffordable) CoffeeTertiary.copy(alpha = 0.15f) else Color.LightGray.copy(alpha = 0.3f)
                                        ),
                                        shape = RoundedCornerShape(50.dp)
                                    ) {
                                        Text(
                                            "${reward.pointsCost} Pts",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 12.sp,
                                            color = if (isAffordable) CoffeePrimary else Color.Gray,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    reward.description,
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    lineHeight = 16.sp
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = {
                                        selectedReward = reward
                                        showRedeemConfirm = true
                                    },
                                    enabled = isAffordable,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = CoffeePrimary,
                                        disabledContainerColor = Color.LightGray.copy(alpha = 0.5f),
                                        disabledContentColor = Color.Gray
                                    ),
                                    shape = RoundedCornerShape(50.dp),
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .height(38.dp)
                                ) {
                                    Text(
                                        if (isAffordable) "Tukar" else "Poin Tidak Cukup",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    // Display Redeem logs if present
                    if (redeems.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Riwayat Penukaran Hadiah", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = CoffeePrimary, modifier = Modifier.padding(vertical = 4.dp))
                        }
                        items(redeems) { redeem ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(2.dp),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .background(Color.Red.copy(alpha = 0.05f), shape = RoundedCornerShape(10.dp)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.CardGiftcard,
                                                contentDescription = null,
                                                tint = Color.Red.copy(alpha = 0.8f),
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(redeem.date, fontSize = 11.sp, color = Color.Gray)
                                            Text(redeem.rewardName, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = CoffeePrimary)
                                        }
                                    }
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.08f)),
                                        shape = RoundedCornerShape(50.dp)
                                    ) {
                                        Text(
                                            "-${redeem.pointsDeducted} Pts",
                                            color = Color.Red,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // REDEEM CONFIRMATION DIALOG
            if (showRedeemConfirm && selectedReward != null) {
                AlertDialog(
                    onDismissRequest = { showRedeemConfirm = false },
                    confirmButton = {
                        Button(
                            onClick = {
                                showRedeemConfirm = false
                                viewModel.redeemReward(
                                    rewardName = selectedReward!!.name,
                                    pointsCost = selectedReward!!.pointsCost,
                                    onSuccess = {
                                        showRedeemSuccess = true
                                    },
                                    onError = { msg ->
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                    }
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CoffeePrimary),
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier.height(44.dp)
                        ) {
                            Text("Konfirmasi", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showRedeemConfirm = false }) {
                            Text("Batal", color = CoffeePrimary, fontWeight = FontWeight.Bold)
                        }
                    },
                    title = {
                        Text(
                            "Konfirmasi Penukaran",
                            fontWeight = FontWeight.Bold,
                            color = CoffeePrimary
                        )
                    },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Tukar reward ini?", fontWeight = FontWeight.SemiBold, color = Color.Gray)
                            Text(
                                "${selectedReward!!.name} (${selectedReward!!.pointsCost} Poin)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = CoffeePrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Divider(color = Color.LightGray.copy(alpha = 0.5f))
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total Poin Anda:", color = Color.Gray)
                                Text("${member!!.points}", fontWeight = FontWeight.Bold, color = CoffeePrimary)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Sisa Poin:", color = Color.Gray)
                                Text(
                                    "${member!!.points - selectedReward!!.pointsCost}",
                                    fontWeight = FontWeight.Bold,
                                    color = CoffeeTertiary
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(28.dp),
                    containerColor = Color.White
                )
            }

            // REDEEM SUCCESS DIALOG
            if (showRedeemSuccess && selectedReward != null) {
                AlertDialog(
                    onDismissRequest = {
                        showRedeemSuccess = false
                        selectedReward = null
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                showRedeemSuccess = false
                                selectedReward = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CoffeePrimary),
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                        ) {
                            Text("Selesai", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    },
                    icon = {
                        Icon(
                            Icons.Default.CardGiftcard,
                            contentDescription = null,
                            tint = CoffeeTertiary,
                            modifier = Modifier.size(64.dp)
                        )
                    },
                    title = {
                        Text(
                            "Redeem Berhasil!",
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            color = CoffeePrimary
                        )
                    },
                    text = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Selamat!",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = CoffeeTertiary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Silakan ambil reward Anda:\n${selectedReward!!.name}\ndi Barista.",
                                textAlign = TextAlign.Center,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "Sisa Poin Anda: ${member!!.points}",
                                fontWeight = FontWeight.SemiBold,
                                color = CoffeePrimary,
                                fontSize = 14.sp
                            )
                        }
                    },
                    shape = RoundedCornerShape(28.dp),
                    containerColor = Color.White
                )
            }
        }
    }
}

// 9. PROFILE SCREEN
@Composable
fun ProfileScreen(navController: NavController, viewModel: CoffeeBlissViewModel) {
    val member by viewModel.currentMember.collectAsState()

    if (member == null) return

    Scaffold(
        containerColor = Color(0xFFF9FAFC)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Modern Custom Header with Floating Back Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White, shape = RoundedCornerShape(50.dp))
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = CoffeePrimary)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text("Profil Member", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = CoffeePrimary)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Stylized Avatar Box
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(CoffeePrimary, Color(0xFF704C42))
                            ),
                            shape = RoundedCornerShape(32.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = member!!.name.take(2).uppercase(),
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Text(
                    text = member!!.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = CoffeePrimary
                )

                Card(
                    colors = CardDefaults.cardColors(containerColor = CoffeeTertiary.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Text(
                        text = member!!.statusTier,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp,
                        color = CoffeePrimary,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Member Info Details
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoRow(label = "Nomor Member", value = member!!.formattedId)
                    InfoRow(label = "Email", value = member!!.email)
                    InfoRow(label = "Nomor Handphone", value = member!!.phone)
                    InfoRow(label = "Jumlah Poin", value = "${member!!.points} Poin")
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        viewModel.selectMember(null)
                        navController.navigate("member_list") {
                            popUpTo("member_list") { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Text("Keluar / Ganti Akun", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, CoffeePrimary.copy(alpha = 0.03f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, color = Color.Gray, fontSize = 14.sp)
            Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = CoffeePrimary)
        }
    }
}

// Reward list helper item
data class RewardItem(
    val name: String,
    val pointsCost: Int,
    val description: String
)
