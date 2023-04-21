package com.apaul9.cinescope.ui.webview


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbWebAPI {
    @GET("3/movie/{id}")
    fun fetchMovie(
        @Path("id") id: String,
        @Query("api_key") apiKey: String

    ): Call<TmdbWebResponse>
}

