package conor.obrien.popularmovies.model;

import java.util.List;

/**
 * An object to hold the returned list of a movie's trailers.
 *
 * @author Conor O'Brien <conorob@donedeal.ie>
 */
public class TrailerList {

    private int id;
    private List<Trailer> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Trailer> getResults() {
        return results;
    }

    public void setResults(List<Trailer> results) {
        this.results = results;
    }
}
