package com.ymovie.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ymovie.app.data.MovieRepository
import com.ymovie.app.data.model.HomeRequestParam
import com.ymovie.app.data.source.MovieRemoteDataSource
import com.ymovie.app.databinding.FragmentHomeBinding
import com.ymovie.app.network.RetrofitApiClient
import com.ymovie.app.network.service.MovieService
import com.ymovie.app.ui.home.adapter.HomeAdapter
import com.ymovie.app.util.RecyclerViewItemDecoration
import com.ymovie.app.util.convertDpToPx
import com.ymovie.app.util.setItemOffset
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by lazy {
        val repository = MovieRepository(
            MovieRemoteDataSource(RetrofitApiClient.retrofitInstance.create(MovieService::class.java))
        )
        ViewModelProvider(this@HomeFragment, HomeViewModelFactory(repository))[HomeViewModel::class.java]
    }
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    private var currentPage = DEFAULT_PAGE

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        collectHomeData()

        homeViewModel.setHomeRequestParam(HomeRequestParam(DEFAULT_LANGUAGE, currentPage, DEFAULT_REGION))
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun initAdapter() {
        homeAdapter = HomeAdapter(requireActivity(), ArrayList())
        linearLayoutManager = LinearLayoutManager(requireActivity()).apply {
            this.orientation = LinearLayoutManager.VERTICAL
        }

        binding.rvHome.apply {
            this.layoutManager = linearLayoutManager
            this.adapter = homeAdapter
            this.addItemDecoration(
                RecyclerViewItemDecoration(setItemOffset(topPx = convertDpToPx(65F, resources)))
            )
        }
    }

    private fun collectHomeData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                homeViewModel.homeData.collect { response ->
                    when (response) {
                        is HomeUiState.Loading -> {

                        }

                        is HomeUiState.Success -> {
                            homeAdapter.addItemToList(response.data)
                        }

                        is HomeUiState.Failure -> {

                        }
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