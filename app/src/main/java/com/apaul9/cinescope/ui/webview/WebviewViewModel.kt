package com.apaul9.cinescope.ui.webview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apaul9.cinescope.BuildConfig
import com.apaul9.cinescope.CineScopeApp.Companion.DEFAULT_TMDB_SEARCH_URL

private const val TAG = "WebviewViewModel"

class WebviewViewModel : ViewModel() {


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

    fun fetchMovie(query: String) {
        val movieRequest: retrofit2.Call<TmdbWebResponse> =
            tmdbWebViewAPI.fetchMovie(query, BuildConfig.apikey)
        Log.d(TAG, "Executed? HELLO")

        movieRequest.enqueue(object : retrofit2.Callback<TmdbWebResponse> {

            override fun onFailure(call: retrofit2.Call<TmdbWebResponse>, t: Throwable) {
                Log.d(TAG, "Failed to get response.")
                Log.d(TAG, "Failed to get response.")
            }

            override fun onResponse(
                call: retrofit2.Call<TmdbWebResponse>,
                response: retrofit2.Response<TmdbWebResponse>
            ) {
                _movie.value = response.body()
                println(movie.value);
            }
        })
    }

}