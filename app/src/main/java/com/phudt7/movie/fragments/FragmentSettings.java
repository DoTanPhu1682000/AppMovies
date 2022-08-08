package com.phudt7.movie.fragments;

import static android.content.Context.MODE_PRIVATE;
import static com.phudt7.movie.Const.Const.*;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.phudt7.movie.R;
import com.phudt7.movie.databinding.FragmentSettingsBinding;
import com.phudt7.movie.vm.FavouritesViewModel;


public class FragmentSettings extends Fragment {
    FragmentSettingsBinding binding;
    SettingCategory settingCategory;
    final int[] checkedItem = {-1};
    final int[] item = {-1};
    int valueSeekbar = 0;
    FavouritesViewModel viewModel;
    int sortBy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(FavouritesViewModel.class);

        setData();
        categoryMovies();
        seekBarMovies();
        releaseYearMovies();
        sortByMovies();

        return binding.getRoot();
    }

    private void setData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(MyPreference, MODE_PRIVATE);
        String categogy = sharedPreferences.getString(CATEGORY, "");
        String seekbar = sharedPreferences.getString(SEEKBAR, "");
        String year = sharedPreferences.getString(RELEASE_YEAR, "");
        String sortBy = sharedPreferences.getString(SORT_BY, "");
        binding.tvPopularMovies.setText(categogy);
        binding.tvSeekbar.setText(seekbar);
        binding.tvYear.setText(year);
        binding.tvReleaseDate.setText(sortBy);
    }

    private void sortByMovies() {
        binding.tvSortBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
                alertDialog.setTitle("Sort By");
                final String[] listItemsSortBy = new String[]{RELEASE_DATE, RATING};
                alertDialog.setSingleChoiceItems(listItemsSortBy, item[0], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        item[0] = which;
                        binding.tvReleaseDate.setText(listItemsSortBy[which]);

                        SharedPreferences sharedPreferences = getContext().getSharedPreferences(MyPreference, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(SORT_BY, binding.tvReleaseDate.getText().toString().trim());
                        editor.apply();

                        dialog.dismiss();
                    }
                });

                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog customAlertDialog = alertDialog.create();
                customAlertDialog.show();
            }
        });
    }

    private void releaseYearMovies() {
        binding.tvFromReleaseYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(requireContext());
                View view = getLayoutInflater().inflate(R.layout.diaglog_release_year, null);
                EditText edt_release_year = (EditText) view.findViewById(R.id.edt_release_year);
                alert.setView(view);

                Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
                Button btn_okay = (Button) view.findViewById(R.id.btn_okay);
                final AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(false);

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                btn_okay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.tvYear.setText(edt_release_year.getText().toString());
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences(MyPreference, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(RELEASE_YEAR, binding.tvYear.getText().toString().trim());
                        editor.apply();
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();

            }
        });
    }

    private void seekBarMovies() {
        binding.tvMoviesRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
                View view = getLayoutInflater().inflate(R.layout.dialog_seekbar, null);
                alertDialog.setTitle("Movie with rate from");
                alertDialog.setView(view);

                SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBar);
                TextView tv_number_seekbar = (TextView) view.findViewById(R.id.tv_number_seekbar);

                seekBar.setProgress(valueSeekbar);
                tv_number_seekbar.setText("" + valueSeekbar);
                seekBar.setMax(10);

                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        valueSeekbar = progress;
                        binding.tvSeekbar.setText("" + progress + " / " + seekBar.getMax());

                        SharedPreferences sharedPreferences = getContext().getSharedPreferences(MyPreference, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(SEEKBAR, binding.tvSeekbar.getText().toString().trim());
                        editor.apply();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        binding.tvSeekbar.setText("" + valueSeekbar + " / " + seekBar.getMax());

                        SharedPreferences sharedPreferences = getContext().getSharedPreferences(MyPreference, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(SEEKBAR, binding.tvSeekbar.getText().toString().trim());
                        editor.apply();
                    }
                });

                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.create();
                alertDialog.show();
            }
        });

    }

    private void categoryMovies() {
        binding.tvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
                alertDialog.setTitle("Category");
                final String[] listItems = new String[]{POPULAR_MOVIES, TOP_RATED_MOVIES, UPCOMING_MOVIES, NOWPLAYING_MOVIES};
                alertDialog.setSingleChoiceItems(listItems, checkedItem[0], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkedItem[0] = which;
                        binding.tvPopularMovies.setText(listItems[which]);

                        SharedPreferences sharedPreferences = getContext().getSharedPreferences(MyPreference, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(CATEGORY, binding.tvPopularMovies.getText().toString().trim());
                        editor.apply();

                        dialog.dismiss();
                    }
                });

                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog customAlertDialog = alertDialog.create();
                customAlertDialog.show();
            }
        });

    }

    public interface SettingCategory {
        void save();
    }

}
