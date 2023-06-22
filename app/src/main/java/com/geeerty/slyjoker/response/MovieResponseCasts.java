package com.geeerty.slyjoker.response;

import com.geeerty.slyjoker.model.Casts;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResponseCasts {
    @SerializedName("cast")
    private List<Casts> casts;

    public List<Casts> getCasts() {
        return casts;
    }

    public void setCasts(List<Casts> casts) {
        this.casts = casts;
    }
}
