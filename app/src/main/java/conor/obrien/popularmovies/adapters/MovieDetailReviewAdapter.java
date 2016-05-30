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
import conor.obrien.popularmovies.model.Review;
import conor.obrien.popularmovies.model.ReviewList;

/**
 * Adapter for the movie grid main view.
 *
 * @author Conor O'Brien
 */

public class MovieDetailReviewAdapter extends ArrayAdapter<Review> {

    private final List<Review> mReviews;

    public MovieDetailReviewAdapter(Context context, ReviewList reviewList) {
        super(context, -1, reviewList.getResults());
        mReviews = reviewList.getResults();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_review_item, viewGroup, false);
        ReviewViewHolder holder = new ReviewViewHolder(view, i);

        view.setTag(holder);
        return view;
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        private TextView mReviewAuthor, mReviewContent;
        private Review mReview;

        public ReviewViewHolder(View reviewView, int position) {
            super(reviewView);
            mReviewAuthor = (TextView) reviewView.findViewById(R.id.textview_review_author);
            mReviewContent = (TextView) reviewView.findViewById(R.id.textview_review_content);

            mReview = mReviews.get(position);
            mReviewAuthor.setText(mReview.getAuthor());
            mReviewContent.setText(mReview.getContent());
        }
    }
}
