import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangePasswordPanel extends JFrame{
    private JLabel WelcomeLabel;
    private JPasswordField CurrentPasswordInput;
    private JLabel CurrentPassword;
    private JLabel NewPassword;
    private JPasswordField NewPasswordInput;
    private JButton ChangePasswordButton;
    private JPanel MyPanel;
    private JLabel CurrentUsername;
    private JTextField CurrentUsernameInput;

    public ChangePasswordPanel(){
        setContentPane(MyPanel);
        setTitle("Cinema Ticket Booking System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setVisible(true);
        ChangePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentusername = CurrentUsernameInput.getText();
                String currentpassword = CurrentPasswordInput.getText();
                String newpassword = NewPasswordInput.getText();
                String result = AuthenticationService.getAuthenticateuser().changepassword(currentusername,currentpassword,newpassword);
                JOptionPane.showMessageDialog(ChangePasswordPanel.this,result);
            }
        });
    }
}
