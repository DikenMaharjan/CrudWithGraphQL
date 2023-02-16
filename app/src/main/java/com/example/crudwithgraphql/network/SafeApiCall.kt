package com.example.crudwithgraphql.network

import com.apollographql.apollo3.ApolloCall
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.exception.ApolloException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SafeApiCall @Inject constructor() {

    suspend fun <T : Operation.Data> execute(apiCall: suspend () -> ApolloCall<T>): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiCall.invoke().execute()
                if (response.data != null && !response.hasErrors()) {
                    Resource.Success(
                        response.data!!
                    )
                } else {
                    Resource.Failure(
                        networkFailure = NetworkFailure.GenericError(
                            errorMsg = response.errors?.firstOrNull()?.message
                                ?: "Something went wrong"
                        )
                    )
                }
            } catch (e: ApolloException) {
                Resource.Failure(
                    networkFailure = NetworkFailure.ApolloError(
                        apolloException = e
                    )
                )
            } catch (e: Exception) {
                Resource.Failure(
                    networkFailure = NetworkFailure.GenericError(
                        errorMsg = e.message ?: "Something went wrong"
                    )
                )
            }
        }

    }
}