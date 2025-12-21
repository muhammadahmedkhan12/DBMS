import javax.swing.*;
import javax.swing.border.EmptyBorder;
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

        // apply theme/layout before showing frame
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
        Color danger = new Color(220, 80, 80);
        Font headerFont = new Font("Segoe UI", Font.BOLD, 20);
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font controlFont = new Font("Segoe UI", Font.PLAIN, 14);

        if (myPanel == null) return;

        // idempotent guard
        if (Boolean.TRUE.equals(myPanel.getClientProperty("themedAddnewmovie"))) return;
        myPanel.putClientProperty("themedAddnewmovie", Boolean.TRUE);

        // rebuild myPanel as a centered column (reuse existing field instances)
        myPanel.removeAll();
        myPanel.setLayout(new BorderLayout());
        myPanel.setBackground(bg);
        myPanel.setOpaque(true);

        // Header label (will be placed in wrapper NORTH)
        JLabel headerLabel = new JLabel("Add New Movie");
        headerLabel.setFont(headerFont);
        headerLabel.setForeground(fg);
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setBorder(new EmptyBorder(V_SPACE, 0, V_SPACE / 2, 0));

        // center column
        JPanel centerCol = new JPanel();
        centerCol.setOpaque(false);
        centerCol.setLayout(new BoxLayout(centerCol, BoxLayout.Y_AXIS));
        centerCol.setBorder(new EmptyBorder(8, 16, 8, 16));
        centerCol.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerCol.setMaximumSize(new Dimension(INNER_MAX_WIDTH, Integer.MAX_VALUE));

        // Movie ID
        Movieidlabel.setForeground(fg);
        Movieidlabel.setFont(labelFont);
        Movieidlabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerCol.add(Movieidlabel);
        centerCol.add(Box.createVerticalStrut(6));

        Movieidtextfield.setFont(controlFont);
        Dimension dId = Movieidtextfield.getPreferredSize();
        Movieidtextfield.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, dId.height));
        Movieidtextfield.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH, dId.height));
        Movieidtextfield.setAlignmentX(Component.CENTER_ALIGNMENT);
        Movieidtextfield.setBorder(new EmptyBorder(6, 8, 6, 8));
        centerCol.add(Movieidtextfield);
        centerCol.add(Box.createVerticalStrut(V_SPACE));

        // Movie Name
        Movienamelabel.setForeground(fg);
        Movienamelabel.setFont(labelFont);
        Movienamelabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerCol.add(Movienamelabel);
        centerCol.add(Box.createVerticalStrut(6));

        Movienmaetextfield.setFont(controlFont);
        Dimension dName = Movienmaetextfield.getPreferredSize();
        Movienmaetextfield.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, dName.height));
        Movienmaetextfield.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH, dName.height));
        Movienmaetextfield.setAlignmentX(Component.CENTER_ALIGNMENT);
        Movienmaetextfield.setBorder(new EmptyBorder(6, 8, 6, 8));
        centerCol.add(Movienmaetextfield);
        centerCol.add(Box.createVerticalStrut(V_SPACE));

        // Movie Rating
        movieratinglabel.setForeground(fg);
        movieratinglabel.setFont(labelFont);
        movieratinglabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerCol.add(movieratinglabel);
        centerCol.add(Box.createVerticalStrut(6));

        movieratingtextfield.setFont(controlFont);
        Dimension dRate = movieratingtextfield.getPreferredSize();
        movieratingtextfield.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, dRate.height));
        movieratingtextfield.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH, dRate.height));
        movieratingtextfield.setAlignmentX(Component.CENTER_ALIGNMENT);
        movieratingtextfield.setBorder(new EmptyBorder(6, 8, 6, 8));
        centerCol.add(movieratingtextfield);
        centerCol.add(Box.createVerticalStrut(V_SPACE));

        // Buttons row (centered)
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttonRow.setOpaque(false);

        // style buttons
        java.util.function.Consumer<AbstractButton> stylePrimary = b -> {
            if (b == null) return;
            b.setFont(controlFont);
            b.setBackground(primary);
            b.setForeground(fg);
            b.setOpaque(true);
            b.setFocusPainted(false);
            b.setHorizontalAlignment(SwingConstants.CENTER);
            b.setHorizontalTextPosition(SwingConstants.CENTER);
            b.setMargin(new Insets(8, 16, 8, 16));
            b.setPreferredSize(new Dimension(300, b.getPreferredSize().height));
            b.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, b.getPreferredSize().height));
        };
        java.util.function.Consumer<AbstractButton> styleDanger = b -> {
            if (b == null) return;
            b.setFont(controlFont);
            b.setBackground(danger);
            b.setForeground(fg);
            b.setOpaque(true);
            b.setFocusPainted(false);
            b.setHorizontalAlignment(SwingConstants.CENTER);
            b.setHorizontalTextPosition(SwingConstants.CENTER);
            b.setMargin(new Insets(8, 16, 8, 16));
            b.setPreferredSize(new Dimension(140, b.getPreferredSize().height));
            b.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, b.getPreferredSize().height));
        };

        stylePrimary.accept(Addmoviebutton);
        styleDanger.accept(BackButton);

        buttonRow.add(Addmoviebutton);
        buttonRow.add(BackButton);

        centerCol.add(buttonRow);

        // center wrapper so inner column stays centered and constrained
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(centerCol);
        myPanel.add(centerWrapper, BorderLayout.CENTER);

        // header at top of the window: wrap myPanel in outer wrapper with NORTH header
        boolean alreadyWrapped = false;
        Container cp = getContentPane();
        if (cp instanceof JComponent) {
            Object flag = ((JComponent) cp).getClientProperty("wrappedAddnewmovie");
            alreadyWrapped = Boolean.TRUE.equals(flag);
        }
        if (!alreadyWrapped) {
            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setBackground(bg);
            // header panel
            JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            headerPanel.setOpaque(false);
            headerPanel.add(headerLabel);
            wrapper.add(headerPanel, BorderLayout.NORTH);

            JPanel grid = new JPanel(new GridBagLayout());
            grid.setOpaque(false);
            grid.add(myPanel);
            wrapper.add(grid, BorderLayout.CENTER);

            if (wrapper instanceof JComponent) ((JComponent) wrapper).putClientProperty("wrappedAddnewmovie", Boolean.TRUE);
            setContentPane(wrapper);
        } else {
            if (getContentPane() instanceof JComponent) ((JComponent) getContentPane()).setBackground(bg);
        }

        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Addnewmovie());
    }
}
