import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*; // added for Color/Font

public class WelcomePage extends JFrame{
    private JLabel WelcomeLabel;
    private JButton SignIn;
    private JButton SignUp;
    private JPanel WelcomePanel;
    private JButton backButton;

    public WelcomePage(){
        setContentPane(WelcomePanel);
        setTitle("Cinema Ticket Booking System");
        
        setSize(1300, 900);
        setLocationRelativeTo(null);

        // apply same centralized theme as Start/AdminWelcome
        Start.applyTheme(this, WelcomePanel, WelcomeLabel,
                new JButton[]{SignIn, SignUp},
                new JButton[]{backButton});

        setVisible(true);
        SignIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new SignIn();
            }
        });
        SignUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new SignUp();
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
        // run on EDT
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WelcomePage();
            }
        });
    }
}
