package conor.obrien.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import conor.obrien.popularmovies.R;
import conor.obrien.popularmovies.model.MovieResult;
import conor.obrien.popularmovies.resources.Constants;

/**
 * Created by Conor O'Brien on 12/11/15.
 * <p/>
 * Adapter for the movie grid main view.
 */
public class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.MovieViewHolder> {

    List<MovieResult> mMovieList;
    MovieResult mMovieResult;
    Context mContext;
    private MovieGridAdapterCallbacks mMovieGridAdapterCallbacks;

    public MovieGridAdapter(Context context, MovieGridAdapterCallbacks movieGridAdapterCallbacks) {
        this.mContext = context;
        this.mMovieGridAdapterCallbacks = movieGridAdapterCallbacks;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_grid_item, viewGroup, false);
        return new MovieViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder viewHolder, int i) {
        mMovieResult = mMovieList.get(i);

        Picasso.with(viewHolder.mMoviePoster.getContext()).load(Constants.URLs.POSTER_URL_MEDIUM + mMovieResult.getPosterPath()).into(viewHolder.
                mMoviePoster);
    }

    @Override
    public int getItemCount() {

        if (mMovieList != null) {
            return mMovieList.size();
        } else {
            return -1;
        }
    }

    public void setMovieList(List<MovieResult> movieList) {
        this.mMovieList = movieList;
    }

    public interface MovieGridAdapterCallbacks {
        void startMovieDetailsView(MovieResult movieResult);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mMoviePoster;

        public MovieViewHolder(View movieView, Context context) {
            super(movieView);
            mMoviePoster = (ImageView) movieView.findViewById(R.id.img_poster);

            movieView.setClickable(true);
            movieView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mMovieResult = mMovieList.get(getAdapterPosition());

            mMovieGridAdapterCallbacks.startMovieDetailsView(mMovieResult);
        }
    }
}