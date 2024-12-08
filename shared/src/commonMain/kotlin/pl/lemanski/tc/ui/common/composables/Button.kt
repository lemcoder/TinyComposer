package pl.lemanski.tc.ui.common.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import pl.lemanski.tc.ui.common.StateComponent

/**
 * This button acts on press.
 */
@Composable
internal fun StateComponent.Button.ToComposable() = Button(onClick = onClick) {
    Text(text = text)
}

/**
 * This button acts on press.
 */
@Composable
internal fun StateComponent.Button.ToComposableActingOnPress() {
    val absoluteElevation = LocalAbsoluteTonalElevation.current + 0.dp
    CompositionLocalProvider(
        LocalContentColor provides MaterialTheme.colorScheme.surface,
        LocalAbsoluteTonalElevation provides absoluteElevation
    ) {
        Box(
            modifier = Modifier
                .minimumInteractiveComponentSize()
                .surface(
                    shape = RoundedCornerShape(percent = 50),
                    backgroundColor = MaterialTheme.colorScheme.onSurface,
                    border = null,
                    shadowElevation = with(LocalDensity.current) { 5.dp.toPx() }
                )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = { onClick() }
                    )
                },
            propagateMinConstraints = true
        ) {
            Row(
                Modifier.defaultMinSize(
                    minWidth = ButtonDefaults.MinWidth,
                    minHeight = ButtonDefaults.MinHeight
                )
                    .padding(ButtonDefaults.ContentPadding),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = text,
                )
            }
        }
    }
}

@Stable
private fun Modifier.surface(
    shape: Shape,
    backgroundColor: Color,
    border: BorderStroke?,
    shadowElevation: Float,
) =
    this.then(
        if (shadowElevation > 0f) {
            Modifier.graphicsLayer(
                shadowElevation = shadowElevation,
                shape = shape,
                clip = false
            )
        } else {
            Modifier
        }
    )
        .then(if (border != null) Modifier.border(border, shape) else Modifier)
        .background(color = backgroundColor, shape = shape)
        .clip(shape)