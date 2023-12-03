package com.ymovie.app.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.ymovie.app.R
import com.ymovie.app.data.MovieRepository
import com.ymovie.app.data.NetworkResponse
import com.ymovie.app.data.source.RemoteMovieDataSource
import com.ymovie.app.databinding.FragmentMovieDetailBinding
import com.ymovie.app.network.NetworkConstants
import com.ymovie.app.network.RetrofitApiClient
import com.ymovie.app.network.service.MovieService
import com.ymovie.app.ui.UiConstants.MOVIE_ID
import com.ymovie.app.ui.UiConstants.MOVIE_NAME

class MovieDetailFragment : Fragment() {
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private val movieDetailViewModel: MovieDetailViewModel by lazy {
        val repository = MovieRepository(
            RemoteMovieDataSource(RetrofitApiClient.retrofitInstance.create(MovieService::class.java))
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

        subscribeUi()

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

        movieDetailViewModel.fetchMovieDetails(movieId)
        movieDetailViewModel.fetchCredits(movieId)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun subscribeUi() {
        movieDetailViewModel.detailBasicsLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    Glide.with(binding.imvBackdrop)
                        .load(NetworkConstants.IMAGE_BASE_URL_W500 + response.data.backdropPath)
                        .into(binding.imvBackdrop)
                    Glide.with(binding.imvPoster)
                        .load(NetworkConstants.IMAGE_BASE_URL_W200 + response.data.posterPath)
                        .into(binding.imvPoster)

                    binding.viewMovieDetailBasics.setMovieDetailData(response.data)
                }

                is NetworkResponse.Failure -> {

                }
            }
        }

        movieDetailViewModel.creditsLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    binding.viewMovieDetailCredits.let {
                        it.setHeaderText(getString(R.string.label_top_cast))
                        it.setCasts(response.data.casts)
                        it.setCrews(response.data.crews)
                    }
                }

                is NetworkResponse.Failure -> {

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