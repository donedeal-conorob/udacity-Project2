package conor.obrien.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import conor.obrien.popularmovies.R;
import conor.obrien.popularmovies.model.Trailer;
import conor.obrien.popularmovies.model.TrailerList;

/**
 * Adapter for the movie grid main view.
 *
 * @author Conor O'Brien
 */

public class MovieDetailTrailerAdapter extends ArrayAdapter<Trailer> {

    private final Context mContext;
    private final List<Trailer> mTrailers;
    private TrailerAdapterCallbacks mTrailerAdapterCallbacks;

    public MovieDetailTrailerAdapter(Context context, TrailerList trailerList, TrailerAdapterCallbacks trailerAdapterCallbacks) {
        super(context, -1, trailerList.getResults());
        mContext = context;
        mTrailers = trailerList.getResults();
        mTrailerAdapterCallbacks = trailerAdapterCallbacks;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_trailer_item, viewGroup, false);
        TrailerViewHolder holder = new TrailerViewHolder(view, i);

        view.setTag(holder);
        return view;
    }

    public interface TrailerAdapterCallbacks {
        void startMovieTrailerActivity(Trailer movieTrailer);
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTrailerName;
        private Trailer mTrailer;
        private int mPosition;

        public TrailerViewHolder(View trailerView, int position) {
            super(trailerView);
            mPosition = position;
            mTrailerName = (TextView) trailerView.findViewById(R.id.textview_trailername);
            mTrailerName.setText(mTrailers.get(mPosition).getName());

            trailerView.setClickable(true);
            trailerView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mTrailer = mTrailers.get(mPosition);

            mTrailerAdapterCallbacks.startMovieTrailerActivity(mTrailer);
        }
    }

}
