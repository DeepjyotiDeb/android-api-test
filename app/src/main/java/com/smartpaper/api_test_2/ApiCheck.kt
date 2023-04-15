package com.smartpaper.api_test_2

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/* create a button using jetpack compose */
interface MyApi {
    @GET("products/1")
    suspend fun getProduct(): Product

//    companion object {
//        var BASE_URL = "https://dummyjson.com/"
//        fun create(): MyApi {
//            val retrofit =
//                Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(
//                    BASE_URL
//                )
//
//            return retrofit.build().create(MyApi::class.java)
//        }
//    }
}

data class Product(val id: Int, val title: String, val price: Int, val rating: Float, val description: String)

@Composable
fun MyComposable(api: MyApi, modifier: Modifier = Modifier) {
    val (product, setProduct) = remember { mutableStateOf<Product?>(null) }
    val errorMessage = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = modifier) {
        if (product == null) {
            Text(text = "Nothing found")
        } else {
            Text(text = "Title: ${product.title}")
            Text(text = "Price ${product.price}")
            Text(text = "Rating ${product.rating}")
            Text(text = "Description ${product.description}")
        }

        if (errorMessage.value.isNotEmpty()) {
            Text(text = errorMessage.value)
        }

        Button(onClick = {
            coroutineScope.launch {
                try{
                    val prodRes = api.getProduct()
                    setProduct(prodRes)
                } catch (e: Exception){
                    errorMessage.value = "Failed to load data: ${e.message}"
                }
            }
        }) {
            Text(text = "Get product")
        }
    }
}