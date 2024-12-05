package pl.lemanski.tc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import pl.lemanski.tc.domain.model.navigation.Destination
import pl.lemanski.tc.domain.model.navigation.WelcomeDestination
import pl.lemanski.tc.domain.service.navigation.NavigationService
import pl.lemanski.tc.domain.service.navigation.back
import pl.lemanski.tc.theme.TcTheme
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    private val navigationService: NavigationService by inject()
    private val navigationState = mutableStateOf<Destination>(WelcomeDestination)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        navigationService.setOnNavigateListener { event ->
            navigationState.value = event.destination
        }

        setContent {
            val scope = rememberCoroutineScope()
            val destination by navigationState

            TcTheme {
                // TODO replace with window insets
                Scaffold { paddingValues ->
                    MainScreen(paddingValues, destination)
                }
            }

            BackHandler {
                scope.launch {
                    navigationService.back()
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewWheel() {
    MaterialTheme {
        val noteList = remember { listOf("A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#") }
        var selected by remember { mutableStateOf(noteList.first()) }
        val s = 200f
        var expanded by remember { mutableStateOf(false) }


        Box(
            modifier = Modifier.size(s.dp),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Wheel(
                    selected = selected,
                    options = noteList,
                    onNoteSelected = {
                        selected = it
                        expanded = false
                    },
                    size = DpSize(s.dp, s.dp)
                )
            }

            if (expanded) {
                IconButton(
                    onClick = { expanded = false },
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = "Close"
                    )
                }
            } else {
                IconButton(
                    onClick = { expanded = true },
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = "Close"
                    )
                }
            }
        }
    }
}

@Composable
fun Wheel(
    selected: String,
    options: List<String>,
    onNoteSelected: (String) -> Unit,
    size: DpSize,
    modifier: Modifier = Modifier
) {
    val angleBetweenNotes = 360f / options.size

    Box(
        modifier = Modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        options.forEachIndexed { index, note ->
            val isSelected = note == selected
            val color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            val angle = angleBetweenNotes * index
            val radians = Math.toRadians(angle.toDouble())
            val x = (((size.width / 2).value - 20) * cos(radians)).toFloat()
            val y = (((size.height / 2).value - 20) * sin(radians)).toFloat()

            Box(
                modifier = Modifier
                    .offset(x = x.dp, y = y.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color)
                    .clickable { onNoteSelected(note) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = note,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
