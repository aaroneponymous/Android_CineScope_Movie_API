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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apaul9.cinescope.CineScopeApp
import com.apaul9.cinescope.R
import com.apaul9.cinescope.databinding.FragmentDashboardBinding
import com.squareup.picasso.Picasso

private const val TAG = "DashboardFragment"

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val movieAdapter = MovieAdapter()


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
                    recyclerView.run {
                        layoutManager = LinearLayoutManager(context)
                        adapter = movieAdapter
                    }
                }
            }

            searchMovies.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
                if (event != null && event.action == MotionEvent.ACTION_DOWN) {
                    val query = searchMovies.text.toString()
                    if (query.isNotEmpty()) {
                        viewModel.searchComplete(query)
                    }
                }
                false
            })

            searchButton.setOnClickListener {
                val query = searchMovies.text.toString()
                if (query.isNotEmpty()) {
                    viewModel.searchComplete(query)
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.movies.observe(viewLifecycleOwner) {
            movieAdapter.updateMovies(it)
        }

        val itemTouchHelperCallback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ) = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    if (direction == ItemTouchHelper.LEFT) {
                        // Send the index location of the item swiped to the left
                        val indexID = movieAdapter.movie[viewHolder.adapterPosition]
                        // Navigate to the MyMoviesFragment and pass all the arguments
                        val action = DashboardFragmentDirections.actionNavigationDashboardToMyMoviesFragment(indexID.id.toString(),
                            indexID.title, indexID.release_date.toString(), indexID.overview, indexID.vote_average.toString(),
                            indexID.original_language, indexID.poster_path.toString())
                        binding.root.findNavController().navigate(action)

                    } else if (direction == ItemTouchHelper.RIGHT) {

                        val movieId = movieAdapter.movie[viewHolder.adapterPosition].id
                        val action = DashboardFragmentDirections.actionNavigationDashboardToWebviewFragment(movieId.toString())
                        binding.root.findNavController().navigate(action)
                    }
                }
            }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class WordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var movie: Movie
        private val movieTitle = view.findViewById<TextView>(R.id.movieTitle)
        private val moviePoster = view.findViewById<ImageView>(R.id.moviePoster)
        private val movieOverview = view.findViewById<TextView>(R.id.overviewMovie)
        private val dateReleased = view.findViewById<TextView>(R.id.dateReleased)
        private val votingAverage = view.findViewById<TextView>(R.id.vote)
        private val languageOriginal = view.findViewById<TextView>(R.id.language)

        fun bind(movie: Movie) {
            this.movie = movie
            movieTitle.text = movie.title
            dateReleased.text = movie.release_date
            movieOverview.text = movie.overview
            votingAverage.text = movie.vote_average.toString() + "/10"
            languageOriginal.text = movie.original_language

            if (movie.poster_path != null) {
                var posterLink = CineScopeApp.DEFAULT_TMDB_IMAGE_URL + movie.poster_path
                Picasso.get().load(posterLink).into(moviePoster)
            }
        }
    }

    private inner class MovieAdapter : RecyclerView.Adapter<WordViewHolder>() {
        var movie: List<Movie> = emptyList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
            val view = layoutInflater.inflate(R.layout.recycler_card, parent, false)
            return WordViewHolder(view)
        }

        override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
            return holder.bind(movie[position])
        }

        override fun getItemCount() = movie.size

        fun updateMovies(newMovies: List<Movie>) {
            this.movie = newMovies
            notifyDataSetChanged()
        }

        fun getMovieAtPosition(position: Int): Movie {
            return movie[position]
        }
    }

}