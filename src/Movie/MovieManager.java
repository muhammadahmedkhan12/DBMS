package Movie;

import Cinema.CinemaManager;
import Cinema.Screen;
import Cinema.Cinema;

import java.io.File;
import java.io.FileWriter;
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
    public void loadmovies(){
        try {
            Scanner scanner = new Scanner(new File("movies.txt"));
            int maxId = 0;
            while (scanner.hasNextLine()){
                String[] data = scanner.nextLine().split(",");
                if (data.length==3){
                    int id = Integer.parseInt(data[0]);
                    movies.add(new Movie(id, data[1], data[2]));
                    if (id > maxId) maxId = id;
                }
            }
            Movie.resetNextMovieId(maxId + 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void savemovies() {
        try {
            FileWriter writer = new FileWriter("movies.txt");
            for(Movie m : movies){
                writer.write(m.getMovieid() + "," + m.getMoviename() + "," + m.getRating() + "\n");
            }
            writer.close();
        }
        catch (Exception e){
            throw new RuntimeException();
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
