package com.apaul9.cinescope.ui.webview

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apaul9.cinescope.BuildConfig
import com.apaul9.cinescope.CineScopeApp.Companion.DEFAULT_TMDB_SEARCH_URL

private const val TAG = "WebviewViewModel"

class WebviewViewModel(app: Application) : AndroidViewModel(app){


    companion object {
        private val tmdbWebViewAPI: TmdbWebAPI by lazy {
            val retrofit = retrofit2.Retrofit.Builder()
                .baseUrl(DEFAULT_TMDB_SEARCH_URL)
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .build()
            return@lazy retrofit.create(TmdbWebAPI::class.java)
        }
    }

    private val _movie = MutableLiveData<TmdbWebResponse>()
    val movie: LiveData<TmdbWebResponse> = _movie
    private var lastFetchedMovie: TmdbWebResponse? = null

    fun getMovie(query: String?) {
        Log.d(TAG, "WEB VIEW MODEL : Query: $query")
        if (query == null) {
            if (lastFetchedMovie != null) {
                _movie.value = lastFetchedMovie!!
            }
            else {
                Log.d(TAG, "Last Fetched Movie: $lastFetchedMovie")
                Log.d(TAG, "Last Fetched Movie is null.")
            }
        }
        else {
            fetchMovie(query)
        }
    }

    fun fetchMovie(query: String) {
        val movieRequest: retrofit2.Call<TmdbWebResponse> =
            tmdbWebViewAPI.fetchMovie(query, BuildConfig.apikey)
        movieRequest.enqueue(object : retrofit2.Callback<TmdbWebResponse> {

            override fun onFailure(call: retrofit2.Call<TmdbWebResponse>, t: Throwable) {
                Log.d(TAG, "Failed to get response.")
            }

            override fun onResponse(
                call: retrofit2.Call<TmdbWebResponse>,
                response: retrofit2.Response<TmdbWebResponse>
            ) {
                lastFetchedMovie = response.body()
                Log.d(TAG, "Last Fetched Movie: $lastFetchedMovie")
                _movie.value = response.body()
                Log.d(TAG, "WEB VIEW MODEL : Response: ${response.body()}")
            }
        })
    }

}