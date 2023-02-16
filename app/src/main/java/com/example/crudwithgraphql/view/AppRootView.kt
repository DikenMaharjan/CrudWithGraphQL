package com.example.crudwithgraphql.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
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
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import com.example.crudwithgraphql.MainActivityViewModel
import com.example.crudwithgraphql.data.photo.repo.PhotosRepository

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppRootView(
    modifier: Modifier = Modifier,
    viewModel: MainActivityViewModel
) {
    val photos = viewModel.photos.collectAsLazyPagingItems()

    val isRefreshing = photos.loadState.refresh is LoadState.Loading

    val showPullRefreshIndicator = isRefreshing && photos.itemCount != 0
    val showLoadingIndicator = isRefreshing && photos.itemCount == 0

    val refreshState = rememberPullRefreshState(
        refreshing = showPullRefreshIndicator,
        onRefresh = photos::refresh
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(text = "Photos") })
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            PhotosList(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .pullRefresh(refreshState),
                photos = photos,
                showLoadingIndicator = showLoadingIndicator
            )
            PullRefreshIndicator(
                refreshing = showPullRefreshIndicator,
                state = refreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
fun PhotosList(
    modifier: Modifier = Modifier,
    photos: LazyPagingItems<PhotosRepository.Photo>,
    showLoadingIndicator: Boolean
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
                        modifier = Modifier.padding(24.dp),
                        photo = photo
                    )
                }
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
    photo: PhotosRepository.Photo
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colors.surface)
    ) {
        Text(
            text = photo.title,
            modifier = Modifier.padding(vertical = 8.dp),
            style = MaterialTheme.typography.subtitle1
        )
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