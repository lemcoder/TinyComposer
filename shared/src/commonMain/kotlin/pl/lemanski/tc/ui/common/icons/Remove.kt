package pl.lemanski.tc.ui.common.icons

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.Outlined.Remove: ImageVector
    get() {
        if (_Remove != null) {
            return _Remove!!
        }
        _Remove = ImageVector.Builder(
            name = "Outlined.Remove",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color(0xFFE8EAED))) {
                moveTo(200f, 520f)
                verticalLineToRelative(-80f)
                horizontalLineToRelative(560f)
                verticalLineToRelative(80f)
                lineTo(200f, 520f)
                close()
            }
        }.build()

        return _Remove!!
    }

@Suppress("ObjectPropertyName")
private var _Remove: ImageVector? = null
