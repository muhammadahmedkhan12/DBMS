import Cinema.Cinema;
import Cinema.CinemaManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddnewCinema extends JFrame {
    private JTextField cinemaidtextfield;
    private JTextField cinemanametextfiled;
    private JTextField screentypetextfield;
    private JLabel cinemaidlabel;
    private JLabel Cinemanamelabel;
    private JLabel cinemascreentypelabel;
    private JButton addcinemabutton;
    private JPanel MyPanel;
    private JButton backButton;

    CinemaManager cinemaManager = new CinemaManager();

    public AddnewCinema(){
        setContentPane(MyPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setVisible(true);

        addcinemabutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cinemaId = cinemaidtextfield.getText().trim();
                String cinemaName = cinemanametextfiled.getText().trim();

                if (cinemaId.isEmpty() || cinemaName.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.");
                    return;
                }

                Cinema newCinema = new Cinema(cinemaId, cinemaName);
                cinemaManager.addcinema(newCinema);
                JOptionPane.showMessageDialog(null, "Cinema added successfully!");

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
        new AddnewCinema();
    }
}
