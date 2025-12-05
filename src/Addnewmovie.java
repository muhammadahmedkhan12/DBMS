import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Movie.Movie;
import Movie.MovieManager;

public class Addnewmovie extends JFrame {
    private JPanel myPanel;
    private JLabel Movienamelabel;
    private JTextField Movienmaetextfield;
    private JLabel movieratinglabel;
    private JTextField movieratingtextfield;
    private JButton Addmoviebutton;
    private JLabel Movieidlabel;
    private JTextField Movieidtextfield;
    private JLabel Movieodlabel;
    private JButton BackButton;


    MovieManager manager = MovieManager.getManager();

    public Addnewmovie() {
        myPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        Movieidlabel = new JLabel("Movie ID:");
        Movieidtextfield = new JTextField();
        Movieidtextfield.setEditable(false);

        Movienamelabel = new JLabel("Movie Name:");
        Movienmaetextfield = new JTextField();

        movieratinglabel = new JLabel("Movie Rating:");
        movieratingtextfield = new JTextField();

        Addmoviebutton = new JButton("Add Movie");

        BackButton = new JButton("Back");

        myPanel.add(Movieidlabel);
        myPanel.add(Movieidtextfield);
        myPanel.add(Movienamelabel);
        myPanel.add(Movienmaetextfield);
        myPanel.add(movieratinglabel);
        myPanel.add(movieratingtextfield);
        myPanel.add(new JLabel());
        myPanel.add(Addmoviebutton);
        myPanel.add(new JLabel());
        myPanel.add(BackButton);

        setContentPane(myPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);


        Movieidtextfield.setEditable(false);
        int nextId = 1;
        if (MovieManager.getManager().getMovies().size() > 0) {
            int lastIndex = MovieManager.getManager().getMovies().size() - 1;
            nextId = MovieManager.getManager().getMovies().get(lastIndex).getMovieid() + 1;
        }
        Movieidtextfield.setText(String.valueOf(nextId));

        Addmoviebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String moviename = Movienmaetextfield.getText().trim();
                String movierating = movieratingtextfield.getText().trim();

                if (moviename.isEmpty() || movierating.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.");
                    return;
                }

                boolean exists = false;
                for (Movie m : manager.getMovies()) {
                    if (m.getMoviename().equalsIgnoreCase(moviename)) {
                        exists = true;
                        break;
                    }
                }
                if (exists) {
                    JOptionPane.showMessageDialog(null, "Movie name already exists.");
                    return;
                }

                Movie movie = new Movie(moviename, movierating);
                manager.addmovie(movie);
                JOptionPane.showMessageDialog(null, "Movie added to movies.txt with ID: " + movie.getMovieid());
                dispose();
            }
        });

        BackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminMain();
                dispose();
            }
        });

        setVisible(true);

    }

    public static void main(String[] args) {
        new Addnewmovie();
    }
}
