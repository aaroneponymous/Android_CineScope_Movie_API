package com.apaul9.cinescope

import android.app.Application

class CineScopeApp: Application() {

    companion object {
        private lateinit var instance : CineScopeApp
        const val DEFAULT_TMDB_SEARCH_URL = "https://api.themoviedb.org/"
        const val DEFAULT_TMDB_IMAGE_URL = "https://image.tmdb.org/t/p/w500"
        const val DEFAULT_IMDB_URL = "https://www.imdb.com/title/"
        const val DEFAULT_GOOGLE_SEARCH_URL = "https://www.google.com/search?q="
    }

    override fun onCreate() {
        instance = this
        super.onCreate()
    }
}