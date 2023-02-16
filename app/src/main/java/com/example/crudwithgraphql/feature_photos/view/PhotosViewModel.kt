package com.example.crudwithgraphql.feature_photos.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.filter
import com.example.crudwithgraphql.feature_photos.data.repo.Photo
import com.example.crudwithgraphql.feature_photos.data.repo.PhotosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

private const val TAG = "PhotosViewModel"

@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val photosRepository: PhotosRepository
) : ViewModel() {
    private val deletedIDs = MutableStateFlow(setOf<String>())

    val photos = photosRepository
        .getPhotosPagingData()
        .cachedIn(viewModelScope)
        .combine(deletedIDs) { pagingData, deletedIDs ->
            pagingData.filter {
                it.id !in deletedIDs
            }
        }


    sealed class SelectionForDeletion {
        object NotSelected : SelectionForDeletion()
        class Selected(val photo: Photo) : SelectionForDeletion()
    }


    var selectionForDeletion: SelectionForDeletion by mutableStateOf(SelectionForDeletion.NotSelected)
        private set

    fun selectForDelete(photo: Photo) {
        selectionForDeletion = SelectionForDeletion.Selected(photo)
    }

    fun deselectDeleteSelection() {
        selectionForDeletion = SelectionForDeletion.NotSelected
    }

    fun deletePhoto(photo: Photo) {
        photosRepository.deletePhoto(photo)
        deletedIDs.value += photo.id
    }

}