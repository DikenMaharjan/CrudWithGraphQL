package com.example.crudwithgraphql.data.datasource

import com.apollographql.apollo3.ApolloClient
import com.example.crudwithgraphql.GetUsersQuery
import com.example.crudwithgraphql.network.Resource
import com.example.crudwithgraphql.network.SafeApiCall
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "RemoteDataSource"

@Singleton
class UsersRemoteDataSource @Inject constructor(
    private val apolloClient: ApolloClient,
    private val safeApiCall: SafeApiCall
) {
    suspend fun getUsers(): Resource<GetUsersQuery.Data> {
        return safeApiCall.execute {
            apolloClient.query(GetUsersQuery())
        }
    }


}