import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminWelcome extends JFrame{
    private JLabel WelcomeLabel;
    private JButton signInButton;
    private JPanel MyPanel;
    private JButton backButton;

    public AdminWelcome(){
        setContentPane(MyPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500,300);
        setLocationRelativeTo(null);
        setVisible(true);
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminSignIn();
            }
        });


        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Start();
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        new AdminWelcome();
    }
}
