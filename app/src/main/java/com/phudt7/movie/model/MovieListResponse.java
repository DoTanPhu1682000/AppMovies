package com.phudt7.movie.model;

import androidx.room.ColumnInfo;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieListResponse {
    @SerializedName("page")
    @Expose
    @ColumnInfo(name = "page")
    private String page;
    @SerializedName("results")
    @Expose
    @ColumnInfo(name = "results")
    private List<Result> results;
    @SerializedName("total_pages")
    @Expose
    @ColumnInfo(name = "totalPages")
    private String totalPages;
    @SerializedName("total_results")
    @Expose
    @ColumnInfo(name = "totalResults")
    private String totalResults;

    public MovieListResponse(String page, List<Result> results, String totalPages, String totalResults) {
        this.page = page;
        this.results = results;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public String getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(String totalPages) {
        this.totalPages = totalPages;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }
}
