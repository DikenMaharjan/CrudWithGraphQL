package com.example.crudwithgraphql.feature_photos.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crudwithgraphql.CreatePhotoMutation
import com.example.crudwithgraphql.UpdatePhotoMutation
import com.example.crudwithgraphql.feature_photos.data.repo.PhotosRepository
import com.example.crudwithgraphql.navigation.CreatePhotosRoute
import com.example.crudwithgraphql.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePhotosViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val photosRepository: PhotosRepository
) : ViewModel() {

    sealed class Intention(val intention: String) {
        object CreatePhoto : Intention("Create")
        class UpdatePhoto(val id: String) : Intention("Update")
    }

    var photoTitle: String by mutableStateOf(
        with(CreatePhotosRoute) {
            savedStateHandle.getTitle()
        }
    )
        private set

    var photoUrl: String by mutableStateOf(
        with(CreatePhotosRoute) {
            savedStateHandle.getUrl()
        }
    )
        private set

    val id: String? = with(CreatePhotosRoute) {
        savedStateHandle.getID()
    }

    val intention = if (id != null) (Intention.UpdatePhoto(id)) else Intention.CreatePhoto

    var photoTitleError by mutableStateOf(false)
        private set

    var photoUrlError by mutableStateOf(false)
        private set

    var createRequestState: Resource<CreatePhotoMutation.Data> by mutableStateOf(
        Resource.Idle
    )
    var updateRequestState: Resource<UpdatePhotoMutation.Data> by mutableStateOf(
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
                when (intention) {
                    Intention.CreatePhoto -> {
                        createRequestState = Resource.Loading
                        createRequestState = photosRepository.createPhoto(
                            photoTitle, photoUrl
                        )
                    }
                    is Intention.UpdatePhoto -> {
                        updateRequestState = Resource.Loading
                        updateRequestState = photosRepository.updatePhoto(
                            intention.id, photoTitle, photoUrl
                        )
                    }
                }
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

    fun shouldShowTouchConsumingDialog(): Boolean {
        return createRequestState is Resource.Loading || updateRequestState is Resource.Loading
    }


}