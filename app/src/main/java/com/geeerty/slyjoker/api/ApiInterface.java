package com.geeerty.slyjoker.api;

import com.geeerty.slyjoker.Utils.Constants;
import com.geeerty.slyjoker.response.MovieResponse;
import com.geeerty.slyjoker.response.MovieResponseCasts;
import com.geeerty.slyjoker.response.MovieResponseReviews;
import com.geeerty.slyjoker.response.MovieResponseVideos;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {
    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<MovieResponseVideos> getMovieTrailers(@Path(Constants.JSON_OBJECT_ID) int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<MovieResponseReviews> getMovieReviews(@Path(Constants.JSON_OBJECT_ID) int id, @Query("api_key") String apiKey);

    //@GET("movie/{id}")
    //Call<MovieResponse> getMovieDetails(@Path(Constants.JSON_OBJECT_ID) int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/casts")
    Call<MovieResponseCasts> getMovieCast(@Path(Constants.JSON_OBJECT_ID) int id, @Query("api_key") String apiKey);
}
