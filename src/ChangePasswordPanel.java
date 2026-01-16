import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

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
        
        setSize(1300, 900);
        setLocationRelativeTo(null);

        // apply theme/layout before attaching listeners
        applyTheme();

        // attach listeners after theming (null-safe)
        if (ChangePasswordButton != null) {
            ChangePasswordButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String currentusername = (CurrentUsernameInput != null) ? CurrentUsernameInput.getText() : "";
                    String currentpassword = (CurrentPasswordInput != null) ? new String(CurrentPasswordInput.getPassword()) : "";
                    String newpassword = (NewPasswordInput != null) ? new String(NewPasswordInput.getPassword()) : "";
                    String result = AuthenticationService.getAuthenticateuser().changepassword(currentusername,currentpassword,newpassword);
                    JOptionPane.showMessageDialog(ChangePasswordPanel.this,result);
                }
            });
        }

        setVisible(true);
    }

    private void applyTheme() {
        final int INNER_MAX_WIDTH = 450;
        final int CONTROL_MAX_WIDTH = 350;
        final int V_SPACE = 20;

        // Theme values (consistent with other forms)
        Color bg = new Color(45, 62, 80);
        Color fg = Color.WHITE;
        Color primary = new Color(0, 150, 136);
        Font headerFont = new Font("Segoe UI", Font.BOLD, 22);
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font controlFont = new Font("Segoe UI", Font.PLAIN, 14);

        if (MyPanel == null) return;

        // avoid reapplying/layout duplicate
        if (Boolean.TRUE.equals(MyPanel.getClientProperty("themedChangePassword"))) return;
        MyPanel.putClientProperty("themedChangePassword", Boolean.TRUE);

        MyPanel.removeAll();
        MyPanel.setBackground(bg);
        MyPanel.setLayout(new BorderLayout());

        // Header: WelcomeLabel at top-center with vertical gap
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        header.setOpaque(false);
        if (WelcomeLabel != null) {
            WelcomeLabel.setForeground(fg);
            WelcomeLabel.setFont(headerFont);
            WelcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
            WelcomeLabel.setBorder(new EmptyBorder(V_SPACE, 0, V_SPACE/2, 0));
            header.add(WelcomeLabel);
        }
        MyPanel.add(header, BorderLayout.NORTH);

        // Inner centered column for labels/fields/buttons
        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBorder(new EmptyBorder(12, 16, 16, 16));
        inner.setAlignmentX(Component.CENTER_ALIGNMENT);
        inner.setMaximumSize(new Dimension(INNER_MAX_WIDTH, Integer.MAX_VALUE));

        // Current username
        if (CurrentUsername != null) {
            CurrentUsername.setForeground(fg);
            CurrentUsername.setFont(labelFont);
            CurrentUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
            CurrentUsername.setBorder(new EmptyBorder(0,0,8,0));
            inner.add(CurrentUsername);
        }
        if (CurrentUsernameInput != null) {
            CurrentUsernameInput.setFont(controlFont);
            CurrentUsernameInput.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, CurrentUsernameInput.getPreferredSize().height));
            CurrentUsernameInput.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH, CurrentUsernameInput.getPreferredSize().height));
            CurrentUsernameInput.setAlignmentX(Component.CENTER_ALIGNMENT);
            inner.add(CurrentUsernameInput);
        }
        inner.add(Box.createVerticalStrut(12));

        // Current password
        if (CurrentPassword != null) {
            CurrentPassword.setForeground(fg);
            CurrentPassword.setFont(labelFont);
            CurrentPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
            CurrentPassword.setBorder(new EmptyBorder(0,0,8,0));
            inner.add(CurrentPassword);
        }
        if (CurrentPasswordInput != null) {
            CurrentPasswordInput.setFont(controlFont);
            CurrentPasswordInput.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, CurrentPasswordInput.getPreferredSize().height));
            CurrentPasswordInput.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH, CurrentPasswordInput.getPreferredSize().height));
            CurrentPasswordInput.setAlignmentX(Component.CENTER_ALIGNMENT);
            inner.add(CurrentPasswordInput);
        }
        inner.add(Box.createVerticalStrut(12));

        // New password
        if (NewPassword != null) {
            NewPassword.setForeground(fg);
            NewPassword.setFont(labelFont);
            NewPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
            NewPassword.setBorder(new EmptyBorder(0,0,8,0));
            inner.add(NewPassword);
        }
        if (NewPasswordInput != null) {
            NewPasswordInput.setFont(controlFont);
            NewPasswordInput.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, NewPasswordInput.getPreferredSize().height));
            NewPasswordInput.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH, NewPasswordInput.getPreferredSize().height));
            NewPasswordInput.setAlignmentX(Component.CENTER_ALIGNMENT);
            inner.add(NewPasswordInput);
        }
        inner.add(Box.createVerticalStrut(20));

        // Button centered
        if (ChangePasswordButton != null) {
            ChangePasswordButton.setFont(controlFont);
            ChangePasswordButton.setForeground(Color.WHITE);
            ChangePasswordButton.setBackground(primary);
            ChangePasswordButton.setOpaque(true);
            ChangePasswordButton.setFocusPainted(false);
            ChangePasswordButton.setMargin(new Insets(8,16,8,16));
            ChangePasswordButton.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, ChangePasswordButton.getPreferredSize().height));
            ChangePasswordButton.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH, ChangePasswordButton.getPreferredSize().height));
            ChangePasswordButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            // center text inside button
            ChangePasswordButton.setHorizontalAlignment(SwingConstants.CENTER);
            ChangePasswordButton.setHorizontalTextPosition(SwingConstants.CENTER);
            JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            btnRow.setOpaque(false);
            btnRow.add(ChangePasswordButton);
            inner.add(btnRow);
        }

        // center inner vertically by placing it in a GridBagLayout wrapper
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(inner, new GridBagConstraints());

        MyPanel.add(centerWrapper, BorderLayout.CENTER);

        // ensure background consistency for content pane
        Container current = getContentPane();
        if (current instanceof JComponent) {
            ((JComponent) current).setBackground(bg);
        }

        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChangePasswordPanel());
    }

}
