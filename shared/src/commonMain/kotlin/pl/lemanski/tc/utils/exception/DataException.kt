package pl.lemanski.tc.utils.exception

class EntryNotFoundException(override val message: String): TcException(message)

class InvalidDataException(override val message: String): TcException(message)