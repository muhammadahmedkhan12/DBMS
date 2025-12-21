import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignIn extends JFrame {

    private JLabel MyLabel;
    private JLabel UserName;
    private JTextField UsernameInput;
    private JLabel MyLabel2;
    private JPasswordField Password;
    private JButton ClickMe;
    private JButton backButton;
    private JButton ChangePasswordButton;
    private JPanel SignInPanel;

    public SignIn() {
        setTitle("Cinema Ticket Booking System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1300, 900);
        setLocationRelativeTo(null);

        applyTheme();
        attachListeners();

        setVisible(true);
    }

    private void attachListeners() {
        ClickMe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = UsernameInput.getText();
                String password = new String(Password.getPassword());

                String result = AuthenticationService
                        .getAuthenticateuser()
                        .login(username, password);

                JOptionPane.showMessageDialog(SignIn.this, result);

                if (result.equalsIgnoreCase("login successfull")) {
                    dispose();
                    new UserMainDashboard(username);
                }
            }
        });

        backButton.addActionListener(e -> {
            new WelcomePage();
            dispose();
        });
    }

    private void applyTheme() {

        Color bg = new Color(34, 45, 62);
        Color primary = new Color(0, 150, 136);
        Color danger = new Color(220, 80, 80);
        Color text = Color.WHITE;

        Font titleFont = new Font("Segoe UI", Font.BOLD, 26);
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 14);

        // ================= TITLE (PAGE TOP) =================
        MyLabel.setFont(titleFont);
        MyLabel.setForeground(text);
        MyLabel.setHorizontalAlignment(SwingConstants.CENTER);


        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(bg);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(120, 0, 10, 200));
        titlePanel.add(MyLabel);

        // ================= FORM PANEL =================
        SignInPanel.setBackground(bg);
        SignInPanel.setLayout(new BoxLayout(SignInPanel, BoxLayout.Y_AXIS));
        SignInPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        SignInPanel.setMaximumSize(new Dimension(700, Integer.MAX_VALUE));

        final int CONTROL_WIDTH = 320;
        final int BUTTON_HEIGHT = 42;
        final int GAP = 14;

        SignInPanel.removeAll();

        // Username
        styleLabel(UserName, labelFont, text, CONTROL_WIDTH, GAP);
        SignInPanel.add(UserName);
        styleInput(UsernameInput, CONTROL_WIDTH);
        SignInPanel.add(UsernameInput);

        // Password
        styleLabel(MyLabel2, labelFont, text, CONTROL_WIDTH, GAP);
        SignInPanel.add(MyLabel2);
        styleInput(Password, CONTROL_WIDTH);
        SignInPanel.add(Password);

        // Buttons row
        JPanel buttonRow = new JPanel();
        buttonRow.setLayout(new BoxLayout(buttonRow, BoxLayout.X_AXIS));
        buttonRow.setOpaque(false);
        buttonRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonRow.setBorder(BorderFactory.createEmptyBorder(GAP, 0, GAP, 0));

        int gap = 12;
        int eachBtnW = (CONTROL_WIDTH - gap) / 2;
        Dimension btnDim = new Dimension(eachBtnW, BUTTON_HEIGHT);

        ClickMe.setFont(buttonFont);
        ClickMe.setBackground(primary);
        ClickMe.setForeground(text);
        ClickMe.setFocusPainted(false);
        ClickMe.setPreferredSize(btnDim);
        ClickMe.setMaximumSize(btnDim);
        ClickMe.setHorizontalAlignment(SwingConstants.CENTER);

        backButton.setFont(buttonFont);
        backButton.setBackground(danger);
        backButton.setForeground(text);
        backButton.setFocusPainted(false);
        backButton.setPreferredSize(btnDim);
        backButton.setMaximumSize(btnDim);
        backButton.setHorizontalAlignment(SwingConstants.CENTER);

        buttonRow.add(ClickMe);
        buttonRow.add(Box.createRigidArea(new Dimension(gap, 0)));
        buttonRow.add(backButton);

        SignInPanel.add(buttonRow);

        // Change password (unchanged)
//        ChangePasswordButton.setAlignmentX(Component.CENTER_ALIGNMENT);
//        SignInPanel.add(ChangePasswordButton);

        // ================= CENTER FORM =================
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(bg);
        centerWrapper.add(SignInPanel);

        // ================= FRAME LAYOUT =================
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(bg);
        root.add(titlePanel, BorderLayout.NORTH);
        root.add(centerWrapper, BorderLayout.CENTER);

        setContentPane(root);
    }

    private void styleLabel(JLabel label, Font font, Color fg, int width, int gap) {
        label.setFont(font);
        label.setForeground(fg);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(gap / 2, 0, gap / 2, 0));
        label.setMaximumSize(new Dimension(width, label.getPreferredSize().height));
    }

    private void styleInput(JComponent input, int width) {
        input.setAlignmentX(Component.CENTER_ALIGNMENT);
        input.setMaximumSize(new Dimension(width, 32));
        input.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SignIn::new);
    }
}
