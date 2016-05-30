package conor.obrien.popularmovies.model;

import java.util.List;

/**
 * Created by Conor O'Brien on 12/11/15.
 */
public class MovieList {

    private int page;
    private int total_pages;
    private int total_results;
    private List<MovieResult> results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPages() {
        return total_pages;
    }

    public void setTotalPages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotalResults() {
        return total_results;
    }

    public void setTotalResults(int total_results) {
        this.total_results = total_results;
    }

    public List<MovieResult> getResults() {
        return results;
    }

    public void setResults(List<MovieResult> results) {
        this.results = results;
    }
}
