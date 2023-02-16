package com.example.crudwithgraphql.data.user.repo

import com.example.crudwithgraphql.data.user.datasource.UsersRemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersRepository @Inject constructor(
    private val usersRemoteDataSource: UsersRemoteDataSource
) {

    suspend fun getUsers() = usersRemoteDataSource.getUsers()
}