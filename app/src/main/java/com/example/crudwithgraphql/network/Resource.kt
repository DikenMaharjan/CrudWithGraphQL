package com.example.crudwithgraphql.network

import com.apollographql.apollo3.exception.ApolloException

sealed class Resource<out T> {
    class Success<T>(val data: T) : Resource<T>()
    class Failure(
        val networkFailure: NetworkFailure,
        val errorMsg:String = networkFailure.errorMsg
    ) : Resource<Nothing>()
}


sealed class NetworkFailure(
    val errorMsg: String
) {
    class ApolloError(
        val apolloException: ApolloException
    ) : NetworkFailure(apolloException.message ?: "Something went wrong")

    class GenericError(
        errorMsg: String
    ) : NetworkFailure(errorMsg)

}