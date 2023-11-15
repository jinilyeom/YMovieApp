package com.ymovie.app.ui.home

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ymovie.app.data.MovieRepository
import com.ymovie.app.data.source.RemoteMovieDataSource
import com.ymovie.app.databinding.FragmentHomeBinding
import com.ymovie.app.network.RetrofitApiClient
import com.ymovie.app.network.service.MovieService
import com.ymovie.app.util.RecyclerViewItemOffset
import com.ymovie.app.util.convertDpToPx

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    private var currentPage = 1

    companion object {
        private const val DEFAULT_LANGUAGE = "en-US"
        private const val DEFAULT_REGION = ""

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = MovieRepository(
            RemoteMovieDataSource(RetrofitApiClient.retrofitInstance.create(MovieService::class.java))
        )
        homeViewModel = ViewModelProvider(this@HomeFragment, HomeViewModelFactory(repository))[HomeViewModel::class.java]

        initAdapter()
        subscribeUi()

        homeViewModel.fetchPopularMovies(DEFAULT_LANGUAGE, currentPage, DEFAULT_REGION)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun initAdapter() {
        val bottomPx = convertDpToPx(65F, resources)
        val rect = Rect(0, 0, 0, bottomPx)

        homeAdapter = HomeAdapter(requireActivity(), ArrayList())
        linearLayoutManager = LinearLayoutManager(requireActivity()).apply {
            this.orientation = LinearLayoutManager.VERTICAL
        }

        binding.rvHome.apply {
            this.layoutManager = linearLayoutManager
            this.adapter = homeAdapter
            this.addItemDecoration(RecyclerViewItemOffset(rect))
        }
    }

    private fun subscribeUi() {
        homeViewModel.popularLiveData.observe(viewLifecycleOwner) { model ->
            homeAdapter.addItemToList(model)

            homeViewModel.fetchTopRatedMovies(DEFAULT_LANGUAGE, currentPage, DEFAULT_REGION)
        }

        homeViewModel.topRatedLiveData.observe(viewLifecycleOwner) { model ->
            homeAdapter.addItemToList(model)

            homeViewModel.fetchUpcomingMovies(DEFAULT_LANGUAGE, currentPage, DEFAULT_REGION)
        }

        homeViewModel.upcomingLiveData.observe(viewLifecycleOwner) { model ->
            homeAdapter.addItemToList(model)
        }
    }
}