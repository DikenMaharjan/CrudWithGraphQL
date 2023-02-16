package com.example.crudwithgraphql.navigation

abstract class ScreenRoutes(val route: String)


object PhotosRoute : ScreenRoutes("photos_route")
object CreatePhotosRoute : ScreenRoutes("create_photos_route")