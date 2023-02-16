package com.example.crudwithgraphql.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier


@Module
@InstallIn(SingletonComponent::class)
object AppDependencies {

    @NonCancellableScope
    @Provides
    fun providesNonCancellableScope(): CoroutineScope {
        val supervisorJob = SupervisorJob()
        return CoroutineScope(Dispatchers.IO + supervisorJob)
    }

}


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NonCancellableScope