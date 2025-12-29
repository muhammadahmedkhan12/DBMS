package Movie;

import Cinema.CinemaManager;
import Cinema.Screen;
import Cinema.Cinema;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class MovieManager {
    private ArrayList<Movie> movies = new ArrayList<>() ;
    private static MovieManager manager = new MovieManager();

    public static MovieManager getManager() {
        return manager;
    }

    public MovieManager(){
        loadmovies();
    }

    public void addmovie(Movie movie){
        movies.add(movie);
        savemovies();
    }

    public boolean editmovies(int movieid , String newname , String newrating){
        boolean found = false;
        for(Movie m : movies){
            if (m.getMovieid() == movieid){
                m.setMoviename(newname);
                m.setRating(newrating);
                found = true;
                break;
            }
        }
        if (found){
            savemovies();

            CinemaManager cinemaManager = CinemaManager.getCinemaManager();
            for (int i = 0; i < cinemaManager.getCinemas().size(); i++) {
                Cinema c = cinemaManager.getCinemas().get(i);
                for (int j = 0; j < c.getScreens().size(); j++) {
                    Screen screen = c.getScreens().get(j);
                    for (int k = 0; k < screen.getMovies().size(); k++) {
                        Movie movie = screen.getMovies().get(k);
                        if (movie.getMovieid() == movieid) {
                            movie.setMoviename(newname);
                            movie.setRating(newrating);
                        }
                    }
                }
            }
            cinemaManager.savecinemas();
            return true;

        }
        return false;
    }
    public void deletemovies(int movieid){
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getMovieid() == movieid){
                movies.remove(i);
                break;
            }
        }
        savemovies();
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }
    public void loadmovies() {
        movies.clear();
        int maxId = 0;
        try (Connection con = Database.DBConnection.getConnection()) {
            String sql = "SELECT movie_id, moviename, rating FROM dbo.movie ORDER BY movie_id";
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("movie_id");
                    String name = rs.getString("moviename");
                    String rating = rs.getString("rating");
                    movies.add(new Movie(id, name, rating));
                    if (id > maxId) maxId = id;
                }
            }
            Movie.resetNextMovieId(maxId + 1);
        } catch (Exception e) {
            e.printStackTrace();
            // optionally fall back to file loading or rethrow
        }
    }

    public void savemovies() {
        try (Connection con = Database.DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try (Statement stmt = con.createStatement()) {
                stmt.executeUpdate("DELETE FROM dbo.movie");
            }

            String insert = "INSERT INTO dbo.movie (movie_id, moviename, rating) VALUES (?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(insert)) {
                for (Movie m : movies) {
                    ps.setInt(1, m.getMovieid());
                    ps.setString(2, m.getMoviename());
                    ps.setString(3, m.getRating());
                    ps.executeUpdate();
                }
            }
            con.commit();
            con.setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Movie getmoviebyid(int movieid) {
        for (Movie movie : movies) {
            if (movie.getMovieid() == movieid) {
                return movie;
            }
        }
        return null;
    }
}
