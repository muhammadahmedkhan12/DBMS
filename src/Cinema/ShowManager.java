package Cinema;

import Database.DBConnection;
import Movie.Movie;
import Movie.MovieManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;


public class ShowManager {

    // Save all shows of a screen to the database
    public static void saveShows(Screen screen) {
        String query = "INSERT INTO Shows (ShowID, MovieID, ScreenID, ShowTime) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {
            for (Show show : screen.getShows()) {
                try (PreparedStatement stmt = con.prepareStatement(query)) {
                    stmt.setInt(1, show.getShowId());
                    stmt.setInt(2, show.getMovie().getMovieid());
                    stmt.setString(3, screen.getScreenId());
                    stmt.setString(4, show.getShowTime());
                    stmt.executeUpdate();
                }
            }
            System.out.println("Shows saved for screen " + screen.getScreenId());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadShows(Screen screen) {
        String query = "SELECT ShowID, MovieID, ShowTime FROM Shows WHERE ScreenID = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, screen.getScreenId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int showId = rs.getInt("ShowID");
                    int movieId = rs.getInt("MovieID");
                    String showTime = rs.getString("ShowTime");

                    // Get the Movie object from MovieManager
                    MovieManager manager = MovieManager.getManager();
                    Movie movie = manager.getMovieById(movieId);
                    if (movie != null) {
                        Show show = new Show(showId, movie, screen.getScreenId(), showTime);
                        screen.addShow(show);
                    }
                }
            }

            System.out.println("Shows loaded for screen " + screen.getScreenId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void deleteShowFromDB(int showId) {
        String query = "DELETE FROM Shows WHERE ShowID = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, showId);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
