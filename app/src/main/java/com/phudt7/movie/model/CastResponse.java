package com.phudt7.movie.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CastResponse {

    private List<Cast> cast;

    public CastResponse(List<Cast> casts) {
        this.cast = casts;
    }

    public List<Cast> getCasts() {
        return cast;
    }

    public void setCasts(List<Cast> casts) {
        this.cast = casts;
    }
}
