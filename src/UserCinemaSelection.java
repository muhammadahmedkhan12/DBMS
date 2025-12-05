import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class UserCinemaSelection extends JFrame {
    private JComboBox<String> movieComboBox;
    private JLabel ratingLabel;
    private DefaultListModel<String> cinemaListModel;
    private JList<String> cinemaList;

    private final ArrayList<String> movieNames  = new ArrayList<>();
    private final ArrayList<String> movieIds    = new ArrayList<>();
    private final ArrayList<String> movieRatings = new ArrayList<>();

    private final String username;
    private final String initialMovie;

    public UserCinemaSelection(String username, String initialMovie) {
        this.username     = username;
        this.initialMovie = initialMovie;

        setTitle("Select Movie & Cinema");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new GridLayout(2, 2));
        top.add(new JLabel("Selected Movie:"));
        movieComboBox = new JComboBox<>();
        top.add(movieComboBox);
        top.add(new JLabel("Rating:"));
        ratingLabel = new JLabel("N/A");
        top.add(ratingLabel);
        add(top, BorderLayout.NORTH);

        cinemaListModel = new DefaultListModel<>();
        cinemaList      = new JList<>(cinemaListModel);
        add(new JScrollPane(cinemaList), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new GridLayout(1, 2));
        JButton next = new JButton("Next: Payment");
        JButton back = new JButton("Back");

        next.addActionListener(e -> {
            String chosenMovie   = (String) movieComboBox.getSelectedItem();
            String movieId       = getMovieIdByName(chosenMovie);
            String selectedCinema = cinemaList.getSelectedValue();

            if (selectedCinema == null || selectedCinema.startsWith("No cinemas")) {
                JOptionPane.showMessageDialog(this, "Please select a cinema first.");
                return;
            }

            new UserPaymentSelection(username, movieId, chosenMovie, selectedCinema).setVisible(true);
            dispose();
        });

        back.addActionListener(e -> {
            new UserMainDashboard(username).setVisible(true);
            dispose();
        });

        bottom.add(next);
        bottom.add(back);
        add(bottom, BorderLayout.SOUTH);

        loadMovies();
        movieComboBox.addActionListener(e -> updateCinemaList());

        setVisible(true);
    }

    private void loadMovies() {
        try (BufferedReader br = new BufferedReader(new FileReader("movies.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", 3);
                if (p.length == 3) {
                    movieComboBox.addItem(p[1]);
                    movieNames.add(p[1]);
                    movieIds.add(p[0]);
                    movieRatings.add(p[2]);
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error reading movies.txt");
        }
        movieComboBox.setSelectedItem(initialMovie);
        updateCinemaList();
    }

    private String getMovieIdByName(String name) {
        for (int i = 0; i < movieNames.size(); i++)
            if (movieNames.get(i).equals(name)) return movieIds.get(i);
        return null;
    }

    private String getRatingByMovieId(String id) {
        for (int i = 0; i < movieIds.size(); i++)
            if (movieIds.get(i).equals(id)) return movieRatings.get(i);
        return "N/A";
    }

    private void updateCinemaList() {
        cinemaListModel.clear();
        String movieName = (String) movieComboBox.getSelectedItem();
        if (movieName == null) return;

        String targetMovieId = getMovieIdByName(movieName);
        ratingLabel.setText(getRatingByMovieId(targetMovieId));
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader("cinemas.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length < 5) continue;

                String cinemaName = d[1];
                int i = 2;

                while (i + 2 < d.length) {
                    String screenId   = d[i];
                    String screenType = d[i + 1];
                    int seatCount;

                    try { seatCount = Integer.parseInt(d[i + 2]); }
                    catch (NumberFormatException e) { break; }

                    i += 3;

                    while (i + 1 < d.length && !d[i].toLowerCase().startsWith("s")) {
                        String movieId  = d[i];
                        String showtime = d[i + 1];
                        i += 2;

                        if (movieId.equals(targetMovieId)) {
                            String info = cinemaName + " | Screen " + screenId + " (" + screenType + "), "
                                    + seatCount + " seats @ " + showtime;
                            cinemaListModel.addElement(info);
                            found = true;
                        }
                    }
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error reading cinemas.txt");
        }
        if (!found) cinemaListModel.addElement("No cinemas found for this movie.");
    }
}
