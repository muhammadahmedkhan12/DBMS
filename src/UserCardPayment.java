import javax.swing.*;
import java.awt.*;
import java.io.*;

public class UserCardPayment extends JFrame {
    private final String username, movieName, cinemaInfo, seat;

    public UserCardPayment(String username, String movieId, String movieName,
                           String cinemaInfo, String seat) {
        this.username   = username;
        this.movieName  = movieName;
        this.cinemaInfo = cinemaInfo;
        this.seat       = seat;

        // themed layout
        setTitle("Card Payment");
        setSize(1300, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        final int INNER_MAX_WIDTH = 450;
        final int CONTROL_MAX_WIDTH = 350;
        final int V_SPACE = 16;
        Color bg = new Color(45, 62, 80);
        Color fg = Color.WHITE;
        Color primary = new Color(0, 150, 136);

        Font headerFont = new Font("Segoe UI", Font.BOLD, 20);
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font controlFont = new Font("Segoe UI", Font.PLAIN, 14);

        // root
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(bg);
        root.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        setContentPane(root);

        // header (top-center)
        JLabel header = new JLabel("Card Payment", SwingConstants.CENTER);
        header.setForeground(fg);
        header.setFont(headerFont);
        header.setBorder(BorderFactory.createEmptyBorder(V_SPACE, 0, V_SPACE/2, 0));
        JPanel headerRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        headerRow.setOpaque(false);
        headerRow.add(header);
        root.add(headerRow, BorderLayout.NORTH);

        // inner column (centered)
        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBorder(BorderFactory.createEmptyBorder(6, 12, 12, 12));
        inner.setAlignmentX(Component.CENTER_ALIGNMENT);
        inner.setMaximumSize(new Dimension(INNER_MAX_WIDTH, Integer.MAX_VALUE));

        // Card Number
        JLabel lblCard = new JLabel("Card Number:");
        lblCard.setForeground(fg);
        lblCard.setFont(labelFont);
        lblCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        inner.add(lblCard);
        inner.add(Box.createVerticalStrut(6));
        JTextField card = new JTextField();
        card.setFont(controlFont);
        card.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, card.getPreferredSize().height));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        inner.add(card);
        inner.add(Box.createVerticalStrut(V_SPACE));

        // Expiry
        JLabel lblExp = new JLabel("Expiry (MM/YY):");
        lblExp.setForeground(fg);
        lblExp.setFont(labelFont);
        lblExp.setAlignmentX(Component.CENTER_ALIGNMENT);
        inner.add(lblExp);
        inner.add(Box.createVerticalStrut(6));
        JTextField exp = new JTextField();
        exp.setFont(controlFont);
        exp.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, exp.getPreferredSize().height));
        exp.setAlignmentX(Component.CENTER_ALIGNMENT);
        inner.add(exp);
        inner.add(Box.createVerticalStrut(V_SPACE));

        // CVV
        JLabel lblCvv = new JLabel("CVV:");
        lblCvv.setForeground(fg);
        lblCvv.setFont(labelFont);
        lblCvv.setAlignmentX(Component.CENTER_ALIGNMENT);
        inner.add(lblCvv);
        inner.add(Box.createVerticalStrut(6));
        JTextField cvv = new JTextField();
        cvv.setFont(controlFont);
        cvv.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, cvv.getPreferredSize().height));
        cvv.setAlignmentX(Component.CENTER_ALIGNMENT);
        inner.add(cvv);
        inner.add(Box.createVerticalStrut(V_SPACE + 6));
        // extra offset to push the buttons further downward
        final int BUTTON_OFFSET = 80;
        inner.add(Box.createVerticalStrut(BUTTON_OFFSET));

        // buttons row (submit + back)
        JButton submit = new JButton("Submit Payment");
        JButton backBtn = new JButton("Back");
        java.util.function.Consumer<AbstractButton> styleBtn = b -> {
            b.setFont(controlFont);
            b.setForeground(Color.WHITE);
            b.setOpaque(true);
            b.setFocusPainted(false);
            // increased top margin so text sits lower inside button
            b.setMargin(new Insets(16, 16, 8, 16));
            b.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH/2 + 20, b.getPreferredSize().height));
            b.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH/2 + 20, b.getPreferredSize().height));
            b.setHorizontalAlignment(SwingConstants.CENTER);
            b.setHorizontalTextPosition(SwingConstants.CENTER);
        };
        styleBtn.accept(submit);
        submit.setBackground(primary);
        styleBtn.accept(backBtn);
        backBtn.setBackground(new Color(220,80,80));

        // increase vertical gap (vgap) to push the row downward
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 20));
        btnRow.setOpaque(false);
        btnRow.add(submit);
        btnRow.add(backBtn);
        inner.add(btnRow);

        // center inner
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(inner, new GridBagConstraints());
        root.add(centerWrapper, BorderLayout.CENTER);

        // listeners (preserve behavior)
        submit.addActionListener(e -> {
            if (card.getText().isBlank() || exp.getText().isBlank() || cvv.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Fill all card fields.");
                return;
            }
            saveTicket();
            JOptionPane.showMessageDialog(this,
                    "Payment Successful!\nMovie: " + movieName +
                            "\n" + cinemaInfo + "\nSeat: " + seat);
            dispose();
        });

        backBtn.addActionListener(e -> {
            new UserPaymentSelection(username, null, movieName, cinemaInfo).setVisible(true);
            dispose();
        });

        // final styling
        getContentPane().setBackground(bg);
        revalidate();
        repaint();
    }

    private void saveTicket() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("tickets.txt", true))) {
            bw.write(username + "," + movieName + "," + cinemaInfo + "," + seat + ",card");
            bw.newLine();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving ticket.");
        }
    }

    public static void main(String[] args) {
        new UserCardPayment("user1", "M001", "Inception",
                "Cinema 1 - 7:00 PM", "A5").setVisible(true);
    }
}
