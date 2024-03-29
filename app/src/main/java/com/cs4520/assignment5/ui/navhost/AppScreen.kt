package com.cs4520.assignment5.ui.navhost

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cs4520.assignment5.core.background.WorkerScheduler
import com.cs4520.assignment5.features.login.LoginViewModel
import com.cs4520.assignment5.ui.common.ProductListScreens
import com.cs4520.assignment5.ui.login.LoginScreen
import com.cs4520.assignment5.ui.productlist.ProductListScreen

@Composable
fun AmazingProductListApp(
    viewModel: LoginViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
    workerScheduler: WorkerScheduler
) {
    NavHost(
        navController = navController,
        startDestination = ProductListScreens.Login.name,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = ProductListScreens.Login.name) {
            LoginScreen(
                login = { username, password ->
                    val isLoginSuccessful = viewModel.login(username, password)
                    if (isLoginSuccessful) {
                        navController.navigate(ProductListScreens.ProductList.name)
                    }
                    isLoginSuccessful
                },
            )
        }
        composable(route = ProductListScreens.ProductList.name) {
            ProductListScreen(workerScheduler = workerScheduler)
        }
    }
}

