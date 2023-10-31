package com.ymovie.app.ui.search;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ymovie.app.data.MovieRepository;
import com.ymovie.app.data.model.movie.MovieList;
import com.ymovie.app.data.source.RemoteMovieDataSource;
import com.ymovie.app.databinding.FragmentSearchBinding;
import com.ymovie.app.network.RetrofitApiClient;
import com.ymovie.app.network.service.MovieService;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

public class SearchFragment extends Fragment {
    private static final boolean DEFAULT_INCLUDE_ADULT = true;
    private static final String DEFAULT_LANGUAGE = "en-US";
    private static final String DEFAULT_PRIMARY_RELEASE_YEAR = "";
    private static final int DEFAULT_PAGE = 1;
    private static final String DEFAULT_REGION = "";
    private static final String DEFAULT_YEAR = "";

    private FragmentSearchBinding binding;
    private SearchViewModel searchViewModel;

    private SearchAdapter searchAdapter;
    private LinearLayoutManager searchLinearLayoutManager;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MovieRepository repository = new MovieRepository(
                new RemoteMovieDataSource(RetrofitApiClient.getRetrofitInstance().create(MovieService.class))
        );
        searchViewModel = new ViewModelProvider(requireActivity(), new SearchViewModelFactory(repository))
                .get(SearchViewModel.class);

        initAdapter();
        subscribeUi();

        binding.searchView.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                searchViewModel.searchMovie(
                        String.valueOf(binding.searchView.getText()),
                        DEFAULT_INCLUDE_ADULT,
                        DEFAULT_LANGUAGE,
                        DEFAULT_PRIMARY_RELEASE_YEAR,
                        DEFAULT_PAGE,
                        DEFAULT_REGION,
                        DEFAULT_YEAR
                );

                binding.searchBar.setText(binding.searchView.getText());
                binding.searchView.hide();

                return false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }

    private void initAdapter() {
        searchAdapter = new SearchAdapter(requireActivity());
        searchLinearLayoutManager = new LinearLayoutManager(requireActivity());
        searchLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        binding.rvSearchResult.setLayoutManager(searchLinearLayoutManager);
        binding.rvSearchResult.setAdapter(searchAdapter);
    }

    private void subscribeUi() {
        searchViewModel.getSearchResultLiveData().observe(getViewLifecycleOwner(), new Observer<MovieList>() {
            @Override
            public void onChanged(MovieList model) {
                if (model == null) {
                    return;
                }

                searchAdapter.setItemToList(model.getMovies());
            }
        });
    }
}
