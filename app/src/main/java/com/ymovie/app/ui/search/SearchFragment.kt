package com.ymovie.app.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ymovie.app.data.MovieRepository
import com.ymovie.app.data.model.SearchRequestParam
import com.ymovie.app.data.source.MovieRemoteDataSource
import com.ymovie.app.databinding.FragmentSearchBinding
import com.ymovie.app.network.RetrofitApiClient
import com.ymovie.app.network.service.MovieService
import com.ymovie.app.util.RecyclerViewItemDecoration
import com.ymovie.app.util.convertDpToPx
import com.ymovie.app.util.setItemOffset
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel: SearchViewModel by lazy {
        val repository = MovieRepository(
            MovieRemoteDataSource(RetrofitApiClient.retrofitInstance.create(MovieService::class.java))
        )
        ViewModelProvider(this@SearchFragment, SearchViewModelFactory(repository))[SearchViewModel::class.java]
    }

    private lateinit var searchAdapter: SearchAdapter
    private lateinit var searchLinearLayoutManager: LinearLayoutManager

    private var currentPage: Int = DEFAULT_PAGE
    private var totalPage: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initSearchView()
        resultSearchMovie()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun initAdapter() {
        searchAdapter = SearchAdapter(requireActivity(), ArrayList())
        searchLinearLayoutManager = LinearLayoutManager(requireActivity()).apply {
            this.orientation = LinearLayoutManager.VERTICAL
        }

        binding.rvSearchResult.let {
            it.layoutManager = searchLinearLayoutManager
            it.adapter = searchAdapter
            it.addItemDecoration(
                RecyclerViewItemDecoration(setItemOffset(topPx = convertDpToPx(20F, resources)))
            )
            it.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (recyclerView.scrollState == RecyclerView.SCROLL_STATE_IDLE) {
                        return
                    }

                    val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                    linearLayoutManager.let { layoutManager ->
                        val itemCount = layoutManager.itemCount
                        val lastVisible = layoutManager.findLastCompletelyVisibleItemPosition()

                        if (currentPage <= totalPage && lastVisible == itemCount - 1) {
                            searchViewModel.setSearchRequestParam(
                                SearchRequestParam(
                                    query = binding.searchView.text.toString(),
                                    page = currentPage
                                )
                            )
                        }
                    }
                }
            })
        }
    }

    private fun initSearchView() {
        binding.searchView.let {
            it.setupWithSearchBar(binding.searchBar)
            it.editText.setOnEditorActionListener { v, actionId, event ->
                when (actionId) {
                    IME_ACTION_SEARCH -> {
                        currentPage = DEFAULT_PAGE
                        searchAdapter.clearItemFromList()

                        searchViewModel.setSearchRequestParam(
                            SearchRequestParam(
                                query = binding.searchView.text.toString(),
                                page = currentPage
                            )
                        )

                        binding.searchBar.text = binding.searchView.text
                        binding.searchView.hide()
                    }
                }

                false
            }
        }
    }

    private fun resultSearchMovie() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                searchViewModel.searchMovie.collect { response ->
                    when (response) {
                        is SearchUiState.Loading -> {

                        }

                        is SearchUiState.Success -> {
                            response.let {
                                currentPage = it.data.page + 1
                                totalPage = it.data.totalPage

                                searchAdapter.addItemToList(it.data.movies ?: emptyList())
                            }
                        }

                        is SearchUiState.Failure -> {

                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val DEFAULT_PAGE = 1

        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }
}