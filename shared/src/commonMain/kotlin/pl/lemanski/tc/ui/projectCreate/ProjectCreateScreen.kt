package pl.lemanski.tc.ui.projectCreate

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pl.lemanski.tc.domain.model.project.Rhythm
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.composables.LoaderScaffold
import pl.lemanski.tc.ui.common.composables.toComposable

@Composable
internal fun ProjectCreateScreen(
    isLoading: Boolean,
    title: String,
    projectName: StateComponent.Input,
    projectBpm: StateComponent.Input,
    projectRhythm: StateComponent.SelectInput<Rhythm>,
    createProjectButton: StateComponent.Button
) {
    LoaderScaffold(isLoading) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h3
            )

            projectName.toComposable()

            projectBpm.toComposable()

            projectRhythm.toComposable()

            createProjectButton.toComposable()
        }
    }
}