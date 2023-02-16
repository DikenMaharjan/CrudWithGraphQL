package com.example.crudwithgraphql.data.datasource

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.example.crudwithgraphql.GetUsersQuery
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val apolloClient: ApolloClient
) {
    suspend fun getUsers(): ApolloResponse<GetUsersQuery.Data> {
        return apolloClient.query(
            GetUsersQuery()
        ).execute()
    }
}