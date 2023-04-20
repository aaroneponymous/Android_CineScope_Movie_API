package com.apaul9.cinescope.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apaul9.cinescope.BuildConfig
import com.apaul9.cinescope.CineScopeApp.Companion.DEFAULT_TMDB_SEARCH_URL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "DashboardViewModel"

class DashboardViewModel : ViewModel() {

    companion object {
        private val tmdpAPI: TmdpAPI by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl(DEFAULT_TMDB_SEARCH_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return@lazy retrofit.create(TmdpAPI::class.java)
        }
    }

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    fun searchMovies(query: String) {
        _movies.value = emptyList() // clear existing results
        val movieRequest: Call<TmdpResponse> = tmdpAPI.searchMovies(BuildConfig.apikey, query)
        movieRequest.enqueue(object : Callback<TmdpResponse> {
            override fun onFailure(call: Call<TmdpResponse>, t: Throwable) {
                Log.d(TAG, "Failed to get response.")
            }

            override fun onResponse(call: Call<TmdpResponse>, response: Response<TmdpResponse>) {
                _movies.value = response.body()?.results
            }
        })
    }
}