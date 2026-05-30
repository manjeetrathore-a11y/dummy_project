package com.manjeet.ubuyapplication.ui.newscreen.UI

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.manjeet.ubuyapplication.R
import com.manjeet.ubuyapplication.model.UbuyMembershipData
import com.manjeet.ubuyapplication.viewmodel.MembershipViewModel

//  Enum to handle 3 distinct screens visual layouts flawlessly
enum class MembershipStatus {
    UNSUBSCRIBED,
    TRIAL_EXPIRED, // For 7-day free trial click layout
    ANNUAL_ACTIVE  // For annual subscription click layout
}

@Composable
fun UbuyMembershipScreen(
    onBack: () -> Unit,
    viewModel: MembershipViewModel = viewModel()
) {

    val ubuyMembershipData by viewModel.membershipData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    UbuyMembershipScreenContent(
        ubuyMembershipData = ubuyMembershipData,
        isLoading = isLoading,
        initialStatus = MembershipStatus.UNSUBSCRIBED,
        onBack = onBack
    )
}

//  PREVIEW
@Composable
private fun UbuyMembershipScreenContent(
    ubuyMembershipData: UbuyMembershipData?,
    isLoading: Boolean,
    initialStatus: MembershipStatus,
    onBack: () -> Unit
) {
    var currentStatus by remember { mutableStateOf(initialStatus) }
    var selectedPlanIndex by remember { mutableStateOf(0) } // 0 for Trial, 1 for Annual
    var isTermsAccepted by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    var showWebView by remember { mutableStateOf(false) }
    var showCashbackHistory by remember { mutableStateOf(false) }


    LaunchedEffect(currentStatus) {
        scrollState.scrollTo(0)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFF1F5))
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color(0xFF10B981)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {

                // ==========================================
                // DYNAMIC STATE HEADER CONTAINER BLOCK
                // ==========================================
                when (currentStatus) {
                    MembershipStatus.TRIAL_EXPIRED -> {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Trial Membership valid till 6 June '26",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF1F2937)
                                    )
                                    Box(
                                        modifier = Modifier.background(
                                            Color(0xFFFEF3C7),
                                            RoundedCornerShape(12.dp)
                                        ).padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "Expired",
                                            color = Color(0xFFD97706),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                                    border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                                ) {
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(Color(0xFFF9FAFB))
                                                .padding(16.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(modifier = Modifier.size(40.dp), contentAlignment = Alignment.Center) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.ic_plus_grey),
                                                    contentDescription = "Membership Plus Icon",
                                                    tint = Color.Unspecified,
                                                    modifier = Modifier.size(46.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(14.dp))
                                            Column {
                                                Text(text = "Trial Membership Plan", color = Color(0xFF1F2937), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                                Text(text = "Plan Price: £139.99", color = Color(0xFF6B7280), fontSize = 12.sp)
                                            }
                                        }

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(Color(0xFFEAEAEA))
                                                .padding(16.dp),
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = "Available Cashback", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color(0xFF0A0A0A))
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(text = "£0.00", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF10B981))
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = {
                                        currentStatus = MembershipStatus.UNSUBSCRIBED
                                        isTermsAccepted = false
                                    },
                                    modifier = Modifier.fillMaxWidth().height(44.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF145334)),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Text("Renew plan", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                OutlinedButton(
                                    onClick = { showCashbackHistory = true },
                                    modifier = Modifier.fillMaxWidth().height(46.dp),
                                    border = BorderStroke(1.dp, Color(0xFF78350F)),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF78350F)),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Text(text = "⏳  View cashback history", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    MembershipStatus.ANNUAL_ACTIVE -> {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Annual subscription valid till 30 May '27",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF1F2937)
                                    )
                                    Box(
                                        modifier = Modifier.background(
                                            Color(0xFFDCFCE7),
                                            RoundedCornerShape(12.dp)
                                        ).padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "Active",
                                            color = Color(0xFF16A34A),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                                    border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                                ) {
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(Color(0xFF1D63ED))
                                                .padding(16.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(modifier = Modifier.size(40.dp), contentAlignment = Alignment.Center) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.ic_plus_orange),
                                                    contentDescription = "Membership Plus Icon",
                                                    tint = Color.Unspecified,
                                                    modifier = Modifier.size(48.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(14.dp))
                                            Column {
                                                Text(text = "1 Year Membership Plan", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                                Text(text = "Plan Price: £1364.59", color = Color(0xFFBFDBFE), fontSize = 12.sp)
                                            }
                                        }

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(Color(0xFFF3F4F6))
                                                .padding(16.dp),
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = "Available Cashback", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0A0A0A))
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(text = "£63.00", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF16A34A))
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                OutlinedButton(
                                    onClick = { showCashbackHistory = true },
                                    modifier = Modifier.fillMaxWidth().height(48.dp),
                                    border = BorderStroke(1.dp, Color(0xFF78350F)),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF78350F)),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Text(text = "⏳  View cashback history", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                    else -> { /* No card on top when unsubscribed */ }
                }
                // ==========================================
                //  STATIC GLOBAL BENEFITS CARD
                // ==========================================
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(Color(0xFFFFFAF1), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_plus_orange),
                                contentDescription = "Ubuy Plus",
                                tint = Color(0xFFD97706),
                                modifier = Modifier.size(48.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Get exclusive benefits with a\nUbuy Plus+ membership",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF78350F),
                            textAlign = TextAlign.Center,
                            lineHeight = 24.sp
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        BenefitRow(
                            iconRes = R.drawable.ic_cashback,
                            title = "Save more",
                            subtitle = "Get 10% cashback on every order"
                        )
                        BenefitRow(
                            iconRes = R.drawable.ic_delivery,
                            title = "Fast shipping",
                            subtitle = "Get express shipping at the rate of standard shipping"
                        )
                        BenefitRow(
                            iconRes = R.drawable.ic_warranty,
                            title = "U-care coverage",
                            subtitle = "Get comprehensive coverage with a 1-year warranty, including free repairs"
                        )
                        BenefitRow(
                            iconRes = R.drawable.ic_customer_service,
                            title = "Resolve issues quickly",
                            subtitle = "Get access to priority customer service"
                        )
                        BenefitRow(
                            iconRes = R.drawable.ic_discount,
                            title = "Exclusive discounts",
                            subtitle = "Enjoy special promotions and offers for members only"
                        )
                    }
                }

                // ==========================================
                //  DYNAMIC BOTTOM ACTIONS LAYOUT
                // ==========================================
                if (currentStatus == MembershipStatus.UNSUBSCRIBED) {
                    Spacer(modifier = Modifier.height(20.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                        border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                            Text(
                                text = "Ubuy Plus+ plans",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1F2937),
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            // PLAN CARD 1: 7-DAY FREE TRIAL
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp)
                                    .clickable { selectedPlanIndex = 0 },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
                                border = BorderStroke(
                                    width = if (selectedPlanIndex == 0) 2.dp else 1.dp,
                                    color = if (selectedPlanIndex == 0) Color(0xFFFBBF24) else Color(0xFFE5E7EB)
                                )
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column {
                                        Text("7-day free trial", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text("Try Ubuy Plus+ free", fontSize = 13.sp, color = Color(0xFF6B7280))
                                    }
                                    Text("Free", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937), modifier = Modifier.padding(top = 1.dp))
                                }
                            }

                            // PLAN CARD 2: ANNUAL SUBSCRIPTION
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                                    .clickable { selectedPlanIndex = 1 },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
                                border = BorderStroke(
                                    width = if (selectedPlanIndex == 1) 2.dp else 1.dp,
                                    color = if (selectedPlanIndex == 1) Color(0xFFFBBF24) else Color(0xFFE5E7EB)
                                )
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column {
                                        Text(text = "Annual subscription", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(text = "Billed yearly to ", fontSize = 13.sp, color = Color(0xFF6B7280))
                                            Text(text = "save ", fontSize = 13.sp, color = Color(0xFF10B981), fontWeight = FontWeight.Medium)
                                            Text(text = "£15.89", fontSize = 13.sp, color = Color(0xFF10B981), fontWeight = FontWeight.Medium)
                                        }
                                    }
                                    Text(text = "£139.99", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937), modifier = Modifier.padding(top = 1.dp))
                                }
                            }

                            // TERMS CHECKBOX
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isTermsAccepted,
                                    onCheckedChange = { isTermsAccepted = it },
                                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF145334))
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Row {
                                    Text("I have read and agree to the ", fontSize = 12.sp, color = Color(0xFF374151))
                                    Text(
                                        text = "Terms & Conditions",
                                        fontSize = 12.sp,
                                        color = Color(0xFF78350F),
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.clickable { showWebView = true }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // MAIN ACTION BUTTON
                            Button(
                                onClick = {
                                    if (isTermsAccepted) {
                                        currentStatus = if (selectedPlanIndex == 0) MembershipStatus.TRIAL_EXPIRED else MembershipStatus.ANNUAL_ACTIVE
                                    }
                                },
                                enabled = isTermsAccepted,
                                modifier = Modifier.fillMaxWidth().height(52.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF145334),
                                    disabledContainerColor = Color(0xFF9CA3AF)
                                ),
                                shape = RoundedCornerShape(26.dp)
                            ) {
                                Text(
                                    text = if (selectedPlanIndex == 0) "Start free trial" else "Subscribe Now",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // BULLET NOTES INFO
                            Text(
                                text = "• You will be charged a once-off refundable fee of USD \$1 for payment verification.",
                                fontSize = 12.sp,
                                color = Color(0xFF6B7280),
                                lineHeight = 16.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Text(
                                text = "• After 7 days, you will be charged £139.99 automatically unless you cancel the trial.",
                                fontSize = 12.sp,
                                color = Color(0xFF6B7280),
                                lineHeight = 16.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // WEB LAYOUT SCREEN
        if (showWebView) {
            Column(
                modifier = Modifier.fillMaxSize().background(Color.White)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().background(Color(0xFFF3F4F6)).padding(horizontal = 12.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { showWebView = false }) {
                        Icon(painter = painterResource(id = R.drawable.ic_back_arrow), contentDescription = "Back to plans", modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Terms & Conditions", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1F2937))
                }

                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                            webViewClient = WebViewClient()
                            settings.apply {
                                javaScriptEnabled = true
                                domStorageEnabled = true
                                userAgentString = "Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"
                            }
                            loadUrl("https://www.ubuy.qa/en/removeheaderfooter/index/static?page=terms&newdesign=ubdesign-new")
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // =========================================================================
        //  DYNAMIC OVERLAY FOR CASHBACK HISTORY (FIXED: Root Box Placement)
        // =========================================================================

        if (showCashbackHistory) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFEFF1F5))
            ) {
                CashbackHistoryScreen(
                    cashbackSection = ubuyMembershipData?.data?.cashbackSection,
                    savingsSection = ubuyMembershipData?.data?.savingsSection,
                    onBackClick = { showCashbackHistory = false }
                )
            }
        }
    }
}

@Composable
fun BenefitRow(
    iconRes: Int,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(painter = painterResource(id = iconRes), contentDescription = title, tint = Color.Unspecified, modifier = Modifier.size(48.dp))
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF6B7280))
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitle, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF374151), lineHeight = 17.sp)
        }
    }
}

// ======================================================
//  PREVIEWS
// ======================================================

@Preview(showBackground = true, name = "1. Unsubscribed State (Default)")
@Composable
fun UbuyMembershipUnsubscribedPreview() {
    UbuyMembershipScreenContent(ubuyMembershipData = null, isLoading = false, initialStatus = MembershipStatus.UNSUBSCRIBED, onBack = {})
}

@Preview(showBackground = true, name = "2. Trial Expired State")
@Composable
fun UbuyMembershipTrialExpiredPreview() {
    UbuyMembershipScreenContent(ubuyMembershipData = null, isLoading = false, initialStatus = MembershipStatus.TRIAL_EXPIRED, onBack = {})
}

@Preview(showBackground = true, name = "3. Annual Active State")
@Composable
fun UbuyMembershipAnnualActivePreview() {
    UbuyMembershipScreenContent(ubuyMembershipData = null, isLoading = false, initialStatus = MembershipStatus.ANNUAL_ACTIVE, onBack = {})
}