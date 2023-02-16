package com.example.crudwithgraphql

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.crudwithgraphql.feature_photos.view.CreatePhotos
import com.example.crudwithgraphql.navigation.CreatePhotosRoute
import com.example.crudwithgraphql.navigation.PhotosRoute
import com.example.crudwithgraphql.ui.theme.CrudWithGraphQLTheme
import com.example.crudwithgraphql.feature_photos.view.Photos
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            CrudWithGraphQLTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = PhotosRoute.route
                    ) {
                        composable(
                            route = PhotosRoute.route
                        ) {
                            Photos(
                                navigateToCreatePhotos = {
                                    navController.navigate(
                                        CreatePhotosRoute.route
                                    )
                                }
                            )
                        }
                        composable(
                            route = CreatePhotosRoute.route
                        ) {
                            CreatePhotos(
                                popBackStack = navController::popBackStack
                            )
                        }
                    }
                }
            }
        }
    }
}


