package Show;

import Database.DBConnection;
import Movie.Movie;
import Movie.MovieManager;
import Screen.Screen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ShowManager {


    public static ArrayList<String[]> getShowsByMovieId(int movieId) {
        ArrayList<String[]> showsList = new ArrayList<>();

        String query = """
            SELECT c.name AS cinemaName,
                   sc.screenid,
                   sc.screentype,
                   sc.NumberOfSeats AS seatCapacity,
                   sh.showtime,
                   sh.showid
            FROM Cinemas c
            JOIN Screens sc ON c.cinemaid = sc.cinemaid
            JOIN Shows sh ON sc.screenid = sh.screenid
            WHERE sh.movieid = ?
            ORDER BY c.name, sh.showtime
            """;

        try (Connection con = DBConnection.getConnection()){
             PreparedStatement stmt = con.prepareStatement(query);

            stmt.setInt(1, movieId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String[] showInfo = new String[6];
                showInfo[0] = rs.getString("cinemaName");
                showInfo[1] = rs.getString("screenid");
                showInfo[2] = rs.getString("screentype");
                showInfo[3] = rs.getString("showtime");
                showInfo[4] = String.valueOf(rs.getInt("seatCapacity"));
                showInfo[5] = String.valueOf(rs.getInt("showid"));

                showsList.add(showInfo);
            }

            System.out.println("Loaded " + showsList.size() + " shows for movie ID: " + movieId);

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return showsList;
    }

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

        try (Connection con = DBConnection.getConnection()){
             PreparedStatement stmt = con.prepareStatement(query);

            stmt.setString(1, screen.getScreenId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int showId = rs.getInt("ShowID");
                    int movieId = rs.getInt("MovieID");
                    String showTime = rs.getString("ShowTime");

                    MovieManager manager = MovieManager.getManager();
                    Movie movie = manager.getMovieById(movieId);
                    if (movie != null) {
                        Show show = new Show(showId, movie, screen.getScreenId(), showTime);
                        screen.addShow(show);
                    }
                }
            }

            System.out.println("Shows loaded for screen " + screen.getScreenId());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteShowFromDB(int showId) {
        String query = "DELETE FROM Shows WHERE ShowID = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, showId);
            stmt.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateShow(Show show) {
        String query = "UPDATE Shows SET ShowTime = ? WHERE ShowID = ?";

        try (Connection con = DBConnection.getConnection()){
             PreparedStatement stmt = con.prepareStatement(query);

            stmt.setString(1, show.getShowTime());
            stmt.setInt(2, show.getShowId());
            stmt.executeUpdate();

            System.out.println("Show updated: ShowID = " + show.getShowId());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}