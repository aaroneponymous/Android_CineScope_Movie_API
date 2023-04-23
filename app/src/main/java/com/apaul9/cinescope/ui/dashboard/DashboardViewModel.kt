package com.apaul9.cinescope.ui.dashboard

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
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

class DashboardViewModel(app: Application) : AndroidViewModel(app) {

    companion object {
        private val tmdbAPI: TmdbAPI by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl(DEFAULT_TMDB_SEARCH_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return@lazy retrofit.create(TmdbAPI::class.java)
        }
    }

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    fun searchComplete(query: String, language: String) {
        _movies.value = emptyList()
        // Initial Search Call To Determine Total Pages
        val movieRequest: Call<TmdbResponse> = tmdbAPI.searchMovies(BuildConfig.apikey,language, query)
        movieRequest.enqueue(object : Callback<TmdbResponse> {
            override fun onFailure(call: Call<TmdbResponse>, t: Throwable) {
                Log.d(TAG, "Failed to get response.")
            }

            override fun onResponse(call: Call<TmdbResponse>, response: Response<TmdbResponse>) {
                val totalResults = response.body()?.total_results
                val totalPages = response.body()?.total_pages

                if (totalResults!!>=1) {
                    for (i in 1..totalPages!!) {
                        searchMovies(query, language, i)
                    }
                }
            }
        })
    }

    fun searchMovies(query: String, language: String, page: Int) {
        val movieRequest: Call<TmdbResponse> = tmdbAPI.searchMovies(BuildConfig.apikey, query, language, page)
        movieRequest.enqueue(object : Callback<TmdbResponse> {

            override fun onFailure(call: Call<TmdbResponse>, t: Throwable) {
                Log.d(TAG, "Failed to get response.")
            }

            override fun onResponse(call: Call<TmdbResponse>, response: Response<TmdbResponse>) {
                val newList: MutableList<Movie> = mutableListOf()
                response.body()?.results?.forEach {
                    Log.d(TAG, "MOVIE: $it")
                    newList.add(it)
                }
                // Append new list to existing list
                _movies.value = _movies.value?.plus(newList)
            }
        })
    }
}