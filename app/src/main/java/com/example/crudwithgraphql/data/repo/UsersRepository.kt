package com.example.crudwithgraphql.data.repo

import com.example.crudwithgraphql.data.datasource.UsersRemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "UsersRepository"

@Singleton
class UsersRepository @Inject constructor(
    private val usersRemoteDataSource: UsersRemoteDataSource
) {

    suspend fun getUsers() = usersRemoteDataSource.getUsers()
}