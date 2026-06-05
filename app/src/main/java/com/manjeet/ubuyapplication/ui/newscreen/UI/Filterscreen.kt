package com.manjeet.ubuyapplication.ui.newscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterAltOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manjeet.ubuyapplication.model.BrandItem
import com.manjeet.ubuyapplication.model.DeviceModel
import com.manjeet.ubuyapplication.model.brandList
import com.manjeet.ubuyapplication.model.colorOptions
import com.manjeet.ubuyapplication.model.modelList


@Composable
fun FilterSheetContent(onApply: () -> Unit, onClear: () -> Unit) {
    var sliderValue by remember { mutableStateOf(15f..85f) }
    var selectedCategory by remember { mutableStateOf("Price range") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
//            .fillMaxHeight()
            .imePadding()
            // --- THE UNIVERSAL LOCK ---
            // This intercepts all touch events. It prevents vertical drags
            // from reaching the BottomSheet while allowing child elements
            // like Sliders and Lists to still function.
            .pointerInput(Unit) {
                detectVerticalDragGestures { change, _ ->
                    // By just having this here and consuming the change,
                    // we "steal" the drag from the BottomSheet smoothly.
                    change.consume()
                }

            }
    ){
        // --- 1. TOP HEADER ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Filters", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = onApply) {
                Icon(Icons.Default.Close, contentDescription = null)
            }
        }

        // --- 2. CATEGORY CHIPS ---
        FilterCategoryRow(
            selectedCategory = selectedCategory,
            onCategorySelect = { selectedCategory = it }
        )

        HorizontalDivider(color = Color(0xFFF2F3F5), thickness = 1.dp)

        // --- 3. DYNAMIC CONTENT AREA (STABILIZED) ---

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {

            when (selectedCategory) {
                "Price range" -> PriceRangeSection()
                "Model" -> ModelSection()
                "Brand" -> BrandSection()
                "Color" -> ColorSection()
                else -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Coming soon",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        HorizontalDivider(color = Color(0xFFF2F3F5), thickness = 1.dp)

        // --- 4. BOTTOM ACTION BUTTONS (STICKY) ---
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onClear,
                    modifier = Modifier.weight(1f).height(54.dp),
                    shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(1.dp, Color(0xFFB87333))
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterAltOff,
                        contentDescription = null,
                        tint = Color(0xFFB87333),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Clear all filters",
                        color = Color(0xFFB87333),
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = onApply,
                    modifier = Modifier.weight(1f).height(54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF135D39)),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Icon(Icons.Default.Check, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Apply filters", fontWeight = FontWeight.Bold)
                }

            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterCategoryRow(selectedCategory: String, onCategorySelect: (String) -> Unit) {
    val categories = listOf(
        "Price range", "Model", "Brand",
        "Material", "Availability & shipping", "Features", "Color", "Reviews & ratings"
    )

    // FlowRow handles the wrapping automatically
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            val isSelected = category == selectedCategory
            Surface(
                modifier = Modifier.clickable { onCategorySelect(category) },
                shape = RoundedCornerShape(14.dp),
                color = if (isSelected) Color(0xFFFFF8F2) else Color.White,
                border = BorderStroke(1.dp, if (isSelected) Color(0xFFB87333) else Color(0xFFE0E0E0))
            ) {
                Text(
                    text = category,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontSize = 14.sp,
                    color = Color.Black, // Text remains black as per screenshot
                    fontWeight = FontWeight.SemiBold // Matches the professional look
                )
            }
        }
    }
}

@Composable
fun ModelSection() {
    val selectedModels = remember { mutableStateListOf<String>() }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Header with Refresh Icon
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Model", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Reset",
                tint = Color.LightGray,
                modifier = Modifier.size(20.dp).clickable { selectedModels.clear() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // List of Models
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            modelList.forEach { model ->
                ModelRow(
                    model = model,
                    isSelected = selectedModels.contains(model.name),
                    onToggle = {
                        if (selectedModels.contains(model.name)) selectedModels.remove(model.name)
                        else selectedModels.add(model.name)
                    }
                )
            }
        }
    }
}

@Composable
fun ModelRow(model: DeviceModel, isSelected: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Brown/Orange Checkbox Border
        Box(
            modifier = Modifier
                .size(20.dp)
                .border(1.5.dp, Color(0xFF8B4513), RoundedCornerShape(4.dp))
                .background(if (isSelected) Color(0xFF8B4513) else Color.Transparent, RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
            }
        }

        Text(
            text = model.name,
            modifier = Modifier.padding(start = 12.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        // Count Badge
        Surface(
            modifier = Modifier.padding(start = 8.dp),
            color = Color(0xFFF2F4F7),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = model.count.toString(),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Brand Logo
        Image(
            painter = painterResource(id = model.brandLogo),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
    }
}


@Composable
fun BrandSection() {
    val selectedBrands = remember { mutableStateListOf<String>() }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Brand", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Reset",
                tint = Color.LightGray,
                modifier = Modifier.size(20.dp).clickable { selectedBrands.clear() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            userScrollEnabled = true
        ) {
            items(brandList) { brand ->
                BrandRow(
                    brand = brand,
                    isChecked = selectedBrands.contains(brand.name),
                    onCheckedChange = { checked ->
                        if (checked) selectedBrands.add(brand.name)
                        else selectedBrands.remove(brand.name)
                    }
                )
            }
        }
    }
}


@Composable
fun BrandRow(brand: BrandItem, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isChecked) }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = Color(0xFFB87333))
        )

        Text(
            text = brand.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 8.dp)
        )

        Surface(
            color = Color(0xFFF2F5F8),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.padding(start = 12.dp)
        ) {
            Text(
                text = brand.productCount.toString(),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                fontSize = 12.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = brand.logoRes),
            contentDescription = null,
            modifier = Modifier.width(60.dp).height(24.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceRangeSection() {

    var sliderValue by remember { mutableStateOf(15f..85f) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Price range", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Icon(
                Icons.Default.Refresh,
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier
                    .size(20.dp)
                    .clickable { sliderValue = 0f..100f }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            PriceRangeInput(
                modifier = Modifier.weight(1f),
                value = sliderValue.start.toInt().toString()
            )

            Box(modifier = Modifier.width(32.dp), contentAlignment = Alignment.Center) {
                HorizontalDivider(
                    thickness = 2.dp,
                    color = Color(0xFFF2F3F5),
                    modifier = Modifier.width(12.dp)
                )
            }

            PriceRangeInput(
                modifier = Modifier.weight(1f),
                value = sliderValue.endInclusive.toInt().toString()
            )
        }

        Spacer(modifier = Modifier.height(40.dp))


        RangeSlider(
            value = sliderValue,
            onValueChange = { sliderValue = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                // This is the "Hard Lock"
                .pointerInput(Unit) {
                    detectVerticalDragGestures { change, _ ->

                        change.consume()
                    }
                },
            valueRange = 0f..100f,
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color(0xFF6B3E26),
                inactiveTrackColor = Color(0xFFF2F3F5)
            )
        )
    }
}

@Composable
fun PriceRangeInput(modifier: Modifier = Modifier, value: String) {
    Surface(
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF7F8FA),
        border = BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "USD",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}


@Composable
fun ColorSection() {
    val selectedColors = remember { mutableStateListOf<String>() }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Color", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            Icon(
                Icons.Default.Refresh,
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier.size(20.dp).clickable { selectedColors.clear() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            userScrollEnabled = true,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(colorOptions) { option ->
                val isSelected = selectedColors.contains(option.name)

                // START YOUR ITEM UI HERE
                Surface(
                    onClick = {
                        if (isSelected) selectedColors.remove(option.name)
                        else selectedColors.add(option.name)
                    },
                    shape = RoundedCornerShape(50.dp),
                    border = BorderStroke(
                        1.dp,
                        if (isSelected) Color(0xFFB87333) else Color(0xFFF2F3F5)
                    ),
                    color = if (isSelected) Color(0xFFFFF8F2) else Color.White
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Color Circle
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(option.color)
                                .border(1.dp, Color.LightGray, CircleShape)
                        )

                        Text(
                            text = option.name,
                            modifier = Modifier.padding(start = 12.dp),
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}


// --- PREVIEWS ---

@Preview(showBackground = true, name = "Filter Sheet - Brand View")
@Composable
fun PreviewBrandSelection() {
    androidx.compose.material3.MaterialTheme {
        Surface(color = Color.White) {
            FilterSheetContentPreviewHelper(initialCategory = "Brand")
        }
    }
}

@Preview(showBackground = true, name = "Filter Sheet - Price View")
@Composable
fun PreviewPriceRangeSelection() {
    androidx.compose.material3.MaterialTheme {
        Surface(color = Color.White) {
            FilterSheetContentPreviewHelper(initialCategory = "Price range")
        }
    }
}

/**
 * A helper Composable to initialize the state for specific preview scenarios.
 */
@Composable
fun FilterSheetContentPreviewHelper(initialCategory: String) {
    var selectedCategory by remember { mutableStateOf(initialCategory) }


    FilterSheetContent(onApply = {}, onClear = {})
}

@Preview(showBackground = true, name = "Brand Section View")
@Composable
fun PreviewBrandSectionOnly() {
    androidx.compose.material3.MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(20.dp)
        ) {
            BrandSection()
        }
    }
}

@Preview(showBackground = true, name = "Full Filter Sheet - Brand Active")
@Composable
fun PreviewFullFilterBrand() {
    androidx.compose.material3.MaterialTheme {
        Surface(color = Color.White) {
            FilterSheetContent(onApply = {}, onClear = {})
        }
    }
}

@Preview(showBackground = true, name = "Model Selection Preview")
@Composable
fun PreviewModelSection() {
    androidx.compose.material3.MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            color = Color.White
        ) {
            ModelSection()
        }
    }
}

@Preview(showBackground = true, name = "Color Grid Preview")
@Composable
fun PreviewColorSection() {
    androidx.compose.material3.MaterialTheme {
        Surface(color = Color.White, modifier = Modifier.padding(20.dp)) {
            ColorSection()
        }
    }
}