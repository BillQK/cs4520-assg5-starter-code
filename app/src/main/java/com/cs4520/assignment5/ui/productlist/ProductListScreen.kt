package com.cs4520.assignment5.ui.productlist

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cs4520.assignment5.R
import com.cs4520.assignment5.common.ApiResult
import com.cs4520.assignment5.core.database.AppDatabase
import com.cs4520.assignment5.core.model.Product
import com.cs4520.assignment5.features.productlist.ProductListRepo
import com.cs4520.assignment5.features.productlist.ProductListViewModel
import com.cs4520.assignment5.features.productlist.ProductListViewModelFactory

@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel = viewModel(
        factory = ProductListViewModelFactory(
            LocalContext.current.applicationContext as Application,
            ProductListRepo(
                AppDatabase.getDatabase(LocalContext.current).productDao(),
                LocalContext.current,
            )
        )
    )
) {
    val productList by viewModel.productList.observeAsState(initial = null)
    LaunchedEffect(Unit) {
        viewModel.loadData()
        viewModel.scheduleProductRefreshWorker(1)
    }
    when (val result = productList) {
        is ApiResult.Success -> {
            ProductList(products = result.data)
        }

        is ApiResult.Error -> {
            ErrorView(exception = result.exception)
        }

        is ApiResult.Empty -> {
            EmptyView()
        }

        null -> LoadingView()
    }
}

@Composable
fun ProductList(products: List<Product>) {
    LazyColumn {
        items(products) { product ->
            ProductItem(product = product)
        }
    }
}

@Composable
fun ProductItem(product: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RectangleShape
    ) {
        Row(
            modifier = Modifier
                .background(
                    color = when (product.type) {
                        "Food" -> Color(0xFFFFD965)
                        "Equipment" -> Color(0xFFE06666)
                        else -> Color.White
                    }
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageId = when (product.type) {
                "Food" -> R.drawable.food
                else -> R.drawable.equipment
            }
            val imagePainter = painterResource(id = imageId)
            Image(
                painter = imagePainter,
                contentDescription = "Product Type",
                modifier = Modifier.size(75.dp).padding(10.dp),
                contentScale = ContentScale.Crop,

            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSurface
                )
                product.expiryDate?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface
                    )
                }
                Text(
                    text = product.price.toString(),
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface
                )
            }
        }
    }
}

@Composable
fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .width(50.dp)
                .height(50.dp),
            color = Color.Gray
        )
    }
}

@Composable
fun ErrorView(exception: Exception) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Error: ${exception.message}")
    }
}

@Composable
fun EmptyView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "No product available")
    }
}

@Preview
@Composable
fun PreviewProductItem() {
    ProductItem(
        Product(
            name = "Sample Product",
            price = 10.99,
            type = "Food",
            expiryDate = "2024-12-31" // Optional expiry date
        )
    )
}
