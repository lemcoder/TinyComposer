package pl.lemanski.tc.ui.preview

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.proejctDetails.components.BottomBar

@Composable
@Preview
fun PreviewPlayground() {
    Surface {
        BottomBar(
            playButton = StateComponent.Button(text = "play", onClick = {}),
            pauseButton = null,
            addButton = StateComponent.Button(text = "add", onClick = {}),
            aiGenerateButton = StateComponent.Button(text = "ai generate", onClick = {}),
        )
    }
}