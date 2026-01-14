import Cinema.Cinema;
import Cinema.CinemaManager;
import Database.DBConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class Editcinemas extends JFrame {
    private JTextField cinemaidtextfield;
    private JTextField Newnametextfield;
    private JTextField newscreentextfield;
    private JLabel Cinemaidlabel;
    private JLabel Newnamelabel;
    private JLabel Newscreentypelabel;
    private JButton editecinemabutton;
    private JPanel MyPanel;
    private JButton backButton;

    CinemaManager cinemaManager = CinemaManager.getCinemaManager();

    public Editcinemas() {
        setContentPane(MyPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);

        // apply theme/layout before attaching listeners
        applyTheme();

        // attach listeners
        if (editecinemabutton != null) {
            editecinemabutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String cinemaid = (cinemaidtextfield != null) ? cinemaidtextfield.getText().trim() : "";
                    String newname = (Newnametextfield != null) ? Newnametextfield.getText().trim() : "";
                    String newScreen = (newscreentextfield != null) ? newscreentextfield.getText().trim() : "";

                    if (cinemaid.isEmpty() || newname.isEmpty()) {
                        JOptionPane.showMessageDialog(Editcinemas.this, "Please enter Cinema ID and new name.");
                        return;
                    }

                    boolean found = false;
                    ArrayList<Cinema> cinemas = cinemaManager.getCinemas();

                    try (Connection con = DBConnection.getConnection()) {
                        String query = "UPDATE Cinemas SET Name = ? WHERE CinemaID = ?";
                        try (PreparedStatement stmt = con.prepareStatement(query)) {
                            stmt.setString(1, newname);
                            stmt.setString(2, cinemaid);
                            int rows = stmt.executeUpdate();
                            if (rows > 0) {
                                found = true;

                                // Update in-memory object as well
                                for (Cinema c : cinemas) {
                                    if (c.getCinemaid().equals(cinemaid)) {
                                        c.setName(newname);
                                        // Optionally update screen type if needed
                                        // c.setScreentype(newScreen);
                                        break;
                                    }
                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(Editcinemas.this, "Error updating cinema in database.");
                        return;
                    }

                    if (found) {
                        JOptionPane.showMessageDialog(Editcinemas.this, "Cinema updated successfully!");
                    } else {
                        JOptionPane.showMessageDialog(Editcinemas.this, "Cinema ID not found in DB.");
                    }
                }
            });
        }

        if (backButton != null) {
            backButton.addActionListener(e -> {
                new AdminMain();
                dispose();
            });
        }

        setVisible(true);
    }

    // Keep your existing applyTheme() method here
    private void applyTheme() {
        // ...existing styling code (same as your original)...
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Editcinemas());
    }
}
