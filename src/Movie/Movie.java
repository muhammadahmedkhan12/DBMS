package Movie;

public class Movie {
    private static int nextMovieId = 1;
    private int movieid;
    private String moviename;
    private String rating;

    public Movie(String moviename, String rating) {
        this.movieid = nextMovieId++;
        this.moviename = moviename;
        this.rating = rating;
    }

    public Movie(int movieid, String moviename, String rating) {
        this.movieid = movieid;
        this.moviename = moviename;
        this.rating = rating;
        if (movieid >= nextMovieId) {
            nextMovieId = movieid + 1;
        }
    }

    public static void resetNextMovieId(int nextId) {
        nextMovieId = nextId;
    }

    public void setMoviename(String moviename) {
        this.moviename = moviename;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getMoviename() {
        return moviename;
    }

    public int getMovieid() {
        return movieid;
    }

    public String getRating() {
        return rating;
    }
}
