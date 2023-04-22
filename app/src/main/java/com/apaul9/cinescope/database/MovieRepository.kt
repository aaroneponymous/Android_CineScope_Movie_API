package com.apaul9.cinescope.database


import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import java.util.concurrent.Executors

class MovieRepository private constructor(context: Context) {

    companion object {
        private var INSTANCE: MovieRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = MovieRepository(context)
            }
        }
        fun get(): MovieRepository {
            return INSTANCE
                ?: throw IllegalStateException("Movie Repository must be initialized.")
        }
    }

    private val database: MovieDatabase= Room.databaseBuilder(//make sure its okay
        context.applicationContext,
        MovieDatabase::class.java,
        "movie_database"
    ).build()

    private val Dao = database.movieDAO()
    private val executor = Executors.newSingleThreadExecutor()

    fun getAllMovies(): LiveData<List<Movie>> = Dao.getAllMovie()

    fun insertMovie(movie: Movie) {
        executor.execute {
            Dao.insert(movie)
        }
    }

    fun deleteMovie(movie: Movie) {
        executor.execute {
            Dao.deleteMovie(movie)
        }
    }

    fun deleteAllMovies() {
        executor.execute {
            Dao.deleteAll()
        }
    }


}