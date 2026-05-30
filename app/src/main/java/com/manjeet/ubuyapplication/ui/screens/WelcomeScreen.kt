package com.manjeet.ubuyapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.manjeet.ubuyapplication.R

// Data class for slides
data class WelcomeSlide(
    val image: Int,
    val title: String,
    val description: String
)

@Composable
fun WelcomeSlidesScreen(navController: NavController) {

    val yellowColor = Color(0xFFFFB300)
    val backgroundColor = Color(0xFFF8F8F8)

    val slides = listOf(
        WelcomeSlide(
            image = R.drawable.img,
            title = "Welcome to Ubuy",
            description = "Discover products from all over the world"
        ),
        WelcomeSlide(
            image = R.drawable.img_1,
            title = "Fast Delivery",
            description = "Get your orders delivered quickly and safely"
        ),
        WelcomeSlide(
            image = R.drawable.img_2,
            title = "Best Shopping Experience",
            description = "Easy shopping with secure payments and great offers"
        )
    )

    val pagerState = rememberPagerState(pageCount = { slides.size })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->

            val slide = slides[page]

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Image(
                        painter = painterResource(id = slide.image),
                        contentDescription = slide.title,
                        modifier = Modifier
                            .size(280.dp), // Bigger image
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = slide.title,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = slide.description,
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Dots Indicator
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(slides.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(
                            if (pagerState.currentPage == index) 12.dp else 8.dp
                        )
                        .clip(CircleShape)
                        .background(
                            if (pagerState.currentPage == index)
                                yellowColor
                            else
                                Color.LightGray
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Login Button only on last slide


        if (pagerState.currentPage == slides.lastIndex) {
            Button(
                onClick = {

                    navController.navigate("login_signup")                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = yellowColor
                )
            ) {
                Text(
                    text = "Login",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun WelcomeSlidesScreenPreview() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFA726)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.img_3),
            contentDescription = "Preview Logo",
            modifier = Modifier.size(140.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Welcome Slides Preview",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}