import javax.swing.*;
import java.awt.*;
import java.io.*;

public class UserPaymentSelection extends JFrame {
    private String username, movieId, movieName, cinemaInfo;
    private JTextField seatField;
    private int maxSeats = 0;

    public UserPaymentSelection(String username, String movieId, String movieName, String cinemaInfo) {
        this.username = username;
        this.movieId = movieId;
        this.movieName = movieName;
        this.cinemaInfo = cinemaInfo;

        extractSeatCapacity();

        setTitle("Choose Payment and Seat");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1));

        add(new JLabel("Movie: " + movieName));
        add(new JLabel("Cinema: " + cinemaInfo));
        add(new JLabel("Enter Seat Number (1 - " + maxSeats + "):"));

        seatField = new JTextField();
        add(seatField);

        JPanel buttons = new JPanel(new GridLayout(1, 2));
        JButton cardBtn = new JButton("Pay by Card");
        JButton cashBtn = new JButton("Pay at Cinema");


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

        buttons.add(cardBtn);
        buttons.add(cashBtn);
        add(buttons);
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> {
            new UserCinemaSelection(username, movieName).setVisible(true);
            dispose();
        });
        add(backBtn);
    }

    private void extractSeatCapacity() {
        try (BufferedReader br = new BufferedReader(new FileReader("cinemas.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (cinemaInfo.contains(line.split(",")[1])) {
                    maxSeats = Integer.parseInt(line.split(",")[4]);
                    break;
                }
            }
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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("tickets.txt", true))) {
            writer.write(username + "," + movieName + "," + cinemaInfo + "," + seat + "," + paymentMethod);
            writer.newLine();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving ticket.");
        }
    }
}
