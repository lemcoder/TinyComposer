package pl.lemanski.tc.ui.common.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pl.lemanski.tc.utils.UUID

@Composable
fun MultiRowLayout(
    modifier: Modifier = Modifier,
    verticalSpacing: Dp = 4.dp,
    content: @Composable () -> Unit
) {
    SubcomposeLayout(
        modifier = modifier
    ) { constraints ->
        val mainPlaceableList = subcompose(-1, content).map { measurable ->
            measurable.measure(Constraints(maxHeight = constraints.maxHeight))
        }

        if (mainPlaceableList.isEmpty()) {
            return@SubcomposeLayout layout(constraints.maxWidth, constraints.maxHeight) {}
        }

        val width = mainPlaceableList.sumOf { it.width }
        val maxHeight = mainPlaceableList.maxOf { it.height }
        val height = (constraints.maxWidth / width) * maxHeight

        // Track the x/y co-ord we have placed children up to
        var xPosition = 0
        var yPoint = 0

        layout(constraints.maxWidth, height) {
            // Place children in the parent layout
            mainPlaceableList.forEachIndexed { index, placeable ->
                // Position item on the screen
                if ((xPosition + placeable.width) > constraints.maxWidth) {
                    var remainder = placeable.width
                    val subPlaceable = subcompose(UUID.random(), content)[index].measure(constraints.copy(maxWidth = constraints.maxWidth - xPosition))
                    subPlaceable.placeRelative(x = xPosition, y = yPoint)
                    remainder -= subPlaceable.width

                    xPosition = 0 // place in new line

                    while (remainder >= constraints.maxWidth) {
                        yPoint += maxHeight + verticalSpacing.roundToPx()
                        val remainderPlaceable = subcompose(UUID.random(), content)[index].measure(constraints.copy(maxWidth = minOf(remainder, constraints.maxWidth)))
                        remainderPlaceable.placeRelative(x = xPosition, y = yPoint)
                        remainder -= remainderPlaceable.width
                    }

                    if (remainder > 0) {
                        yPoint += maxHeight + verticalSpacing.roundToPx()
                        val remainderPlaceable = subcompose(UUID.random(), content)[index].measure(constraints.copy(maxWidth = minOf(remainder, constraints.maxWidth)))
                        remainderPlaceable.placeRelative(x = xPosition, y = yPoint)
                        xPosition += remainder
                    }

                    return@forEachIndexed
                }

                placeable.placeRelative(x = xPosition, y = yPoint)

                // Record the y co-ord placed up to
                xPosition += placeable.width
            }
        }
    }
}