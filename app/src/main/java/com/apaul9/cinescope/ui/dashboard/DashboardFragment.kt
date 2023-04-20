package com.apaul9.cinescope.ui.dashboard

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.util.Log.*
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.apaul9.cinescope.R
import com.apaul9.cinescope.databinding.FragmentDashboardBinding

private const val TAG = "DashboardFragment"

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DashboardViewModel by lazy {
        ViewModelProvider(this).get(DashboardViewModel::class.java)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        binding.apply {
            viewModel.movies.observe(viewLifecycleOwner) { movies ->

                if (movies.isNotEmpty()) {
                    movies.forEach {
                        Log.d(TAG, "Movie: $it")
                    }


                }
                else {
                    Toast.makeText(context, R.string.no_movies_found, Toast.LENGTH_SHORT).show()
                }
            }

            searchButton.setOnClickListener {
                val query = searchMovies.text.toString()
                if (query.isNotEmpty()) {
                    Log.d(TAG, "Movie To Search: $query")
                    viewModel.searchMovies(query)
                }
            }

        }

        return binding.root
    }


    companion object {
        fun newInstance() = DashboardFragment()
    }

}