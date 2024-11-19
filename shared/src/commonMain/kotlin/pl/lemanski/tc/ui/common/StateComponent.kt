package pl.lemanski.tc.ui.common

sealed class StateComponent {
    data class Button(
        val text: String,
        val onClick: () -> Unit
    ) : StateComponent()

    data class Input(
        val value: String,
        val type: Type,
        val hint: String,
        val onValueChange: (String) -> Unit
    ) {
        enum class Type {
            NUMBER,
            TEXT,
        }
    }

    data class SelectInput<T>(
        val value: T,
        val hint: String,
        val onSelected: (T) -> Unit,
        val options: Set<Option<T>>
    ) {
        data class Option<T>(
            val name: String,
            val value: T
        )
    }
}