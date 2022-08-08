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

import java.util.List;

public class MoviesDetailAdapter extends RecyclerView.Adapter<MoviesDetailAdapter.ViewHolder> {
    private List<Cast> casts;
    private final Context context;

    public MoviesDetailAdapter(List<Cast> casts, Context context) {
        this.casts = casts;
        this.context = context;
    }

    public void setData(List<Cast> casts) {
        this.casts = casts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_cast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cast cast = casts.get(position);

        holder.tv_cast1.setText(cast.getName());
        Glide.with(context).load("https://image.tmdb.org/t/p/original/" + cast.getProfilePath()).into(holder.img_cast);
    }

    @Override
    public int getItemCount() {
        if (casts != null) {
            return casts.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_cast1;
        private final ImageView img_cast;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_cast1 = itemView.findViewById(R.id.tv_cast1);
            img_cast = itemView.findViewById(R.id.img_cast);
        }
    }
}
