import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

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

        // apply centralized theme (matches Start/AdminWelcome look exactly)
        Start.applyTheme(this, MyPanel, WelcomeLabel,
                new JButton[]{signInButton},
                new JButton[]{backButton});

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
        // run on EDT
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AdminWelcome();
            }
        });
    }
}
