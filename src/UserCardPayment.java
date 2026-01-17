import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import Ticket.TicketManager;


public class UserCardPayment extends JFrame {

    private final String username;
    private final String movieName;
    private final String cinemaInfo;
    private final String seat;
    private String screentype;
    private int showId;

    public UserCardPayment(
            String username,
            String movieName,
            String screentype,
            String cinemaInfo,
            String seat,
            int showId

    ) {
        this.screentype = screentype;
        this.username = username;
        this.movieName = movieName;
        this.cinemaInfo = cinemaInfo;
        this.seat = seat;
        this.showId = showId;


        setTitle("Card Payment");
        setSize(1300, 900);
        setLocationRelativeTo(null);
        

        // Theme constants
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

        // Root panel
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(bg);
        root.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(root);

        // Header
        JLabel header = new JLabel("Card Payment", SwingConstants.CENTER);
        header.setFont(headerFont);
        header.setForeground(fg);
        header.setBorder(new EmptyBorder(V_SPACE, 0, V_SPACE, 0));
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        headerPanel.setOpaque(false);
        headerPanel.add(header);
        root.add(headerPanel, BorderLayout.NORTH);

        // Inner column centered
        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBorder(new EmptyBorder(12, 16, 12, 16));
        inner.setAlignmentX(Component.CENTER_ALIGNMENT);
        inner.setMaximumSize(new Dimension(INNER_MAX_WIDTH, Integer.MAX_VALUE));

        // Booking summary
        JLabel summaryLabel = new JLabel("Booking Summary", SwingConstants.CENTER);
        summaryLabel.setForeground(fg);
        summaryLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        summaryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        inner.add(summaryLabel);
        inner.add(Box.createVerticalStrut(8));

        JLabel movieLabel = new JLabel("Movie: " + movieName, SwingConstants.CENTER);
        movieLabel.setForeground(fg);
        movieLabel.setFont(labelFont);
        movieLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        inner.add(movieLabel);
        inner.add(Box.createVerticalStrut(4));

        JLabel cinemaLabel = new JLabel("Cinema: " + cinemaInfo, SwingConstants.CENTER);
        cinemaLabel.setForeground(fg);
        cinemaLabel.setFont(labelFont);
        cinemaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        inner.add(cinemaLabel);
        inner.add(Box.createVerticalStrut(4));

        JLabel seatLabel = new JLabel("Seat: " + seat, SwingConstants.CENTER);
        seatLabel.setForeground(fg);
        seatLabel.setFont(labelFont);
        seatLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        inner.add(seatLabel);
        inner.add(Box.createVerticalStrut(V_SPACE));

        // Card details section
        JLabel cardDetailsLabel = new JLabel("Enter Card Details", SwingConstants.CENTER);
        cardDetailsLabel.setForeground(fg);
        cardDetailsLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        cardDetailsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        inner.add(cardDetailsLabel);
        inner.add(Box.createVerticalStrut(12));

        // Card number field
        JLabel cardLabel = new JLabel("Card Number:");
        cardLabel.setForeground(fg);
        cardLabel.setFont(labelFont);
        cardLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        inner.add(cardLabel);
        inner.add(Box.createVerticalStrut(6));

        JTextField cardField = new JTextField();
        cardField.setFont(controlFont);
        cardField.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, 35));
        cardField.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH, 35));
        cardField.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel cardRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        cardRow.setOpaque(false);
        cardRow.add(cardField);
        inner.add(cardRow);
        inner.add(Box.createVerticalStrut(12));

        JLabel expLabel = new JLabel("Expiry (MM/YY):");
        expLabel.setForeground(fg);
        expLabel.setFont(labelFont);
        expLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        inner.add(expLabel);
        inner.add(Box.createVerticalStrut(6));

        JTextField expField = new JTextField();
        expField.setFont(controlFont);
        expField.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, 35));
        expField.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH, 35));
        expField.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel expRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        expRow.setOpaque(false);
        expRow.add(expField);
        inner.add(expRow);
        inner.add(Box.createVerticalStrut(12));

        JLabel cvvLabel = new JLabel("CVV:");
        cvvLabel.setForeground(fg);
        cvvLabel.setFont(labelFont);
        cvvLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        inner.add(cvvLabel);
        inner.add(Box.createVerticalStrut(6));

        JTextField cvvField = new JTextField();
        cvvField.setFont(controlFont);
        cvvField.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, 35));
        cvvField.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH, 35));
        cvvField.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel cvvRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        cvvRow.setOpaque(false);
        cvvRow.add(cvvField);
        inner.add(cvvRow);
        inner.add(Box.createVerticalStrut(V_SPACE));

        // Buttons
        JButton submit = new JButton("Submit Payment");
        JButton backBtn = new JButton("Back");

        java.util.function.Consumer<AbstractButton> styleBtn = b -> {
            b.setFont(controlFont);
            b.setForeground(Color.WHITE);
            b.setOpaque(true);
            b.setFocusPainted(false);
            b.setHorizontalAlignment(SwingConstants.CENTER);
            b.setHorizontalTextPosition(SwingConstants.CENTER);
            b.setMargin(new Insets(8, 16, 8, 16));
            b.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH / 2 - 8, b.getPreferredSize().height));
            b.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH / 2 - 8, b.getPreferredSize().height));
        };

        styleBtn.accept(submit);
        submit.setBackground(primary);

        styleBtn.accept(backBtn);
        backBtn.setBackground(danger);

        JPanel buttonsRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        buttonsRow.setOpaque(false);
        buttonsRow.add(submit);
        buttonsRow.add(backBtn);
        inner.add(buttonsRow);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        centerWrapper.add(inner, gbc);
        root.add(centerWrapper, BorderLayout.CENTER);

        submit.addActionListener(e -> {
            if (cardField.getText().isBlank()
                    || expField.getText().isBlank()
                    || cvvField.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Please fill all card details.");
                return;
            }

            if (saveTicket()) {
                JOptionPane.showMessageDialog(this,
                        "Payment Successful!\n\n" + "Movie: " + movieName + "\n" +
                                "Cinema: " + cinemaInfo + "\n" + "Seat: " + seat);
                new UserMainDashboard(username).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Seat already booked or error occurred.");
            }
        });

        backBtn.addActionListener(e -> {
            new UserPaymentSelection(
                    username, movieName, screentype, cinemaInfo, showId
            ).setVisible(true);
            dispose();
        });

        getContentPane().setBackground(bg);
        revalidate();
        repaint();
        setVisible(true);
    }

    private boolean saveTicket() {
        int seatNumber = Integer.parseInt(seat);

        boolean success = TicketManager.saveTicket(username, movieName, cinemaInfo, seatNumber, "Card", screentype);

        if (!success) {
            JOptionPane.showMessageDialog(this, "Seat " + seatNumber + " has just been booked. Please select another seat.");
        }

        return success;
    }

}