import Cinema.Cinema;
import Cinema.CinemaManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    public Editcinemas(){
        setContentPane(MyPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setVisible(true);

        editecinemabutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cinemaid = cinemaidtextfield.getText();
                String newname = Newnametextfield.getText();
                String newScreen = newscreentextfield.getText();

                boolean found = false;
                ArrayList<Cinema> cinemas = cinemaManager.getCinemas();

                for (int i = 0; i < cinemas.size(); i++) {
                    Cinema c = cinemas.get(i);
                    if (c.getCinemaid().equals(cinemaid)) {
                        c.setName(newname);
//                        c.setScreentype(newScreen);
                        found = true;
                        break;
                    }
                }

                if (found) {
                    cinemaManager.savecinemas();
                    JOptionPane.showMessageDialog(null, "Cinema updated successfully!");
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
        new Editcinemas();
    }
}
