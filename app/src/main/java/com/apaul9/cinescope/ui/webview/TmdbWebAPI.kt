package com.apaul9.cinescope.ui.webview


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbWebAPI {
    @GET("3/search/movie")
    fun getMovie(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): Call<TmdbWebResponse>
}
