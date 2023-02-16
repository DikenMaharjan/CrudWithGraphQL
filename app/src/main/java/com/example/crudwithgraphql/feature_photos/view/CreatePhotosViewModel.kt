package com.example.crudwithgraphql.feature_photos.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crudwithgraphql.CreatePhotoMutation
import com.example.crudwithgraphql.feature_photos.data.repo.PhotosRepository
import com.example.crudwithgraphql.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePhotosViewModel @Inject constructor(
    private val photosRepository: PhotosRepository
) : ViewModel() {

    var photoTitle: String by mutableStateOf("")
        private set

    var photoUrl: String by mutableStateOf("")
        private set

    var photoTitleError by mutableStateOf(false)
        private set

    var photoUrlError by mutableStateOf(false)
        private set

    var createRequestState: Resource<CreatePhotoMutation.Data> by mutableStateOf(
        Resource.Idle
    )

    fun onPhotoTitleChange(text: String) {
        photoTitleError = false
        photoTitle = text
    }

    fun onPhotoUrlChange(text: String) {
        photoUrlError = false
        photoUrl = text
    }

    fun createPhoto() {
        viewModelScope.launch {
            if (isValid()) {
                createRequestState = Resource.Loading
                createRequestState = photosRepository.createPhoto(
                    photoTitle, photoUrl
                )
            }
        }
    }

    private fun isValid(): Boolean {
        // todo do some validations and show errors in UI

        var valid = true

        if (photoTitle.isBlank()) {
            photoTitleError = true
            valid = false
        }

        if (photoUrl.isBlank()) {
            photoUrlError = true
            valid = false
        }

        return valid
    }


}