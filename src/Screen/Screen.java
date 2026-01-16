//package Cinema;
//
//import Movie.Movie;
//import java.util.ArrayList;
//import Seat.Seat;
//
//public class Screen {
//    private static int nextScreenNumber = 1;
//    private String type;
//    private ArrayList<Movie> movies;
//    private ArrayList<String> showtimes;
//    private String showtime;
//    private String screenid;
//    private int numberOfSeats;
//    private Seat[] seats;
//
//    public Screen(String type, int numberOfSeats){
//        this.screenid = "S" + nextScreenNumber++;
//        this.type = type;
//        this.movies = new ArrayList<>();
//        this.showtimes = new ArrayList<>();
//        this.numberOfSeats = numberOfSeats;
//        this.seats = new Seat[numberOfSeats];
//        for (int i = 0; i < numberOfSeats; i++) {
//            this.seats[i] = new Seat("Seat" + (i + 1), false);
//        }
//    }
//
//    public Screen(String screenid, String type, int numberOfSeats){
//        this.screenid = screenid;
//        this.type = type;
//        this.movies = new ArrayList<>();
//        this.showtimes = new ArrayList<>();
//        this.numberOfSeats = numberOfSeats;
//        this.seats = new Seat[numberOfSeats];
//        for (int i = 0; i < numberOfSeats; i++) {
//            this.seats[i] = new Seat("Seat" + (i + 1), false);
//        }
//    }
//
//    public static void resetScreenNumber() {
//        nextScreenNumber = 1;
//    }
//
//    public int getNumberOfSeats() {
//        return numberOfSeats;
//    }
//
//    public Seat[] getSeats() {
//        return seats;
//    }
//
//    public ArrayList<String> getShowtimes() {
//        return showtimes;
//    }
//    public String getScreenid() {
//        return screenid;
//    }
//    public void addMovie(Movie movie, String showtime) {
//        movies.add(movie);
//        showtimes.add(showtime);
//    }
//
//    public ArrayList<Movie> getMovies() {
//        return this.movies;
//    }
//
//    public String getShowtime() {
//        return this.showtime;
//    }
//
//    public String getScreentype() {
//        return this.type;
//    }
//
//    public void setShowtime(String showtime) {
//        this.showtime = showtime;
//    }
//
//    public void setScreentype(String type) {
//        this.type = type;
//    }
//}
package Screen;

import Show.Show;
import Database.DBConnection;

import java.util.ArrayList;
import java.sql.*;

public class Screen {
    private String screenId;
    private String cinemaId;
    private String screenType;
    private int seatCapacity;


    private ArrayList<Show> shows = new ArrayList<>();

    public Screen(String screenId, String cinemaId, String screenType, int seatCapacity) {
        this.screenId = screenId;
        this.cinemaId = cinemaId;
        this.screenType = screenType;
        this.seatCapacity = seatCapacity;

    }
    public static String getNextScreenId() {
        try (Connection con = DBConnection.getConnection()){
            String query = "SELECT MAX(ScreenID) AS MaxID FROM Screens";
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String maxId = rs.getString("MaxID");
                if (maxId != null) {
                    int num = Integer.parseInt(maxId.substring(1)); // remove 'S'
                    return "S" + (num + 1);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "S1";
    }

    public String getScreenId() { return screenId; }
    public String getCinemaId() { return cinemaId; }
    public String getScreenType() { return screenType; }
    public int getSeatCapacity() { return seatCapacity; }
    public ArrayList<Show> getShows() { return shows; }

    public void setScreenType(String screenType) { this.screenType = screenType; }
    public void setSeatCapacity(int seatCapacity) { this.seatCapacity = seatCapacity; }

    public void addShow(Show show) {
        shows.add(show);
    }

    public void removeShow(int showId) {
        shows.removeIf(s -> s.getShowId() == showId);
    }
}
