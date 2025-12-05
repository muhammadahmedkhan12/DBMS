import Cinema.CinemaManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeleteCinemas extends JFrame {
    private JTextField cinemaidtextfield;
    private JButton DeleteCinema;
    private JPanel MyPanel;
    private JLabel cinemaidlabel;
    private JButton backButton;

    CinemaManager manager = new CinemaManager();

    public DeleteCinemas() {
        setTitle("Delete Cinema");
        setContentPane(MyPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setVisible(true);

        DeleteCinema.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cinemaId = cinemaidtextfield.getText();

                if (cinemaId.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a Cinema ID.");
                    return;
                }

                boolean found = false;
                for (int i = 0; i < manager.getCinemas().size(); i++) {
                    if (manager.getCinemas().get(i).getCinemaid().equals(cinemaId)) {
                        found = true;
                        break;
                    }
                }

                if (found) {
                    manager.deletecinema(cinemaId);
                    JOptionPane.showMessageDialog(null, "Cinema deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(null, "Cinema ID not found.");
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminMain();
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        new DeleteCinemas();
    }
}
