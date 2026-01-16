package Cinema;

import Database.DBConnection;
import Movie.Movie;
import Screen.Screen;
import Show.Show;
import Show.ShowManager;

import java.sql.*;
import java.util.ArrayList;

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
    public String addMovieToCinema(Cinema cinema, Screen screen, Movie movie, String showtime) {
        Cinema foundCinema = null;
        for (Cinema c : cinemas) {
            if (c.getCinemaid().equals(cinema.getCinemaid())) {
                foundCinema = c;
                break;
            }
        }
        if (foundCinema == null) return "Cinema not found";

        Screen foundScreen = null;
        for (Screen s : foundCinema.getScreens()) {
            if (s.getScreenId().equals(screen.getScreenId())) {
                foundScreen = s;
                break;
            }
        }
        if (foundScreen == null) return "Screen not found";


        try (Connection con = DBConnection.getConnection()){

            String query = "INSERT INTO Shows (MovieID, ScreenID, ShowTime) VALUES (?, ?, ?)";

            PreparedStatement stmt = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, movie.getMovieid());
            stmt.setString(2, foundScreen.getScreenId());
            stmt.setString(3, showtime);
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int newShowId = generatedKeys.getInt(1);
                    Show show = new Show(newShowId, movie, foundScreen.getScreenId(), showtime);
                    foundScreen.addShow(show);
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            return "Error adding movie to cinema";
        }

        return "Movie '" + movie.getMoviename() + "' added to cinema '" +
                foundCinema.getName() + "' on screen '" + foundScreen.getScreenId() +
                "' at " + showtime;
    }



    public String deleteMovieFromCinema(Cinema cinema, Screen screen, Movie movie) {
        Show toRemove = null;

        for (Show s : screen.getShows()) {
            if (s.getMovie().getMovieid() == movie.getMovieid()) {
                toRemove = s;
                break;
            }
        }

        if (toRemove != null) {
            screen.removeShow(toRemove.getShowId());
            ShowManager.saveShows(screen);
            return movie.getMoviename() + " is successfully removed from cinema "
                    + cinema.getName() + " screen " + screen.getScreenId();
        }

        return "Movie not found on this screen";
    }

    public void editcinema(String cinemaId, String newName) {
        String query = "UPDATE Cinemas SET Name = ? WHERE CinemaID = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, newName);
            stmt.setString(2, cinemaId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Cinema updated successfully in DB");
            }
            else {
                System.out.println("Cinema ID not found in DB");
            }
            for (Cinema c : cinemas) {
                if (c.getCinemaid().equals(cinemaId)) {
                    c.setName(newName);
                    break;
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deletecinema(String cinemaId) {
        String query = "DELETE FROM Cinemas WHERE CinemaID = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, cinemaId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Cinema deleted successfully from DB");
            }
            else {
                System.out.println("Cinema ID not found in DB");
            }

            cinemas.removeIf(c -> c.getCinemaid().equals(cinemaId));

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ArrayList<Cinema> getCinemas() {
        return cinemas;
    }

public void savecinemas() {
    String insertQuery = "INSERT INTO Cinemas (CinemaID, Name) VALUES (?, ?)";
    String checkQuery = "SELECT COUNT(*) FROM Cinemas WHERE CinemaID = ?";

    try (Connection con = DBConnection.getConnection()) {

        for (Cinema cinema : cinemas) {

            try (PreparedStatement checkStmt = con.prepareStatement(checkQuery)) {
                checkStmt.setString(1, cinema.getCinemaid());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    rs.next();
                    int count = rs.getInt(1);
                    if (count > 0) {
                        System.out.println("Cinema " + cinema.getCinemaid() + " already exists. Skipping insert.");
                        continue;
                    }
                }
            }
            try (PreparedStatement stmt = con.prepareStatement(insertQuery)) {
                stmt.setString(1, cinema.getCinemaid());
                stmt.setString(2, cinema.getName());
                stmt.executeUpdate();
            }
        }

        System.out.println("Cinemas saved to database successfully!");
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    public boolean cinemaExists(String cinemaId) {
        for (Cinema c : cinemas) {
            if (c.getCinemaid().equals(cinemaId)) {
                return true;
            }
        }
        return false;
    }

    public void loadcinemas() {
        cinemas.clear();
        String query = "SELECT CinemaID, Name FROM Cinemas";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String cinemaId = rs.getString("CinemaID");
                String name = rs.getString("Name");

                Cinema cinema = new Cinema(cinemaId, name);
                cinemas.add(cinema);
            }

            System.out.println("Cinemas loaded from database successfully!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


}