package com.manjeet.ubuyapplication.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import com.manjeet.ubuyapplication.R
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun SplashAnimationScreen() {

    val backgroundColor = Color(0xFFF2F2F2)
    val yellowColor = Color(0xFFFFB300)

    val circleOffsetY = remember { Animatable(-700f) }
    val circleScale = remember { Animatable(0f) }
    val glowScale = remember { Animatable(0f) }

    var currentStep by remember { mutableStateOf(1) }
    var showText by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // STEP 1 → yellow logo visible
        delay(1000)

        // STEP 2 → logo changes to white logo
        currentStep = 2
        delay(600)

        // STEP 3 → yellow circle comes from top
        currentStep = 3

        launch {
            circleOffsetY.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 400,
                    easing = FastOutSlowInEasing
                )
            )
        }

        launch {
            circleScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 900)
            )
        }

        delay(1000)

        // STEP 4 → glow expanding
        glowScale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 900,
                easing = LinearOutSlowInEasing
            )
        )

        delay(700)

        // STEP 5 → Show final screen with text
        currentStep = 4
        showText = true
        delay(5000)  // Keep showing for 5 seconds
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (currentStep) {
            1 -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundColor),
                    contentAlignment = Alignment.Center

                ) {
                    AppLogo(
                        size = 90.dp,
                        logoRes = R.drawable.logo
                    )
                }
            }

            2 -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    AppLogo(
                        shape = RoundedCornerShape(26.dp),
                        size = 90.dp,
                        logoRes = R.drawable.logo1
                    )
                }
            }

            3 -> {

                val screenSize = 2500.dp  // Adjust this value

                Box(
                    modifier = Modifier
                        .fillMaxSize()
//                        .clip(CircleShape)

                        .background(backgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size((1400 * glowScale.value).dp)
                            .background(yellowColor)
                    )

                    Box(
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    0,
                                    circleOffsetY.value.roundToInt()
                                )
                            }
                            .size((180 * circleScale.value).dp)
                            .clip(CircleShape)
                            .background(yellowColor)
                    )

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center




                    ) {
                        AppLogo(
                            shape = RoundedCornerShape(26.dp),
                            size = 90.dp,
                            logoRes = R.drawable.logo1
                        )
                           Spacer(modifier = Modifier.height(40.dp))
//
//                        Text(
//                                text = "Delivering Happiness",
//                                color = Color.White,
//                                fontSize = 28.sp,
//                                fontWeight = FontWeight.Bold,
//                                textAlign = TextAlign.Center
//                            )
//
//                            Spacer(modifier = Modifier.height(8.dp))
//
//                            Text(
//                                text = "Worldwide",
//                                color = Color.White,
//                                fontSize = 28.sp,
//                                fontWeight = FontWeight.Bold,
//                                textAlign = TextAlign.Center
//                            )


                    }
                }
            }

            4 -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(yellowColor),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        AppLogo(
                            shape = RoundedCornerShape(26.dp),
                            size = 90.dp,
                            logoRes = R.drawable.logo1
                        )

//                        Spacer(modifier = Modifier.height(40.dp))

//                        if (showText) {
//                            Text(
//                                text = "Delivering Happiness",
//                                color = Color.White,
//                                fontSize = 28.sp,
//                                fontWeight = FontWeight.Bold,
//                                textAlign = TextAlign.Center
//                            )
//
//                            Spacer(modifier = Modifier.height(8.dp))
//
//                            Text(
//                                text = "Worldwide",
//                                color = Color.White,
//                                fontSize = 28.sp,
//                                fontWeight = FontWeight.Bold,
//                                textAlign = TextAlign.Center
//                            )
//                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppLogo(
    size: androidx.compose.ui.unit.Dp,
    logoRes: Int,
    shape: androidx.compose.foundation.shape.CornerBasedShape = RoundedCornerShape(16.dp)
) {
    Image(
        painter = painterResource(id = logoRes),
        contentDescription = "App Logo",
        modifier = Modifier
            .size(size)
            .clip(shape)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashAnimationScreenPreview() {
    SplashAnimationScreen()
}