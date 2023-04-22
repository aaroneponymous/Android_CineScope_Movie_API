package com.apaul9.cinescope.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity(tableName = "movie_table")
data class Movie (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "movie_id")
    var id: Long = 0L,

    @ColumnInfo(name = "movie_title")
    var movieTitle: String = "",

    @ColumnInfo(name = "date_released")
    var releaseDate: String = "",

    @ColumnInfo(name = "movie_overview")
    var movieOverview: String = "",

    @ColumnInfo(name = "movie_poster")
    var moviePoster: String = "",

    @ColumnInfo(name = "movie_rating")
    var movieRating: String = ""
)