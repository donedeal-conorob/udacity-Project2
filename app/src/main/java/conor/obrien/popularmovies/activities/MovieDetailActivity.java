package conor.obrien.popularmovies.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;
import java.util.Set;

import conor.obrien.popularmovies.R;
import conor.obrien.popularmovies.api.DiscoverMovieInterface;
import conor.obrien.popularmovies.fragments.MovieDetailFragment;
import conor.obrien.popularmovies.model.MovieResult;
import conor.obrien.popularmovies.model.Review;
import conor.obrien.popularmovies.model.ReviewList;
import conor.obrien.popularmovies.model.Trailer;
import conor.obrien.popularmovies.model.TrailerList;
import conor.obrien.popularmovies.resources.Api;
import conor.obrien.popularmovies.resources.Constants;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailFragment.MovieDetailFragmentCallbacks {

    MovieDetailFragment mMovieDetailFragment;
    MovieResult mMovieResult;
    RestAdapter mRestAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setEndpoint(Constants.URLs.API_URL).build();
    DiscoverMovieInterface mMovieApi = mRestAdapter.create(DiscoverMovieInterface.class);

    List<Trailer> mTrailerList;
    List<Review> mReviewList;

    boolean mIsFavourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mMovieDetailFragment = (MovieDetailFragment) getSupportFragmentManager().findFragmentById(R.id.movie_detail_fragment);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras.containsKey(Constants.Keys.MOVIE_RESULT_KEY)) {
            mMovieResult = extras.getParcelable(Constants.Keys.MOVIE_RESULT_KEY);
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mIsFavourite) {
            mMovieDetailFragment.fillFavouriteStar();
        }
    }

    public void getTrailers(String id) {
        mMovieApi.getTrailers(id, Api.Keys.API_KEY, new Callback<TrailerList>() {
            @Override
            public void success(TrailerList trailerList, Response response) {
                mTrailerList = trailerList.getResults();
                mMovieDetailFragment.populateTrailerList(trailerList);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(this.getClass().getSimpleName(), error.toString());
            }
        });
    }

    public void getReviews(String id) {
        mMovieApi.getReviews(id, Api.Keys.API_KEY, new Callback<ReviewList>() {
            @Override
            public void success(ReviewList reviewList, Response response) {
                mReviewList = reviewList.getResults();
                mMovieDetailFragment.populateReviewList(reviewList);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(this.getClass().getSimpleName(), error.toString());
            }
        });
    }

    @Override
    public MovieResult getMovieResult() {
        return mMovieResult;
    }

    @Override
    public void startMovieTrailerActivity(Trailer movieTrailer) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + movieTrailer.getKey())));
    }

    @Override
    public boolean isFavourite() {
        return mIsFavourite;
    }

    @Override
    public void setIsFavourite(boolean isFavourite) {
        mIsFavourite = isFavourite;
    }

    public void populateMovieDetailFragment() {
        if (mMovieResult != null) {
            if (mTrailerList == null) {
                getTrailers(String.valueOf(mMovieResult.getId()));
            }
            if (mReviewList == null) {
                getReviews(String.valueOf(mMovieResult.getId()));
            }

            SharedPreferences prefs = getSharedPreferences(Constants.SharedPreferences.FAVOURITE_MOVIES, 0);
            Set<String> favourites = prefs.getAll().keySet();
            if (favourites.contains(String.valueOf(mMovieResult.getId()))) {
                mIsFavourite = true;
            }
        }
    }
}
