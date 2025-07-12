package id.neotica.modernadb.domain

sealed class NeoResult<T>(
    val data: T? = null,
    val errorMessage: String? = null
) {
    class Success<T>(data: T) : NeoResult<T>(data)
    class Loading<T>(data: T? = null) : NeoResult<T>(data)
    class Error<T>(errorMessage: String? = null) : NeoResult<T>(null, errorMessage)
}