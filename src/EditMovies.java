import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
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
        setSize(500, 320);
        setLocationRelativeTo(null);

        // apply theme/layout before attaching listeners
        applyTheme();

        // ...existing code...
        if (EditMoviebuttton != null) {
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
                    boolean success = movieManager.editMovie(movieid, newmoviename, newmovierating);
                    if (success) {
                        JOptionPane.showMessageDialog(null, "Movie updated successfully");
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Movie id not found");
                    }
                }
            });
        }
        if (backButton != null) {
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new AdminMain();
                    dispose();
                }
            });
        }

        // show last
        setVisible(true);
    }

    // apply compact centered theme (idempotent)
    private void applyTheme() {
        final int INNER_MAX_WIDTH = 450;
        final int CONTROL_MAX_WIDTH = 350;
        final int V_SPACE = 20;
        final int BUTTON_WIDTH = 300; // same width for both buttons

        Color bg = new Color(45, 62, 80);
        Color fg = Color.WHITE;
        Color primary = new Color(0, 150, 136);
        Color danger = new Color(220, 80, 80);
        Font headerFont = new Font("Segoe UI", Font.BOLD, 20);
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font controlFont = new Font("Segoe UI", Font.PLAIN, 14);

        if (MyPanel == null) return;

        // idempotent guard
        if (Boolean.TRUE.equals(MyPanel.getClientProperty("themedEditMovies"))) return;
        MyPanel.putClientProperty("themedEditMovies", Boolean.TRUE);

        // style labels
        if (Movieiidlabel != null) {
            Movieiidlabel.setForeground(fg);
            Movieiidlabel.setFont(labelFont);
            Movieiidlabel.setBorder(new EmptyBorder(0, 0, 6, 0));
            Movieiidlabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        }
        if (Movienamelabel != null) {
            Movienamelabel.setForeground(fg);
            Movienamelabel.setFont(labelFont);
            Movienamelabel.setBorder(new EmptyBorder(0, 0, 6, 0));
            Movienamelabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        }
        if (Newratinglabel != null) {
            Newratinglabel.setForeground(fg);
            Newratinglabel.setFont(labelFont);
            Newratinglabel.setBorder(new EmptyBorder(0, 0, 6, 0));
            Newratinglabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        // style text fields
        java.util.function.Consumer<JTextField> styleField = f -> {
            if (f == null) return;
            f.setFont(controlFont);
            Dimension d = f.getPreferredSize();
            f.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, d.height));
            f.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH, d.height));
            f.setAlignmentX(Component.CENTER_ALIGNMENT);
            f.setBorder(new EmptyBorder(6, 8, 6, 8));
        };
        styleField.accept(Movieidtextfield);
        styleField.accept(Movienametextfield);
        styleField.accept(Newratingtextfield);

        // style buttons
        java.util.function.Consumer<AbstractButton> stylePrimaryBtn = b -> {
            if (b == null) return;
            b.setFont(controlFont);
            b.setBackground(primary);
            b.setForeground(fg);
            b.setOpaque(true);
            b.setFocusPainted(false);
            b.setHorizontalAlignment(SwingConstants.CENTER);
            b.setHorizontalTextPosition(SwingConstants.CENTER);
            b.setMargin(new Insets(8, 16, 8, 16));
            b.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, b.getPreferredSize().height));
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
        };
        java.util.function.Consumer<AbstractButton> styleDangerBtn = b -> {
            if (b == null) return;
            b.setFont(controlFont);
            b.setBackground(danger);
            b.setForeground(fg);
            b.setOpaque(true);
            b.setFocusPainted(false);
            b.setHorizontalAlignment(SwingConstants.CENTER);
            b.setHorizontalTextPosition(SwingConstants.CENTER);
            b.setMargin(new Insets(8, 16, 8, 16));
            b.setMaximumSize(new Dimension(200, b.getPreferredSize().height));
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
        };
        stylePrimaryBtn.accept(EditMoviebuttton);
        styleDangerBtn.accept(backButton);

        // enforce same width for both buttons and center alignment
        if (EditMoviebuttton != null) {
            Dimension d = EditMoviebuttton.getPreferredSize();
            EditMoviebuttton.setPreferredSize(new Dimension(BUTTON_WIDTH, d.height));
            EditMoviebuttton.setMaximumSize(new Dimension(BUTTON_WIDTH, d.height));
            EditMoviebuttton.setAlignmentX(Component.CENTER_ALIGNMENT);
        }
        if (backButton != null) {
            Dimension d = backButton.getPreferredSize();
            backButton.setPreferredSize(new Dimension(BUTTON_WIDTH, d.height));
            backButton.setMaximumSize(new Dimension(BUTTON_WIDTH, d.height));
            backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        // set panel background
        MyPanel.setBackground(bg);
        MyPanel.setOpaque(true);

        // rebuild wrapper: header at top, MyPanel centered in the window
        boolean alreadyWrapped = false;
        Container cp = getContentPane();
        if (cp instanceof JComponent) {
            Object flag = ((JComponent) cp).getClientProperty("wrappedEditMovies");
            alreadyWrapped = Boolean.TRUE.equals(flag);
        }

        if (!alreadyWrapped) {
            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setBackground(bg);

            // header at top center with vertical gap
            JLabel header = new JLabel("Edit Movie");
            header.setFont(headerFont);
            header.setForeground(fg);
            header.setHorizontalAlignment(SwingConstants.CENTER);
            header.setBorder(new EmptyBorder(V_SPACE, 0, V_SPACE, 0));
            JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            headerPanel.setOpaque(false);
            headerPanel.add(header);
            wrapper.add(headerPanel, BorderLayout.NORTH);

            // center MyPanel inside a GridBagLayout so it stays centered and constrained
            JPanel centerGrid = new JPanel(new GridBagLayout());
            centerGrid.setOpaque(false);

            // ensure MyPanel uses vertical stacking so elements align
            MyPanel.setLayout(new BoxLayout(MyPanel, BoxLayout.Y_AXIS));
            MyPanel.setBorder(new EmptyBorder(12, 16, 12, 16));
            MyPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            MyPanel.setMaximumSize(new Dimension(INNER_MAX_WIDTH, Integer.MAX_VALUE));
            MyPanel.setPreferredSize(new Dimension(INNER_MAX_WIDTH, MyPanel.getPreferredSize().height));

            // If MyPanel currently contains components defined by form builder, keep them but enforce spacing:
            // ensure each child is center-aligned
            for (Component c : MyPanel.getComponents()) {
                if (c instanceof JComponent) {
                    ((JComponent) c).setAlignmentX(Component.CENTER_ALIGNMENT);
                }
            }

            // insert vertical space (V_SPACE) before the first button so buttons are pushed down
            // run after layout is BoxLayout (we are now in BoxLayout)
            try {
                Component[] comps = MyPanel.getComponents();
                int firstButtonIndex = Integer.MAX_VALUE;
                for (int i = 0; i < comps.length; i++) {
                    if (comps[i] == EditMoviebuttton || comps[i] == backButton) {
                        firstButtonIndex = Math.min(firstButtonIndex, i);
                    }
                }
                if (firstButtonIndex != Integer.MAX_VALUE) {
                    boolean alreadyStrut = false;
                    if (firstButtonIndex > 0 && MyPanel.getComponentCount() > 0 && MyPanel.getComponent(firstButtonIndex - 1) instanceof Box.Filler) {
                        alreadyStrut = true;
                    }
                    if (!alreadyStrut) {
                        // add a vertical gap so fields and labels have breathing room before buttons
                        MyPanel.add(Box.createVerticalStrut(V_SPACE), firstButtonIndex);
                    }
                }
            } catch (Exception ex) {
                // fail-safe: ignore layout insertion errors
            }

            // ensure a gap immediately after the EditMovie button so the following button is visible
            try {
                if (EditMoviebuttton != null) {
                    Component[] comps2 = MyPanel.getComponents();
                    int editIdx = -1;
                    for (int i = 0; i < comps2.length; i++) {
                        if (comps2[i] == EditMoviebuttton) { editIdx = i; break; }
                    }
                    if (editIdx != -1) {
                        boolean alreadyAfter = (editIdx + 1 < MyPanel.getComponentCount() && MyPanel.getComponent(editIdx + 1) instanceof Box.Filler);
                        if (!alreadyAfter) {
                            // place a vertical strut right after EditMoviebuttton
                            MyPanel.add(Box.createVerticalStrut(V_SPACE), editIdx + 1);
                        }
                    }
                }
            } catch (Exception ex) {
                // ignore
            }

            centerGrid.add(MyPanel);
            wrapper.add(centerGrid, BorderLayout.CENTER);

            if (wrapper instanceof JComponent) ((JComponent) wrapper).putClientProperty("wrappedEditMovies", Boolean.TRUE);
            setContentPane(wrapper);
        } else {
            if (getContentPane() instanceof JComponent) {
                ((JComponent) getContentPane()).setBackground(bg);
            }
        }

        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EditMovies());
    }
}
