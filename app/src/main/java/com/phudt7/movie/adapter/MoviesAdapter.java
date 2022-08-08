package com.phudt7.movie.adapter;

import static com.phudt7.movie.Const.Const.*;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.phudt7.movie.R;
import com.phudt7.movie.db.MoviesDatabase;
import com.phudt7.movie.model.Result;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    private List<Result> results;
    private final Context context;
    MoviesDatabase moviesDatabase;
    ItemClickListener itemClickListener;
    private GridLayoutManager gridLayoutManager;
    SharedPreferences sharedPreferences;

    public MoviesAdapter(List<Result> results, Context context, GridLayoutManager gridLayoutManager, ItemClickListener itemClickListener) {
        this.results = results;
        this.context = context;
        this.gridLayoutManager = gridLayoutManager;
        this.moviesDatabase = MoviesDatabase.getInstance(context);
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onClick(Result result);

        void insertMovies(Result result);

        void deleteMovies(Result result);

    }

    @Override
    public int getItemViewType(int position) {
        int spanCount = gridLayoutManager.getSpanCount();
        if (spanCount == 1) {
            return VIEW_TYPE_LIST;
        } else {
            return VIEW_TYPE_GIRD;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_LIST) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_movies, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_movies2, parent, false);
        }
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (results != null && results.get(position) != null) {
            Result result = results.get(position);
            holder.tv_title.setText(result.getTitle());
            Glide.with(context).load("https://image.tmdb.org/t/p/original/" + result.getPosterPath()).into(holder.img_moviess);


            if (getItemViewType(position) == VIEW_TYPE_LIST) {
                holder.tv_release_date_detail.setText(result.getReleaseDate());
                holder.tv_rating_detail.setText(String.valueOf(result.getVoteAverage() + "" + "/10"));
                holder.tv_overview_detail.setText(result.getOverview());
                holder.img_starMovies.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MoviesDatabase.getInstance(context).moviesDAO().getMoviesId(result.getId()) == null) {
                            holder.img_starMovies.setImageResource(R.mipmap.ic_start_selected);
                            itemClickListener.insertMovies(result);
                        } else if (MoviesDatabase.getInstance(context).moviesDAO().getMoviesId(result.getId()) != null) {
                            holder.img_starMovies.setImageResource(R.mipmap.ic_star_border_black);
                            itemClickListener.deleteMovies(result);
                        }
                    }
                });

                if (MoviesDatabase.getInstance(context).moviesDAO().getMoviesId(result.getId()) != null) {
                    holder.img_starMovies.setImageResource(R.mipmap.ic_start_selected);
                } else {
                    holder.img_starMovies.setImageResource(R.mipmap.ic_star_border_black);
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onClick(result);
                    }
                });
            }
        }
    }


    @Override
    public int getItemCount() {
        if (results != null) {
            return results.size();
        }
        return 0;
    }

    public void add(Result result) {
        results.add(result);
        notifyItemInserted(results.size() - 1);
    }

    public void addBottomItem() {
        add(new Result());
    }

    public void removeItem() {
        int position = results.size() - 1;
        Result item = getItem(position);

        if (item != null) {
            results.remove(position);
            notifyItemRemoved(position);
        }
    }

    private Result getItem(int position) {
        return results.get(position);
    }

    public void addAll(List<Result> results) {
        for (Result r : results) {
            add(r);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title, tv_release_date_detail, tv_rating_detail, tv_overview_detail;
        private ImageView img_moviess, img_starMovies;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType == VIEW_TYPE_LIST) {
                tv_title = itemView.findViewById(R.id.tv_title);
                tv_release_date_detail = itemView.findViewById(R.id.tv_release_date_detail);
                tv_rating_detail = itemView.findViewById(R.id.tv_rating_detail);
                tv_overview_detail = itemView.findViewById(R.id.tv_overview_detail);
                img_moviess = itemView.findViewById(R.id.img_moviess);
                img_starMovies = itemView.findViewById(R.id.img_starMovies);
            } else {
                tv_title = itemView.findViewById(R.id.tv_title1);
                img_moviess = itemView.findViewById(R.id.img_avatar1);
            }
        }
    }

    public int filter(List<Result> listMovies){
        results.remove(null);
        String year = sharedPreferences.getString(RELEASE_YEAR, "");
        String sortBy = sharedPreferences.getString(SORT_BY, "");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Result> listFilter = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (Result moviesDetail: listMovies){
            Date date;
            try {
                date = simpleDateFormat.parse(moviesDetail.getReleaseDate());
                calendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
//            if (moviesDetail.getVoteAverage() >= sortBy && calendar.get(Calendar.YEAR) >= year) {
                listFilter.add(moviesDetail);
//            }
        }
        results.addAll(listFilter);
        notifyDataSetChanged();
        return listFilter == null ? 0 : listFilter.size();
    }
}
