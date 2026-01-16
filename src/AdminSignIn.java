import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
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
        setSize(1300,900);
        setLocationRelativeTo(null);

        applyTheme(); // apply consistent theme/layout before showing

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

    private void applyTheme() {
        // Keep idempotent: wrap once
        if (getRootPane().getClientProperty("adminSignInThemed") == Boolean.TRUE) {
            return;
        }

        // Theme values (consistent with other forms)
        Color panelBg = new Color(30, 45, 60);
        Color headingFg = Color.WHITE;
        Color labelFg = Color.WHITE;
        Color inputBg = Color.WHITE;
        Color inputFg = Color.BLACK;
        Color primary = new Color(0, 150, 136);
        Color danger = new Color(220, 80, 80);
        Font headingFont = new Font("SansSerif", Font.BOLD, 20);
        Font labelFont = new Font("SansSerif", Font.PLAIN, 14);
        Font buttonFont = new Font("SansSerif", Font.BOLD, 14);

        final int INNER_MAX_WIDTH = 450;
        final int CONTROL_MAX_WIDTH = 350;
        final int FIELD_HEIGHT = 30;
        final int V_SPACE = 20;

        // Wrap MyPanel into a centered wrapper so form remains centered
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(panelBg);
        // remove MyPanel from current content and add to wrapper
        // ensure MyPanel background matches wrapper so inner area looks cohesive
        MyPanel.setOpaque(false);
        MyPanel.setBackground(new Color(0,0,0,0));
        wrapper.add(MyPanel);
        setContentPane(wrapper);

        // header/title
        if (WelcomeLabel != null) {
            WelcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
            WelcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            WelcomeLabel.setFont(headingFont);
            WelcomeLabel.setForeground(headingFg);
            WelcomeLabel.setBorder(new EmptyBorder(V_SPACE, 10, V_SPACE, 10)); // give vertical gap
            // allow label to size naturally
            WelcomeLabel.setMaximumSize(new Dimension(INNER_MAX_WIDTH, WelcomeLabel.getPreferredSize().height));
        }

        // Style labels and inputs
        if (Username != null) {
            Username.setForeground(labelFg);
            Username.setFont(labelFont);
            Username.setAlignmentX(Component.CENTER_ALIGNMENT);
            Username.setBorder(new EmptyBorder(0,0,5,0));
        }
        if (Password != null) {
            Password.setForeground(labelFg);
            Password.setFont(labelFont);
            Password.setAlignmentX(Component.CENTER_ALIGNMENT);
            Password.setBorder(new EmptyBorder(0,0,5,0));
        }

        if (UsernameInput != null) {
            UsernameInput.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, FIELD_HEIGHT));
            UsernameInput.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH, FIELD_HEIGHT));
            UsernameInput.setAlignmentX(Component.CENTER_ALIGNMENT);
            UsernameInput.setBackground(inputBg);
            UsernameInput.setForeground(inputFg);
            UsernameInput.setOpaque(true);
            UsernameInput.setFocusable(true);
            UsernameInput.setEnabled(true);
        }

        if (PasswordInput != null) {
            PasswordInput.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, FIELD_HEIGHT));
            PasswordInput.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH, FIELD_HEIGHT));
            PasswordInput.setAlignmentX(Component.CENTER_ALIGNMENT);
            PasswordInput.setBackground(inputBg);
            PasswordInput.setForeground(inputFg);
            PasswordInput.setOpaque(true);
            PasswordInput.setFocusable(true);
            PasswordInput.setEnabled(true);
        }

        // Style buttons
        if (SignIn != null) {
            SignIn.setBackground(primary);
            SignIn.setForeground(Color.WHITE);
            SignIn.setFont(buttonFont);
            SignIn.setOpaque(true);
            SignIn.setBorderPainted(false);
            SignIn.setAlignmentX(Component.CENTER_ALIGNMENT);
            SignIn.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, 36));
            SignIn.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH, 36));
            SignIn.setHorizontalAlignment(SwingConstants.CENTER);
            SignIn.setMargin(new Insets(8,16,8,16));
        }
        if (backButton != null) {
            backButton.setBackground(danger);
            backButton.setForeground(Color.WHITE);
            backButton.setFont(buttonFont);
            backButton.setOpaque(true);
            backButton.setBorderPainted(false);
            backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            backButton.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH/2, 36));
            backButton.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH/2, 36));
            backButton.setHorizontalAlignment(SwingConstants.CENTER);
            backButton.setMargin(new Insets(8,16,8,16));
        }

        // Ensure MyPanel children are laid out centered: if MyPanel uses BoxLayout in form builder, this will work.
        // Otherwise, try to nudge common components to be centered by setting alignmentX.
        // Add small vertical gaps if possible by tweaking component borders (non-invasive).
        // Mark themed
        getRootPane().putClientProperty("adminSignInThemed", Boolean.TRUE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminSignIn());
    }
}
