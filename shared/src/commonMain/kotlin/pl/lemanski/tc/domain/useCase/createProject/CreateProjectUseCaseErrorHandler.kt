package pl.lemanski.tc.domain.useCase.createProject

interface CreateProjectUseCaseErrorHandler {
    fun onInvalidProjectName()
    fun onInvalidProjectBpm()
    fun onProjectSaveError()
}
