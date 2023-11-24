package com.ymovie.app.ui.search

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private var currentPage: Int = DEFAULT_PAGE
    private var totalPage: Int = 0

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
                            searchViewModel.searchMovie(
                                binding.searchView.text.toString(),
                                DEFAULT_INCLUDE_ADULT,
                                DEFAULT_LANGUAGE,
                                DEFAULT_PRIMARY_RELEASE_YEAR,
                                currentPage,
                                DEFAULT_REGION,
                                DEFAULT_YEAR
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
                        searchAdapter.clearList()

                        searchViewModel.searchMovie(
                            binding.searchView.text.toString(),
                            DEFAULT_INCLUDE_ADULT,
                            DEFAULT_LANGUAGE,
                            DEFAULT_PRIMARY_RELEASE_YEAR,
                            currentPage,
                            DEFAULT_REGION,
                            DEFAULT_YEAR
                        )

                        binding.searchBar.text = binding.searchView.text
                        binding.searchView.hide()
                    }
                }

                false
            }
        }
    }

    private fun subscribeUi() {
        searchViewModel.searchResultLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    response.let {
                        currentPage = it.data.page + 1
                        totalPage = it.data.totalPage

                        searchAdapter.addItemToList(it.data.movies ?: emptyList())
                    }
                }

                is NetworkResponse.Failure -> {
                    searchAdapter.addItemToList(emptyList())
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