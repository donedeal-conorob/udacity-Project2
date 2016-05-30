package conor.obrien.popularmovies.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import conor.obrien.popularmovies.R;
import conor.obrien.popularmovies.adapters.MovieDetailReviewAdapter;
import conor.obrien.popularmovies.adapters.MovieDetailTrailerAdapter;
import conor.obrien.popularmovies.model.MovieResult;
import conor.obrien.popularmovies.model.ReviewList;
import conor.obrien.popularmovies.model.Trailer;
import conor.obrien.popularmovies.model.TrailerList;
import conor.obrien.popularmovies.resources.Constants;

/**
 * A fragment for showing a Movie's Details.
 */
public class MovieDetailFragment extends Fragment implements MovieDetailTrailerAdapter.TrailerAdapterCallbacks, View.OnClickListener {

    LinearLayout mTrailerContainer, mReviewContainer;
    MovieDetailTrailerAdapter mTrailerAdapter;
    MovieDetailReviewAdapter mReviewAdapter;
    MovieDetailFragmentCallbacks mMovieDetailFragmentCallbacks;
    TextView mMovieTitleTextView, mMovieSynopsisTextView, mMovieUserRatingTextView, mMovieReleaseDateTextView;
    ImageView mMoviePosterImageView, mFavouriteStarImageView;

    public MovieDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        mTrailerContainer = (LinearLayout) view.findViewById(R.id.trailer_container);
        mReviewContainer = (LinearLayout) view.findViewById(R.id.review_container);

        mMovieDetailFragmentCallbacks = (MovieDetailFragmentCallbacks) getActivity();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getView() == null || mMovieDetailFragmentCallbacks.getMovieResult() == null) {
            return;
        }

        mMovieDetailFragmentCallbacks.populateMovieDetailFragment();

        setupViews(mMovieDetailFragmentCallbacks.getMovieResult());
    }

    public void setupViews(MovieResult movieResult) {
        mMovieTitleTextView = (TextView) getView().findViewById(R.id.textview_movie_title);
        mMoviePosterImageView = (ImageView) getView().findViewById(R.id.imageview_movie_poster);
        mMovieSynopsisTextView = (TextView) getView().findViewById(R.id.textview_movie_synopsis);
        mMovieUserRatingTextView = (TextView) getView().findViewById(R.id.textview_user_rating);
        mMovieReleaseDateTextView = (TextView) getView().findViewById(R.id.textview_release_date);
        mFavouriteStarImageView = (ImageView) getView().findViewById(R.id.imageview_movie_favourite);

        mMovieTitleTextView.setText(movieResult.getTitle());
        mMovieSynopsisTextView.setText(movieResult.getOverview());
        mMovieUserRatingTextView.setText(getString(R.string.movie_details_rating) + " " + String.valueOf(movieResult.getVoteAverage()));
        mMovieReleaseDateTextView.setText(getString(R.string.movie_details_release_date) + " " + movieResult.getReleaseDate());
        Picasso.with(getActivity()).load(Constants.URLs.POSTER_URL_LARGE + movieResult.getPosterPath()).into(mMoviePosterImageView);

        mFavouriteStarImageView.setOnClickListener(this);
    }

    public void populateTrailerList(TrailerList trailerList) {
        mTrailerAdapter = new MovieDetailTrailerAdapter(getContext(), trailerList, this);

        final int count = mTrailerAdapter.getCount();
        for (int i = 0; i < count; i++) {
            mTrailerContainer.addView(mTrailerAdapter.getView(i, null, mTrailerContainer));
        }
    }

    public void populateReviewList(ReviewList reviewList) {
        mReviewAdapter = new MovieDetailReviewAdapter(getContext(), reviewList);

        final int count = mReviewAdapter.getCount();
        for (int i = 0; i < count; i++) {
            mReviewContainer.addView(mReviewAdapter.getView(i, null, mReviewContainer));
        }
    }

    public void startMovieTrailerActivity(Trailer movieTrailer) {
        mMovieDetailFragmentCallbacks.startMovieTrailerActivity(movieTrailer);
    }

    @Override
    public void onClick(View v) {
        if (v == null) {
            return;
        }

        if (v == mFavouriteStarImageView) {
            if (mMovieDetailFragmentCallbacks.isFavourite()) {
                unfavouriteMovie();
                emptyFavouriteStar();
            } else {
                fillFavouriteStar();
            }
        }
    }

    public void fillFavouriteStar() {
        mFavouriteStarImageView.setImageDrawable(getResources().getDrawable(android.R.drawable.star_big_on));
        mMovieDetailFragmentCallbacks.getMovieResult().favouriteMovie(getContext());
    }

    public void emptyFavouriteStar() {
        mFavouriteStarImageView.setImageDrawable(getResources().getDrawable(android.R.drawable.star_big_off));
        mMovieDetailFragmentCallbacks.setIsFavourite(false);
    }

    public void unfavouriteMovie() {
        mMovieDetailFragmentCallbacks.getMovieResult().unfavouriteMovie(getContext());
    }

    public interface MovieDetailFragmentCallbacks {
        void populateMovieDetailFragment();
        MovieResult getMovieResult();

        void startMovieTrailerActivity(Trailer movieTrailer);

        boolean isFavourite();

        void setIsFavourite(boolean isFavourite);
    }
}
