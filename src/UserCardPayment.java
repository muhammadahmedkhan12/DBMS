import javax.swing.*;
import java.awt.*;
import java.sql.*;
import Database.DBConnection;

public class UserCardPayment extends JFrame {

    private final String username;
    private final String movieName;
    private final String cinemaInfo;
    private final String seat;

    private final int movieId;


    public UserCardPayment(
            String username,
            int movieId,
            String movieName,
            String cinemaName,
            String seat
    ) {
        this.username = username;
        this.movieId = movieId;
        this.movieName = movieName;
        this.cinemaInfo = cinemaName;
        this.seat = seat;

        // ================= WINDOW =================
        setTitle("Card Payment");
        setSize(1300, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Color bg = new Color(45, 62, 80);
        Color fg = Color.WHITE;
        Color primary = new Color(0, 150, 136);

        Font headerFont = new Font("Segoe UI", Font.BOLD, 20);
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font controlFont = new Font("Segoe UI", Font.PLAIN, 14);

        // ================= ROOT =================
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(bg);
        root.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(root);

        JLabel header = new JLabel("Card Payment", SwingConstants.CENTER);
        header.setFont(headerFont);
        header.setForeground(fg);
        root.add(header, BorderLayout.NORTH);

        // ================= CENTER =================
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JTextField cardField = createField("Card Number", labelFont, controlFont, fg);
        JTextField expField = createField("Expiry (MM/YY)", labelFont, controlFont, fg);
        JTextField cvvField = createField("CVV", labelFont, controlFont, fg);

        center.add(cardField);
        center.add(expField);
        center.add(cvvField);

        root.add(center, BorderLayout.CENTER);

        // ================= BUTTONS =================
        JButton submit = new JButton("Submit Payment");
        JButton backBtn = new JButton("Back");

        submit.setBackground(primary);
        submit.setForeground(Color.WHITE);

        backBtn.setBackground(new Color(220, 80, 80));
        backBtn.setForeground(Color.WHITE);

        JPanel btnRow = new JPanel(new FlowLayout());
        btnRow.setOpaque(false);
        btnRow.add(submit);
        btnRow.add(backBtn);

        root.add(btnRow, BorderLayout.SOUTH);

        // ================= ACTIONS =================
        submit.addActionListener(e -> {
            if (cardField.getText().isBlank()
                    || expField.getText().isBlank()
                    || cvvField.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Fill all card details.");
                return;
            }

            if (saveTicket()) {
                JOptionPane.showMessageDialog(this,
                        "Payment Successful!\n\nMovie: " + movieName +
                                "\n" + cinemaInfo +
                                "\nSeat: " + seat);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Seat already booked or error occurred.");
            }
        });

        backBtn.addActionListener(e -> {
            new UserPaymentSelection(
                    username,
                    movieId,
                    movieName,
                    cinemaInfo // seat capacity already validated earlier
            ).setVisible(true);
            dispose();
        });
    }

    // ================= DB SAVE =================
    private boolean saveTicket() {
        int seatNumber = Integer.parseInt(seat);

        String sql = """
                INSERT INTO Tickets
                (Username, MovieID, ShowID, SeatNumber, PaymentMethod)
                VALUES (?, ?, ?, ?, 'card')
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setInt(2, movieId);
            ps.setInt(3, showId);
            ps.setInt(4, seatNumber);

            ps.executeUpdate();
            return true;

        } catch (SQLIntegrityConstraintViolationException ex) {
            return false;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // ================= HELPER =================
    private JTextField createField(
            String labelText,
            Font labelFont,
            Font fieldFont,
            Color fg
    ) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(labelText);
        label.setForeground(fg);
        label.setFont(labelFont);

        JTextField field = new JTextField();
        field.setFont(fieldFont);
        field.setMaximumSize(new Dimension(350, 30));

        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(field);
        panel.add(Box.createVerticalStrut(15));

        return field;
    }
}
