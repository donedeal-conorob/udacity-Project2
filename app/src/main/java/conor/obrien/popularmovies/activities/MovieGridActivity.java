package conor.obrien.popularmovies.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import conor.obrien.popularmovies.R;
import conor.obrien.popularmovies.api.DiscoverMovieInterface;
import conor.obrien.popularmovies.fragments.MovieDetailFragment;
import conor.obrien.popularmovies.fragments.MovieGridFragment;
import conor.obrien.popularmovies.model.MovieList;
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

public class MovieGridActivity extends AppCompatActivity implements MovieGridFragment.MovieGridFragmentCallbacks,
        MovieDetailFragment.MovieDetailFragmentCallbacks {

    static final String EXTRA_HAS_MOVIES = "hasMovies";
    static final String EXTRA_MOVIE_RESULTS = "movieResults";
    static final String EXTRA_MOVIE_RESULT = "movieResult";

    MovieGridFragment mMovieGridFragment;
    MovieDetailFragment mMovieDetailFragment;
    RestAdapter restAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setEndpoint(Constants.URLs.API_URL).build();
    DiscoverMovieInterface mMovieApi = restAdapter.create(DiscoverMovieInterface.class);

    List<MovieResult> mMovieResults = new ArrayList<>();
    List<Trailer> mTrailerList;
    List<Review> mReviewList;
    MovieResult mMovieResult;

    boolean mHasMovies, mIsDualPanel, mIsFavourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_grid);

        if (findViewById(R.id.movie_detail_fragment_container) != null) {
            mIsDualPanel = true;

            mMovieDetailFragment = (MovieDetailFragment) getSupportFragmentManager().findFragmentByTag(Constants.FragmentTags.MOVIE_DETAIL);

            if (mMovieDetailFragment == null) {
                getSupportFragmentManager().beginTransaction().add(R.id.movie_detail_fragment_container, new MovieDetailFragment(),
                        Constants.FragmentTags.MOVIE_DETAIL).commit();
            }
        }

        mMovieGridFragment = (MovieGridFragment) getSupportFragmentManager().findFragmentById(R.id.movie_grid_fragment);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mMovieDetailFragment = (MovieDetailFragment) getSupportFragmentManager().findFragmentByTag(Constants.FragmentTags.MOVIE_DETAIL);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(EXTRA_HAS_MOVIES, mHasMovies);
        if (mMovieResults != null) {
            outState.putParcelableArrayList(EXTRA_MOVIE_RESULTS, (ArrayList<? extends Parcelable>) mMovieResults);
        }
        if (mMovieResult != null) {
            outState.putParcelable(EXTRA_MOVIE_RESULT, mMovieResult);
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mHasMovies = savedInstanceState.getBoolean(EXTRA_HAS_MOVIES);

        if (savedInstanceState.containsKey(EXTRA_MOVIE_RESULTS)) {
            mMovieResults = savedInstanceState.getParcelableArrayList(EXTRA_MOVIE_RESULTS);
            mMovieGridFragment.populateMovieGrid(mMovieResults);
        }
        if (savedInstanceState.containsKey(EXTRA_MOVIE_RESULT)) {
            mMovieResult = savedInstanceState.getParcelable(EXTRA_MOVIE_RESULT);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_grid, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort_by_highest_rated) {
            getMovies(Constants.SortBy.VOTE_AVERAGE_DESC);
        } else if (id == R.id.action_sort_by_most_popular) {
            getMovies(Constants.SortBy.POPULARITY_DESC);
        } else if (id == R.id.action_sort_by_favourites) {
            SharedPreferences prefs = getSharedPreferences(Constants.SharedPreferences.FAVOURITE_MOVIES, 0);
            Map<String, ?> favouriteMovies = prefs.getAll();

            mMovieResults = new ArrayList<>();
            Gson gson = new Gson();
            for (Map.Entry<String, ?> entry : favouriteMovies.entrySet()) {
                mMovieResults.add(gson.fromJson((String) entry.getValue(), MovieResult.class));
            }
            mMovieGridFragment.populateMovieGrid(mMovieResults);
            mHasMovies = true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieGridItemClicked(MovieResult movieResult) {
        if (mIsDualPanel) {
            mMovieResult = movieResult;
            if (mMovieDetailFragment != null) {
                mMovieDetailFragment.setupViews(mMovieResult);
                populateMovieDetailFragment();
            }
        } else {
            startMovieDetailsActivity(movieResult);
        }
    }

    private void startMovieDetailsActivity(MovieResult movieResult) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(Constants.Keys.MOVIE_RESULT_KEY, movieResult);
        intent.putExtras(extras);
        startActivity(intent);
    }

    public void getMovies(String sortby) {
        if (Constants.SortBy.POPULARITY_DESC.equals(sortby)) {
            mMovieApi.getPopularMovies(Api.Keys.API_KEY, new Callback<MovieList>() {
                @Override
                public void success(MovieList movieList, Response response) {
                    mMovieResults = movieList.getResults();
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(this.getClass().getSimpleName(), error.toString());
                }
            });
        } else {
            mMovieApi.getTopRatedMovies(Api.Keys.API_KEY, new Callback<MovieList>() {
                @Override
                public void success(MovieList movieList, Response response) {
                    mMovieResults = movieList.getResults();
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(this.getClass().getSimpleName(), error.toString());
                }
            });
        }
        mMovieGridFragment.populateMovieGrid(mMovieResults);
        mHasMovies = true;
    }

    @Override
    public Boolean hasMovies() {
        return mHasMovies;
    }

    @Override
    public void populateMovieDetailFragment() {
        mTrailerList = null;
        mReviewList = null;
        mIsFavourite = false;
        mMovieDetailFragment.emptyFavouriteStar();

        if (mMovieResult != null) {

            getTrailers(String.valueOf(mMovieResult.getId()));
            getReviews(String.valueOf(mMovieResult.getId()));

            SharedPreferences prefs = getSharedPreferences(Constants.SharedPreferences.FAVOURITE_MOVIES, 0);
            Set<String> favourites = prefs.getAll().keySet();

            if (favourites.contains(String.valueOf(mMovieResult.getId()))) {
                mMovieDetailFragment.fillFavouriteStar();
                setIsFavourite(true);
            }
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
}
