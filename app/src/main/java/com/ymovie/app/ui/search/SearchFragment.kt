package com.ymovie.app.ui.search

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
import com.ymovie.app.data.source.RemoteMovieDataSource
import com.ymovie.app.databinding.FragmentSearchBinding
import com.ymovie.app.network.RetrofitApiClient
import com.ymovie.app.network.service.MovieService
import com.ymovie.app.util.RecyclerViewItemOffset
import com.ymovie.app.util.convertDpToPx

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var searchLinearLayoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = MovieRepository(
            RemoteMovieDataSource(RetrofitApiClient.retrofitInstance.create(MovieService::class.java))
        )
        searchViewModel = ViewModelProvider(this@SearchFragment, SearchViewModelFactory(repository))[SearchViewModel::class.java]

        initAdapter()
        initSearchView()
        subscribeUi()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun initAdapter() {
        val bottomPx = convertDpToPx(20F, resources)
        val rect = Rect(0, 0, 0, bottomPx)

        searchAdapter = SearchAdapter(requireActivity(), ArrayList())
        searchLinearLayoutManager = LinearLayoutManager(requireActivity()).apply {
            this.orientation = LinearLayoutManager.VERTICAL
        }

        binding.rvSearchResult.let {
            it.layoutManager = searchLinearLayoutManager
            it.adapter = searchAdapter
            it.addItemDecoration(RecyclerViewItemOffset(rect))
        }
    }

    private fun initSearchView() {
        binding.searchView.let {
            it.setupWithSearchBar(binding.searchBar)
            it.editText.setOnEditorActionListener { v, actionId, event ->
                searchViewModel.searchMovie(
                    binding.searchView.text.toString(),
                    DEFAULT_INCLUDE_ADULT,
                    DEFAULT_LANGUAGE,
                    DEFAULT_PRIMARY_RELEASE_YEAR,
                    DEFAULT_PAGE,
                    DEFAULT_REGION,
                    DEFAULT_YEAR
                )

                binding.searchBar.text = binding.searchView.text
                binding.searchView.hide()

                false
            }
        }
    }

    private fun subscribeUi() {
        searchViewModel.searchResultLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    searchAdapter.setItemToList(
                        response.data.movies ?: emptyList()
                    )
                }

                is NetworkResponse.Failure -> {
                    searchAdapter.setItemToList(emptyList())
                }
            }
        }
    }

    companion object {
        private const val DEFAULT_INCLUDE_ADULT = true
        private const val DEFAULT_LANGUAGE = "en-US"
        private const val DEFAULT_PRIMARY_RELEASE_YEAR = ""
        private const val DEFAULT_PAGE = 1
        private const val DEFAULT_REGION = ""
        private const val DEFAULT_YEAR = ""

        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }
}