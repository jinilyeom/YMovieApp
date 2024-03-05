package com.ymovie.app.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.ymovie.app.R
import com.ymovie.app.data.MovieRepository
import com.ymovie.app.data.source.MovieRemoteDataSource
import com.ymovie.app.databinding.FragmentMovieDetailBinding
import com.ymovie.app.network.NetworkConstants
import com.ymovie.app.network.RetrofitApiClient
import com.ymovie.app.network.service.MovieService
import com.ymovie.app.ui.UiConstants.MOVIE_ID
import com.ymovie.app.ui.UiConstants.MOVIE_NAME
import kotlinx.coroutines.launch

class MovieDetailFragment : Fragment() {
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private val movieDetailViewModel: MovieDetailViewModel by lazy {
        val repository = MovieRepository(
            MovieRemoteDataSource(RetrofitApiClient.retrofitInstance.create(MovieService::class.java))
        )
        ViewModelProvider(this@MovieDetailFragment, MovieDetailViewModelFactory(repository))[MovieDetailViewModel::class.java]
    }

    private var movieId: Int = -1
    private var movieName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            movieId = it.getInt(MOVIE_ID)
            movieName = it.getString(MOVIE_NAME) ?: ""
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieDetailViewModel.setMovieId(movieId)

        resultMovieDetails()
        resultMovieCredits()

        binding.topAppBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val position = appBarLayout.totalScrollRange + verticalOffset

            if (position > COLLAPSED_START_POSITION) {
                // Expanded
                binding.topToolBar.title = ""
            } else {
                // Collapsed
                binding.topToolBar.title = movieName
            }
        }

        binding.topToolBar.setNavigationOnClickListener {
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun resultMovieDetails() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                movieDetailViewModel.movieDetail.collect { response ->
                    when (response) {
                        is MovieDetailUiState.Loading -> {

                        }

                        is MovieDetailUiState.Success -> {
                            Glide.with(binding.imvBackdrop)
                                .load(NetworkConstants.IMAGE_BASE_URL_W500 + response.data.backdropPath)
                                .into(binding.imvBackdrop)
                            Glide.with(binding.imvPoster)
                                .load(NetworkConstants.IMAGE_BASE_URL_W200 + response.data.posterPath)
                                .into(binding.imvPoster)

                            binding.viewMovieDetailBasics.setMovieDetailData(response.data)
                        }

                        is MovieDetailUiState.Failure -> {

                        }
                    }
                }
            }
        }
    }

    private fun resultMovieCredits() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                movieDetailViewModel.movieCredit.collect { response ->
                    when (response) {
                        is MovieCreditUiState.Loading -> {

                        }

                        is MovieCreditUiState.Success -> {
                            binding.viewMovieDetailCredits.let {
                                it.setHeaderText(getString(R.string.label_top_cast))
                                it.setCasts(response.data.casts)
                                it.setCrews(response.data.crews)
                            }
                        }

                        is MovieCreditUiState.Failure -> {

                        }
                    }
                }
            }
        }
    }

    companion object {
        const val COLLAPSED_START_POSITION = 100

        fun newInstance(movieId: Int, movieName: String): MovieDetailFragment {
            val args = Bundle().apply {
                putInt(MOVIE_ID, movieId)
                putString(MOVIE_NAME, movieName)
            }

            return MovieDetailFragment().apply {
                arguments = args
            }
        }
    }
}