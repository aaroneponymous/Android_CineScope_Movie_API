package com.apaul9.cinescope.ui.dashboard

import android.app.Application
import android.content.Context
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
import java.util.*

private const val TAG = "DashboardViewModel"

class DashboardViewModel(app: Application) : AndroidViewModel(app) {

    private val sharedPreferences = app.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    private val isSortVoteHL = sharedPreferences.getBoolean("sortVoteHL", false)
    private val isSortVoteLH = sharedPreferences.getBoolean("sortVoteLH", false)
    private val isLatest = sharedPreferences.getBoolean("latest", false)
    private val isOldest = sharedPreferences.getBoolean("oldest", false)
    private val isSafe = sharedPreferences.getBoolean("safe", false)


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

    fun searchComplete(query: String) {
        Log.d(TAG, "isSortVoteHL: $isSortVoteHL")
        Log.d(TAG, "isSortVoteLH: $isSortVoteLH")
        Log.d(TAG, "isLatest: $isLatest")
        Log.d(TAG, "isOldest: $isOldest")
        Log.d(TAG, "isSafe: $isSafe")


        _movies.value = emptyList()
        // Initial Search Call To Determine Total Pages
        val movieRequest: Call<TmdbResponse> = tmdbAPI.searchMovies(BuildConfig.apikey, query)
        movieRequest.enqueue(object : Callback<TmdbResponse> {
            override fun onFailure(call: Call<TmdbResponse>, t: Throwable) {
                Log.d(TAG, "Failed to get response.")
            }

            override fun onResponse(call: Call<TmdbResponse>, response: Response<TmdbResponse>) {
                val totalResults = response.body()?.total_results
                val totalPages = response.body()?.total_pages

                if (totalResults!!>=1) {
                    for (i in 1..totalPages!!) {
                        searchMovies(query, Locale.getDefault().language, i)


                    }
                }


            }
        })
    }

    fun searchMovies(query: String, language: String, page: Int) {
        val movieRequest: Call<TmdbResponse> = tmdbAPI.searchMovies(BuildConfig.apikey, query, language, page)
        val sortByVoteHighToLow = isSortVoteHL
        val sortByVoteLowToHigh = isSortVoteLH
        val sortByReleaseDateNewestToOldest = isLatest
        val sortByReleaseDateOldestToNewest = isOldest
        val isSafeSearchOn = isSafe
        val newList: MutableList<Movie> = mutableListOf()
        movieRequest.enqueue(object : Callback<TmdbResponse> {

            override fun onFailure(call: Call<TmdbResponse>, t: Throwable) {
                Log.d(TAG, "Failed to get response.")
            }

            override fun onResponse(call: Call<TmdbResponse>, response: Response<TmdbResponse>) {
                response.body()?.results?.forEach {
                    Log.d(TAG, "DASHBOARD VIEWMODEL: MOVIE: $it")
                    newList.add(it)
                }


                _movies.value = _movies.value?.plus(newList)

                if (sortByVoteHighToLow) {
                    _movies.value = _movies.value?.sortedByDescending { it.vote_average }
                } else if (sortByVoteLowToHigh) {
                    _movies.value = _movies.value?.sortedBy { it.vote_average }
                }

                if (sortByReleaseDateNewestToOldest) {
                    _movies.value = _movies.value?.sortedByDescending { it.release_date }
                } else if (sortByReleaseDateOldestToNewest) {
                    _movies.value = _movies.value?.sortedBy { it.release_date }
                }
                if (isSafeSearchOn) {
                    _movies.value = _movies.value?.filter { !it.adult }
                }
            }

        })
    }
}