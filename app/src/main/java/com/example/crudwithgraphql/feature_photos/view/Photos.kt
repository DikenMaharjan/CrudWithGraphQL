package com.example.crudwithgraphql.feature_photos.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import com.example.crudwithgraphql.feature_photos.data.repo.Photo

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Photos(
    modifier: Modifier = Modifier,
    viewModel: PhotosViewModel = hiltViewModel(),
    navigateToCreatePhotos: () -> Unit
) {
    val photos = viewModel.photos.collectAsLazyPagingItems()

    val isRefreshing = photos.loadState.refresh is LoadState.Loading

    val showPullRefreshIndicator = isRefreshing && photos.itemCount != 0
    val showLoadingIndicator = isRefreshing && photos.itemCount == 0

    val refreshState = rememberPullRefreshState(
        refreshing = showPullRefreshIndicator, onRefresh = photos::refresh
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(text = "Photos") })
        }, floatingActionButton = {
            FloatingActionButton(onClick = navigateToCreatePhotos) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Photos Icon")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            PhotosList(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .pullRefresh(refreshState),
                photos = photos,
                showLoadingIndicator = showLoadingIndicator,
                selectForDelete = viewModel::selectForDelete
            )
            PullRefreshIndicator(
                refreshing = showPullRefreshIndicator,
                state = refreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
    val selectionForDeletion = viewModel.selectionForDeletion
    if (selectionForDeletion is PhotosViewModel.SelectionForDeletion.Selected) {
        AlertDialog(
            onDismissRequest = viewModel::deselectDeleteSelection,
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deletePhoto(selectionForDeletion.photo)
                        viewModel.deselectDeleteSelection()
                    }
                ) { Text("Yes") }
            },
            dismissButton = {
                TextButton(onClick = viewModel::deselectDeleteSelection) {
                    Text("Cancel")
                }
            },
            title = { Text("Are you sure?") },
            text = {
                Text(
                    text = "Do you want to delete ${selectionForDeletion.photo.title}",
                    fontSize = 14.sp,
                    lineHeight = 18.sp
                )
            },
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhotosList(
    modifier: Modifier = Modifier,
    photos: LazyPagingItems<Photo>,
    showLoadingIndicator: Boolean,
    selectForDelete: (Photo) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        if (showLoadingIndicator) {
            item {
                Box(
                    modifier = Modifier.fillParentMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        } else {
            items(photos) { photo ->
                photo?.let {
                    PhotoItem(
                        modifier = Modifier
                            .padding(12.dp)
                            .animateItemPlacement(),
                        photo = photo,
                        selectForDelete = selectForDelete
                    )
                }
                Divider(
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                if (photos.loadState.append is LoadState.Loading) {
                    Box(modifier = Modifier.fillParentMaxWidth()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }
}

@Composable
fun PhotoItem(
    modifier: Modifier = Modifier,
    photo: Photo,
    selectForDelete: (Photo) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colors.surface)
    ) {
        PhotoItemHeader(photo = photo, selectForDelete = selectForDelete)
        AsyncImage(
            model = photo.url,
            contentDescription = "Photo",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            placeholder = ColorPainter(Color.Gray)
        )
    }
}


@Composable
fun PhotoItemHeader(
    photo: Photo,
    selectForDelete: (Photo) -> Unit
) {
    var menuOpened by remember {
        mutableStateOf(false)
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = photo.title,
            modifier = Modifier
                .padding(vertical = 12.dp)
                .weight(1f),
            style = MaterialTheme.typography.subtitle1
        )
        Box {
            Icon(
                imageVector = Icons.Default.MoreVert,
                "More Icon",
                modifier = Modifier.clickable {
                    menuOpened = true
                }
            )
            DropdownMenu(expanded = menuOpened, onDismissRequest = { menuOpened = false }) {
                DropdownMenuItem(onClick = {}) {
                    Text(text = "Update")
                }
                DropdownMenuItem(onClick = {
                    selectForDelete(photo)
                    menuOpened = false
                }) {
                    Text(text = "Delete")
                }
            }
        }
    }

}
