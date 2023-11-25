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
import com.ymovie.app.data.NetworkResponse
import com.ymovie.app.data.model.movie.MovieList
import com.ymovie.app.data.source.RemoteMovieDataSource
import com.ymovie.app.databinding.FragmentHomeBinding
import com.ymovie.app.network.RetrofitApiClient
import com.ymovie.app.network.service.MovieService
import com.ymovie.app.ui.home.adapter.HomeAdapter
import com.ymovie.app.util.RecyclerViewItemOffset
import com.ymovie.app.util.convertDpToPx

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    private var currentPage = DEFAULT_PAGE

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

        homeViewModel.fetchHomeData(DEFAULT_LANGUAGE, currentPage, DEFAULT_REGION)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        homeViewModel.clearHomeDataLiveData()
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
        homeViewModel.homeDataLiveData.observe(viewLifecycleOwner) { responses ->
            responses.forEach { response ->
                when (response) {
                    is NetworkResponse.Success -> {
                        homeAdapter.addItemToList(response.data)
                    }

                    is NetworkResponse.Failure -> {
                        homeAdapter.addItemToList(MovieList())
                    }
                }
            }
        }
    }

    companion object {
        private const val DEFAULT_PAGE = 1
        private const val DEFAULT_LANGUAGE = "en-US"
        private const val DEFAULT_REGION = ""

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}