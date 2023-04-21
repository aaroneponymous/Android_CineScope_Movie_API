package com.apaul9.cinescope.ui.dashboard


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbAPI {
    @GET("3/search/movie")
    fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): Call<TmdbResponse>
}
