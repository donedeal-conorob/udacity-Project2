package conor.obrien.popularmovies.resources;

/**
 * Created by Conor O'Brien on 12/11/15.
 * <p/>
 * Class to store app constants
 */
public class Constants {

    public static class URLs {

        public static final String API_URL = "http://api.themoviedb.org/3";
        public static final String POSTER_URL_SMALL = "http://image.tmdb.org/t/p/w185/";
        public static final String POSTER_URL_MEDIUM = "http://image.tmdb.org/t/p/w370/";
        public static final String POSTER_URL_LARGE = "http://image.tmdb.org/t/p/w500/";
    }

    public static class Keys {

        public static final String MOVIE_RESULT_KEY = "conor.obrien.popularmovies.MovieResult";
    }

    public static class SortBy {

        public static final String POPULARITY_DESC = "popularity.desc";
        public static final String VOTE_AVERAGE_DESC = "vote_average.desc";
    }

    public static class SharedPreferences {
        public static final String FAVOURITE_MOVIES = "favouriteMovies";
    }

    public static class FragmentTags {
        public static final String MOVIE_DETAIL = "movieDetail";
    }
}
