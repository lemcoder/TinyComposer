package pl.lemanski.tc.ui.projectsList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.composables.LoaderScaffold
import pl.lemanski.tc.ui.common.composables.toComposable

@Composable
internal fun ProjectListScreen(
    isLoading: Boolean,
    title: String,
    projectCards: List<ProjectsListContract.State.ProjectCard>,
    addButton: StateComponent.Button
) {
    LoaderScaffold(isLoading) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.body1
            )

            projectCards.forEach { projectCard ->
                projectCard.toComposable()
            }

            addButton.toComposable()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ProjectsListContract.State.ProjectCard.toComposable() {
    Surface(
        onClick = onClick,
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.h5
        )

        Text(
            text = description,
            style = MaterialTheme.typography.body1
        )
    }
}