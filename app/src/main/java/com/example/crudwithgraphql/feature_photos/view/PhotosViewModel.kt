package com.example.crudwithgraphql.feature_photos.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.crudwithgraphql.feature_photos.data.repo.PhotosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    photosRepository: PhotosRepository

) : ViewModel() {
    val photos = photosRepository.getPhotosPagingData().cachedIn(viewModelScope)

}