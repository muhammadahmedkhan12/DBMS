import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Movie.MovieManager;
import Movie.Movie;
import Cinema.CinemaManager;
import Cinema.Cinema;
import Cinema.Screen;

public class DeleteMovie extends JFrame {
    private JPanel MyPanel;
    private JLabel Movieidlabel;
    private JTextField Movieidtextfield;
    private JLabel choosecinema;
    private JComboBox<String> Cinemaslist;
    private JButton DeleteMoviebutton;
    private JButton backButton;

    MovieManager manager = MovieManager.getManager();
    CinemaManager cinemaManager = CinemaManager.getCinemaManager();

    public DeleteMovie() {
        setTitle("Delete Movie");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);

        MyPanel = new JPanel();
        MyPanel.setLayout(new GridLayout(3, 2, 10, 10));
        Movieidlabel = new JLabel("Movie ID:");
        Movieidtextfield = new JTextField();
        choosecinema = new JLabel("Choose Cinema:");
        Cinemaslist = new JComboBox<>();
        DeleteMoviebutton = new JButton("Delete Movie");

        MyPanel.add(Movieidlabel);
        MyPanel.add(Movieidtextfield);
        MyPanel.add(choosecinema);
        MyPanel.add(Cinemaslist);
        MyPanel.add(new JLabel());
        MyPanel.add(DeleteMoviebutton);

        setContentPane(MyPanel);

        for (Cinema cinema : cinemaManager.getCinemas()) {
            Cinemaslist.addItem(cinema.getName());
        }

        DeleteMoviebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String movieIdStr = Movieidtextfield.getText().trim();
                String cinemaname = (String) Cinemaslist.getSelectedItem();

                if (movieIdStr.isEmpty() || cinemaname == null) {
                    JOptionPane.showMessageDialog(null, "Please enter all fields.");
                    return;
                }

                int movieId;
                try {
                    movieId = Integer.parseInt(movieIdStr);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Movie ID must be an integer.");
                    return;
                }

                Movie movie = manager.getmoviebyid(movieId);
                if (movie == null) {
                    JOptionPane.showMessageDialog(null, "Movie ID not found.");
                    return;
                }

                Cinema cinema = null;
                for (Cinema c : cinemaManager.getCinemas()) {
                    if (c.getName().equals(cinemaname)) {
                        cinema = c;
                        break;
                    }
                }
                if (cinema == null) {
                    JOptionPane.showMessageDialog(null, "Cinema not found.");
                    return;
                }

                boolean foundInAnyScreen = false;
                for (Screen screen : cinema.getScreens()) {
                    for (int i = screen.getMovies().size() - 1; i >= 0; i--) {
                        if (screen.getMovies().get(i).getMovieid() == movieId) {
                            screen.getMovies().remove(i);
                            screen.getShowtimes().remove(i);
                            foundInAnyScreen = true;
                        }
                    }
                }
                if (foundInAnyScreen) {
                    cinemaManager.savecinemas();
                    JOptionPane.showMessageDialog(null, "Movie removed from all screens in cinema " + cinema.getName());
                } else {
                    JOptionPane.showMessageDialog(null, "Movie not found in any screen of the selected cinema.");
                }
                dispose();
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new DeleteMovie();
    }
}