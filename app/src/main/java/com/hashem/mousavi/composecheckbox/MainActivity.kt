package com.hashem.mousavi.composecheckbox

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hashem.mousavi.composecheckbox.ui.theme.ComposeCheckBoxTheme
import kotlinx.coroutines.launch
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeCheckBoxTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CheckBox() {
                            Toast.makeText(applicationContext, "$it", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CheckBox(
    width: Dp = 200.dp,
    height: Dp = 100.dp,
    duration: Int = 1000,
    onCheckedChanged: (Boolean) -> Unit
) {
    var checked by remember {
        mutableStateOf(false)
    }
    val colorAnim = remember {
        Animatable(Color.Gray)
    }

    val progress = remember {
        androidx.compose.animation.core.Animatable(0f)
    }

    LaunchedEffect(checked) {
        if (checked) {
            launch {
                colorAnim.animateTo(
                    Color(0xFF00BFA5),
                    animationSpec = tween(durationMillis = duration)
                )
            }
            launch {
                progress.animateTo(
                    1f,
                    animationSpec = tween(durationMillis = duration)
                )
            }

        } else {
            launch {
                colorAnim.animateTo(
                    Color.Gray,
                    animationSpec = tween(durationMillis = duration)
                )
            }
            launch {
                progress.animateTo(
                    0f,
                    animationSpec = tween(durationMillis = duration)
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .size(width = width, height = height)
    ) {
        Canvas(modifier = Modifier
            .matchParentSize()
            .clickable(indication = null, interactionSource = remember {
                MutableInteractionSource()
            }) {
                checked = !checked
                onCheckedChanged(checked)
            }) {
            //draw background
            drawRoundRect(
                color = colorAnim.value,
                cornerRadius = CornerRadius(height.toPx() / 2, height.toPx() / 2)
            )

            val radius = height.toPx() / 2 - 2.dp.toPx()
            val x =
                progress.value * (width.toPx() - 2 * radius - 4.dp.toPx()) + radius + 2.dp.toPx()
            val y = radius + 2.dp.toPx()

            val r = radius * 0.8f


            translate(
                x,
                y
            ) {
                drawCircle(
                    color = Color.White,
                    radius = radius,
                    center = Offset(
                        0f,
                        0f
                    )
                )

                //draw lines
                var xs: Float = ((progress.value - 1) * r / sqrt(8.0)).toFloat()
                var ys: Float = (r / sqrt(8.0)).toFloat()
                var xe: Float = ((progress.value + 1) * r / sqrt(8.0)).toFloat()
                var ye: Float = -(r / sqrt(8.0)).toFloat()

                drawLine(
                    color = colorAnim.value,
                    start = Offset(xs, ys),
                    end = Offset(xe, ye),
                    strokeWidth = 1.dp.toPx()* (1 + progress.value)
                )

                xs = -(r / sqrt(8.0)).toFloat()
                ys = ((progress.value - 1) * r / sqrt(8.0)).toFloat()
                xe = ((1 - progress.value) * r / sqrt(8.0)).toFloat()
                ye = (r / sqrt(8.0)).toFloat()

                drawLine(
                    color = colorAnim.value,
                    start = Offset(xs, ys),
                    end = Offset(xe, ye),
                    strokeWidth = 1.dp.toPx() * (1 + progress.value)
                )

            }

        }
    }
}

//private fun calcColor(p: Float, c1: Color, c2: Color) : Color{
//    val r1 =
//}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeCheckBoxTheme {
        CheckBox() {

        }
    }
}