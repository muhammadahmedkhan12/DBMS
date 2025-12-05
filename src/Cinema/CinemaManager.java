package Cinema;

import Movie.Movie;
import Movie.MovieManager;

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

    public void loadcinemas() {
        try {
            Scanner scanner = new Scanner(new File("cinemas.txt"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] data = line.split(",");

                String cinemaId = data[0];
                String cinemaName = data[1];
                Cinema cinema = new Cinema(cinemaId, cinemaName);

                int i = 2;
                Screen.resetScreenNumber();
                while (i + 2 < data.length) {
                    String screenId = data[i];
                    String screenType = data[i + 1];
                    int numberOfSeats = Integer.parseInt(data[i + 2]);
                    i += 3;

                    Screen screen = new Screen(screenId, screenType, numberOfSeats);

                    while (i + 1 < data.length && !data[i].startsWith("s") && !data[i].startsWith("S")) {
                        String movieIdStr = data[i];
                        String showtime = data[i + 1];
                        int movieId;
                        try {
                            movieId = Integer.parseInt(movieIdStr);
                        } catch (Exception ex) {
                            break;
                        }

                        Movie movie = MovieManager.getManager().getmoviebyid(movieId);

                        if (movie != null) {
                            screen.addMovie(movie, showtime);
                        }
                        i += 2;
                    }

                    cinema.addScreen(screen);
                }

                cinemas.add(cinema);
            }

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void savecinemas() {
        try {
            FileWriter writer = new FileWriter("cinemas.txt");
            for (int i = 0; i < cinemas.size(); i++) {
                Cinema cinema = cinemas.get(i);
                writer.write(cinema.getCinemaid() + "," + cinema.getName());

                ArrayList<Screen> screens = cinema.getScreens();
                for (int j = 0; j < screens.size(); j++) {
                    Screen screen = screens.get(j);
                    writer.write("," + screen.getScreenid() + "," + screen.getScreentype() + "," + screen.getNumberOfSeats());

                    ArrayList<Movie> movies = screen.getMovies();
                    ArrayList<String> showtimes = screen.getShowtimes();

                    for (int k = 0; k < movies.size(); k++) {
                        writer.write("," + movies.get(k).getMovieid() + "," + showtimes.get(k));
                    }
                }
                writer.write("\n");
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}