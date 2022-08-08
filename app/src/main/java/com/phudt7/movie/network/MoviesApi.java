package com.phudt7.movie.network;

import com.phudt7.movie.model.CastResponse;
import com.phudt7.movie.model.MovieListResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesApi {
    @GET("3/movie/popular?api_key=e7631ffcb8e766993e5ec0c1f4245f93")
    Call<MovieListResponse> getPage(@Query("page") int page);

    @GET("3/movie/top_rated?api_key=e7631ffcb8e766993e5ec0c1f4245f93")
    Call<MovieListResponse> getTopRated(@Query("page") int page);

    @GET("3/movie/upcoming?api_key=e7631ffcb8e766993e5ec0c1f4245f93")
    Call<MovieListResponse> getUpcoming(@Query("page") int page);

    @GET("3/movie/now_playing?api_key=e7631ffcb8e766993e5ec0c1f4245f93")
    Call<MovieListResponse> getNowplaying(@Query("page") int page);

    @GET("3/movie/{movieId}/credits")
    Call<CastResponse> getCast(@Path("movieId") int id, @Query("api_key") String api_key);

    @GET("3/movie/popular?api_key=e7631ffcb8e766993e5ec0c1f4245f93&page=3")
    Call<List<MovieListResponse>> getMoviesList();
}
