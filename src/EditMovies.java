import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Movie.MovieManager;
import Cinema.CinemaManager;

public class EditMovies extends JFrame {
    private JPanel MyPanel;
    private JLabel Movieiidlabel;
    private JTextField Movieidtextfield;
    private JLabel Movienamelabel;
    private JTextField Movienametextfield;
    private JLabel Newratinglabel;
    private JTextField Newratingtextfield;
    private JButton EditMoviebuttton;
    private JButton backButton;

    MovieManager movieManager = MovieManager.getManager();
    CinemaManager cinemaManager = CinemaManager.getCinemaManager();

    public EditMovies() {
        setContentPane(MyPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setVisible(true);

        EditMoviebuttton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int movieid;
                try {
                    movieid = Integer.parseInt(Movieidtextfield.getText().trim());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Invalid movie id");
                    return;
                }
                String newmoviename = Movienametextfield.getText().trim();
                String newmovierating = Newratingtextfield.getText().trim();
                if (newmoviename.isEmpty() || newmovierating.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter new name and rating.");
                    return;
                }
                boolean success = movieManager.editmovies(movieid, newmoviename, newmovierating);
                if (success) {
                    JOptionPane.showMessageDialog(null, "Movie updated successfully");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Movie id not found");
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminMain();
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        new EditMovies();
    }
}
