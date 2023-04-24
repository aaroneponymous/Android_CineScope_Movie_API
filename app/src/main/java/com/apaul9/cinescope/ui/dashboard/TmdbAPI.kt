package com.apaul9.cinescope.ui.dashboard


import com.apaul9.cinescope.ui.webview.TmdbWebResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbAPI {
    @GET("3/search/movie")
    fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Call<TmdbResponse>

}
