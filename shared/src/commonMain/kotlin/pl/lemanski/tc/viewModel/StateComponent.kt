package pl.lemanski.tc.viewModel

sealed class StateComponent {
    data class Button(
        val text: String,
        val onClick: () -> Unit
    ) : StateComponent()

    data class Text(
        val text: String
    ) : StateComponent()


}