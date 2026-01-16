import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;
import Database.DBConnection;

public class UserCinemaSelection extends JFrame {

    private JLabel selectedMovieLabel;
    private JLabel ratingLabel;
    private DefaultListModel<String> cinemaListModel;
    private JList<String> cinemaList;

    private final String username;
    private final String initialMovie;
    private int selectedMovieId = -1;
    String cinemaName;
    String screentype ;
    int showId ;

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
            String selected = cinemaList.getSelectedValue();

            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Please select a cinema & showtime");
                return;
            }

            int showId = extractShowId(selected);
            if (showId == -1) {
                JOptionPane.showMessageDialog(this, "Invalid show selected");
                return;
            }
            new UserPaymentSelection(
                    username,
                    initialMovie,
                    cinemaName,
                    screentype,
                    showId
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


        try (Connection con = DBConnection.getConnection()){

            String sql = "SELECT movieid, rating FROM Movies WHERE movieName = ?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, initialMovie);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                selectedMovieId = rs.getInt("movieid");
                ratingLabel.setText("Rating: " + rs.getString("rating"));
            }
            else {
                ratingLabel.setText("Rating: Not Available");
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            ratingLabel.setText("Rating: Error");
        }
    }

    private void loadCinemasFromDB() {
        cinemaListModel.clear();

        if (selectedMovieId == -1) {
            cinemaListModel.addElement("Error: Movie not found in database");
            return;
        }



        try (Connection con = DBConnection.getConnection()){
            String query = """
            SELECT c.name AS cinemaName,
                   sc.screenid,
                   sc.screentype,
                   sc.NumberOfSeats AS seatCapacity,
                   sh.showtime,
                   sh.showid
            FROM Cinemas c
            JOIN Screens sc ON c.cinemaid = sc.cinemaid
            JOIN Shows sh ON sc.screenid = sh.screenid
            WHERE sh.movieid = ?
            ORDER BY c.name, sh.showtime
            """;
             PreparedStatement stmt = con.prepareStatement(query);

            stmt.setInt(1, selectedMovieId);
            ResultSet rs = stmt.executeQuery();

            boolean found = false;
            while (rs.next()) {
                String cinemaName = rs.getString("cinemaName");
                String screenId = rs.getString("screenid");   // Treat as String
                String screenType = rs.getString("screentype");
                int seatCapacity = rs.getInt("seatCapacity");
                String showTime = rs.getString("showtime");
                int showId = rs.getInt("showid");
                this.cinemaName=cinemaName;
                this.screentype=screenType;
                this.showId=showId;

                String info = String.format(
                        "%s | Screen: %s (%s) | Time: %s | Seats: %d | Show ID: %d",
                        cinemaName, screenId, screenType, showTime, seatCapacity, showId
                );

                cinemaListModel.addElement(info);
                found = true;
            }

            if (!found) {
                cinemaListModel.addElement("No cinemas found showing this movie.");
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            cinemaListModel.addElement("Error loading cinemas from database.");
        }
    }


    private int extractShowId(String text) {
        try {
            for (String part : text.split("\\|")) {
                if (part.trim().startsWith("Show ID:")) {
                    return Integer.parseInt(part.replace("Show ID:", "").trim());
                }
            }
        }
        catch (Exception ignored) {}
        return -1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new UserCinemaSelection("testuser", "Inception"));
    }
}
