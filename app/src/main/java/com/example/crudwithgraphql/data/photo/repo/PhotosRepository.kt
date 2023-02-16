package com.example.crudwithgraphql.data.photo.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.apollographql.apollo3.ApolloClient
import com.example.crudwithgraphql.data.photo.PhotosPagingSource
import com.example.crudwithgraphql.network.SafeApiCall
import com.example.crudwithgraphql.utils.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotosRepository @Inject constructor(
    private val apolloClient: ApolloClient,
    private val safeApiCall: SafeApiCall
) {
    data class Photo(
        val title: String,
        val url: String,
        val id: String,
        val thumbnailUrl: String
    )

    fun getPhotosPagingData(
        pageSize: Int = Constants.DEFAULT_PAGE_LIMIT
    ): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize
            ),
            initialKey = null,
            pagingSourceFactory = {
                PhotosPagingSource(
                    apolloClient = apolloClient,
                    safeApiCall = safeApiCall,
                    pageSize = pageSize
                )
            }
        ).flow
    }

}