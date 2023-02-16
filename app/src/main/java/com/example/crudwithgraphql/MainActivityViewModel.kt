package com.example.crudwithgraphql

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.crudwithgraphql.data.photo.repo.PhotosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val TAG = "MainActivityViewModel"

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    photosRepository: PhotosRepository
) : ViewModel() {

    val photos = photosRepository.getPhotosPagingData().cachedIn(viewModelScope)
}