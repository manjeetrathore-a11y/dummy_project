package com.manjeet.ubuyapplication.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.manjeet.ubuyapplication.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MembershipViewModel(application: Application) : AndroidViewModel(application) {

    private val _membershipData = MutableStateFlow<UbuyMembershipData?>(null)
    val membershipData: StateFlow<UbuyMembershipData?> = _membershipData

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadMembershipDataFromAssets()
    }

    private fun loadMembershipDataFromAssets() {
        viewModelScope.launch {
            _isLoading.value = true

            val parsedData = withContext(Dispatchers.IO) {
                try {
                    //  1. Assets se strict file stream read
                    val jsonString = getApplication<Application>().assets
                        .open("ubuy_membership_data.json")
                        .bufferedReader()
                        .use { it.readText() }

                    Log.d("MANJEET_REAL_DATA", "JSON Content Fetched: $jsonString")

                    //  2. Parsing into your exact 10-on-10 model class
                    val result = Gson().fromJson(jsonString, UbuyMembershipData::class.java)

                    //  3. Validation line check for real entries
                    if (result?.data?.cashbackSection?.tableData?.values != null &&
                        result.data?.cashbackSection?.tableData?.values!!.isNotEmpty()) {
                        Log.d("MANJEET_REAL_DATA", "Success! Real Data Parsed Flawlessly.")
                        result
                    } else {
                        Log.e("MANJEET_REAL_DATA", "File read successfully but values inside JSON are empty/null!")
                        createMockData()
                    }
                } catch (e: Exception) {
                    Log.e("MANJEET_REAL_DATA", "Assets system crashed or name mismatch!", e)
                    createMockData()
                }
            }

            _membershipData.value = parsedData
            _isLoading.value = false
        }
    }

    // Safe Fallback Mock Data
    private fun createMockData(): UbuyMembershipData {
        val dummyTxnList = arrayListOf(
            TxnValue("Order Cashback", "#UB-98231", "Credited", "£12.50", "28 May 2026, 02:30 PM"),
            TxnValue("Order Cashback", "#UB-76432", "Credited", "£25.00", "20 May 2026, 11:15 AM"),
            TxnValue("Welcome Bonus", "System", "Credited", "£25.50", "15 May 2026, 09:00 AM")
        )

        return UbuyMembershipData(
            error = false,
            code = "200",
            msg = "Success",
            data = Data(
                cashbackSection = CashbackSection(
                    headings = "Cashback Details",
                    tableData = TableData(
                        heading = Heading("Type", "Order ID", "Status", "Amount", "Date"),
                        values = dummyTxnList
                    )
                ),
                savingsSection = SavingsSection(
                    totalSavings = TotalSavings(
                        heading = "Total Savings",
                        subHeading = "Your overall savings",
                        currency = "£",
                        amount = "63.00"
                    )
                )
            )
        )
    }
}