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


    companion object {
        fun newInstance() = WebviewFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWebviewBinding.inflate(inflater, container, false)

        val movieID = args.movieID
        viewModel.fetchMovie(movieID)
        Log.d(TAG, "movieID: $movieID")
        Log.d(TAG, "movie: ${viewModel.movie.value}")

        binding.apply {
            viewModel.movie.observe(viewLifecycleOwner) {
                webView.webViewClient = WebViewClient()
                if (it != null) {
                    if (it.homepage != null && it.homepage != "") {
                        webView.loadUrl(it.homepage)
                    }
                    else if (it.imdb_id != null && it.imdb_id != "") {
                        Log.d(TAG, "IMDB ID: ${it.imdb_id}")
                        webView.loadUrl(DEFAULT_IMDB_URL + it.imdb_id)
                    }
                    else {
                        webView.loadUrl(DEFAULT_GOOGLE_SEARCH_URL + it.original_title)
                    }
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}