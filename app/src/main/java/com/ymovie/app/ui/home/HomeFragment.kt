package com.ymovie.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ymovie.app.data.MovieRepository
import com.ymovie.app.data.source.RemoteMovieDataSource
import com.ymovie.app.databinding.FragmentHomeBinding
import com.ymovie.app.network.RetrofitApiClient
import com.ymovie.app.network.service.MovieService

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var homeLinearLayoutManager: LinearLayoutManager

    private var currentPage = 1

    companion object {
        private const val LAST_PAGE = 3
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
    }

    override fun onResume() {
        super.onResume()

        homeViewModel.fetchTopRatedMovies(DEFAULT_LANGUAGE, currentPage, DEFAULT_REGION)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun initAdapter() {
        homeAdapter = HomeAdapter(requireActivity(), ArrayList())
        homeLinearLayoutManager = LinearLayoutManager(requireActivity()).apply {
            this.orientation = LinearLayoutManager.VERTICAL
        }

        binding.rvHome.apply {
            this.layoutManager = homeLinearLayoutManager
            this.adapter = homeAdapter
            this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (recyclerView.scrollState == RecyclerView.SCROLL_STATE_IDLE) {
                        return
                    }

                    val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager? ?: return
                    val itemCount = linearLayoutManager.itemCount
                    val lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition()

                    if (currentPage < LAST_PAGE && lastVisible == itemCount - 1) {
                        homeViewModel.fetchTopRatedMovies(DEFAULT_LANGUAGE, ++currentPage, DEFAULT_REGION)
                    }
                }
            })
        }
    }

    private fun subscribeUi() {
        homeViewModel.movieLiveData.observe(viewLifecycleOwner) { model ->
            homeAdapter.addItemToList(model.movies ?: ArrayList())
        }
    }
}