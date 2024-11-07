package pl.lemanski.tc.exception

class EntryNotFoundException(override val message: String): TCException(message)

class InvalidDataException(override val message: String): TCException(message)