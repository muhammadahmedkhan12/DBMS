package Movie;

import Database.DBConnection;

import java.sql.*;
import java.util.ArrayList;

public class MovieManager {
    private static MovieManager manager;
    private ArrayList<Movie> movies;

    public MovieManager() {
        movies = new ArrayList<>();
        loadMoviesFromDB();
    }

    public static MovieManager getManager() {
        if (manager == null) {
            manager = new MovieManager();
        }
        return manager;
    }

    public void loadMoviesFromDB() {
        movies.clear();

        String query = "SELECT MovieID, MovieName, Rating FROM Movies";

        try (Connection con = DBConnection.getConnection()){
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery() ;

            while (rs.next()) {
                Movie m = new Movie(
                        rs.getInt("MovieID"),
                        rs.getString("MovieName"),
                        rs.getString("Rating")
                );
                movies.add(m);
            }

            System.out.println("Movies loaded from DB");

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void addMovie(Movie movie) {
        String query = "INSERT INTO Movies (MovieID,MovieName, Rating) VALUES (?,?,?)";

        try (Connection con = DBConnection.getConnection()){
             PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, movie.getMovieid());
            stmt.setString(2, movie.getMoviename());
            stmt.setString(3, movie.getRating());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                movie.setMovieid(keys.getInt(1));
            }
            loadMoviesFromDB();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean deleteMovie(int movieId) {

        String query = "DELETE FROM Movies WHERE MovieID = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setInt(1, movieId);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                loadMoviesFromDB();
                return true;
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean editMovie(int movieId, String newName, String newRating) {

        String query = "UPDATE Movies SET MovieName = ?, Rating = ? WHERE MovieID = ?";

        try (Connection con = DBConnection.getConnection()){
             PreparedStatement stmt = con.prepareStatement(query);

            stmt.setString(1, newName);
            stmt.setString(2, newRating);
            stmt.setInt(3, movieId);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                loadMoviesFromDB();
                return true;
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public Movie getMovieById(int movieId) {
        for (Movie m : movies) {
            if (m.getMovieid() == movieId) {
                return m;
            }
        }
        return null;
    }
    public Movie getMovieByName(String movieName) {
        for (Movie m : movies) {
            if (m.getMoviename().equalsIgnoreCase(movieName)) {
                return m;
            }
        }
        String sql = "SELECT MovieID, MovieName, Rating FROM Movies WHERE MovieName = ?";

        try (Connection con = DBConnection.getConnection()){
             PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, movieName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Movie movie = new Movie(
                        rs.getInt("MovieID"),
                        rs.getString("MovieName"),
                        rs.getString("Rating")
                );

                movies.add(movie);

                System.out.println("Movie found: " + movieName + " (ID: " + movie.getMovieid() + ")");
                return movie;
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
