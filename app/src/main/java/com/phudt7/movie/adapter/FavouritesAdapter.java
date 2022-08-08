package com.phudt7.movie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.phudt7.movie.R;
import com.phudt7.movie.model.Result;

import java.util.ArrayList;
import java.util.List;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder> {
    private List<Result> results;
    private List<Result> resultsFull;
    private final Context context;
    MoviesAdapter.ItemClickListener itemClickListener;

    public FavouritesAdapter(List<Result> results, Context context, MoviesAdapter.ItemClickListener itemClickListener) {
        this.results = results;
        this.context = context;
        this.itemClickListener = itemClickListener;
        this.resultsFull = results;
    }

    @NonNull
    @Override
    public FavouritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_favourite, parent, false);
        return new FavouritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouritesViewHolder holder, int position) {
        Result result = results.get(position);
        holder.tv_title.setText(result.getTitle());
        Glide.with(context).load("https://image.tmdb.org/t/p/original/" + result.getPosterPath()).into(holder.img_movies);
        holder.tv_release_date_detail.setText(result.getReleaseDate());
        holder.tv_rating_detail.setText(String.valueOf(result.getVoteAverage() + "" + "/10"));
        holder.tv_overview_detail.setText(result.getOverview());
        holder.img_star.setImageResource(R.mipmap.ic_start_selected);
        holder.img_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.deleteMovies(result);
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    results = resultsFull;
                } else {
                    List<Result> filteredList = new ArrayList<>();
                    for (Result row : resultsFull) {
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    results = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = results;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                results = (ArrayList<Result>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class FavouritesViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title, tv_release_date_detail, tv_rating_detail, tv_overview_detail;
        private ImageView img_movies, img_star;

        public FavouritesViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_release_date_detail = itemView.findViewById(R.id.tv_release_date_detail);
            tv_rating_detail = itemView.findViewById(R.id.tv_rating_detail);
            tv_overview_detail = itemView.findViewById(R.id.tv_overview_detail);
            img_movies = itemView.findViewById(R.id.img_movies);
            img_star = itemView.findViewById(R.id.img_star);
        }
    }
}
