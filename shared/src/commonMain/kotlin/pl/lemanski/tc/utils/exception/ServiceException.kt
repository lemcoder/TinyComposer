package pl.lemanski.tc.utils.exception

class NavigationStateException(override val message: String) : TcException(message)

class ApplicationStateException(override val message: String) : TcException(message)