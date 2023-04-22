package com.apaul9.cinescope.ui.webview

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.apaul9.cinescope.CineScopeApp.Companion.DEFAULT_GOOGLE_SEARCH_URL
import com.apaul9.cinescope.CineScopeApp.Companion.DEFAULT_IMDB_URL
import com.apaul9.cinescope.R
import com.apaul9.cinescope.databinding.FragmentWebviewBinding

private const val TAG = "WebViewFragment"

class WebviewFragment : Fragment(R.layout.fragment_webview) {


    val args: WebviewFragmentArgs by navArgs()
    private var _binding: FragmentWebviewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WebviewViewModel by lazy {
        ViewModelProvider(this).get(WebviewViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWebviewBinding.inflate(inflater, container, false)

        val movieID = args.movieID
        if (!(movieID.equals("-1"))) {
            movieID?.let { viewModel.fetchMovie(it) }
            Log.d(TAG, "movieID: $movieID")
        }

        binding.apply {
            viewModel.movie.observe(viewLifecycleOwner) {
                webView.webViewClient = WebViewClient()
                if (it != null) {
                    if (it.homepage != null && it.homepage != "") {
                        webView.loadUrl(it.homepage)
                        Log.d(TAG, "Homepage: ${it.homepage}")
                    }
                    else if (it.imdb_id != null && it.imdb_id != "") {
                        Log.d(TAG, "IMDB ID: ${it.imdb_id}")
                        webView.loadUrl(DEFAULT_IMDB_URL + it.imdb_id)
                    }
                    else {
                        webView.loadUrl(DEFAULT_GOOGLE_SEARCH_URL + it.original_title)
                        Log.d(TAG, "Google Search: ${DEFAULT_GOOGLE_SEARCH_URL + it.original_title}")
                    }
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val webView = binding.webView

        viewModel.movie.observe(viewLifecycleOwner) {
            webView.webViewClient = WebViewClient()
            if (it != null) {
                if (it.homepage != null && it.homepage != "") {
                    webView.loadUrl(it.homepage)
                    Log.d(TAG, "Homepage: ${it.homepage}")
                }
                else if (it.imdb_id != null && it.imdb_id != "") {
                    Log.d(TAG, "IMDB ID: ${it.imdb_id}")
                    webView.loadUrl(DEFAULT_IMDB_URL + it.imdb_id)
                }
                else {
                    webView.loadUrl(DEFAULT_GOOGLE_SEARCH_URL + it.original_title)
                    Log.d(TAG, "Google Search: ${DEFAULT_GOOGLE_SEARCH_URL + it.original_title}")
                }
            }
        }


    }

}