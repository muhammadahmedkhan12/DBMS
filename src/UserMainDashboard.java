import Movie.MovieManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
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

        // set content and basic frame props
        setContentPane(MyPanel);
        setTitle("Cinema Ticket Booking System");
        
        setSize(1300, 900);
        setLocationRelativeTo(null);

        // populate combo safely
        if (moviebox != null && manager.getMovies() != null) {
            for (int i = 0; i < manager.getMovies().size(); i++) {
                Object mov = manager.getMovies().get(i).getMoviename();
                if (mov != null) moviebox.addItem(mov);
            }
        }

        // apply visual theme and layout constraints
        applyTheme();

        // attach listeners after theming (null-safe)
        if (ChangePasswordButton != null) {
            ChangePasswordButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    new ChangePasswordPanel();
                }
            });
        }

        if (SelectCinema != null) {
            SelectCinema.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String selectedmovie = (moviebox != null && moviebox.getSelectedItem() != null)
                            ? moviebox.getSelectedItem().toString() : "";
                    new UserCinemaSelection(username, selectedmovie);
                    dispose();
                }
            });
        }

        if (backButton != null) {
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new WelcomePage();
                    dispose();
                }
            });
        }

        // show last
        setVisible(true);
    }

    // New theme helper — keeps fields unchanged and only adjusts visuals/layout
    private void applyTheme() {
        final int INNER_MAX_WIDTH = 450;
        final int CONTROL_MAX_WIDTH = 350;
        final int V_SPACE = 18;

        Color bg = new Color(45, 62, 80);
        Color fg = Color.WHITE;
        Color primary = new Color(0, 150, 136);
        Color danger = new Color(220, 80, 80);
        Font headerFont = new Font("Segoe UI", Font.BOLD, 26);
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font controlFont = new Font("Segoe UI", Font.PLAIN, 14);

        if (MyPanel == null) return;

        // base background
        MyPanel.setBackground(bg);

        // --- Header (title) at top-center with spacing ---
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        headerPanel.setOpaque(false);
        if (label != null) {
            label.setForeground(fg);
            label.setFont(headerFont);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setBorder(new EmptyBorder(V_SPACE + 6, 0, V_SPACE, 0)); // more vertical space under title
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            // allow full rendering but cap width so it doesn't stretch
            label.setMaximumSize(new Dimension(INNER_MAX_WIDTH, label.getPreferredSize().height));
            headerPanel.add(label);
        }

        // --- Inner column (content) that will be centered in the page ---
        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBorder(new EmptyBorder(12, 16, 12, 16));
        inner.setAlignmentX(Component.CENTER_ALIGNMENT);
        inner.setMaximumSize(new Dimension(INNER_MAX_WIDTH, Integer.MAX_VALUE));

        // Content: Select Movie label row (right-aligned) then centered combo box
        if (movieselectlabel != null) {
            // Set font and color
            Font newFont = new Font("Segoe UI", Font.BOLD, 16); // Change as needed
            movieselectlabel.setFont(newFont);
            movieselectlabel.setForeground(fg);

            // Optional spacing: reduce bottom margin
            movieselectlabel.setBorder(new EmptyBorder(V_SPACE / 2, 0, V_SPACE / 2, 100));

            // Place the label in a panel to align to right
            JPanel labelRow = new JPanel(new BorderLayout());
            labelRow.setOpaque(false);
            labelRow.setMaximumSize(new Dimension(INNER_MAX_WIDTH, movieselectlabel.getPreferredSize().height));

            // Add label to the EAST to align right
            labelRow.add(movieselectlabel, BorderLayout.EAST);

            // Add to the inner panel
            inner.add(labelRow);

            // Optional spacing after label
            inner.add(Box.createVerticalStrut(8));
        }


        if (moviebox != null) {
            moviebox.setFont(controlFont);
            moviebox.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, moviebox.getPreferredSize().height));
            moviebox.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH, moviebox.getPreferredSize().height));
            // center the combo box using a flow row
            JPanel comboRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            comboRow.setOpaque(false);
            comboRow.add(moviebox);
            inner.add(comboRow);
        }

        inner.add(Box.createVerticalStrut(20));

        // Buttons stacked vertically and centered: SelectCinema -> ChangePassword -> Back
        JPanel buttonsCol = new JPanel();
        buttonsCol.setOpaque(false);
        buttonsCol.setLayout(new BoxLayout(buttonsCol, BoxLayout.Y_AXIS));
        buttonsCol.setAlignmentX(Component.CENTER_ALIGNMENT);

        java.util.function.Consumer<AbstractButton> styleBtn = b -> {
            b.setFont(controlFont);
            b.setForeground(Color.WHITE);
            b.setOpaque(true);
            b.setFocusPainted(false);
            b.setHorizontalAlignment(SwingConstants.CENTER);
            b.setHorizontalTextPosition(SwingConstants.CENTER);
            b.setMargin(new Insets(8, 16, 8, 16));
            b.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, b.getPreferredSize().height));
            b.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH, b.getPreferredSize().height));
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
        };

        if (SelectCinema != null) {
            styleBtn.accept(SelectCinema);
            SelectCinema.setBackground(primary);
            buttonsCol.add(SelectCinema);
            buttonsCol.add(Box.createVerticalStrut(12));
        }
        if (ChangePasswordButton != null) {
            styleBtn.accept(ChangePasswordButton);
            ChangePasswordButton.setBackground(primary);
            buttonsCol.add(ChangePasswordButton);
            buttonsCol.add(Box.createVerticalStrut(12));
        }
        if (backButton != null) {
            styleBtn.accept(backButton);
            backButton.setBackground(danger);
            buttonsCol.add(backButton);
        }

        inner.add(buttonsCol);

        // --- Build main panel: title at NORTH, inner content centered in CENTER ---
        MyPanel.removeAll();
        MyPanel.setLayout(new BorderLayout());
        MyPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel centerWrapper = new JPanel(new GridBagLayout()); // centers inner both axes
        centerWrapper.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        centerWrapper.add(inner, gbc);

        MyPanel.add(centerWrapper, BorderLayout.CENTER);

        // Keep overall constraints so controls don't stretch full width
        MyPanel.setMaximumSize(new Dimension(INNER_MAX_WIDTH, Integer.MAX_VALUE));
        MyPanel.setPreferredSize(new Dimension(INNER_MAX_WIDTH, MyPanel.getPreferredSize().height));

        // ensure content pane background matches
        Container current = getContentPane();
        if (current instanceof JComponent) {
            ((JComponent) current).setBackground(bg);
        }

        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserMainDashboard("testuser"));
    }
}
