package com.manjeet.ubuyapplication.ui.newscreen.UI

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

// ─── DATA MODELS ───
data class WarrantyPriceTier(
    val id: String,
    val title: String,
    val saveBadge: String?,
    val price: String
)

data class SelectedWarrantyPlan(
    val planName: String,
    val tierTitle: String,
    val price: String,
    val bannerColor: Color,
    val logoResId: Int
)

data class WarrantyPageData(
    val planName: String,
    val bannerColor: Color,
    val logoResId: Int,
    val benefits: List<Pair<String, Boolean>>,
    val priceTiers: List<WarrantyPriceTier>
)


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
// AFTER:
fun UCareWarrantyBottomSheetContent(
    initialPage: Int = 0,
    previouslySelectedPlan: SelectedWarrantyPlan? = null,
    onCloseClick: () -> Unit,
    onPlanSelected: (SelectedWarrantyPlan) -> Unit
) {
    val basicIcon = com.manjeet.ubuyapplication.R.drawable.ic_warranty_g
    val plusIcon = com.manjeet.ubuyapplication.R.drawable.ic_warranty_s
    val platinumIcon = com.manjeet.ubuyapplication.R.drawable.ic_warranty_si

    val warrantyPages = remember {
        listOf(
            WarrantyPageData(
                planName = "Basic",
                bannerColor = Color(0xFF0F7643),
                logoResId = basicIcon,
                benefits = listOf(
                    "Extended warranty up to 3 years" to true,
                    "Free repair under Extended warranty. T&C apply" to true,
                    "International warranty coverage (1 Year)" to false,
                    "Accidental damage warranty" to false,
                    "25% repair charges (per incident) under accidental damage warranty. T&C apply" to false,
                    "Spill damage" to false,
                    "Fire protection" to false
                ),
                priceTiers = listOf(
                    WarrantyPriceTier("basic_1yr", "1 Year Basic Plan", null, "INR 4682.60"),
                    WarrantyPriceTier("basic_2yr", "2 Years Basic Plan", "Save 34%", "INR 6123.40"),
                    WarrantyPriceTier("basic_3yr", "3 Years Basic Plan", "Save 38%", "INR 8644.80")
                )
            ),
            WarrantyPageData(
                planName = "Plus",
                bannerColor = Color(0xFFB45309),
                logoResId = plusIcon,
                benefits = listOf(
                    "Extended warranty up to 3 years" to true,
                    "Free repair under Extended warranty. T&C apply" to true,
                    "International warranty coverage (1 Year)" to true,
                    "Accidental damage warranty" to true,
                    "25% repair charges (per incident) under accidental damage warranty. T&C apply" to true,
                    "Spill damage" to false,
                    "Fire protection" to false
                ),
                priceTiers = listOf(
                    WarrantyPriceTier("plus_1yr", "1 Year Plus Plan", null, "INR 6483.60"),
                    WarrantyPriceTier("plus_2yr", "2 Years Plus Plan", "Save 38%", "INR 7924.40"),
                    WarrantyPriceTier("plus_3yr", "3 Years Plus Plan", "Save 50%", "INR 9725.40")
                )
            ),
            WarrantyPageData(
                planName = "Platinum",
                bannerColor = Color(0xFF4B5563),
                logoResId = platinumIcon,
                benefits = listOf(
                    "Extended warranty up to 3 years" to true,
                    "Free repair under Extended warranty. T&C apply" to true,
                    "International warranty coverage (1 Year)" to true,
                    "Accidental damage warranty" to true,
                    "25% repair charges (per incident) under accidental damage warranty. T&C apply" to true,
                    "Spill damage" to true,
                    "Fire protection" to true
                ),
                priceTiers = listOf(
                    WarrantyPriceTier("plat_1yr", "1 Year Platinum Plan", null, "INR 8999.00"),
                    WarrantyPriceTier(
                        "plat_2yr",
                        "2 Years Platinum Plan",
                        "Save 42%",
                        "INR 11200.00"
                    ),
                    WarrantyPriceTier(
                        "plat_3yr",
                        "3 Years Platinum Plan",
                        "Save 55%",
                        "INR 14500.00"
                    )
                )
            )
        )
    }

    val pagerState =
        rememberPagerState(initialPage = initialPage, pageCount = { warrantyPages.size })
    var selectedPlanId by remember { mutableStateOf<String?>(previouslySelectedPlan?.let { plan ->
        warrantyPages.flatMap { it.priceTiers }.find { it.title == plan.tierTitle }?.id
    }) }
    var selectedPlanTitle by remember { mutableStateOf<String?>(previouslySelectedPlan?.let {
        "${it.planName} Plan - ${it.tierTitle}"
    }) }
    var termsChecked by remember { mutableStateOf(previouslySelectedPlan != null) }

    // Pre-jump to previously selected plan's page
    LaunchedEffect(Unit) {
        if (previouslySelectedPlan != null) {
            val pageIndex = warrantyPages.indexOfFirst { it.planName == previouslySelectedPlan.planName }
            if (pageIndex >= 0) pagerState.scrollToPage(pageIndex)
        }
    }

    // ─── LOCK: Consumes ALL vertical drag gestures so ModalBottomSheet never sees them ───
    val lockSheetModifier = Modifier.pointerInput(Unit) {
        detectVerticalDragGestures { _, _ -> }
    }

    // ─── LOCK: Consumes all fling velocity so sheet never bounces ───
    val consumeAllScrollConnection = remember {
        object : NestedScrollConnection {
            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                return available
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.95f)
            .background(Color(0xFFF3F4F6))
            .then(lockSheetModifier) // ← locks entire sheet from any vertical drag
            .nestedScroll(consumeAllScrollConnection)
    ) {
        // CLOSE BUTTON ROW (Fixed Top)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.Black
                )
            }
        }

        // SWIPEABLE HORIZONTAL PAGER CONTAINER
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(horizontal = 24.dp),
            pageSpacing = 12.dp
        ) { pageIndex ->
            val pageData = warrantyPages[pageIndex]

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(consumeAllScrollConnection)
                    .verticalScroll(
                        state = rememberScrollState(),
                        reverseScrolling = false
                    )
                    .padding(bottom = 16.dp)
            ) {

                // 🌟 MASTER CARD WRAPPER
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {

                        // A. TOP GREEN/COLOR BANNER
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = pageData.bannerColor,
                                    shape = RoundedCornerShape(
                                        topStart = 24.dp,
                                        topEnd = 24.dp,
                                        bottomStart = 0.dp,
                                        bottomEnd = 0.dp
                                    )
                                )
                                .padding(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier.size(40.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = pageData.logoResId),
                                        contentDescription = "${pageData.planName} Brand Image",
                                        modifier = Modifier.size(39.dp),
                                        contentScale = ContentScale.Fit
                                    )
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text(
                                        text = pageData.planName,
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        "UCare warranty plan",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }

                        // B. BENEFIT ROWS MATRIX
                        pageData.benefits.forEachIndexed { index, (text, isAvailable) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(if (index % 2 == 0) Color(0xFFF9FAFB) else Color.White)
                                    .padding(vertical = 12.dp, horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = text,
                                    fontSize = 13.sp,
                                    color = Color(0xFF374151),
                                    modifier = Modifier.weight(0.85f),
                                    lineHeight = 18.sp
                                )
                                Icon(
                                    imageVector = if (isAvailable) Icons.Default.CheckCircle else Icons.Default.Cancel,
                                    contentDescription = null,
                                    tint = if (isAvailable) Color(0xFF10B981) else Color(0xFFEF4444),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFE5E7EB))
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // C. TERMS AND CONDITIONS FIELD
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = termsChecked,
                                onCheckedChange = { termsChecked = it },
                                colors = CheckboxDefaults.colors(checkedColor = pageData.bannerColor)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = buildAnnotatedString {
                                    append("I read and agree to the ")
                                    withStyle(
                                        style = SpanStyle(
                                            color = Color(0xFFB87333),
                                            fontWeight = FontWeight.Bold,
                                            textDecoration = TextDecoration.Underline
                                        )
                                    ) {
                                        append("Terms & Conditions.")
                                    }
                                },
                                fontSize = 13.sp,
                                color = Color.Black
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // D. INTERACTIVE PRICING TIERS LIST
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            pageData.priceTiers.forEach { tier ->
                                val isSelected = selectedPlanId == tier.id

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp)
                                        .clickable {
                                            selectedPlanId = tier.id
                                            selectedPlanTitle =
                                                "${pageData.planName} Plan - ${tier.title}"
                                        },
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) pageData.bannerColor else Color(
                                            0xFFE5E7EB
                                        )
                                    ),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) pageData.bannerColor.copy(
                                            alpha = 0.04f
                                        ) else Color.White
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(14.dp)
                                    ) {
                                        if (tier.saveBadge != null) {
                                            Surface(
                                                color = Color(0xFFE6F4EA),
                                                shape = RoundedCornerShape(6.dp),
                                                modifier = Modifier.padding(bottom = 8.dp)
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(
                                                        horizontal = 6.dp,
                                                        vertical = 2.dp
                                                    ),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Percent,
                                                        contentDescription = null,
                                                        tint = Color(0xFF137333),
                                                        modifier = Modifier.size(10.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(2.dp))
                                                    Text(
                                                        text = tier.saveBadge,
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(0xFF137333)
                                                    )
                                                }
                                            }
                                        }

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = tier.title,
                                                fontSize = 14.sp,
                                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                                                color = Color.Black,
                                                modifier = Modifier.weight(1f)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = tier.price,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Black,
                                                color = if (isSelected) pageData.bannerColor else Color.Black
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }
                } // 🌟 MASTER CARD CLOSES HERE PERFECTLY

                // E. DOT INDICATORS OUTSIDE CARD
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(warrantyPages.size) { index ->
                        val isCurrentPage = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 3.dp)
                                .size(width = if (isCurrentPage) 16.dp else 6.dp, height = 4.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isCurrentPage) Color(0xFF374151) else Color(
                                        0xFFD1D5DB
                                    )
                                )
                        )
                    }
                }
            }
        }

        // ─── 3. FIXED BOTTOM PERSISTENT ACTION BUTTON ───
        val isButtonEnabled = termsChecked && selectedPlanId != null

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White
        ) {
            Button(
                onClick = {
                    if (isButtonEnabled) {
                        val currentPage = warrantyPages[pagerState.currentPage]
                        val selectedTier = currentPage.priceTiers.first { it.id == selectedPlanId }
                        onPlanSelected(
                            SelectedWarrantyPlan(
                                planName = currentPage.planName,
                                tierTitle = selectedTier.title,
                                price = selectedTier.price,
                                bannerColor = currentPage.bannerColor,
                                logoResId = currentPage.logoResId
                            )
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .height(50.dp),
                enabled = isButtonEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0F7643),
                    disabledContainerColor = Color(0xFF90A99A),
                    contentColor = Color.White,
                    disabledContentColor = Color.White.copy(alpha = 0.8f)
                ),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text(
                    text = "Select plan",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


// ─── 4. THREE PARALLEL SLIDE STUDIO PREVIEWS ───

@Preview(showBackground = true, name = "Slide 1: Basic Plan (Green)", widthDp = 360, heightDp = 760)
@Composable
fun PreviewBasicSlideMatches() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF3F4F6)) {
            Box(contentAlignment = Alignment.BottomCenter) {
                UCareWarrantyBottomSheetContent(initialPage = 0, onCloseClick = {}, onPlanSelected = {})
            }
        }
    }
}

@Preview(showBackground = true, name = "Slide 2: Plus Plan (Saffron)", widthDp = 360, heightDp = 760)
@Composable
fun PreviewPlusSlideMatches() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF3F4F6)) {
            Box(contentAlignment = Alignment.BottomCenter) {
                UCareWarrantyBottomSheetContent(initialPage = 1, onCloseClick = {}, onPlanSelected = {})
            }
        }
    }
}

@Preview(showBackground = true, name = "Slide 3: Platinum Plan (Grey)", widthDp = 360, heightDp = 760)
@Composable
fun PreviewPlatinumSlideMatches() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF3F4F6)) {
            Box(contentAlignment = Alignment.BottomCenter) {
                UCareWarrantyBottomSheetContent(initialPage = 2, onCloseClick = {}, onPlanSelected = {})
            }
        }
    }
}