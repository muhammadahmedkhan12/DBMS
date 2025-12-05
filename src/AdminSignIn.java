import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminSignIn extends JFrame{
    private JLabel WelcomeLabel;
    private JLabel Username;
    private JTextField UsernameInput;
    private JPasswordField PasswordInput;
    private JLabel Password;
    private JButton SignIn;
    private JPanel MyPanel;
    private JButton backButton;


    public AdminSignIn(){
        setContentPane(MyPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500,300);
        setLocationRelativeTo(null);
        setVisible(true);

        SignIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = UsernameInput.getText();
                String Password = PasswordInput.getText();
                String result = AuthenticationServiceAdmin.getInstance().login(username,Password);
                JOptionPane.showMessageDialog(AdminSignIn.this,result);
                if (result.equals("login successfull")) {
                    dispose();
                    new AdminMain();
                }

            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminWelcome();
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        new AdminSignIn();
    }
}
