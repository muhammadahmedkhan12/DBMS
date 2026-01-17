import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import Movie.Movie;
import Movie.MovieManager;
import Show.ShowManager;

public class UserCinemaSelection extends JFrame {

    private JLabel selectedMovieLabel;
    private JLabel ratingLabel;
    private DefaultListModel<String> cinemaListModel;
    private JList<String> cinemaList;

    private final String username;
    private final String initialMovie;
    private Movie selectedMovie;
    private ArrayList<String[]> shows;

    public UserCinemaSelection(String username, String initialMovie) {
        this.username = username;
        this.initialMovie = initialMovie;

        setTitle("Select Cinema");
        setSize(1300, 900);
        setLocationRelativeTo(null);

        Color bg = new Color(45, 62, 80);
        Color fg = Color.WHITE;
        Color primary = new Color(0, 150, 136);
        Color danger = new Color(220, 80, 80);

        Font headerFont = new Font("Segoe UI", Font.BOLD, 22);
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font movieFont = new Font("Segoe UI", Font.BOLD, 18);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(bg);
        root.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(root);

        JLabel title = new JLabel("Select Cinema & Showtime", SwingConstants.CENTER);
        title.setForeground(fg);
        title.setFont(headerFont);
        root.add(title, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        root.add(center, BorderLayout.CENTER);

        JLabel movieTitle = new JLabel("Selected Movie:");
        movieTitle.setForeground(fg);
        movieTitle.setFont(labelFont);
        movieTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(movieTitle);

        selectedMovieLabel = new JLabel(initialMovie);
        selectedMovieLabel.setForeground(primary);
        selectedMovieLabel.setFont(movieFont);
        selectedMovieLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(selectedMovieLabel);

        ratingLabel = new JLabel("Rating: Loading...");
        ratingLabel.setForeground(fg);
        ratingLabel.setFont(labelFont);
        ratingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(ratingLabel);

        center.add(Box.createVerticalStrut(20));

        JLabel cinemasLabel = new JLabel("Available Cinemas & Showtimes:");
        cinemasLabel.setForeground(fg);
        cinemasLabel.setFont(labelFont);
        cinemasLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(cinemasLabel);

        cinemaListModel = new DefaultListModel<>();
        cinemaList = new JList<>(cinemaListModel);
        JScrollPane scrollPane = new JScrollPane(cinemaList);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        center.add(scrollPane);

        JButton next = new JButton("Next: Select Seats");
        JButton back = new JButton("Back");

        styleButton(next, primary, fg);
        styleButton(back, danger, fg);

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.add(next);
        btnPanel.add(back);
        center.add(btnPanel);

        next.addActionListener(e -> {
            int selectedIndex = cinemaList.getSelectedIndex();

            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(this, "Please select a cinema & showtime");
                return;
            }

            if (shows == null || selectedIndex >= shows.size()) {
                JOptionPane.showMessageDialog(this, "Invalid show selected");
                return;
            }

            String[] selectedShow = shows.get(selectedIndex);

            new UserPaymentSelection(
                    username,
                    initialMovie,
                    selectedShow[2],
                    selectedShow[0],
                    Integer.parseInt(selectedShow[5])
            ).setVisible(true);

            dispose();
        });

        back.addActionListener(e -> {
            new UserMainDashboard(username).setVisible(true);
            dispose();
        });

        loadMovieRating();
        loadCinemasFromDB();

        setVisible(true);
    }

    private void styleButton(JButton b, Color bg, Color fg) {
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(200, 40));
    }

    private void loadMovieRating() {
        selectedMovie = MovieManager.getManager().getMovieByName(initialMovie);

        if (selectedMovie != null) {
            ratingLabel.setText("Rating: " + selectedMovie.getRating());
        }
        else {
            ratingLabel.setText("Rating: Not Available");
        }
    }

    private void loadCinemasFromDB() {
        cinemaListModel.clear();

        if (selectedMovie == null) {
            cinemaListModel.addElement("Error: Movie not found in database");
            return;
        }
        shows = ShowManager.getShowsByMovieId(selectedMovie.getMovieid());

        if (shows.isEmpty()) {
            cinemaListModel.addElement("No cinemas found showing this movie.");
        }
        else {
            for (String[] show : shows) {
                String info = String.format(
                        "%s | Screen: %s (%s) | Time: %s | Seats: %s | Show ID: %s",
                        show[0], show[1], show[2], show[3], show[4], show[5]
                );
                cinemaListModel.addElement(info);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new UserCinemaSelection("testuser", "Inception"));
    }
}