package Screen;

import Cinema.Cinema;
import Database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import Show.ShowManager;
import Show.Show;

public class ScreenManager {

    // Save a single screen to the database
    public static void saveScreen(Screen screen) {

        try (Connection con = DBConnection.getConnection()){

            String query = "INSERT INTO Screens (ScreenID, CinemaID, ScreenType, NumberOfSeats) VALUES (?, ?, ?, ?)";

            PreparedStatement stmt = con.prepareStatement(query);

            stmt.setString(1, screen.getScreenId());
            stmt.setString(2, screen.getCinemaId()); // <-- must provide CinemaID
            stmt.setString(3, screen.getScreenType());
            stmt.setInt(4, screen.getSeatCapacity());

            stmt.executeUpdate();
            System.out.println("Screen " + screen.getScreenId() + " saved successfully.");

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadScreens(Cinema cinema) {
        int maxId=0;

        try (Connection con = DBConnection.getConnection()){
            String query = "SELECT ScreenID, ScreenType, NumberOfSeats FROM Screens WHERE CinemaID = ?";
            PreparedStatement stmt = con.prepareStatement(query) ;

            stmt.setString(1, cinema.getCinemaid());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String screenId = rs.getString("ScreenID");
                    String type = rs.getString("ScreenType");
                    int seats = rs.getInt("NumberOfSeats");

                    Screen screen = new Screen(screenId, cinema.getCinemaid(), type, seats);
                    cinema.addScreen(screen);

                    ShowManager.loadShows(screen);
                }
            }

            System.out.println("Screens loaded for cinema " + cinema.getName());

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteScreen(Screen screen) {
        for (Show show : screen.getShows()) {
            ShowManager.deleteShowFromDB(show.getShowId());
        }

        String query = "DELETE FROM Screens WHERE ScreenID = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, screen.getScreenId());
            stmt.executeUpdate();

            System.out.println("Screen " + screen.getScreenId() + " deleted successfully.");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteAllScreens(Cinema cinema) {
        for (Screen screen : new ArrayList<>(cinema.getScreens())) {
            deleteScreen(screen);
            cinema.getScreens().remove(screen);
        }
    }
}
