package com.apaul9.cinescope.ui.mymovies

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.apaul9.cinescope.database.Movie
import com.apaul9.cinescope.database.MovieRepository

class MyMoviesViewModel(app: Application) : AndroidViewModel(app){



    init {
        MovieRepository.initialize(app)
    }

    private val movieRepository = MovieRepository.get()
    val movies = movieRepository.getAllMovies() // LiveData

    fun insertMovie(movie: Movie) {
        movieRepository.insertMovie(movie)
    }

    fun deleteMovie(movie: Movie) {
        movieRepository.deleteMovie(movie)
    }

    fun deleteAllMovies() {
        movieRepository.deleteAllMovies()
    }

}