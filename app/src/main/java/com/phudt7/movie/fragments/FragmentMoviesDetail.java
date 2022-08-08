package com.phudt7.movie.fragments;

import static com.phudt7.movie.Const.Const.*;

import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.phudt7.movie.R;
import com.phudt7.movie.activity.MainActivity;
import com.phudt7.movie.adapter.MoviesDetailAdapter;
import com.phudt7.movie.databinding.FragmentMoviesDetailBinding;
import com.phudt7.movie.db.MoviesDatabase;
import com.phudt7.movie.model.Cast;
import com.phudt7.movie.model.CastResponse;
import com.phudt7.movie.model.Reminder;
import com.phudt7.movie.model.Result;
import com.phudt7.movie.network.ApiClient;
import com.phudt7.movie.network.MoviesApi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentMoviesDetail extends Fragment {
    FragmentMoviesDetailBinding binding;
    FragmentMoviesDetailArgs args;
    private List<Cast> casts;
    MoviesDetailAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMoviesDetailBinding.inflate(inflater, container, false);
        args = FragmentMoviesDetailArgs.fromBundle(getArguments());
        binding.tvReleaseDateDetail.setText(args.getData().releaseDate);
        binding.tvRatingDetail.setText(String.valueOf(args.getData().getVoteAverage()));
        binding.tvOverviewDetail.setText(args.getData().overview);
        Glide.with(requireContext()).load("https://image.tmdb.org/t/p/original/" + args.getData().getPosterPath()).into(binding.imgMovies);

        casts = new ArrayList<>();
        callApi();
        sendNotification();

        return binding.getRoot();
    }

    private void sendNotification() {
        binding.btnReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int selectedYear = calendar.get(Calendar.YEAR);
                int selectedMonth = calendar.get(Calendar.MONTH);
                int selectedDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                int lastSelectedHour = calendar.get(Calendar.HOUR_OF_DAY);
                int lastSelectedMinute = calendar.get(Calendar.MINUTE);
                DatePickerDialog.OnDateSetListener dateSetListener = (view2, year, monthOfYear, dayOfMonth)
                        -> {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                            android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                            (timePicker, hourOfDay, minute) -> {
                                calendar.set(year, monthOfYear, dayOfMonth, hourOfDay, minute, 0);
                                String information = "Year: "
                                        + args.getData().getReleaseDate()
                                        .substring(0, args.getData().getReleaseDate().indexOf('-'))
                                        + "  Rate: " + args.getData().getVoteAverage().toString() + "/10";
                                String title = args.getData().getOriginalTitle() + " - " + args.getData().getReleaseDate()
                                        .substring(0, args.getData().getReleaseDate().indexOf('-')) + " - "
                                        + args.getData().getVoteAverage().toString() + "/10";
                                Reminder reminder = new Reminder((int) calendar.getTimeInMillis()
                                        , args.getData().getId(), args.getData().getPosterPath(), title, args.getData().getOriginalTitle()
                                        , information, calendar.getTimeInMillis());
                                reminder.setSchedule(requireContext());
                                MoviesDatabase.getInstance(requireContext()).moviesDAO().insertReminder(reminder);
                            }, lastSelectedHour, lastSelectedMinute, false);
                    timePickerDialog.show();
                };

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        dateSetListener, selectedYear, selectedMonth, selectedDayOfMonth);
                datePickerDialog.show();
            }
        });
    }

    private void callApi() {
        MoviesApi moviesApi = ApiClient.getClient().create(MoviesApi.class);
        Call<CastResponse> call = moviesApi.getCast(args.getData().getId(), API_KEY);

        call.enqueue(new Callback<CastResponse>() {
            @Override
            public void onResponse(Call<CastResponse> call, Response<CastResponse> response) {
                casts = response.body().getCasts();
                adapter = new MoviesDetailAdapter(casts, requireContext());
                adapter.setData(casts);
                binding.rvDetail.setAdapter(adapter);
                binding.rvDetail.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            }

            @Override
            public void onFailure(Call<CastResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}