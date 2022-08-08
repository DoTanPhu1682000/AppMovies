package com.phudt7.movie.fragments;

import static android.content.Context.MODE_PRIVATE;
import static com.phudt7.movie.Const.Const.*;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import com.phudt7.movie.R;
import com.phudt7.movie.adapter.MoviesAdapter;
import com.phudt7.movie.databinding.FragmentMoviesBinding;
import com.phudt7.movie.db.MoviesDatabase;
import com.phudt7.movie.model.MovieListResponse;
import com.phudt7.movie.model.Result;
import com.phudt7.movie.network.ApiClient;
import com.phudt7.movie.network.MoviesApi;
import com.phudt7.movie.vm.FavouritesViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentMovies extends Fragment implements MoviesAdapter.ItemClickListener {
    FragmentMoviesBinding binding;
    MoviesAdapter moviesAdapter;
    private List<Result> results;
    GridLayoutManager gridLayoutManager;
    LinearLayoutManager linearLayoutManager;
    FavouritesViewModel viewModel;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    int page = 1;
    boolean isLoading = false;
    Call<MovieListResponse> call;
    String categoryMovies;
    String categorySortBy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMoviesBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(FavouritesViewModel.class);

        gridLayoutManager = new GridLayoutManager(requireContext(), 1);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.rvMovies.setLayoutManager(gridLayoutManager);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(MyPreference, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String categogy = sharedPreferences.getString(CATEGORY, "");
        String sortBy = sharedPreferences.getString(SORT_BY, "");
        categoryMovies = categogy;
        categorySortBy = sortBy;
        if (Objects.equals(categoryMovies, POPULAR_MOVIES)) {
            callApi(page);
        } else if (Objects.equals(categoryMovies, TOP_RATED_MOVIES)) {
            callApiTopRated(page);
        } else if (Objects.equals(categoryMovies, UPCOMING_MOVIES)) {
            callApiUpcoming(page);
        } else if (Objects.equals(categoryMovies, NOWPLAYING_MOVIES)) {
            callApiNowplaying(page);
        }

        results = new ArrayList<>();
        loadMore();
        arrowDrop();

        return binding.getRoot();
    }

    private void arrowDrop() {
        binding.tvPopularMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(POPULAR_MOVIES);
                binding.llArrowDrop.setVisibility(View.GONE);
                callApi(page);
                editor.putString(CATEGORY, binding.tvPopularMovies.getText().toString().trim());
                editor.apply();
            }
        });

        binding.tvTopRatedMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(TOP_RATED_MOVIES);
                binding.llArrowDrop.setVisibility(View.GONE);
                callApiTopRated(page);
                editor.putString(CATEGORY, binding.tvTopRatedMovies.getText().toString().trim());
                editor.apply();
            }
        });

        binding.tvUpcomingMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(UPCOMING_MOVIES);
                binding.llArrowDrop.setVisibility(View.GONE);
                callApiUpcoming(page);
                editor.putString(CATEGORY, binding.tvUpcomingMovies.getText().toString().trim());
                editor.apply();
            }
        });

        binding.tvNowplayingMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(NOWPLAYING_MOVIES);
                binding.llArrowDrop.setVisibility(View.GONE);
                callApiNowplaying(page);
                editor.putString(CATEGORY, binding.tvNowplayingMovies.getText().toString().trim());
                editor.apply();
            }
        });
    }

    private void callApiNowplaying(int page) {
        MoviesApi moviesApi = ApiClient.getClient().create(MoviesApi.class);
        Call<MovieListResponse> call = moviesApi.getNowplaying(page);

        call.enqueue(new Callback<MovieListResponse>() {
            @Override
            public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                results = response.body().getResults();
                if (Objects.equals(categorySortBy, RELEASE_DATE)) {
                    Collections.sort(results, Result.releaseDateSettings);
                } else if (Objects.equals(categorySortBy, RATING)) {
                    Collections.sort(results, Result.ratingSettings);
                }
                moviesAdapter = new MoviesAdapter(results, getActivity(), gridLayoutManager, FragmentMovies.this);
                binding.rvMovies.setAdapter(moviesAdapter);
                moviesAdapter.addBottomItem();
            }

            @Override
            public void onFailure(Call<MovieListResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void callApiUpcoming(int page) {
        MoviesApi moviesApi = ApiClient.getClient().create(MoviesApi.class);
        Call<MovieListResponse> call = moviesApi.getUpcoming(page);

        call.enqueue(new Callback<MovieListResponse>() {
            @Override
            public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                results = response.body().getResults();
                if (Objects.equals(categorySortBy, RELEASE_DATE)) {
                    Collections.sort(results, Result.releaseDateSettings);
                } else if (Objects.equals(categorySortBy, RATING)) {
                    Collections.sort(results, Result.ratingSettings);
                }
                moviesAdapter = new MoviesAdapter(results, getActivity(), gridLayoutManager, FragmentMovies.this);
                binding.rvMovies.setAdapter(moviesAdapter);
                moviesAdapter.addBottomItem();
            }

            @Override
            public void onFailure(Call<MovieListResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void callApiTopRated(int page) {
        MoviesApi moviesApi = ApiClient.getClient().create(MoviesApi.class);
        Call<MovieListResponse> call = moviesApi.getTopRated(page);

        call.enqueue(new Callback<MovieListResponse>() {
            @Override
            public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                results = response.body().getResults();
                if (Objects.equals(categorySortBy, RELEASE_DATE)) {
                    Collections.sort(results, Result.releaseDateSettings);
                } else if (Objects.equals(categorySortBy, RATING)) {
                    Collections.sort(results, Result.ratingSettings);
                }
                moviesAdapter = new MoviesAdapter(results, getActivity(), gridLayoutManager, FragmentMovies.this);
                binding.rvMovies.setAdapter(moviesAdapter);
                moviesAdapter.addBottomItem();
            }

            @Override
            public void onFailure(Call<MovieListResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void loadMore() {
        binding.nestedMovies.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    isLoading = true;
                    page++;
                    binding.pbLoading.setVisibility(View.VISIBLE);
                    nextPage(page);
                }
            }
        });
    }

    private void callApi(int page) {
        MoviesApi moviesApi = ApiClient.getClient().create(MoviesApi.class);
        call = moviesApi.getPage(page);
        call.enqueue(new Callback<MovieListResponse>() {
            @Override
            public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                results = response.body().getResults();
                if (Objects.equals(categorySortBy, RELEASE_DATE)) {
                    Collections.sort(results, Result.releaseDateSettings);
                } else if (Objects.equals(categorySortBy, RATING)) {
                    Collections.sort(results, Result.ratingSettings);
                }
                moviesAdapter = new MoviesAdapter(results, getActivity(), gridLayoutManager, FragmentMovies.this);
                binding.rvMovies.setAdapter(moviesAdapter);
                moviesAdapter.addBottomItem();
            }

            @Override
            public void onFailure(Call<MovieListResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void nextPage(int page) {
        MoviesApi moviesApi = ApiClient.getClient().create(MoviesApi.class);
        if (Objects.equals(categoryMovies, POPULAR_MOVIES)) {
            call = moviesApi.getPage(page);
        } else if (Objects.equals(categoryMovies, TOP_RATED_MOVIES)) {
            call = moviesApi.getTopRated(page);
        } else if (Objects.equals(categoryMovies, UPCOMING_MOVIES)) {
            call = moviesApi.getUpcoming(page);
        } else if (Objects.equals(categoryMovies, NOWPLAYING_MOVIES)) {
            call = moviesApi.getNowplaying(page);
        }

        call.enqueue(new Callback<MovieListResponse>() {
            @Override
            public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                results = response.body().getResults();
                if (Objects.equals(categorySortBy, RELEASE_DATE)){
                    Collections.sort(results, Result.releaseDateSettings);
                } else if (Objects.equals(categorySortBy, RATING)){
                    Collections.sort(results, Result.ratingSettings);
                }
                moviesAdapter.removeItem();
                isLoading = false;
                moviesAdapter.addAll(results);
                moviesAdapter.addBottomItem();
                binding.rvMovies.setAdapter(moviesAdapter);
            }

            @Override
            public void onFailure(Call<MovieListResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(Result result) {
        NavDirections navDirections = FragmentMoviesDirections.actionFragmentMoviesToFragmentMoviesDetail(result);
        Navigation.findNavController(getView()).navigate(navDirections);
    }

    @Override
    public void insertMovies(Result result) {
        MoviesDatabase.getInstance(requireContext()).moviesDAO().insertMovies(result);
        viewModel.number(MoviesDatabase.getInstance(requireContext()).moviesDAO().coutMovie());
    }

    @Override
    public void deleteMovies(Result result) {
        MoviesDatabase.getInstance(requireContext()).moviesDAO().deleteMovies(result);
        viewModel.number(MoviesDatabase.getInstance(requireContext()).moviesDAO().coutMovie());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list_items, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.fragmentMovies:
                Toast.makeText(requireContext(), "fragmentMovies", Toast.LENGTH_LONG).show();
        }

        if (item.getItemId() == R.id.arrowDrop)
            binding.llArrowDrop.setVisibility(View.VISIBLE);

        if (item.getItemId() == R.id.itemlistGird) {
            switchLayout();
            switchIcon(item);
        }


        return super.onOptionsItemSelected(item);
    }

    private void switchIcon(MenuItem item) {
        if (gridLayoutManager.getSpanCount() == 2) {
            item.setIcon(getResources().getDrawable(R.mipmap.ic_grid_on_white));
        } else {
            item.setIcon(getResources().getDrawable(R.mipmap.ic_list_white));
        }
    }

    private void switchLayout() {
        if (gridLayoutManager.getSpanCount() == 1) {
            gridLayoutManager.setSpanCount(2);
        } else {
            gridLayoutManager.setSpanCount(1);
        }
        moviesAdapter.notifyItemRangeChanged(0, moviesAdapter.getItemCount());
    }
}
