package com.phudt7.movie.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.phudt7.movie.R;
import com.phudt7.movie.adapter.FavouritesAdapter;
import com.phudt7.movie.adapter.MoviesAdapter;
import com.phudt7.movie.databinding.FragmentFavouritesBinding;
import com.phudt7.movie.db.MoviesDatabase;
import com.phudt7.movie.model.Result;
import com.phudt7.movie.vm.FavouritesViewModel;

import java.util.ArrayList;
import java.util.List;

public class FragmentFavourites extends Fragment implements MoviesAdapter.ItemClickListener {
    FragmentFavouritesBinding binding;
    FavouritesAdapter favouritesAdapter;
    LinearLayoutManager linearLayoutManager;
    private List<Result> results;
    FavouritesViewModel viewModel;
    SearchManager searchManager;
    SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(FavouritesViewModel.class);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.rvFavourites.setLayoutManager(linearLayoutManager);
        results = new ArrayList<>();

        results = MoviesDatabase.getInstance(requireContext()).moviesDAO().getMovies();
        favouritesAdapter = new FavouritesAdapter(results, getActivity(), FragmentFavourites.this);
        binding.rvFavourites.setAdapter(favouritesAdapter);

        return binding.getRoot();
    }

    @Override
    public void onClick(Result result) {
    }

    @Override
    public void insertMovies(Result result) {
    }

    @Override
    public void deleteMovies(Result result) {
        MoviesDatabase.getInstance(requireContext()).moviesDAO().deleteMovies(result);
        results = MoviesDatabase.getInstance(requireContext()).moviesDAO().getMovies();
        favouritesAdapter = new FavouritesAdapter(results, getActivity(), FragmentFavourites.this);
        binding.rvFavourites.setAdapter(favouritesAdapter);
        viewModel.number(MoviesDatabase.getInstance(requireContext()).moviesDAO().coutMovie());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_item, menu);

        searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.actionSearch).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                favouritesAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                favouritesAdapter.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.actionSearch)
            return true;
        return super.onOptionsItemSelected(item);
    }
}