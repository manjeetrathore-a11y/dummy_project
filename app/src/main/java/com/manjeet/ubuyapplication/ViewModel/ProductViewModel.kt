package com.manjeet.ubuyapplication.ViewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.manjeet.ubuyapplication.model.ProductSearchResponse
import com.manjeet.ubuyapplication.model.Products
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductViewModel : ViewModel() {

    private var allProducts: List<Products> = emptyList()

    var productsList by mutableStateOf<List<Products>>(emptyList())
        private set

    var searchQuery by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    var selectedProduct by mutableStateOf<Products?>(null)
        private set

    // ── 🌟 Jo data model me hai, bss unhi ke dynamic filters ──
    var availableBrands by mutableStateOf<List<String>>(emptyList())
        private set
    var availableCountries by mutableStateOf<List<String>>(emptyList())
        private set

    // Active selected filters tracking states
    var selectedBrand by mutableStateOf<String?>(null)
        private set
    var selectedCountry by mutableStateOf<String?>(null)

    fun loadProductsFromAssets(context: Context, fileName: String = "search_products.json") {
        isLoading = true
        viewModelScope.launch {
            try {
                // 1. Check karein ki file read hui ya nahi
                val jsonString = withContext(Dispatchers.IO) {
                    context.assets.open(fileName).bufferedReader().use { it.readText() }
                }

                Log.d("MANJEET_DEBUG", "1. FILE READ SUCCESS! Total Characters: ${jsonString.length}")
                Log.d("MANJEET_DEBUG", "2. JSON PREVIEW: ${jsonString.take(200)}")

                // 2. GSON Parsing Test
                val response = Gson().fromJson(jsonString, ProductSearchResponse::class.java)
                Log.d("MANJEET_DEBUG", "3. GSON PARSED OBJECT: $response")

                val parsedProducts = response.data?.products
                Log.d("MANJEET_DEBUG", "4. PRODUCTS LIST SIZE: ${parsedProducts?.size ?: "NULL"}")

                if (!parsedProducts.isNullOrEmpty()) {
                    allProducts = parsedProducts

                    // Filters extract karein jo data present h usse
                    extractFiltersFromData(parsedProducts)

                    // Final list apply karein
                    applyCombinedFiltersAndSearch()

                    Log.d("MANJEET_DEBUG", "5. SUCCESS: Data loaded into productsList!")
                } else {
                    Log.e("MANJEET_DEBUG", "5. ERROR: List is empty or keys mismatched inside ProductSearchResponse!")
                }

            } catch (fileNotFound: java.io.FileNotFoundException) {
                Log.e("MANJEET_DEBUG", "FATAL ERROR: File '$fileName' assets folder mein nahi mili! Name check karo.")
            } catch (gsonError: com.google.gson.JsonSyntaxException) {
                Log.e("MANJEET_DEBUG", "FATAL ERROR: JSON ka format kharab hai ya Data Type mismatch hai! -> ${gsonError.message}")
            } catch (e: Exception) {
                Log.e("MANJEET_DEBUG", "FATAL ERROR: Kuch aur locha hai -> ${e.message}")
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    // ── 🌟 FIXED: Explicit types lagaye hain taaki Compiler parameter infer error na de ──
    private fun extractFiltersFromData(products: List<Products>) {
        availableBrands = products.mapNotNull { p: Products -> p.brand }
            .filter { s: String -> s.isNotBlank() }
            .distinct()
            .sorted()

        availableCountries = products.mapNotNull { p: Products -> p.country }
            .filter { s: String -> s.isNotBlank() }
            .distinct()
            .sorted()

        Log.d("MANJEET_DEBUG", "EXTRACTED: Brands(${availableBrands.size}), Countries(${availableCountries.size})")
    }

    // ── 🌟 Master function jo bss available parameters ko hi process karega ──
    private fun applyCombinedFiltersAndSearch() {
        var result = allProducts

        // 1. Search Query logic (Aapka original)
        if (searchQuery.isNotBlank()) {
            result = result.filter { product: Products ->
                (product.name?.contains(searchQuery, ignoreCase = true) == true) ||
                        (product.brand?.contains(searchQuery, ignoreCase = true) == true) ||
                        (product.store?.contains(searchQuery, ignoreCase = true) == true)
            }
        }

        // 2. Filter by selected Brand
        selectedBrand?.let { brand: String ->
            result = result.filter { product: Products -> product.brand?.equals(brand, ignoreCase = true) == true }
        }

        // 3. Filter by selected Country
        selectedCountry?.let { country: String ->
            result = result.filter { product: Products -> product.country?.equals(country, ignoreCase = true) == true }
        }

        // Output update kiya
        productsList = result
    }

    // UI Click Functions
    fun selectBrandFilter(brand: String?) {
        selectedBrand = brand
        applyCombinedFiltersAndSearch()
    }

    fun selectCountryFilter(country: String?) {
        selectedCountry = country
        applyCombinedFiltersAndSearch()
    }

    fun clearAllFilters() {
        selectedBrand = null
        selectedCountry = null
        applyCombinedFiltersAndSearch()
    }

    fun onSearchQueryChanged(newQuery: String) {
        searchQuery = newQuery
        applyCombinedFiltersAndSearch()
    }

    fun clearSearch() {
        searchQuery = ""
        applyCombinedFiltersAndSearch()
    }

    fun selectProduct(product: Products) {
        selectedProduct = product
    }
}