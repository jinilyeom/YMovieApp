package com.ymovie.app.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ymovie.app.data.MovieRepository;
import com.ymovie.app.data.model.movie.MovieList;
import com.ymovie.app.data.source.RemoteMovieDataSource;
import com.ymovie.app.databinding.FragmentHomeBinding;
import com.ymovie.app.network.RetrofitApiClient;
import com.ymovie.app.network.service.MovieService;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {
    private static final int LAST_PAGE = 3;
    private static final String DEFAULT_LANGUAGE = "en-US";
    private static final String DEFAULT_REGION = "";

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;

    private HomeAdapter homeAdapter;
    private LinearLayoutManager homeLinearLayoutManager;

    private int currentPage = 1;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MovieRepository repository = new MovieRepository(
                new RemoteMovieDataSource(RetrofitApiClient.getRetrofitInstance().create(MovieService.class))
        );
        homeViewModel = new ViewModelProvider(requireActivity(), new HomeViewModelFactory(repository))
                .get(HomeViewModel.class);

        initAdapter();
        subscribeUi();
    }

    @Override
    public void onResume() {
        super.onResume();

        homeViewModel.fetchTopRatedMovies(DEFAULT_LANGUAGE, currentPage, DEFAULT_REGION);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }

    private void initAdapter() {
        homeAdapter = new HomeAdapter(requireActivity());
        homeLinearLayoutManager = new LinearLayoutManager(requireActivity());
        homeLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        binding.rvHome.setLayoutManager(homeLinearLayoutManager);
        binding.rvHome.setAdapter(homeAdapter);
        binding.rvHome.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                    return;
                }

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (linearLayoutManager == null) {
                    return;
                }

                int itemCount = linearLayoutManager.getItemCount();
                int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                if (currentPage < LAST_PAGE && lastVisible == itemCount - 1) {
                    homeViewModel.fetchTopRatedMovies(DEFAULT_LANGUAGE, ++currentPage, DEFAULT_REGION);
                }
            }
        });
    }

    private void subscribeUi() {
        homeViewModel.getMovieLiveData().observe(getViewLifecycleOwner(), new Observer<MovieList>() {
            @Override
            public void onChanged(MovieList model) {
                if (model == null) {
                    return;
                }

                homeAdapter.addItemToList(model.getMovies());
            }
        });
    }
}
