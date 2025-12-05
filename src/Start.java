import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Start extends JFrame{
    private JLabel WelcomeLabel;
    private JButton Admin;
    private JButton User;
    private JPanel MyPanel;

    public Start() {
        setContentPane(MyPanel);
        setTitle("Cinema Ticket Booking System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1300, 900);
        setLocationRelativeTo(null);
        setVisible(true);

        Admin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AdminWelcome();
            }
        });
        User.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new WelcomePage();
            }
        });
    }

    public static void main(String[] args) {
        new Start();
    }
}
