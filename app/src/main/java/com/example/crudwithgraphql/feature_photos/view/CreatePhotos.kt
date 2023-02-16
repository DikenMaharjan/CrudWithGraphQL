package com.example.crudwithgraphql.feature_photos.view

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.crudwithgraphql.network.Resource

@Composable
fun CreatePhotos(
    modifier: Modifier = Modifier,
    viewModel: CreatePhotosViewModel = hiltViewModel(),
    popBackStack: () -> Unit,
) {
    val context = LocalContext.current
    LaunchedEffect(viewModel.createRequestState) {
        val state = viewModel.createRequestState
        if (state is Resource.Success) {
            context.showToast("Created Successfully")
            popBackStack()
        } else if (state is Resource.Failure) {
            context.showToast(state.errorMsg)
        }
    }
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = popBackStack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = " Back Icon"
                        )
                    }
                },
                title = {
                    Text(text = "Create Photo")
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            CreatePhotoForm(viewModel = viewModel)
            if (viewModel.createRequestState is Resource.Loading) {
                TouchConsumingDialog()
            }
        }
    }
}

private fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

@Composable
fun TouchConsumingDialog(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                }, indication = null, onClick = {}
            ),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun CreatePhotoForm(
    viewModel: CreatePhotosViewModel
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(24.dp)
    ) {
        TextInputs(
            value = viewModel.photoTitle,
            labelText = "Title",
            onValueChange = viewModel::onPhotoTitleChange,
            isError = viewModel.photoTitleError
        )
        TextInputs(
            value = viewModel.photoUrl,
            onValueChange = viewModel::onPhotoUrlChange,
            labelText = "Photo URL",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
            isError = viewModel.photoUrlError
        )

        Button(
            onClick = viewModel::createPhoto,
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.large)
                .padding(vertical = 32.dp)
        ) {
            Text(text = "Create")
        }
    }

}

@Composable
fun TextInputs(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isError: Boolean
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        label = {
            Text(text = labelText)
        },
        keyboardOptions = keyboardOptions,
        isError = isError
    )
}