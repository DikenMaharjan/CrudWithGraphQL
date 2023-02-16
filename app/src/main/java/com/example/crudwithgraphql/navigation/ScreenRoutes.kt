package com.example.crudwithgraphql.navigation

import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavType
import androidx.navigation.navArgument

abstract class ScreenRoutes(val routePrefix: String) {
    abstract val route: String
}


object PhotosRoute : ScreenRoutes("photos_route") {
    override val route: String
        get() = routePrefix
}

object CreatePhotosRoute : ScreenRoutes("create_photos_route") {
    override val route: String
        get() = "$routePrefix?$ID_KEY={$ID_KEY}&$TITLE_KEY={$TITLE_KEY}&$URL_KEY={$URL_KEY}"

    private val urlNavType = object : NavType<String>(true) {
        override fun get(
            bundle: Bundle,
            key: String
        ): String? {
            return bundle.getString(key)
        }

        override fun parseValue(value: String): String {
            return Uri.decode(value)
        }

        override fun put(
            bundle: Bundle,
            key: String,
            value: String
        ) {
            bundle.putString(key, value)
        }
    }

    fun createNavigationRoute(
        id: String,
        title: String,
        url: String
    ): String {
        return "$routePrefix?$ID_KEY=$id&$TITLE_KEY=$title&$URL_KEY=${Uri.encode(url)}"
    }

    fun createNavArguments() = listOf(
        navArgument(name = TITLE_KEY) {
            type = NavType.StringType
            nullable = true
        },
        navArgument(name = URL_KEY) {
            type = urlNavType
            nullable = true
        },
        navArgument(name = ID_KEY) {
            type = NavType.StringType
            nullable = true
        }
    )

    fun SavedStateHandle.getTitle() = this.get<String>(TITLE_KEY) ?: ""
    fun SavedStateHandle.getUrl() = this.get<String>(URL_KEY) ?: ""
    fun SavedStateHandle.getID() = this.get<String>(ID_KEY)

    private const val TITLE_KEY = "TitleKey"
    private const val URL_KEY = "UrlKey"
    private const val ID_KEY = "IdKey"

}