package com.phudt7.movie.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phudt7.movie.adapter.MoviesAdapter;
import com.phudt7.movie.adapter.MoviesDetailAdapter;
import com.phudt7.movie.adapter.ReminderAdapter;
import com.phudt7.movie.databinding.DialogConfirmBinding;
import com.phudt7.movie.databinding.DialogReminderBinding;
import com.phudt7.movie.databinding.FragmentReminderBinding;
import com.phudt7.movie.db.MoviesDatabase;
import com.phudt7.movie.model.Reminder;
import com.phudt7.movie.model.Result;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class FragmentReminder extends Fragment implements View.OnClickListener {
    FragmentReminderBinding binding;
    private List<Reminder> reminderList = new ArrayList<>();
    ReminderAdapter reminderAdapter;
    private boolean display = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReminderBinding.inflate(inflater, container, false);
        setupRV();
        setData();
        return binding.getRoot();
    }

    private void setupRV() {
        reminderAdapter = new ReminderAdapter(requireContext(), false, FragmentReminder.this);
        binding.rvReminder.setAdapter(reminderAdapter);
        binding.rvReminder.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    private void setData() {
        reminderList.clear();
        List<Reminder> reminderLst = MoviesDatabase.getInstance(requireContext()).moviesDAO().getReminder();
        Calendar calendar = Calendar.getInstance();
        for (Reminder reminder : reminderLst) {
            if (calendar.getTimeInMillis() >= reminder.getTime())
                MoviesDatabase.getInstance(requireContext()).moviesDAO().deleteReminder(reminder);
            else
                reminderList.add(reminder);
        }
        reminderAdapter.setData(reminderList);
    }

    @Override
    public void onClick(View v) {
        Reminder reminder = (Reminder) v.getTag();
        Dialog dialog = new Dialog(requireContext());
        DialogReminderBinding dialogReminderBinding = DialogReminderBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogReminderBinding.getRoot());
        dialog.show();

        dialogReminderBinding.tvCancel.setOnClickListener(v1 -> {
            dialog.dismiss();
        });
        dialogReminderBinding.tvDelete.setOnClickListener(v12 -> {
            Dialog dialog1 = new Dialog(requireContext());
            DialogConfirmBinding dialogConfirmBinding = DialogConfirmBinding.inflate(getLayoutInflater());
            dialog1.setContentView(dialogConfirmBinding.getRoot());
            dialog1.show();
            dialogConfirmBinding.btnNo.setOnClickListener(v13 -> {
                dialog1.dismiss();
                dialog.dismiss();
            });
            dialogConfirmBinding.btnYes.setOnClickListener(v14 -> {
                MoviesDatabase.getInstance(requireContext()).moviesDAO().deleteReminder(reminder);
                setData();
                reminder.cancelSchedule(requireContext());
                dialog1.dismiss();
                dialog.dismiss();
            });
        });
    }
}