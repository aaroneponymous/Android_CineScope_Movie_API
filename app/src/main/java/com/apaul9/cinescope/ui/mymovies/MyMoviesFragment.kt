package com.apaul9.cinescope.ui.mymovies

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues.TAG
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apaul9.cinescope.CineScopeApp
import com.apaul9.cinescope.R
import com.apaul9.cinescope.databinding.FragmentMyMoviesBinding
import com.apaul9.cinescope.ui.dashboard.DashboardViewModel
import com.squareup.picasso.Picasso

const val TAG = "MyMoviesFragment"

class MyMoviesFragment : Fragment(R.layout.fragment_my_movies) {

    val args: MyMoviesFragmentArgs by navArgs()
    private lateinit var viewModel: MyMoviesViewModel
    private var _binding: FragmentMyMoviesBinding? = null
    private val binding get() = _binding!!
    private val movieAdapter = MovieAdapter()
    private var newMovie = com.apaul9.cinescope.database.Movie()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MyMoviesViewModel::class.java)
        if (args.movieID != null) {
            newMovie.id = args.movieID.toLong()
            newMovie.movieTitle = args.movieTitle
            newMovie.moviePoster = args.moviePoster
            newMovie.movieOverview = args.movieOverView
            newMovie.releaseDate = args.movieReleaseDate
            newMovie.movieLanguage = args.movieLanguage
            newMovie.movieRating = args.movieVoteAverage
            viewModel.insertMovie(newMovie)
        }
    }




    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyMoviesBinding.inflate(inflater, container, false)



        binding.apply {
            viewModel.movies.observe(viewLifecycleOwner) { movies ->
                if (movies.isNotEmpty()) {
                    recyclerMyMovies.run {
                        layoutManager = LinearLayoutManager(context)
                        adapter = movieAdapter
                    }
                }
            }
        }
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.movies.observe(viewLifecycleOwner) {
            movieAdapter.updateMovies(it)
        }


        /*binding.recyclerMyMovies.apply {
            adapter = movieAdapter
            setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    val timer = object : CountDownTimer(5000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {}

                        override fun onFinish() {
                            // handle long tap completed here
                            val thisMovie =
                                movieAdapter.getMovieAtPosition((layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition())
                            itemLongTapped(thisMovie)
                        }
                    }
                    timer.start()
                    true
                } else {
                    false
                }
            }
        }*/
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
                        val thisMovie = movieAdapter.getMovieAtPosition(viewHolder.adapterPosition)
                        itemDeletedAlert(movie = thisMovie)



                    } else if (direction == ItemTouchHelper.RIGHT) {
                        val movieID = movieAdapter.getMovieAtPosition(viewHolder.adapterPosition).id.toString()
                        val action = MyMoviesFragmentDirections.actionMyMoviesFragmentToNavigationWebview(movieID)
                        view.findNavController().navigate(action)
                    }
                }
            }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerMyMovies)
    }

    fun itemDeletedAlert(movie: com.apaul9.cinescope.database.Movie) {

        val msg = resources.getString(R.string.delete_Alert) + movie.movieTitle + "?"
        val builder = AlertDialog.Builder(context)
        with(builder) {
            setMessage(msg)
            // If the user clicks the "Delete" button, delete the movie
            setPositiveButton(R.string.delete) { _, _ ->
                viewModel.deleteMovie(movie)
            }
            // If the user clicks the "Cancel" button, cancel the dialog and refresh the adapter
            setNegativeButton(R.string.cancel) { _, _ ->
                movieAdapter.notifyDataSetChanged()
            }

            show()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun itemLongTapped(movie: com.apaul9.cinescope.database.Movie) {
        // delete all movies Alert
        val msg = "Delete all movies?"
        val builder = AlertDialog.Builder(context)
        with(builder) {
            setMessage(msg)
            setPositiveButton(R.string.delete) { _, _ ->
                viewModel.deleteAllMovies()
            }
            setNegativeButton(R.string.cancel) { _, _ ->
                movieAdapter.notifyDataSetChanged()
            }
            show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class WordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var movie: com.apaul9.cinescope.database.Movie
        private val movieTitle = view.findViewById<TextView>(R.id.movieTitle)
        private val moviePoster = view.findViewById<ImageView>(R.id.moviePoster)
        private val movieOverview = view.findViewById<TextView>(R.id.overviewMovie)
        private val dateReleased = view.findViewById<TextView>(R.id.dateReleased)
        private val votingAverage = view.findViewById<TextView>(R.id.vote)
        private val languageOriginal = view.findViewById<TextView>(R.id.language)

        @SuppressLint("SetTextI18n")
        fun bind(movie: com.apaul9.cinescope.database.Movie) {
            this.movie = movie
            movieTitle.text = movie.movieTitle
            dateReleased.text = movie.releaseDate
            movieOverview.text = movie.movieOverview
            votingAverage.text = movie.movieRating + "/10"
            languageOriginal.text = movie.movieLanguage

            if (movie.moviePoster != null) {
                Log.d(TAG, "Movie Poster: ${movie.moviePoster}")
                val posterLink = CineScopeApp.DEFAULT_TMDB_IMAGE_URL + movie.moviePoster
                Picasso.get().load(posterLink).into(moviePoster)
            }
        }
    }

    private inner class MovieAdapter : RecyclerView.Adapter<WordViewHolder>() {
        var movie: List<com.apaul9.cinescope.database.Movie> = emptyList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
            val view = layoutInflater.inflate(R.layout.recycler_card, parent, false)
            return WordViewHolder(view)
        }

        override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
            return holder.bind(movie[position])
        }

        override fun getItemCount() = movie.size

        @SuppressLint("NotifyDataSetChanged")
        fun updateMovies(newMovies: List<com.apaul9.cinescope.database.Movie>) {
            this.movie = newMovies
            notifyDataSetChanged()
        }

        fun getMovieAtPosition(position: Int): com.apaul9.cinescope.database.Movie {
            return movie[position]
        }
    }

}