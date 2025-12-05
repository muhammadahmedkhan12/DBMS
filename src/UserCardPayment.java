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

        setTitle("Card Payment");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        add(new JLabel("Card Number:")); JTextField card = new JTextField(); add(card);
        add(new JLabel("Expiry (MM/YY):")); JTextField exp = new JTextField(); add(exp);
        add(new JLabel("CVV:")); JTextField cvv = new JTextField(); add(cvv);

        JButton submit = new JButton("Submit Payment");
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

        add(new JLabel()); add(submit);
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> {
            new UserPaymentSelection(username, null, movieName, cinemaInfo).setVisible(true);
            dispose();
        });
        add(backBtn);
    }

    private void saveTicket() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("tickets.txt", true))) {
            bw.write(username + "," + movieName + "," + cinemaInfo + "," + seat + ",card");
            bw.newLine();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving ticket.");
        }
    }
}
