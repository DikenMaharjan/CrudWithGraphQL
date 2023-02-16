package com.example.crudwithgraphql.feature_users.data.repo

import com.example.crudwithgraphql.feature_users.data.datasource.UsersRemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersRepository @Inject constructor(
    private val usersRemoteDataSource: UsersRemoteDataSource
) {

    suspend fun getUsers() = usersRemoteDataSource.getUsers()
}