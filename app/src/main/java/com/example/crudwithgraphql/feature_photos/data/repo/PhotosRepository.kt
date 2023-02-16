package com.example.crudwithgraphql.feature_photos.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.apollographql.apollo3.ApolloClient
import com.example.crudwithgraphql.CreatePhotoMutation
import com.example.crudwithgraphql.DeletePhotoMutation
import com.example.crudwithgraphql.di.NonCancellableScope
import com.example.crudwithgraphql.feature_photos.data.PhotosPagingSource
import com.example.crudwithgraphql.network.Resource
import com.example.crudwithgraphql.network.SafeApiCall
import com.example.crudwithgraphql.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotosRepository @Inject constructor(
    private val apolloClient: ApolloClient,
    private val safeApiCall: SafeApiCall,
    @NonCancellableScope private val nonCancellableScope: CoroutineScope
) {

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

    suspend fun createPhoto(
        photoTitle: String,
        photoUrl: String
    ): Resource<CreatePhotoMutation.Data> {
        return safeApiCall.execute {
            apolloClient.mutation(
                CreatePhotoMutation(
                    title = photoTitle, url = photoUrl, thumbnailUrl = photoUrl
                )
            )
        }
    }

    fun deletePhoto(photo: Photo) {
        nonCancellableScope.launch {
            safeApiCall.execute {
                apolloClient.mutation(
                    DeletePhotoMutation(
                        photo.id
                    )
                )
            }
        }
    }
}

data class Photo(
    val title: String,
    val url: String,
    val id: String,
    val thumbnailUrl: String
)

