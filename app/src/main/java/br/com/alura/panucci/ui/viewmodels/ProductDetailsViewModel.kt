package br.com.alura.panucci.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.alura.panucci.dao.ProductDao
import br.com.alura.panucci.ui.uistate.ProductDetailsUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import kotlin.random.Random

class ProductDetailsViewModel(
    private val dao: ProductDao = ProductDao()
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductDetailsUiState>(ProductDetailsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun findProductById(id: String, discounted: Boolean = false) {
        val discount = when (discounted) {
            true -> BigDecimal("0.1")
            else -> BigDecimal.ZERO
        }

        viewModelScope.launch {
            val timeMillis = Random.nextLong(1000, 3000)
            delay(timeMillis)
            val dataState = dao.findById(id)?.let { product ->
                val currentPrice = product.price

                ProductDetailsUiState.Success(product = product.copy(price = currentPrice - (currentPrice * discount)))

            } ?: ProductDetailsUiState.Failure

            _uiState.update { dataState }
        }
    }

}