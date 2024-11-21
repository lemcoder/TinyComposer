package pl.lemanski.tc.ui.projectCreate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.lemanski.tc.domain.model.project.Rhythm
import pl.lemanski.tc.ui.common.StateComponent
import pl.lemanski.tc.ui.common.composables.LoaderScaffold
import pl.lemanski.tc.ui.common.composables.ToComposable

@Composable
internal fun ProjectCreateScreen(
    isLoading: Boolean,
    title: String,
    projectName: StateComponent.Input,
    projectBpm: StateComponent.Input,
    projectRhythm: StateComponent.SelectInput<Rhythm>,
    createProjectButton: StateComponent.Button
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
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium
                )

                IconButton({}) {}
            }

            projectName.ToComposable()

            projectBpm.ToComposable()

            projectRhythm.ToComposable()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                createProjectButton.ToComposable()
            }
        }
    }
}