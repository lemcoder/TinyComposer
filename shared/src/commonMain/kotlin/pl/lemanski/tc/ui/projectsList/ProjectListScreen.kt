package pl.lemanski.tc.ui.projectsList

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.composables.LoaderScaffold

@Composable
internal fun ProjectListScreen(
    isLoading: Boolean,
    title: String,
    projectCards: List<ProjectsListContract.State.ProjectCard>,
    addButton: StateComponent.Button
) {
    LoaderScaffold(isLoading) {
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
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(projectCards) { projectCard ->
                    projectCard.toComposable()
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ProjectsListContract.State.ProjectCard.toComposable() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onLongClick = { onDelete(id) },
            ) {
                onClick(id)
            },
    ) {
        val colors = OutlinedTextFieldDefaults.colors()

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
