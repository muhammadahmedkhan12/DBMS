import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MakeNewAdmin extends JFrame{

    private JTextField Userinput;
    private JPasswordField PasswordInput;
    private JButton MakeNewAdmin;
    private JLabel WelcomeLabel;
    private JLabel Username;
    private JLabel Password;
    private JPanel MyPanel;
    private JButton backButton;

    public MakeNewAdmin() {
        setContentPane(MyPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setVisible(true);
        MakeNewAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = Userinput.getText();
                String password = PasswordInput.getText();
                String result = AuthenticationServiceAdmin.getInstance().MakeNewAdmin(username,password);
                JOptionPane.showMessageDialog(MakeNewAdmin.this,result);
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
        new MakeNewAdmin();
    }


}
