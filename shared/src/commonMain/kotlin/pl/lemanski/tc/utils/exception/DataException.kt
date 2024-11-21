package pl.lemanski.tc.utils.exception

internal class EntryNotFoundException(override val message: String): TcException(message)

internal class InvalidDataException(override val message: String): TcException(message)