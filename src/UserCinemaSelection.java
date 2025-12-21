import javax.swing.*;
 import javax.swing.border.EmptyBorder;
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
        setSize(1300, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Build redesigned layout: top title, centered column with controls, centered buttons
        // Theme & sizing constants
        final int INNER_MAX_WIDTH = 450;
        final int CONTROL_MAX_WIDTH = 350;
        final int V_SPACE = 20;
        Color bg = new Color(45, 62, 80);
        Color fg = Color.WHITE;
        Color primary = new Color(0, 150, 136);
        Color danger = new Color(220, 80, 80);
        Font headerFont = new Font("Segoe UI", Font.BOLD, 22);
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Root container
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(bg);
        root.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(root);

        // Header (top-center)
        JLabel title = new JLabel("Select Movie & Cinema", SwingConstants.CENTER);
        title.setForeground(fg);
        title.setFont(headerFont);
        title.setBorder(new EmptyBorder(V_SPACE/2, 0, V_SPACE, 0));
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        header.setOpaque(false);
        header.add(title);
        root.add(header, BorderLayout.NORTH);

        // Inner column centered both axes
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBorder(new EmptyBorder(12, 16, 12, 16));
        inner.setAlignmentX(Component.CENTER_ALIGNMENT);
        inner.setMaximumSize(new Dimension(INNER_MAX_WIDTH, Integer.MAX_VALUE));

        // Selected Movie label (right-aligned within inner)
        JLabel movieLabel = new JLabel("Selected Movie:");
        movieLabel.setForeground(fg);
        movieLabel.setFont(labelFont);
        movieLabel.setBorder(new EmptyBorder(0, 0, 6, 300));
        JPanel movieLabelRow = new JPanel(new BorderLayout());
        movieLabelRow.setOpaque(false);
        movieLabelRow.setMaximumSize(new Dimension(INNER_MAX_WIDTH, movieLabel.getPreferredSize().height));
        movieLabelRow.add(movieLabel, BorderLayout.EAST);
        inner.add(movieLabelRow);
        inner.add(Box.createVerticalStrut(6));

        // Movie combo (centered)
        movieComboBox = new JComboBox<>();
        movieComboBox.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, movieComboBox.getPreferredSize().height));
        movieComboBox.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH, movieComboBox.getPreferredSize().height));
        movieComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel comboRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        comboRow.setOpaque(false);
        comboRow.add(movieComboBox);
        inner.add(comboRow);

        inner.add(Box.createVerticalStrut(V_SPACE));

        // Rating label (left-aligned slightly under combo)
        ratingLabel = new JLabel("Rating: N/A");
        ratingLabel.setForeground(fg);
        ratingLabel.setFont(labelFont);
        ratingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ratingLabel.setBorder(new EmptyBorder(0, 6, V_SPACE/2, 0));
        // keep it visually aligned with combo by wrapping in a centered row but left inset
        JPanel ratingRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        ratingRow.setOpaque(false);
        ratingRow.add(ratingLabel);
        inner.add(ratingRow);

        inner.add(Box.createVerticalStrut(V_SPACE / 2));

        // Cinema list with constrained size so it doesn't stretch full page
        cinemaListModel = new DefaultListModel<>();
        cinemaList = new JList<>(cinemaListModel);
        cinemaList.setVisibleRowCount(8);
        JScrollPane listScroll = new JScrollPane(cinemaList);
        listScroll.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH, 220));
        listScroll.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, 220));
        JPanel listRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        listRow.setOpaque(false);
        listRow.add(listScroll);
        inner.add(listRow);

        inner.add(Box.createVerticalStrut(V_SPACE));

        // Buttons row centered (Next then Back) — kept together with even spacing
        JButton next = new JButton("Next: Payment");
        JButton back = new JButton("Back");
        styleButton(next, CONTROL_MAX_WIDTH, primary, fg);
        styleButton(back, CONTROL_MAX_WIDTH, danger, fg);

        JPanel buttonsRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        buttonsRow.setOpaque(false);
        buttonsRow.add(next);
        buttonsRow.add(back);
        inner.add(buttonsRow);

        // Add inner to center wrapper and place in root center
        centerWrapper.add(inner);
        root.add(centerWrapper, BorderLayout.CENTER);

        // Wire listeners (kept behavior)
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

        // Load data and wire combo change
        loadMovies();
        movieComboBox.addActionListener(e -> updateCinemaList());

        // Finalize
        getContentPane().setBackground(bg);
        revalidate();
        repaint();
        setVisible(true);
    }

    // small helper to style buttons consistently
    private void styleButton(AbstractButton b, int width, Color bg, Color fg) {
        b.setBackground(bg);
        b.setForeground(fg);
        b.setOpaque(true);
        b.setFocusPainted(false);
        b.setMargin(new Insets(8, 16, 8, 16));
        b.setMaximumSize(new Dimension(width, b.getPreferredSize().height));
        b.setPreferredSize(new Dimension(width, b.getPreferredSize().height));
        b.setHorizontalAlignment(SwingConstants.CENTER);
        b.setHorizontalTextPosition(SwingConstants.CENTER);
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
        ratingLabel.setText("Rating: " + getRatingByMovieId(targetMovieId));
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserCinemaSelection("testuser", "— choose movie —").setVisible(true));
    }
}
