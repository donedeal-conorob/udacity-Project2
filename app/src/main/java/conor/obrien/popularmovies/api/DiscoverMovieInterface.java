package conor.obrien.popularmovies.api;

import conor.obrien.popularmovies.model.MovieList;
import conor.obrien.popularmovies.model.ReviewList;
import conor.obrien.popularmovies.model.TrailerList;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Conor O'Brien on 12/11/15.
 */
public interface DiscoverMovieInterface {
    @GET("/movie/popular")
    void getPopularMovies(@Query("api_key") String apiKey, Callback<MovieList> movieListCallback);

    @GET("/movie/top_rated")
    void getTopRatedMovies(@Query("api_key") String apiKey, Callback<MovieList> movieListCallback);

    @GET("/movie/{id}/videos")
    void getTrailers(@Path("id") String id, @Query("api_key") String apiKey, Callback<TrailerList> trailerListCallback);

    @GET("/movie/{id}/reviews")
    void getReviews(@Path("id") String id, @Query("api_key") String apiKey, Callback<ReviewList> reviewListCallback);
}
