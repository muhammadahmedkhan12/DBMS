package Cinema;

import Movie.Movie;

public class Show {
    private int showId;
    private Movie movie;    // the movie object
    private String screenId; // the screen where it plays
    private String showTime;

    public Show(int showId, Movie movie, String screenId, String showTime) {
        this.showId = showId;
        this.movie = movie;
        this.screenId = screenId;
        this.showTime = showTime;
    }

    // Getters
    public int getShowId() { return showId; }
    public Movie getMovie() { return movie; }
    public String getScreenId() { return screenId; }
    public String getShowTime() { return showTime; }

    // Setters if needed
    public void setShowTime(String showTime) { this.showTime = showTime; }

    public void setShowId(int showId) {
        this.showId = showId;
    }
}
