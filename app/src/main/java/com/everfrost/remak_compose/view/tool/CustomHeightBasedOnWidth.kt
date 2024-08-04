package com.everfrost.remak_compose.view.tool

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints

fun Modifier.customHeightBasedOnWidth(aspectRatio: Float): Modifier = this.then(
    layout { measurable, constraints ->
        val width = constraints.maxWidth
        val height = (width * aspectRatio).toInt()
        val placeable = measurable.measure(Constraints.fixed(width, height))
        layout(width, height) {
            placeable.placeRelative(0, 0)
        }
    }
)