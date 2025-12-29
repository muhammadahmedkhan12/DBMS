import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import Cinema.CinemaManager;
import Cinema.Cinema;
import Cinema.Screen;
import Ticket.TicketManager;

public class UserPaymentSelection extends JFrame {
    private final String username;
    private final String movieId;
    private final String movieName;
    private final String cinemaInfo;
    private final JTextField seatField;
    private int maxSeats = 0;

    public UserPaymentSelection(String username, String movieId, String movieName, String cinemaInfo) {
        this.username = username;
        this.movieId = movieId;
        this.movieName = movieName;
        this.cinemaInfo = cinemaInfo;

        seatField = new JTextField();

        extractSeatCapacity();

        setTitle("Choose Payment and Seat");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 900);
        setLocationRelativeTo(null);

        // create components (preserve original names/behavior)
        JLabel movieLabel = new JLabel("Movie: " + movieName, SwingConstants.CENTER);
        JLabel cinemaLabel = new JLabel("Cinema: " + cinemaInfo, SwingConstants.CENTER);
        JLabel seatLabel = new JLabel("Enter Seat Number (1 - " + maxSeats + "):", SwingConstants.CENTER);

        JButton cardBtn = new JButton("Pay by Card");
        JButton cashBtn = new JButton("Pay at Cinema");
        JButton backBtn = new JButton("Back");

        // apply theme and layout (centers content and constrains widths)
        applyTheme(movieLabel, cinemaLabel, seatLabel, seatField, cardBtn, cashBtn, backBtn);

        // attach listeners
        cardBtn.addActionListener(e -> {
            String seat = seatField.getText().trim();
            if (!isValidSeat(seat)) return;
            new UserCardPayment(username, movieId, movieName, cinemaInfo, seat).setVisible(true);
            dispose();
        });

        cashBtn.addActionListener(e -> {
            String seat = seatField.getText().trim();
            if (!isValidSeat(seat)) return;
            saveTicket("cash", seat);
            JOptionPane.showMessageDialog(this, "Ticket booked! Pay at cinema.");
            dispose();
        });

        backBtn.addActionListener(e -> {
            new UserCinemaSelection(username, movieName).setVisible(true);
            dispose();
        });

        // Finalize window visibility moved after UI build in applyTheme
    }

    // apply theme and build centered layout
    private void applyTheme(JLabel movieLabel, JLabel cinemaLabel, JLabel seatLabel,
                            JTextField seatField, JButton cardBtn, JButton cashBtn, JButton backBtn) {
        final int INNER_MAX_WIDTH = 450;
        final int CONTROL_MAX_WIDTH = 350;
        final int V_SPACE = 20;
        final int BUTTON_OFFSET = 30; // extra vertical space to push buttons down

        Color bg = new Color(45, 62, 80);
        Color fg = Color.WHITE;
        Color primary = new Color(0, 150, 136);
        Color danger = new Color(220, 80, 80);
        Font headerFont = new Font("Segoe UI", Font.BOLD, 20);
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        // special font for movie & cinema labels (user request)
        Font labelFontLeftNudged = new Font("Arial", Font.PLAIN, 16);
        Font controlFont = new Font("Segoe UI", Font.PLAIN, 14);

        // root panel
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(bg);
        root.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(root);

        // Header (top-centre)
        JLabel header = new JLabel("Payment & Seat Selection", SwingConstants.CENTER);
        header.setForeground(fg);
        header.setFont(headerFont);
        // reduce bottom inset so the content (movie/cinema labels) appear a bit higher
        header.setBorder(new EmptyBorder(V_SPACE, 0, V_SPACE / 4, 0));
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        headerPanel.setOpaque(false);
        headerPanel.add(header);
        root.add(headerPanel, BorderLayout.NORTH);

        // Inner vertical column centered
        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        // reduce top padding to pull the first label upward, keeping side/bottom paddings
        inner.setBorder(new EmptyBorder(6, 16, 12, 16));
        inner.setAlignmentX(Component.CENTER_ALIGNMENT);
        inner.setMaximumSize(new Dimension(INNER_MAX_WIDTH, Integer.MAX_VALUE));

        // movie/cinema/seat labels and fields (centered)
        if (movieLabel != null) {
            movieLabel.setForeground(fg);
            // use the requested font family and slightly reduce top spacing so it sits higher
            movieLabel.setFont(labelFontLeftNudged);
            movieLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            movieLabel.setBorder(new EmptyBorder(0, 0, V_SPACE / 3, 0));
            inner.add(movieLabel);
            inner.add(Box.createVerticalStrut(8));
        }
        if (cinemaLabel != null) {
            cinemaLabel.setForeground(fg);
            // use the requested font family and slightly reduce top spacing so it sits higher
            cinemaLabel.setFont(labelFontLeftNudged);
            cinemaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            cinemaLabel.setBorder(new EmptyBorder(0, 0, V_SPACE / 3, 0));
            inner.add(cinemaLabel);
            inner.add(Box.createVerticalStrut(12));
        }
        if (seatLabel != null) {
            seatLabel.setForeground(fg);
            seatLabel.setFont(labelFont);
            seatLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            seatLabel.setBorder(new EmptyBorder(0, 0, 6, 0));
            inner.add(seatLabel);
            inner.add(Box.createVerticalStrut(6));
        }

        if (seatField != null) {
            seatField.setFont(controlFont);
            seatField.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, seatField.getPreferredSize().height));
            seatField.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH, seatField.getPreferredSize().height));
            seatField.setAlignmentX(Component.CENTER_ALIGNMENT);
            inner.add(seatField);
            seatField.setMargin(new Insets(5, 5, 0, 5));

        }

        // keep original spacing, then add a small extra offset to push buttons downward
        inner.add(Box.createVerticalStrut(V_SPACE));
        inner.add(Box.createVerticalStrut(BUTTON_OFFSET));

        // buttons row (card and cash together) and back below
        JPanel buttonsRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        buttonsRow.setOpaque(false);

        java.util.function.Consumer<AbstractButton> styleBtn = b -> {
            b.setFont(controlFont);
            b.setForeground(Color.WHITE);
            b.setOpaque(true);
            b.setFocusPainted(false);
            b.setHorizontalAlignment(SwingConstants.CENTER);
            b.setHorizontalTextPosition(SwingConstants.CENTER);
            b.setMargin(new Insets(8, 16, 8, 16));
            b.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH / 2 + 20, b.getPreferredSize().height));
            b.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH / 2 + 20, b.getPreferredSize().height));
        };

        if (cardBtn != null) {
            styleBtn.accept(cardBtn);
            cardBtn.setBackground(primary);
            buttonsRow.add(cardBtn);
        }
        if (cashBtn != null) {
            styleBtn.accept(cashBtn);
            cashBtn.setBackground(danger);
            buttonsRow.add(cashBtn);
        }
        inner.add(buttonsRow);
        inner.add(Box.createVerticalStrut(12));

        if (backBtn != null) {
            styleBtn.accept(backBtn);
            backBtn.setBackground(danger);
            // place back button centered below
            JPanel backRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            backRow.setOpaque(false);
            backRow.add(backBtn);
            inner.add(backRow);
        }

        // center inner column in the page
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        centerWrapper.add(inner, gbc);
        root.add(centerWrapper, BorderLayout.CENTER);

        // final styling
        getContentPane().setBackground(bg);
        revalidate();
        repaint();
    }

    private void extractSeatCapacity() {
        // Use CinemaManager to determine seat capacity for the selected cinema and screen
        try {
            // cinemaInfo expected format like "CinemaName | Screen S1 (2D)" or just cinema name
            String cinemaName = cinemaInfo;
            String screenId = null;
            if (cinemaInfo.contains("|")) {
                String[] parts = cinemaInfo.split("\\|", 2);
                cinemaName = parts[0].trim();
                String screenPart = parts[1].trim(); // e.g. "Screen S1 (2D)"
                if (screenPart.contains("S")) {
                    int idx = screenPart.indexOf('S');
                    int end = screenPart.indexOf(' ', idx);
                    if (end == -1) end = screenPart.length();
                    screenId = screenPart.substring(idx, end).trim();
                }
            }

            CinemaManager cm = CinemaManager.getCinemaManager();
            for (Cinema c : cm.getCinemas()) {
                if (c.getName().equalsIgnoreCase(cinemaName) || c.getCinemaid().equals(cinemaName)) {
                    if (screenId == null && !c.getScreens().isEmpty()) {
                        maxSeats = c.getScreens().get(0).getNumberOfSeats();
                        return;
                    }
                    for (Screen s : c.getScreens()) {
                        if (screenId == null || s.getScreenid().equalsIgnoreCase(screenId)) {
                            maxSeats = s.getNumberOfSeats();
                            return;
                        }
                    }
                }
            }
            // fallback
            maxSeats = 100;
        } catch (Exception e) {
            maxSeats = 100;
        }
    }

    private boolean isValidSeat(String seatStr) {
        try {
            int seat = Integer.parseInt(seatStr);
            if (seat < 1 || seat > maxSeats) {
                JOptionPane.showMessageDialog(this, "Seat must be between 1 and " + maxSeats + ".");
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Seat number must be numeric.");
            return false;
        }
        return true;
    }

    private void saveTicket(String paymentMethod, String seat) {
        try {
            // best-effort parse of cinemaInfo to cinemaId and screenId
            String cinemaName = cinemaInfo;
            String screenId = null;
            if (cinemaInfo.contains("|")) {
                String[] parts = cinemaInfo.split("\\|", 2);
                cinemaName = parts[0].trim();
                String screenPart = parts[1].trim();
                if (screenPart.contains("S")) {
                    int idx = screenPart.indexOf('S');
                    int end = screenPart.indexOf(' ', idx);
                    if (end == -1) end = screenPart.length();
                    screenId = screenPart.substring(idx, end).trim();
                }
            }

            // find cinema id by name
            String cinemaId = null;
            CinemaManager cm = CinemaManager.getCinemaManager();
            for (Cinema c : cm.getCinemas()) {
                if (c.getName().equalsIgnoreCase(cinemaName) || c.getCinemaid().equals(cinemaName)) {
                    cinemaId = c.getCinemaid();
                    break;
                }
            }

            TicketManager tm = new TicketManager();
            // seatLabel use seat number as label
            int ticketId = tm.reserveSeat(username, getIntOrNull(movieId), cinemaId, screenId == null ? "S1" : screenId, seat, "unknown", paymentMethod, null);
            if (ticketId <= 0) {
                JOptionPane.showMessageDialog(this, "Seat is already booked. Please choose another seat.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving ticket: " + ex.getMessage());
        }
    }

    private Integer getIntOrNull(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return null; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserPaymentSelection ups = new UserPaymentSelection("testuser", "M001", "Inception", "CinemaX - Hall 1");
            ups.setVisible(true);
        });
    }
}
