import Cinema.Cinema;
import Cinema.CinemaManager;
import Database.DBConnection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class DeleteCinemas extends JFrame {
    private JTextField cinemaidtextfield;
    private JButton DeleteCinema;
    private JPanel MyPanel;
    private JLabel cinemaidlabel;
    private JButton backButton;

    CinemaManager manager = CinemaManager.getCinemaManager();

    public DeleteCinemas() {
        setTitle("Delete Cinema");
        setContentPane(MyPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);

        // Delete cinema action
        DeleteCinema.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cinemaId = cinemaidtextfield.getText().trim();

                if (cinemaId.isEmpty()) {
                    JOptionPane.showMessageDialog(DeleteCinemas.this, "Please enter a Cinema ID.");
                    return;
                }

                boolean found = false;

                try (Connection con = DBConnection.getConnection()) {
                    String query = "DELETE FROM Cinemas WHERE CinemaID = ?";
                    try (PreparedStatement stmt = con.prepareStatement(query)) {
                        stmt.setString(1, cinemaId);
                        int rows = stmt.executeUpdate();
                        if (rows > 0) {
                            found = true;

                            // Remove from in-memory list
                            manager.getCinemas().removeIf(c -> c.getCinemaid().equals(cinemaId));
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(DeleteCinemas.this, "Error deleting cinema from database.");
                    return;
                }

                if (found) {
                    JOptionPane.showMessageDialog(DeleteCinemas.this, "Cinema deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(DeleteCinemas.this, "Cinema ID not found in DB.");
                }
            }
        });

        // Back button action
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminMain();
                dispose();
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DeleteCinemas::new);
    }
}
