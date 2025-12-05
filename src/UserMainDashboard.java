import Movie.MovieManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserMainDashboard extends JFrame {
    private JPanel MyPanel;
    private JButton ChangePasswordButton;
    private JPanel labelpanel;
    private JLabel label;
    private JPanel MainPanel;
    private JLabel movieselectlabel;
    private JComboBox moviebox;
    private JButton SelectCinema;
    private JButton backButton;
    MovieManager manager = new MovieManager();
    private String username;

    public UserMainDashboard(String username) {
        this.username = username;

        setContentPane(MyPanel);
        setTitle("Cinema Ticket Booking System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setVisible(true);

        for (int i = 0; i < manager.getMovies().size(); i++) {
            moviebox.addItem(manager.getMovies().get(i).getMoviename());
        }

        ChangePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ChangePasswordPanel();
            }
        });

        SelectCinema.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedmovie = (String) moviebox.getSelectedItem();
                new UserCinemaSelection(username, selectedmovie);
                dispose();
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new WelcomePage();
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        new UserMainDashboard("testuser");
    }
}
