 // ...existing package if any...

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SelectCinema extends JFrame {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JLabel selectMovieLabel;
    private JComboBox<String> movieCombo;
    private JLabel selectCinemaLabel;
    private JComboBox<String> cinemaCombo;
    private JButton selectButton;
    private JButton changePasswordButton;
    private JButton backButton;

    public SelectCinema() {
        // basic frame
        setTitle("Select Cinema - Cinema Ticket Booking System");
        
        setSize(1300, 900);
        setLocationRelativeTo(null);

        initComponents();
        applyTheme();
        setVisible(true);
    }

    private void initComponents() {
        // create components (keep names predictable)
        mainPanel = new JPanel(new BorderLayout());
        titleLabel = new JLabel("Select Cinema");
        selectMovieLabel = new JLabel("Select Movie:");
        movieCombo = new JComboBox<>();
        selectCinemaLabel = new JLabel("Select Cinema:");
        cinemaCombo = new JComboBox<>();
        selectButton = new JButton("Select Cinema");
        changePasswordButton = new JButton("Change Password");
        backButton = new JButton("Back");

        // small sample items (real app will populate from model)
        movieCombo.addItem("— choose movie —");
        movieCombo.addItem("Movie A");
        movieCombo.addItem("Movie B");

        cinemaCombo.addItem("— choose cinema —");
        cinemaCombo.addItem("Cinema 1");
        cinemaCombo.addItem("Cinema 2");

        setContentPane(mainPanel);
    }

    private void applyTheme() {
        final int INNER_MAX_WIDTH = 450;
        final int CONTROL_MAX_WIDTH = 350;
        final int V_SPACE = 18;

        // Theme values (consistent with other forms)
        Color bg = new Color(45, 62, 80);
        Color fg = Color.WHITE;
        Color primary = new Color(0, 150, 136);
        Color danger = new Color(220, 80, 80);
        Font headerFont = new Font("Segoe UI", Font.BOLD, 26);
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font controlFont = new Font("Segoe UI", Font.PLAIN, 14);

        // root background
        mainPanel.setBackground(bg);

        // Header: top-center with spacing
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        header.setOpaque(false);
        titleLabel.setForeground(fg);
        titleLabel.setFont(headerFont);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(new EmptyBorder(V_SPACE + 6, 0, V_SPACE, 0));
        header.add(titleLabel);

        // Center content column (stacked)
        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBorder(new EmptyBorder(12, 16, 12, 16));
        inner.setAlignmentX(Component.CENTER_ALIGNMENT);
        inner.setMaximumSize(new Dimension(INNER_MAX_WIDTH, Integer.MAX_VALUE));

        // Select Movie label aligned to right edge of inner
        selectMovieLabel.setForeground(fg);
        selectMovieLabel.setFont(labelFont);
        selectMovieLabel.setBorder(new EmptyBorder(V_SPACE/2, 0, V_SPACE/2, 0));
        JPanel movieLabelRow = new JPanel(new BorderLayout());
        movieLabelRow.setOpaque(false);
        movieLabelRow.setMaximumSize(new Dimension(INNER_MAX_WIDTH, selectMovieLabel.getPreferredSize().height));
        movieLabelRow.add(selectMovieLabel, BorderLayout.EAST);
        inner.add(movieLabelRow);
        inner.add(Box.createVerticalStrut(8));

        // combo centered
        JPanel movieComboRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        movieComboRow.setOpaque(false);
        movieCombo.setFont(controlFont);
        movieCombo.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, movieCombo.getPreferredSize().height));
        movieCombo.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH, movieCombo.getPreferredSize().height));
        movieComboRow.add(movieCombo);
        inner.add(movieComboRow);

        inner.add(Box.createVerticalStrut(16));

        // Select Cinema label and combo (label right-aligned, combo centered)
        selectCinemaLabel.setForeground(fg);
        selectCinemaLabel.setFont(labelFont);
        selectCinemaLabel.setBorder(new EmptyBorder(V_SPACE/2, 0, V_SPACE/2, 0));
        JPanel cinemaLabelRow = new JPanel(new BorderLayout());
        cinemaLabelRow.setOpaque(false);
        cinemaLabelRow.setMaximumSize(new Dimension(INNER_MAX_WIDTH, selectCinemaLabel.getPreferredSize().height));
        cinemaLabelRow.add(selectCinemaLabel, BorderLayout.EAST);
        inner.add(cinemaLabelRow);
        inner.add(Box.createVerticalStrut(8));

        JPanel cinemaComboRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        cinemaComboRow.setOpaque(false);
        cinemaCombo.setFont(controlFont);
        cinemaCombo.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, cinemaCombo.getPreferredSize().height));
        cinemaCombo.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH, cinemaCombo.getPreferredSize().height));
        cinemaComboRow.add(cinemaCombo);
        inner.add(cinemaComboRow);

        inner.add(Box.createVerticalStrut(20));

        // Buttons stacked vertically and centered with even spacing
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

        styleBtn.accept(selectButton);
        selectButton.setBackground(primary);
        buttonsCol.add(selectButton);
        buttonsCol.add(Box.createVerticalStrut(12));

        styleBtn.accept(changePasswordButton);
        changePasswordButton.setBackground(primary);
        buttonsCol.add(changePasswordButton);
        buttonsCol.add(Box.createVerticalStrut(12));

        styleBtn.accept(backButton);
        backButton.setBackground(danger);
        buttonsCol.add(backButton);

        inner.add(buttonsCol);

        // Build mainPanel with header at NORTH and inner centered in CENTER
        mainPanel.removeAll();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(header, BorderLayout.NORTH);

        JPanel centerWrapper = new JPanel(new GridBagLayout()); // centers inner both axes
        centerWrapper.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        centerWrapper.add(inner, gbc);

        mainPanel.add(centerWrapper, BorderLayout.CENTER);

        // ensure background consistency
        Container current = getContentPane();
        if (current instanceof JComponent) {
            ((JComponent) current).setBackground(bg);
        }

        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        // run on EDT
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SelectCinema();
            }
        });
    }
}
