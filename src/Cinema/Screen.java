package Cinema;

import Movie.Movie;
import java.util.ArrayList;
import Seat.Seat;

public class Screen {
    private static int nextScreenNumber = 1;
    private String type;
    private ArrayList<Movie> movies;
    private ArrayList<String> showtimes;
    private String showtime;
    private String screenid;
    private int numberOfSeats;
    private Seat[] seats;

    public Screen(String type, int numberOfSeats){
        this.screenid = "S" + nextScreenNumber++;
        this.type = type;
        this.movies = new ArrayList<>();
        this.showtimes = new ArrayList<>();
        this.numberOfSeats = numberOfSeats;
        this.seats = new Seat[numberOfSeats];
        for (int i = 0; i < numberOfSeats; i++) {
            this.seats[i] = new Seat("Seat" + (i + 1), false);
        }
    }

    public Screen(String screenid, String type, int numberOfSeats){
        this.screenid = screenid;
        this.type = type;
        this.movies = new ArrayList<>();
        this.showtimes = new ArrayList<>();
        this.numberOfSeats = numberOfSeats;
        this.seats = new Seat[numberOfSeats];
        for (int i = 0; i < numberOfSeats; i++) {
            this.seats[i] = new Seat("Seat" + (i + 1), false);
        }
    }

    public static void resetScreenNumber() {
        nextScreenNumber = 1;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public Seat[] getSeats() {
        return seats;
    }

    public ArrayList<String> getShowtimes() {
        return showtimes;
    }
    public String getScreenid() {
        return screenid;
    }
    public void addMovie(Movie movie, String showtime) {
        movies.add(movie);
        showtimes.add(showtime);
    }

    public ArrayList<Movie> getMovies() {
        return this.movies;
    }

    public String getShowtime() {
        return this.showtime;
    }

    public String getScreentype() {
        return this.type;
    }

    public void setShowtime(String showtime) {
        this.showtime = showtime;
    }

    public void setScreentype(String type) {
        this.type = type;
    }
}