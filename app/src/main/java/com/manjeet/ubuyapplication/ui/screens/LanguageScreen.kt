package com.manjeet.ubuyapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.manjeet.ubuyapplication.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageCountrySelectionScreen(
    navController: NavController
) {
    val backgroundColor = Color(0xFFF2F4F8)
    val cardColor = Color.White
    val fieldColor = Color(0xFFE3DDDD)
    val buttonColor = Color(0xFF074823)

    var selectedCountry by remember { mutableStateOf("Select Country") }
    var selectedLanguage by remember { mutableStateOf("Select Language") }
    var selectedCountryFlag by remember { mutableStateOf<Int?>(null) }
    var selectedLanguageFlag by remember { mutableStateOf<Int?>(null) }

    var countrySheetOpen by remember { mutableStateOf(false) }
    var languageSheetOpen by remember { mutableStateOf(false) }

    var countrySearchQuery by remember { mutableStateOf("") }
    var languageSearchQuery by remember { mutableStateOf("") }

    val countryList = listOf(
        "India" to R.drawable.ic_india_flag,
        "United States" to R.drawable.ic_usa_flag,
        "United Kingdom" to R.drawable.ic_uk_flag,
        "Canada" to R.drawable.ic_canada_flag,
        "Australia" to R.drawable.ic_australia_flag,
        "India" to R.drawable.ic_india_flag,
        "United States" to R.drawable.ic_usa_flag

    )

    val languageList = listOf(
        "English (UK)" to R.drawable.ic_uk_flag,
        "English (USA)" to R.drawable.ic_usa_flag,
        "English (USA)" to R.drawable.ic_usa_flag,
        "English (USA)" to R.drawable.ic_usa_flag,
        "English (USA)" to R.drawable.ic_usa_flag
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    )

    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp, start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ubuy_logo),
                    contentDescription = "Ubuy Logo",
                    modifier = Modifier.size(70.dp)
                )
            }


            Spacer(modifier = Modifier.height(18.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp),
                shape = RoundedCornerShape(28.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(cardColor)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.globe),
                        contentDescription = "Location Image",
                        modifier = Modifier.size(45.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Select your Location and Preferred Language",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Country",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    SelectionField(
                        text = selectedCountry,
                        backgroundColor = fieldColor,
                        flagResId = selectedCountryFlag,
                        onClick = {
                            countrySheetOpen = true
                            countrySearchQuery = ""
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Language",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    SelectionField(
                        text = selectedLanguage,
                        backgroundColor = fieldColor,
                        flagResId = selectedLanguageFlag,
                        onClick = {
                            languageSheetOpen = true
                            languageSearchQuery = ""
                        }
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            navController.navigate("home_screen")                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = buttonColor
                        )
                    ) {
                        Text(
                            text = "Continue",
                            fontSize = 18.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    if (countrySheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { countrySheetOpen = false },
            containerColor = Color.White
        ) {
            BottomSheetContent(
                title = "Select Country",
                options = countryList,
                searchQuery = countrySearchQuery,
                onSearchQueryChange = { countrySearchQuery = it },
                selectedItem = selectedCountry,
                onItemSelected = { country, flagId ->
                    selectedCountry = country
                    selectedCountryFlag = flagId
                    countrySheetOpen = false
                }
            )
        }
    }

    if (languageSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { languageSheetOpen = false },
            containerColor = Color.White
        ) {
            BottomSheetContent(
                title = "Select Language",
                options = languageList,
                searchQuery = languageSearchQuery,
                onSearchQueryChange = { languageSearchQuery = it },
                selectedItem = selectedLanguage,
                onItemSelected = { language, flagId ->
                    selectedLanguage = language
                    selectedLanguageFlag = flagId
                    languageSheetOpen = false
                }
            )
        }
    }
}

@Composable
fun SelectionField(
    text: String,
    backgroundColor: Color,
    flagResId: Int?,
    onClick: () -> Unit
) {
    OutlinedTextField(
        value = text,
        onValueChange = {},
        readOnly = true,
        singleLine = true,

        placeholder = {
            Text(
                text = "Select Country",
                fontSize = 14.sp,
                color = Color.Gray
            )
        },

        modifier = Modifier
            .padding(2.dp)
            .height(45.dp) // increased so text + hint is visible
            .width(360.dp)
            .clickable { onClick() },

        leadingIcon = {
            if (flagResId != null) {
                Image(
                    painter = painterResource(id = flagResId),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
            }
        },

        trailingIcon = {
            Box(
                modifier = Modifier
                    .size(39.dp)
                    .background(
                        color = Color(0xFFF2FAFC),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { onClick() }, // opens bottom sheet
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
        },

        textStyle = TextStyle(
            fontSize = 14.sp,
            color = Color.Gray
        ),

        shape = RoundedCornerShape(14.dp),

        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFE2E5E7),   // light sky blue
            unfocusedContainerColor = Color(0xFFE2E5E7), // light sky blue
            focusedBorderColor = Color.Gray,
            unfocusedBorderColor = Color.LightGray,
            cursorColor = Color.Black
        )
    )
}

@Composable
fun BottomSheetContent(
    title: String,
    options: List<Pair<String, Int>>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedItem: String,
    onItemSelected: (String, Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        TextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = {
                Text("Search...", color = Color.Gray)
            },

            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = Color.Gray
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(66.dp)
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(40.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFE3DDDD),
                unfocusedContainerColor = Color(0xFFE3DDDD),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true
        )



        LazyColumn {
            val filteredOptions = options.filter {
                it.first.contains(searchQuery, ignoreCase = true)
            }
            items(filteredOptions.size) { index ->
                val (item, flagId) = filteredOptions[index]
                BottomSheetItem(
                    item = item,
                    flagId = flagId,
                    isSelected = item == selectedItem,
                    onClick = { onItemSelected(item, flagId) }
                )
            }
        }
    }
}

@Composable
fun BottomSheetItem(
    item: String,
    flagId: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .border(
                    width = 1.dp,
                    color = Color.Gray, // change border color here
                    shape = RoundedCornerShape(50.dp)
                )
                .background(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(14.dp)
                )
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        )

        {
            Image(
                painter = painterResource(id = flagId),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .padding(end = 2.dp)
            )
            Text(
                text = item,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier
                    .padding(2.dp)

            )

            Spacer(modifier = Modifier.width(220.dp))

            if (isSelected) {
                Surface(
                    modifier = Modifier
                        .size(24.dp),
                    shape = RoundedCornerShape(50),
                    color = Color(0xFF0B6B35)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .padding(4.dp)
                            .size(16.dp)
                    )
                }
            }
        }
    }
}
@androidx.compose.ui.tooling.preview.Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun LanguageCountrySelectionScreenPreview() {
    val navController = rememberNavController()
    LanguageCountrySelectionScreen(navController)
}

@androidx.compose.ui.tooling.preview.Preview(
    showBackground = true,
    heightDp = 500
)
@Composable
fun BottomSheetContentCountryPreview() {
    val countryList = listOf(
        "India" to R.drawable.ic_india_flag,
        "United States" to R.drawable.ic_usa_flag,
        "United Kingdom" to R.drawable.ic_uk_flag,
        "Canada" to R.drawable.ic_canada_flag,
        "Australia" to R.drawable.ic_australia_flag
    )
    BottomSheetContent(
        title = "Select Country",
        options = countryList,
        searchQuery = "",
        onSearchQueryChange = {},
        selectedItem = "India",
        onItemSelected = { _, _ -> }
    )



}

@androidx.compose.ui.tooling.preview.Preview(
    showBackground = true,
    heightDp = 400
)
@Composable
fun BottomSheetContentLanguagePreview() {
    val languageList = listOf(
        "English (UK)" to R.drawable.ic_uk_flag,
        "English (USA)" to R.drawable.ic_usa_flag
    )
    BottomSheetContent(
        title = "Select Language",
        options = languageList,
        searchQuery = "",
        onSearchQueryChange = {},
        selectedItem = "English (UK)",
        onItemSelected = { _, _ -> }
    )
}

@androidx.compose.ui.tooling.preview.Preview(
    showBackground = true
)
@Composable
fun SelectionFieldPreview() {
    SelectionField(
        text = "India",
        backgroundColor = Color(0xFFE3DDDD),
        flagResId = R.drawable.ic_india_flag,
        onClick = {}
    )
}

@androidx.compose.ui.tooling.preview.Preview(
    showBackground = true
)
@Composable
fun BottomSheetItemPreview() {
    BottomSheetItem(
        item = "India",
        flagId = R.drawable.ic_india_flag,
        isSelected = true,
        onClick = {}
    )
}