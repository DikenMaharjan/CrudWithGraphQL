package com.example.crudwithgraphql.feature_photos.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.apollographql.apollo3.ApolloClient
import com.example.crudwithgraphql.GetPhotosQuery
import com.example.crudwithgraphql.feature_photos.data.repo.Photo
import com.example.crudwithgraphql.network.Resource
import com.example.crudwithgraphql.network.SafeApiCall

class PhotosPagingSource(
    private val apolloClient: ApolloClient,
    private val safeApiCall: SafeApiCall,
    private val pageSize: Int
) : PagingSource<PhotosPagingSource.Key, Photo>() {
    data class Key(
        val page: Int,
        val limit: Int
    )

    override fun getRefreshKey(state: PagingState<Key, Photo>): Key? {
        return null
    }

    override suspend fun load(params: LoadParams<Key>): LoadResult<Key, Photo> {
        val refreshKey = params.key ?: Key(
            page = 1,
            limit = pageSize
        )

        val response = safeApiCall.execute {
            apolloClient.query(
                GetPhotosQuery(
                    page = refreshKey.page,
                    limit = pageSize
                )
            )
        }
        return when (response) {
            is Resource.Failure -> {
                LoadResult.Error(
                    Throwable(response.errorMsg)
                )
            }
            is Resource.Success -> {
                LoadResult.Page(
                    data = response.data.toPhotos(),
                    prevKey = response.data.toPrevKey(),
                    nextKey = response.data.toNextKey()
                )
            }
            Resource.Idle, Resource.Loading -> error("Invalid States")
        }
    }


    private fun GetPhotosQuery.Data.toNextKey(): Key? = this.photos?.links?.next?.let { next ->
        Key(
            page = next.page,
            limit = next.limit
        )
    }


    private fun GetPhotosQuery.Data.toPrevKey(): Key? = this.photos?.links?.prev?.let { prev ->
        Key(
            page = prev.page,
            limit = prev.limit
        )
    }

    private fun GetPhotosQuery.Data.toPhotos(): List<Photo> =
        this.photos?.data?.mapNotNull {
            try {
                Photo(
                    title = it?.title!!,
                    url = it.url!!,
                    id = it.id!!,
                    thumbnailUrl = it.thumbnailUrl!!
                )
            } catch (e: java.lang.NullPointerException) {
                null
            }
        } ?: emptyList()
}


