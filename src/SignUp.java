import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUp extends JFrame {

    private JLabel WelcomLabel;
    private JLabel Username;
    private JTextField UsernameInput;
    private JLabel Password;
    private JButton signUpButton;
    private JPanel SignUpPanel;
    private JPasswordField passwordInput;
    private JLabel Email;
    private JTextField EmailInput;
    private JLabel ContactNo;
    private JTextField ContactInput;
    private JLabel ReEnterPassword;
    private JPasswordField ReEnterPasswordInput;
    private JLabel PasswordCheck;
    private JButton backButton;

    public SignUp(){
        setContentPane(SignUpPanel);
        setTitle("Cinema Ticket Booking System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500,500);
        setLocationRelativeTo(null);
        setVisible(true);
        PasswordCheck.setForeground(Color.blue);

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = UsernameInput.getText();
                String password = passwordInput.getText();
                String ReEnteredPassword = ReEnterPasswordInput.getText();
                String email = EmailInput.getText();
                String contactno = ContactInput.getText();


                String result;

                if (username.length() < 6) {
                    JOptionPane.showMessageDialog(SignUp.this, "Username should not be less than 6 characters!");
                    return;
                }

                if (!password.equals(ReEnteredPassword)) {
                    result = "Sorry, password and Re-entered password are not same";
                    JOptionPane.showMessageDialog(SignUp.this, result);
                    return;
                }

                try {
                    if (!AuthenticationService.chkEmail(email)) {
                        JOptionPane.showMessageDialog(SignUp.this, "Invalid or already registered email!");
                        return;
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(SignUp.this, "Error while validating email.");
                    return;
                }

                try {
                    if (!AuthenticationService.chkContact(contactno)) {
                        JOptionPane.showMessageDialog(SignUp.this, "Invalid or already registered contact number!");
                        return;
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(SignUp.this, "Error while validating contact number.");
                    return;
                }

                result = AuthenticationService.getAuthenticateuser().SignUp(username, password, email, contactno);
                JOptionPane.showMessageDialog(SignUp.this, result);

                if (!result.toLowerCase().contains("sorry") && !result.toLowerCase().contains("fail")&&!result.toLowerCase().contains("username already exists") && !result.toLowerCase().contains("invalid or already used email") && !result.toLowerCase().contains("invalid or already used contact number")&&!result.toLowerCase().contains("password could not be less than 8")&& !result.toLowerCase().contains("username could not be less than 6")) {
                    dispose();
                    new SignIn();
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
        new SignUp();
    }

}
