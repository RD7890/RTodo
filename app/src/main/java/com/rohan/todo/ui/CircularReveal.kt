package com.rohan.todo.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.hypot

class CircularRevealShape(
    private val progress: Float,
    private val center: Offset
) : Shape {
    override fun createOutline(
        size: IntSize,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val maxRadius = hypot(size.width.toFloat(), size.height.toFloat())
        val radius = maxRadius * progress
        val path = Path().apply {
            addOval(Rect(center = center, radius = radius))
        }
        return Outline.Generic(path)
    }
}

@Composable
fun CircularReveal(
    revealProgress: Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    Box(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { size = it }
            .clip(CircularRevealShape(revealProgress, Offset(size.width / 2f, size.height / 2f)))
    ) {
        content()
    }
}
