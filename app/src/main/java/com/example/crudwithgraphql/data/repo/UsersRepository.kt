package com.example.crudwithgraphql.data.repo

import com.example.crudwithgraphql.data.datasource.RemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "UsersRepository"

@Singleton
class UsersRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {

    suspend fun getUsers() = remoteDataSource.getUsers()
}