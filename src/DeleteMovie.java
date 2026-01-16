import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Movie.MovieManager;
import Cinema.CinemaManager;
import Cinema.Cinema;
import Screen.Screen;
import Show.Show;
import Show.ShowManager;

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
        
        setSize(600, 360);
        setLocationRelativeTo(null);

        MyPanel = new JPanel();
        // create components (keep field names intact)
        Movieidlabel = new JLabel("Movie ID:");
        Movieidtextfield = new JTextField();
        choosecinema = new JLabel("Choose Cinema:");
        Cinemaslist = new JComboBox<>();
        DeleteMoviebutton = new JButton("Delete Movie");
        // initial simple content pane; will be rebuilt/styled by applyTheme()
        setContentPane(MyPanel);

        for (Cinema cinema : cinemaManager.getCinemas()) {
            Cinemaslist.addItem(cinema.getName());
        }

        DeleteMoviebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String movieIdStr = Movieidtextfield.getText().trim();
                String cinemaName = (String) Cinemaslist.getSelectedItem();

                if (movieIdStr.isEmpty() || cinemaName == null) {
                    JOptionPane.showMessageDialog(null, "Please enter all fields.");
                    return;
                }

                int movieId;
                try {
                    movieId = Integer.parseInt(movieIdStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Movie ID must be an integer.");
                    return;
                }

                // Find selected cinema
                Cinema cinema = null;
                for (Cinema c : cinemaManager.getCinemas()) {
                    if (c.getName().equals(cinemaName)) {
                        cinema = c;
                        break;
                    }
                }
                if (cinema == null) {
                    JOptionPane.showMessageDialog(null, "Cinema not found.");
                    return;
                }

                boolean found = false;

                // Iterate all screens in the selected cinema
                for (Screen screen : cinema.getScreens()) {
                    for (int i = screen.getShows().size() - 1; i >= 0; i--) {
                        Show show = screen.getShows().get(i);
                        if (show.getMovie() != null && show.getMovie().getMovieid() == movieId) {
                            // Delete from DB
                            ShowManager.deleteShowFromDB(show.getShowId());
                            // Remove from screen's show list
                            screen.getShows().remove(i);
                            found = true;
                        }
                    }
                }

                if (found) {
                    JOptionPane.showMessageDialog(null,
                            "Show(s) of Movie ID " + movieId + " deleted from cinema " + cinema.getName());
                } else {
                    JOptionPane.showMessageDialog(null,
                            "No show found for Movie ID " + movieId + " in cinema " + cinema.getName());
                }

            }
        });



        // apply theme/layout and then show
        applyTheme();
        setVisible(true);
    }

    // apply compact centered theme (idempotent)
    private void applyTheme() {
        final int INNER_MAX_WIDTH = 450;
        final int CONTROL_MAX_WIDTH = 350;
        final int V_SPACE = 20;

        Color bg = new Color(45, 62, 80);
        Color fg = Color.WHITE;
        Color primary = new Color(0, 150, 136);
        Font headerFont = new Font("Segoe UI", Font.BOLD, 20);
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font controlFont = new Font("Segoe UI", Font.PLAIN, 14);

        if (MyPanel == null) return;

        // idempotent guard
        if (Boolean.TRUE.equals(MyPanel.getClientProperty("themedDeleteMovie"))) return;
        MyPanel.putClientProperty("themedDeleteMovie", Boolean.TRUE);

        // rebuild MyPanel as a centered column (keep field instances)
        MyPanel.removeAll();
        MyPanel.setLayout(new BorderLayout());
        MyPanel.setBackground(bg);
        MyPanel.setOpaque(true);

        // header handled by wrapper (so it sits at top-center of window)
        JLabel headerLabel = new JLabel(getTitle());
        headerLabel.setFont(headerFont);
        headerLabel.setForeground(fg);
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setBorder(new EmptyBorder(V_SPACE, 0, V_SPACE / 2, 0));

        JPanel centerCol = new JPanel();
        centerCol.setOpaque(false);
        centerCol.setLayout(new BoxLayout(centerCol, BoxLayout.Y_AXIS));
        centerCol.setBorder(new EmptyBorder(8, 16, 8, 16));
        centerCol.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerCol.setMaximumSize(new Dimension(INNER_MAX_WIDTH, Integer.MAX_VALUE));

        // Movie ID label + field
        Movieidlabel.setForeground(fg);
        Movieidlabel.setFont(labelFont);
        Movieidlabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerCol.add(Movieidlabel);
        centerCol.add(Box.createVerticalStrut(6));

        Movieidtextfield.setFont(controlFont);
        Dimension mid = Movieidtextfield.getPreferredSize();
        Movieidtextfield.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, mid.height));
        Movieidtextfield.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH, mid.height));
        Movieidtextfield.setAlignmentX(Component.CENTER_ALIGNMENT);
        Movieidtextfield.setBorder(new EmptyBorder(6, 8, 6, 8));
        centerCol.add(Movieidtextfield);
        centerCol.add(Box.createVerticalStrut(V_SPACE));

        // Choose cinema label + combo
        choosecinema.setForeground(fg);
        choosecinema.setFont(labelFont);
        choosecinema.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerCol.add(choosecinema);
        centerCol.add(Box.createVerticalStrut(6));

        Cinemaslist.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, Cinemaslist.getPreferredSize().height));
        Cinemaslist.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH, Cinemaslist.getPreferredSize().height));
        Cinemaslist.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerCol.add(Cinemaslist);
        centerCol.add(Box.createVerticalStrut(V_SPACE));

        // button row centered
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttonRow.setOpaque(false);
        DeleteMoviebutton.setFont(controlFont);
        DeleteMoviebutton.setForeground(fg);
        DeleteMoviebutton.setBackground(primary);
        DeleteMoviebutton.setOpaque(true);
        DeleteMoviebutton.setFocusPainted(false);
        DeleteMoviebutton.setMargin(new Insets(8, 16, 8, 16));
        DeleteMoviebutton.setPreferredSize(new Dimension(300, DeleteMoviebutton.getPreferredSize().height));
        DeleteMoviebutton.setHorizontalAlignment(SwingConstants.CENTER);
        buttonRow.add(DeleteMoviebutton);

        centerCol.add(buttonRow);

        // center wrapper so inner column stays centered and constrained
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(centerCol);
        MyPanel.add(centerWrapper, BorderLayout.CENTER);

        // header at top of the window: wrap MyPanel in outer wrapper with NORTH header
        boolean alreadyWrapped = false;
        Container cp = getContentPane();
        if (cp instanceof JComponent) {
            Object flag = ((JComponent) cp).getClientProperty("wrappedDeleteMovie");
            alreadyWrapped = Boolean.TRUE.equals(flag);
        }
        if (!alreadyWrapped) {
            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setBackground(bg);
            wrapper.add(headerLabel, BorderLayout.NORTH);
            JPanel grid = new JPanel(new GridBagLayout());
            grid.setOpaque(false);
            grid.add(MyPanel);
            wrapper.add(grid, BorderLayout.CENTER);
            if (wrapper instanceof JComponent) ((JComponent) wrapper).putClientProperty("wrappedDeleteMovie", Boolean.TRUE);
            setContentPane(wrapper);
        } else {
            if (getContentPane() instanceof JComponent) ((JComponent) getContentPane()).setBackground(bg);
        }

        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DeleteMovie());
    }
}