package com.phudt7.movie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.phudt7.movie.R;
import com.phudt7.movie.model.Cast;
import com.phudt7.movie.model.Reminder;

import java.util.ArrayList;
import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {
    private List<Reminder> reminderList = new ArrayList<>();
    private Context context;
    private boolean display;
    private View.OnClickListener onClickListener;

    public ReminderAdapter(Context context, boolean display, View.OnClickListener onClickListener) {
        this.context = context;
        this.display = display;
        this.onClickListener = onClickListener;
    }

    public void setData(List<Reminder> reminderList) {
        this.reminderList = reminderList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reminder_movies, parent, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        Reminder reminder = reminderList.get(position);
        if (!display) {
            Glide.with(context).load("https://image.tmdb.org/t/p/original/" + reminder.getLinkMovie()).into(holder.img_reminder);
            holder.img_keyboard_arrow_right_black.setTag(reminder);
            holder.img_keyboard_arrow_right_black.setOnClickListener(onClickListener);
        } else {
            holder.img_reminder.setVisibility(View.GONE);
            holder.img_keyboard_arrow_right_black.setVisibility(View.GONE);
        }
        holder.tv_title.setText(reminder.getTitle());
        holder.tv_calendar.setText(reminder.convertTime());

    }

    @Override
    public int getItemCount() {
        if (!display) {
            return reminderList == null ? 0 : reminderList.size();
        } else {
            if (reminderList == null)
                return 0;
            else return Math.min(reminderList.size(), 2);
        }
    }

    public class ReminderViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_reminder, img_keyboard_arrow_right_black;
        private TextView tv_title, tv_calendar;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            img_reminder = itemView.findViewById(R.id.img_reminder);
            img_keyboard_arrow_right_black = itemView.findViewById(R.id.img_keyboard_arrow_right_black);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_calendar = itemView.findViewById(R.id.tv_calendar);
        }
    }
}
