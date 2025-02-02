package pl.lemanski.tc.ui.projectsList

import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.ToComposable
import pl.lemanski.tc.ui.common.composables.LoaderScaffold
import pl.lemanski.tc.ui.common.composables.ToComposable
import tinycomposer.shared.generated.resources.Res
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class, ExperimentalResourceApi::class)
@Composable
internal fun ProjectListScreen(
    isLoading: Boolean,
    title: String,
    noProjectsText: String,
    loadSampleProjectsButton: StateComponent.Button,
    projectCards: List<ProjectsListContract.State.ProjectCard>,
    addButton: StateComponent.Button,
    snackBar: StateComponent.SnackBar?
) {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes("files/anim/cat.json").decodeToString()
        )
    }

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
                    .padding(bottom = 24.dp), // 16 + 8 (label padding on create screen) -> 24
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title, style = MaterialTheme.typography.headlineMedium
                )

                IconButton(onClick = addButton.onClick) {
                    Icon(
                        Icons.Default.Add, contentDescription = "Create project"
                    )
                }
            }

            if (projectCards.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(
                        space = 16.dp,
                        alignment = Alignment.Top // Animation will take half of screen height
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    BoxWithConstraints(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = rememberLottiePainter(
                                composition = composition,
                                iterations = Compottie.IterateForever
                            ),
                            modifier = Modifier.size(minOf(maxWidth, 300.dp)),
                            contentDescription = "Cat loader animation",
                            tint = Color.Unspecified,
                        )
                    }

                    Text(
                        text = noProjectsText,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )

                    loadSampleProjectsButton.ToComposable()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items = projectCards,
                        key = { it.id.hashCode() },
                        contentType = { it::class }) { projectCard ->
                        projectCard.toComposable(Modifier.animateItemPlacement())
                    }
                }
            }
        }

        snackBar.ToComposable(snackBarState)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ProjectsListContract.State.ProjectCard.toComposable(modifier: Modifier) {
    val colors = OutlinedTextFieldDefaults.colors()
    val scope = rememberCoroutineScope()
    val hapticFeedback = LocalHapticFeedback.current

    AnchoredDragBox(
        modifier = modifier
            .fillMaxWidth()
            .zIndex(10f)
            .clickable { onClick(id) },
        endContentWidth = 64.dp,
        endContent = { anchoredDraggableState, _ ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        scope.launch {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
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

        Surface(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 3.dp,
            shape = RoundedCornerShape(8.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
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
                if (endContent != null) {
                    endContent(state, endSwipeProgress)
                }
            }
        }

        Box(
            modifier = Modifier
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
                    state, Orientation.Horizontal
                )
        ) {
            content(state, endSwipeProgress)
        }
    }
}

enum class DragAnchors {
    Start, Center, End,
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun rememberAnchoredDraggableState(
    initialValue: DragAnchors = DragAnchors.Center,
    positionalThreshold: (distance: Float) -> Float = { distance -> distance * 0.5f },
    velocityThreshold: Dp = 100.dp,
    animationSpec: SpringSpec<Float> = SpringSpec(),
): AnchoredDraggableState<DragAnchors> {
    val density = LocalDensity.current
    val decayAnimation = rememberSplineBasedDecay<Float>()

    return remember {
        AnchoredDraggableState(
            initialValue = initialValue,
            positionalThreshold = positionalThreshold,
            velocityThreshold = { velocityThreshold.value / density.density },
            snapAnimationSpec = animationSpec,
            decayAnimationSpec = decayAnimation
        )
    }
}