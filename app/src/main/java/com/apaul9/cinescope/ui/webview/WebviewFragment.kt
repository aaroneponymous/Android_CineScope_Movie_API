package com.apaul9.cinescope.ui.webview

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apaul9.cinescope.R

class WebviewFragment : Fragment(R.layout.fragment_webview) {

    companion object {
        fun newInstance() = WebviewFragment()
    }

    private lateinit var viewModel: WebviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_webview, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WebviewViewModel::class.java)
        // TODO: Use the ViewModel
    }

}