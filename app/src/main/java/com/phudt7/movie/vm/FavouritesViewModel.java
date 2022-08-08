package com.phudt7.movie.vm;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FavouritesViewModel extends ViewModel {
    public final MutableLiveData<Integer> countFavourites = new MutableLiveData<>();
    public final MutableLiveData<View> categorySettings = new MutableLiveData<View>();

    public void number(Integer count) {
        countFavourites.setValue(count);
    }

    public LiveData<Integer> getCount() {
        return countFavourites;
    }

    public void category(View view) {
        categorySettings.setValue(view);
    }

    public LiveData<View> getCategory() {
        return categorySettings;
    }

}
