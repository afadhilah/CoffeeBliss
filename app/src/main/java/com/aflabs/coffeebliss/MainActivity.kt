package com.aflabs.coffeebliss

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aflabs.coffeebliss.data.CoffeeBlissDatabase
import com.aflabs.coffeebliss.data.CoffeeBlissRepository
import com.aflabs.coffeebliss.ui.CoffeeBlissViewModel
import com.aflabs.coffeebliss.ui.CoffeeBlissViewModelFactory
import com.aflabs.coffeebliss.ui.screens.*
import com.aflabs.coffeebliss.ui.theme.CoffeeBlissTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Room database & Repository
        val database = CoffeeBlissDatabase.getDatabase(applicationContext)
        val repository = CoffeeBlissRepository(database.dao)

        // Initialize ViewModel
        val viewModel = ViewModelProvider(
            this,
            CoffeeBlissViewModelFactory(repository)
        )[CoffeeBlissViewModel::class.java]

        setContent {
            CoffeeBlissTheme {
                val navController = rememberNavController()
                
                NavHost(
                    navController = navController,
                    startDestination = "splash"
                ) {
                    composable("splash") {
                        SplashScreen(navController)
                    }
                    composable("member_list") {
                        MemberListScreen(navController, viewModel)
                    }
                    composable("add_member") {
                        AddMemberScreen(navController, viewModel)
                    }
                    composable("dashboard") {
                        DashboardScreen(navController, viewModel)
                    }
                    composable("card") {
                        MemberCardScreen(navController, viewModel)
                    }
                    composable("transactions") {
                        TransactionHistoryScreen(navController, viewModel)
                    }
                    composable("add_transaction") {
                        AddTransactionScreen(navController, viewModel)
                    }
                    composable("rewards") {
                        RewardsScreen(navController, viewModel)
                    }
                    composable("profile") {
                        ProfileScreen(navController, viewModel)
                    }
                }
            }
        }
    }
}

