package pl.lemanski.tc.ui.common.icons

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Outlined.Ai: ImageVector
    get() {
        if (_Ai != null) {
            return _Ai!!
        }
        _Ai = ImageVector.Builder(
            name = "Outlined.Ai",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFFE8EAED))) {
                moveTo(19f, 9f)
                lineToRelative(1.25f, -2.75f)
                lineToRelative(2.75f, -1.25f)
                lineToRelative(-2.75f, -1.25f)
                lineToRelative(-1.25f, -2.75f)
                lineToRelative(-1.25f, 2.75f)
                lineToRelative(-2.75f, 1.25f)
                lineToRelative(2.75f, 1.25f)
                close()
            }
            path(fill = SolidColor(Color(0xFFE8EAED))) {
                moveTo(19f, 15f)
                lineToRelative(-1.25f, 2.75f)
                lineToRelative(-2.75f, 1.25f)
                lineToRelative(2.75f, 1.25f)
                lineToRelative(1.25f, 2.75f)
                lineToRelative(1.25f, -2.75f)
                lineToRelative(2.75f, -1.25f)
                lineToRelative(-2.75f, -1.25f)
                close()
            }
            path(fill = SolidColor(Color(0xFFE8EAED))) {
                moveTo(11.5f, 9.5f)
                lineTo(9f, 4f)
                lineTo(6.5f, 9.5f)
                lineTo(1f, 12f)
                lineToRelative(5.5f, 2.5f)
                lineTo(9f, 20f)
                lineToRelative(2.5f, -5.5f)
                lineTo(17f, 12f)
                lineTo(11.5f, 9.5f)
                close()
                moveTo(9.99f, 12.99f)
                lineTo(9f, 15.17f)
                lineToRelative(-0.99f, -2.18f)
                lineTo(5.83f, 12f)
                lineToRelative(2.18f, -0.99f)
                lineTo(9f, 8.83f)
                lineToRelative(0.99f, 2.18f)
                lineTo(12.17f, 12f)
                lineTo(9.99f, 12.99f)
                close()
            }
        }.build()

        return _Ai!!
    }

@Suppress("ObjectPropertyName")
private var _Ai: ImageVector? = null
