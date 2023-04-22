package com.apaul9.cinescope.ui.mymovies

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apaul9.cinescope.R

class MyMoviesFragment : Fragment() {

    companion object {
        fun newInstance() = MyMoviesFragment()
    }

    private lateinit var viewModel: MyMoviesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_movies, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyMoviesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}