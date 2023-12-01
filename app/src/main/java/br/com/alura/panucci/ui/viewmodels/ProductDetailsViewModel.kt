package br.com.alura.panucci.ui.viewmodels

import androidx.lifecycle.ViewModel
import br.com.alura.panucci.dao.ProductDao
import br.com.alura.panucci.ui.uistate.ProductDetailsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.math.BigDecimal

class ProductDetailsViewModel(
    private val dao: ProductDao = ProductDao()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailsUiState())
    val uiState = _uiState.asStateFlow()

    fun findProductById(id: String,discounted:Boolean = false) {
        val discount = when (discounted) {
            true -> BigDecimal("0.1")
            else -> BigDecimal.ZERO
        }

        dao.findById(id)?.let { product ->
            val currentPrice = product.price


            _uiState.update {
                it.copy(product = product.copy(price = currentPrice - (currentPrice * discount)))
            }
        }
    }

}