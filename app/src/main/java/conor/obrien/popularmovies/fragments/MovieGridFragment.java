package conor.obrien.popularmovies.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import conor.obrien.popularmovies.R;
import conor.obrien.popularmovies.adapters.MovieGridAdapter;
import conor.obrien.popularmovies.model.MovieResult;
import conor.obrien.popularmovies.resources.Constants;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieGridFragment extends Fragment implements MovieGridAdapter.MovieGridAdapterCallbacks {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    MovieGridAdapter mAdapter;
    MovieGridFragmentCallbacks mMovieGridFragmentCallbacks;

    public MovieGridFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_grid, container, false);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mAdapter = new MovieGridAdapter(getActivity(), this);
        mRecyclerView.setAdapter(mAdapter);

        mMovieGridFragmentCallbacks = (MovieGridFragmentCallbacks) getActivity();

        mLayoutManager = new GridLayoutManager(view.getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!mMovieGridFragmentCallbacks.hasMovies()) {
            mMovieGridFragmentCallbacks.getMovies(Constants.SortBy.POPULARITY_DESC);
        }
    }

    public void populateMovieGrid(List<MovieResult> movieResults) {
        mAdapter.setMovieList(movieResults);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void startMovieDetailsView(MovieResult movieResult) {
        mMovieGridFragmentCallbacks.onMovieGridItemClicked(movieResult);
    }

    public interface MovieGridFragmentCallbacks {
        void onMovieGridItemClicked(MovieResult movieResult);

        void getMovies(String sortBy);

        Boolean hasMovies();
    }
}
