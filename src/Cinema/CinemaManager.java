package Cinema;

import Database.DBConnection;
import Movie.Movie;
import Movie.MovieManager;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class CinemaManager {
    private static CinemaManager cinemaManager = new CinemaManager();

    public static CinemaManager getCinemaManager() {
        return cinemaManager;
    }

    private ArrayList<Cinema> cinemas = new ArrayList<>();

    public CinemaManager(){
        loadcinemas();
    }
    public void addcinema(Cinema cinema){
        cinemas.add(cinema);
        savecinemas();
    }
    public String addmovietocinema(Cinema cinema, Screen screen, Movie movie, String showtime){
        int foundcinemaindex = -1;
        boolean foundmovie = false;
        for (int i = 0; i < cinemas.size(); i++) {
            if(cinemas.get(i).getCinemaid().equals(cinema.getCinemaid())){
                foundcinemaindex = i;
            }
        }
        MovieManager manager = MovieManager.getManager();
        for (int i = 0; i < manager.getMovies().size(); i++) {
            if (manager.getMovies().get(i).getMoviename().equals(movie.getMoviename())){
                foundmovie = true;
            }
        }
        if (foundcinemaindex > -1 && foundmovie){
            int index = -1;
            for (int i = 0; i < cinemas.get(foundcinemaindex).getScreens().size(); i++) {
                if (cinemas.get(foundcinemaindex).getScreens().get(i).getScreenid().equals(screen.getScreenid())){
                    index = i;
                }
            }
            cinemas.get(foundcinemaindex).getScreens().get(index).addMovie(movie, showtime);
            savecinemas();
            return "movie added to cinema succefully";
        }
        else {
            return "movei or cinema not found";
        }
    }
    public String deletemoviefromcinema(Cinema cinema, Screen screen, Movie movie){
        int foundcinemaindex = -1;
        for (int i = 0; i < cinemas.size(); i++) {
            if(cinemas.get(i).getCinemaid().equals(cinema.getCinemaid())){
                foundcinemaindex = i;
            }
        }
        int foundscreenindex = -1;
        for (int i = 0; i < cinemas.get(foundcinemaindex).getScreens().size(); i++) {
            if (cinemas.get(foundcinemaindex).getScreens().get(i).getScreenid().equals(screen.getScreenid())){
                foundscreenindex = i;
            }
        }
        int foundmovieindex = -1;
        for (int i = 0; i < cinemas.get(foundcinemaindex).getScreens().get(foundscreenindex).getMovies().size(); i++) {
            if (cinemas.get(foundcinemaindex).getScreens().get(foundscreenindex).getMovies().get(i).getMovieid() == movie.getMovieid()){
                Movie movie1 = cinemas.get(foundcinemaindex).getScreens().get(foundscreenindex).getMovies().remove(i);
                savecinemas();
                return movie1.getMoviename() + " is successfully removed from cinema " + cinemas.get(foundcinemaindex).getName() + "screen " + cinemas.get(foundcinemaindex).getScreens().get(foundscreenindex).getScreenid();
            }
        }
        return "movie not found at cinema";
    }
    public void editcinema(String cinemaid , String newname , String newscreentyoe){
        for(Cinema c : cinemas){
            if (c.getCinemaid().equals(cinemaid)){
                c.setName(newname);
                break;
            }
        }
        savecinemas();
    }

    public void deletecinema(String cinemaid){
        for (int i = 0; i < cinemas.size(); i++) {
            if (cinemas.get(i).getCinemaid().equals(cinemaid)){
                cinemas.remove(i);
                break;
            }
        }
        savecinemas();
    }

    public ArrayList<Cinema> getCinemas() {
        return cinemas;
    }

    // java
    public void loadcinemas() {
        cinemas.clear();
        try (Connection con = DBConnection.getConnection()) {
            String cinemaQuery = "SELECT cinema_id, name FROM cinema";
            try (PreparedStatement psCinema = con.prepareStatement(cinemaQuery);
                 ResultSet rsCinema = psCinema.executeQuery()) {

                while (rsCinema.next()) {
                    String cinemaId = rsCinema.getString("cinema_id");
                    String cinemaName = rsCinema.getString("name");
                    Cinema cinema = new Cinema(cinemaId, cinemaName);

                    String screenQuery = "SELECT screen_id, screen_type, number_of_seats FROM screen WHERE cinema_id = ?";
                    try (PreparedStatement psScreen = con.prepareStatement(screenQuery)) {
                        psScreen.setString(1, cinemaId);
                        try (ResultSet rsScreen = psScreen.executeQuery()) {
                            while (rsScreen.next()) {
                                String screenId = rsScreen.getString("screen_id");
                                String screenType = rsScreen.getString("screen_type");
                                int numberOfSeats = rsScreen.getInt("number_of_seats");

                                Screen screen = new Screen(screenId, screenType, numberOfSeats);

                                String smQuery = "SELECT movie_id, showtime FROM shows WHERE screen_id = ? ORDER BY id";
                                try (PreparedStatement psSM = con.prepareStatement(smQuery)) {
                                    psSM.setString(1, screenId);
                                    try (ResultSet rsSM = psSM.executeQuery()) {
                                        while (rsSM.next()) {
                                            int movieId = rsSM.getInt("movie_id");
                                            String showtime = rsSM.getString("showtime");
                                            Movie movie = MovieManager.getManager().getmoviebyid(movieId);
                                            if (movie != null) {
                                                screen.addMovie(movie, showtime);
                                            }
                                        }
                                    }
                                }

                                cinema.addScreen(screen);
                            }
                        }
                    }

                    cinemas.add(cinema);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void savecinemas() {
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try (Statement stmt = con.createStatement()) {
                // wipe previous data (simpler approach). Adjust if you want upserts instead.
                stmt.executeUpdate("DELETE FROM shows");
                stmt.executeUpdate("DELETE FROM screen");
                stmt.executeUpdate("DELETE FROM cinema");
            }

            String insertCinema = "INSERT INTO cinema (cinema_id, name) VALUES (?, ?)";
            String insertScreen = "INSERT INTO screen (screen_id, cinema_id, screen_type, number_of_seats) VALUES (?, ?, ?, ?)";
            String insertScreenMovie = "INSERT INTO shows (screen_id, movie_id, showtime) VALUES (?, ?, ?)";

            try (PreparedStatement psCinema = con.prepareStatement(insertCinema);
                 PreparedStatement psScreen = con.prepareStatement(insertScreen);
                 PreparedStatement psSM = con.prepareStatement(insertScreenMovie)) {

                for (Cinema cinema : cinemas) {
                    psCinema.setString(1, cinema.getCinemaid());
                    psCinema.setString(2, cinema.getName());
                    psCinema.executeUpdate();

                    for (Screen screen : cinema.getScreens()) {
                        psScreen.setString(1, screen.getScreenid());
                        psScreen.setString(2, cinema.getCinemaid());
                        psScreen.setString(3, screen.getScreentype());
                        psScreen.setInt(4, screen.getNumberOfSeats());
                        psScreen.executeUpdate();

                        ArrayList<Movie> movies = screen.getMovies();
                        ArrayList<String> showtimes = screen.getShowtimes();
                        for (int k = 0; k < movies.size(); k++) {
                            psSM.setString(1, screen.getScreenid());
                            psSM.setInt(2, movies.get(k).getMovieid());
                            psSM.setString(3, showtimes.get(k));
                            psSM.executeUpdate();
                        }
                    }
                }

                con.commit();
            } catch (Exception ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}