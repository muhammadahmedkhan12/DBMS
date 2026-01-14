package Cinema;

import Database.DBConnection;
import Movie.Movie;
import Movie.MovieManager;
import java.sql.*;

import java.io.File;
import java.io.FileWriter;
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
    public String addMovieToCinema(Cinema cinema, Screen screen, Movie movie, String showtime) {
        // 1️⃣ Find cinema and screen as before
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

        // 2️⃣ Insert into DB without specifying ShowID
        String query = "INSERT INTO Shows (MovieID, ScreenID, ShowTime) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

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

        } catch (Exception e) {
            e.printStackTrace();
            return "Error adding movie to cinema";
        }

        return "Movie '" + movie.getMoviename() + "' added to cinema '" +
                foundCinema.getName() + "' on screen '" + foundScreen.getScreenId() +
                "' at " + showtime;
    }



    public String deleteMovieFromCinema(Cinema cinema, Screen screen, Movie movie) {
        // Find the show in the screen that matches the movie
        Show toRemove = null;

        for (Show s : screen.getShows()) {
            if (s.getMovie().getMovieid() == movie.getMovieid()) {
                toRemove = s;
                break; // remove only the first show of this movie; remove all if needed
            }
        }

        if (toRemove != null) {
            screen.removeShow(toRemove.getShowId()); // remove the Show from the screen
            ShowManager.saveShows(screen); // save the updated shows to DB
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
            } else {
                System.out.println("Cinema ID not found in DB");
            }

            // Optionally update in-memory object
            for (Cinema c : cinemas) {
                if (c.getCinemaid().equals(cinemaId)) {
                    c.setName(newName);
                    break;
                }
            }

        } catch (Exception e) {
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
            } else {
                System.out.println("Cinema ID not found in DB");
            }

            // Remove from in-memory list as well
            cinemas.removeIf(c -> c.getCinemaid().equals(cinemaId));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ArrayList<Cinema> getCinemas() {
        return cinemas;
    }

//    public void loadcinemas() {
//        try {
//            Scanner scanner = new Scanner(new File("cinemas.txt"));
//            while (scanner.hasNextLine()) {
//                String line = scanner.nextLine().trim();
//                if (line.isEmpty()) {
//                    continue;
//                }
//
//                String[] data = line.split(",");
//
//                String cinemaId = data[0];
//                String cinemaName = data[1];
//                Cinema cinema = new Cinema(cinemaId, cinemaName);
//
//                int i = 2;
//                Screen.resetScreenNumber();
//                while (i + 2 < data.length) {
//                    String screenId = data[i];
//                    String screenType = data[i + 1];
//                    int numberOfSeats = Integer.parseInt(data[i + 2]);
//                    i += 3;
//
//                    Screen screen = new Screen(screenId, screenType, numberOfSeats);
//
//                    while (i + 1 < data.length && !data[i].startsWith("s") && !data[i].startsWith("S")) {
//                        String movieIdStr = data[i];
//                        String showtime = data[i + 1];
//                        int movieId;
//                        try {
//                            movieId = Integer.parseInt(movieIdStr);
//                        } catch (Exception ex) {
//                            break;
//                        }
//
//                        Movie movie = MovieManager.getManager().getmoviebyid(movieId);
//
//                        if (movie != null) {
//                            screen.addMovie(movie, showtime);
//                        }
//                        i += 2;
//                    }
//
//                    cinema.addScreen(screen);
//                }
//
//                cinemas.add(cinema);
//            }
//
//            scanner.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public void savecinemas() {
//        try {
//            FileWriter writer = new FileWriter("cinemas.txt");
//            for (int i = 0; i < cinemas.size(); i++) {
//                Cinema cinema = cinemas.get(i);
//                writer.write(cinema.getCinemaid() + "," + cinema.getName());
//
//                ArrayList<Screen> screens = cinema.getScreens();
//                for (int j = 0; j < screens.size(); j++) {
//                    Screen screen = screens.get(j);
//                    writer.write("," + screen.getScreenid() + "," + screen.getScreentype() + "," + screen.getNumberOfSeats());
//
//                    ArrayList<Movie> movies = screen.getMovies();
//                    ArrayList<String> showtimes = screen.getShowtimes();
//
//                    for (int k = 0; k < movies.size(); k++) {
//                        writer.write("," + movies.get(k).getMovieid() + "," + showtimes.get(k));
//                    }
//                }
//                writer.write("\n");
//            }
//            writer.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
public void savecinemas() {
    String insertQuery = "INSERT INTO Cinemas (CinemaID, Name) VALUES (?, ?)";
    String checkQuery = "SELECT COUNT(*) FROM Cinemas WHERE CinemaID = ?";

    try (Connection con = DBConnection.getConnection()) {

        for (Cinema cinema : cinemas) {

            // Check if CinemaID already exists
            try (PreparedStatement checkStmt = con.prepareStatement(checkQuery)) {
                checkStmt.setString(1, cinema.getCinemaid());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    rs.next();
                    int count = rs.getInt(1);
                    if (count > 0) {
                        System.out.println("Cinema " + cinema.getCinemaid() + " already exists. Skipping insert.");
                        continue; // skip inserting duplicate
                    }
                }
            }

            // Insert new cinema
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}