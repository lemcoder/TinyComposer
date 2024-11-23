package pl.lemanski.tc.ui.projectsList

import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.composables.LoaderScaffold
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@Composable
internal fun ProjectListScreen(
    isLoading: Boolean,
    title: String,
    projectCards: List<ProjectsListContract.State.ProjectCard>,
    addButton: StateComponent.Button,
    snackBar: StateComponent.SnackBar?
) {
    val scope = rememberCoroutineScope()

    LoaderScaffold(isLoading) { snackBarState ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp), // 16 + 8 (label padding on create screen) -> 20
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium
                )

                IconButton(
                    onClick = addButton.onClick,
                    modifier = Modifier.border(
                        width = 1.dp,
                        color = OutlinedTextFieldDefaults.colors().unfocusedIndicatorColor,
                        shape = CircleShape
                    )
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Create project"
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = projectCards,
                    key = { it.id.hashCode() },
                    contentType = { it::class }
                ) { projectCard ->
                    projectCard.toComposable(Modifier.animateItem())
                }
            }
        }

        LaunchedEffect(snackBar) {
            if (snackBar == null) {
                snackBarState.currentSnackbarData?.dismiss()
                return@LaunchedEffect
            }

            val result = snackBarState.showSnackbar(snackBar.message, snackBar.action, true)
            when (result) {
                SnackbarResult.Dismissed -> { /* do nothing */
                }
                SnackbarResult.ActionPerformed -> snackBar.onAction?.invoke()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ProjectsListContract.State.ProjectCard.toComposable(modifier: Modifier) {
    val colors = OutlinedTextFieldDefaults.colors()
    val scope = rememberCoroutineScope()

    AnchoredDragBox(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(id) },
        endContentWidth = 64.dp,
        endContent = { anchoredDraggableState, _ ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        scope.launch {
                            onDelete(id)
                            anchoredDraggableState.animateTo(DragAnchors.Center)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier
                )
            }
        }
    ) { _, _ ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = colors.unfocusedIndicatorColor,
                    shape = OutlinedTextFieldDefaults.shape
                )
                .padding(8.dp),
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.headlineSmall,
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                color = colors.unfocusedIndicatorColor,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnchoredDragBox(
    modifier: Modifier = Modifier,
    state: AnchoredDraggableState<DragAnchors> = rememberAnchoredDraggableState(),
    endContentWidth: Dp = 0.dp,
    endContent: @Composable (RowScope.(anchoredDraggableState: AnchoredDraggableState<DragAnchors>, endSwipeProgress: Float) -> Unit)? = null,
    content: @Composable BoxScope.(anchoredDraggableState: AnchoredDraggableState<DragAnchors>, endSwipeProgress: Float) -> Unit,
) {
    val endWidthPx = with(LocalDensity.current) { endContentWidth.toPx() }

    val draggableAnchors: DraggableAnchors<DragAnchors> = DraggableAnchors {
        DragAnchors.Center at 0f
        DragAnchors.End at -endWidthPx
    }

    state.updateAnchors(draggableAnchors)

    val offsetRange = Float.NEGATIVE_INFINITY..0f

    val endSwipeProgress = if (state.requireOffset() < 0f) {
        (state.requireOffset() / endWidthPx).absoluteValue
    } else 0f
    val endContentLiveWidth = endContentWidth * endSwipeProgress

    Box(
        modifier = modifier.clipToBounds()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .matchParentSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Row(
                modifier = Modifier
                    .wrapContentHeight()
                    .width(endContentLiveWidth)
                    .clipToBounds()
            ) {
                endContent?.invoke(this, state, endSwipeProgress)
            }
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .offset {
                IntOffset(
                    state
                        .requireOffset()
                        .coerceIn(offsetRange)
                        .roundToInt(), 0
                )
            }
            .anchoredDraggable(
                state,
                Orientation.Horizontal
            )
        ) {
            content(state, endSwipeProgress)
        }
    }
}
enum class DragAnchors {
    Center,
    End,
}

/**
 * Create and [remember] a [AnchoredDraggableState] with the default animation clock.
 *
 * @param initialValue The initial value of the state.
 * @param positionalThreshold The positional threshold, in px, to be used when calculating the
 * target state while a drag is in progress and when settling after the drag ends. This is the
 * distance from the start of a transition. It will be, depending on the direction of the
 * interaction, added or subtracted from/to the origin offset. It should always be a positive value.
 * @param velocityThreshold The velocity threshold (in px per second) that the end velocity has to
 * exceed in order to animate to the next state, even if the [positionalThreshold] has not been
 * reached.
 * @param animationSpec The default animation that will be used to animate to a new state.
 * @param confirmValueChange Optional callback invoked to confirm or veto a pending state change.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun rememberAnchoredDraggableState(
    initialValue: DragAnchors = DragAnchors.Center,
    positionalThreshold: (distance: Float) -> Float = { distance -> distance * 0.5f },
    velocityThreshold: Dp = 100.dp,
    animationSpec: SpringSpec<Float> = SpringSpec(),
    confirmValueChange: (DragAnchors) -> Boolean = { true }
): AnchoredDraggableState<DragAnchors> {
    val density = LocalDensity.current
    return remember {
        AnchoredDraggableState(
            initialValue = initialValue,
            positionalThreshold = positionalThreshold,
            velocityThreshold = { with(density) { velocityThreshold.toPx() } },
            snapAnimationSpec = animationSpec,
            decayAnimationSpec = exponentialDecay(),
            confirmValueChange = confirmValueChange
        )
    }
}