import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignIn extends JFrame{
    private JLabel MyLabel;
    private JLabel UserName;
    private JTextField UsernameInput;
    private JButton ClickMe;
    private JPasswordField Password;
    private JLabel MyLabel2;
    private JPanel SignInPanel;
    private JButton backButton;
    private JButton ChangePasswordButton;


    public SignIn(){
        setContentPane(SignInPanel);
        setTitle("Cinema Ticket Booking System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500,300);
        setLocationRelativeTo(null);
        setVisible(true);
        ClickMe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = UsernameInput.getText();
                String password = Password.getText();
                String result = AuthenticationService.getAuthenticateuser().login(username,password);
                JOptionPane.showMessageDialog(SignIn.this,result);
                if (result.equalsIgnoreCase("login successfull")) {
                    dispose();
                    new UserMainDashboard(username);
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new WelcomePage();
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        new SignIn();
    }
}
