package com.apaul9.cinescope.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(movie: Movie)

    @Query("DELETE FROM movie_table")
    fun deleteAll()

    @Delete
    fun deleteMovie(movie: Movie)

    @Query("SELECT * FROM movie_table LIMIT 1")
    fun getAnyMovie(): Array<Movie>

    @Query("SELECT * FROM movie_table ORDER BY movie_id DESC")
    fun getAllMovie(): LiveData<List<Movie>>
}