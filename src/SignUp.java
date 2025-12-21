import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;
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
        setSize(1300,900);
        setLocationRelativeTo(null);

        // apply theme & layout constraints
        applyTheme();

        setVisible(true);

        // ensure inputs are clearly visible and can receive focus after the frame is shown
        SwingUtilities.invokeLater(() -> {
            if (UsernameInput != null) {
                UsernameInput.setEditable(true);
                UsernameInput.setEnabled(true);
                UsernameInput.setOpaque(true);
                UsernameInput.setBackground(Color.WHITE);
                UsernameInput.setForeground(Color.BLACK);
                UsernameInput.setCaretColor(Color.BLACK);
                UsernameInput.requestFocusInWindow();
            }
            if (passwordInput != null) {
                passwordInput.setEditable(true);
                passwordInput.setEnabled(true);
                passwordInput.setOpaque(true);
                passwordInput.setBackground(Color.WHITE);
                passwordInput.setForeground(Color.BLACK);
                passwordInput.setCaretColor(Color.BLACK);
            }
            if (ReEnterPasswordInput != null) {
                ReEnterPasswordInput.setEditable(true);
                ReEnterPasswordInput.setEnabled(true);
                ReEnterPasswordInput.setOpaque(true);
                ReEnterPasswordInput.setBackground(Color.WHITE);
                ReEnterPasswordInput.setForeground(Color.BLACK);
                ReEnterPasswordInput.setCaretColor(Color.BLACK);
            }
            if (EmailInput != null) {
                EmailInput.setEditable(true);
                EmailInput.setEnabled(true);
                EmailInput.setOpaque(true);
                EmailInput.setBackground(Color.WHITE);
                EmailInput.setForeground(Color.BLACK);
                EmailInput.setCaretColor(Color.BLACK);
            }
            if (ContactInput != null) {
                ContactInput.setEditable(true);
                ContactInput.setEnabled(true);
                ContactInput.setOpaque(true);
                ContactInput.setBackground(Color.WHITE);
                ContactInput.setForeground(Color.BLACK);
                ContactInput.setCaretColor(Color.BLACK);
            }
        });

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

    // REPLACED applyTheme() : ensures all text fields/password fields are explicitly editable,
    // opaque, have white background, black foreground, visible caret and a light border.
    private void applyTheme() {
        if (SignUpPanel == null) return;

        Color panelBg = new Color(34, 45, 62);
        Color primary = new Color(0, 150, 136);
        Color danger = new Color(220, 80, 80);
        Color textOnDark = Color.WHITE;
        Font headerFont = new Font("Segoe UI", Font.BOLD, 22);
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Wrap root panel once to center both horizontally and vertically
        Container cp = getContentPane();
        if (cp == SignUpPanel) {
            JPanel wrapper = new JPanel(new GridBagLayout());
            wrapper.setBackground(panelBg);
            wrapper.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.CENTER;
            gbc.weightx = 1.0; gbc.weighty = 1.0;
            wrapper.add(SignUpPanel, gbc);
            wrapper.putClientProperty("wrapped", Boolean.TRUE);
            setContentPane(wrapper);
        } else {
            if (cp instanceof JComponent) ((JComponent)cp).setBackground(panelBg);
        }

        // base
        SignUpPanel.setBackground(panelBg);
        SignUpPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        SignUpPanel.setLayout(new BoxLayout(SignUpPanel, BoxLayout.Y_AXIS));

        final int CONTROL_WIDTH = 320;
        final int CONTROL_HEIGHT = 36;
        final int BUTTON_HEIGHT = 42;
        final int V_SPACE = 16;

        // helper: style generic control (buttons/fields)
        java.util.function.Consumer<JComponent> styleControl = comp -> {
            comp.setAlignmentX(Component.CENTER_ALIGNMENT);
            if (comp instanceof JButton) {
                comp.setPreferredSize(new Dimension(CONTROL_WIDTH, BUTTON_HEIGHT));
                comp.setMaximumSize(new Dimension(CONTROL_WIDTH, BUTTON_HEIGHT));
            } else {
                comp.setPreferredSize(new Dimension(CONTROL_WIDTH, CONTROL_HEIGHT));
                comp.setMaximumSize(new Dimension(CONTROL_WIDTH, CONTROL_HEIGHT));
            }
            Border existing = comp.getBorder();
            Border spacer = BorderFactory.createEmptyBorder(V_SPACE/2, 0, V_SPACE/2, 0);
            comp.setBorder(existing != null ? BorderFactory.createCompoundBorder(spacer, existing) : spacer);
        };

        // Title/header (idempotent insertion)
        if (WelcomLabel != null) {
            WelcomLabel.setVisible(true);
            String current = WelcomLabel.getText();
            if (current == null || current.trim().isEmpty() || current.trim().equals("...")) {
                WelcomLabel.setText(getTitle() != null && !getTitle().isEmpty()
                        ? getTitle()
                        : "Create an account — Cinema Ticket Booking System");
            }
            WelcomLabel.setForeground(textOnDark);
            WelcomLabel.setFont(headerFont);
            WelcomLabel.setHorizontalAlignment(SwingConstants.CENTER);
            WelcomLabel.setOpaque(false);

            // remove any previous header wrapper
            Component oldHeader = null;
            for (Component c : SignUpPanel.getComponents()) {
                if (c instanceof JPanel && "signup-header".equals(((JPanel)c).getClientProperty("role"))) {
                    oldHeader = c;
                    break;
                }
            }
            if (oldHeader != null) SignUpPanel.remove(oldHeader);

            // --- ADJUSTED: add a right inset to visually nudge the label left ---
            // V_SPACE is defined above in applyTheme scope; use a modest top/bottom inset and a larger right inset
            WelcomLabel.setBorder(BorderFactory.createEmptyBorder(V_SPACE/2, 0, V_SPACE/2, 200));

            JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8));
            headerPanel.setOpaque(false);
            headerPanel.putClientProperty("role", "signup-header");
            headerPanel.add(WelcomLabel);
            headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            SignUpPanel.add(headerPanel, 0);
        }

        // Labels: keep style
        if (Username != null) { Username.setForeground(textOnDark); Username.setFont(labelFont); Username.setAlignmentX(Component.CENTER_ALIGNMENT); }
        if (Password != null) { Password.setForeground(textOnDark); Password.setFont(labelFont); Password.setAlignmentX(Component.CENTER_ALIGNMENT); }
        if (Email != null) { Email.setForeground(textOnDark); Email.setFont(labelFont); Email.setAlignmentX(Component.CENTER_ALIGNMENT); }
        if (ContactNo != null) { ContactNo.setForeground(textOnDark); ContactNo.setFont(labelFont); ContactNo.setAlignmentX(Component.CENTER_ALIGNMENT); }
        if (ReEnterPassword != null) { ReEnterPassword.setForeground(textOnDark); ReEnterPassword.setFont(labelFont); ReEnterPassword.setAlignmentX(Component.CENTER_ALIGNMENT); }

        // Guidance label
        if (PasswordCheck != null) {
            PasswordCheck.setVisible(true);
            PasswordCheck.setForeground(Color.BLUE);
            PasswordCheck.setFont(labelFont);
            PasswordCheck.setAlignmentX(Component.CENTER_ALIGNMENT);
            PasswordCheck.setBorder(BorderFactory.createEmptyBorder(V_SPACE/2, 6, V_SPACE/2, 0));
            PasswordCheck.setMaximumSize(new Dimension(420, PasswordCheck.getPreferredSize().height));
            PasswordCheck.setHorizontalAlignment(SwingConstants.LEFT);
        }

        // --- Ensure text fields are editable/visible with clear foreground/background/caret & border ---
        java.util.function.Consumer<JTextComponent> ensureTextVisible = tc -> {
            if (tc == null) return;
            try {
                tc.setEditable(true);
                tc.setEnabled(true);
                tc.setOpaque(true);
                tc.setBackground(Color.WHITE);
                tc.setForeground(Color.BLACK);
                tc.setCaretColor(Color.BLACK);
                tc.setDisabledTextColor(Color.GRAY);
                tc.setSelectionColor(new Color(0x3399FF));
                tc.setSelectedTextColor(Color.WHITE);
                tc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                DefaultCaret caret = new DefaultCaret();
                caret.setBlinkRate(500);
                tc.setCaret(caret);
                if (tc instanceof JTextField) {
                    ((JTextField)tc).setColumns(20);
                } else if (tc instanceof JPasswordField) {
                    ((JPasswordField)tc).setColumns(20);
                    try { ((JPasswordField)tc).setEchoChar('\u2022'); } catch (Exception ignored) {}
                }
                // visible border
                tc.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(0xCCCCCC)),
                        BorderFactory.createEmptyBorder(4,6,4,6)
                ));
            } catch (Exception ignored) {}
        };

        // Apply sizing + visibility to each input
        if (UsernameInput != null) { styleControl.accept(UsernameInput); ensureTextVisible.accept(UsernameInput); }
        if (passwordInput != null) { styleControl.accept(passwordInput); ensureTextVisible.accept(passwordInput); }
        if (ReEnterPasswordInput != null) { styleControl.accept(ReEnterPasswordInput); ensureTextVisible.accept(ReEnterPasswordInput); }
        if (EmailInput != null) { styleControl.accept(EmailInput); ensureTextVisible.accept(EmailInput); }
        if (ContactInput != null) { styleControl.accept(ContactInput); ensureTextVisible.accept(ContactInput); }

        // Buttons: place SignUp button below PasswordCheck, then Back button (vertical)
        if (signUpButton != null && backButton != null) {
            // remove existing parents to avoid duplicates
            Container p1 = signUpButton.getParent();
            if (p1 != null) p1.remove(signUpButton);
            Container p2 = backButton.getParent();
            if (p2 != null) p2.remove(backButton);

            // vertical placement: signUpButton directly under guidance line, then backButton beneath it
            signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            signUpButton.setBackground(primary);
            signUpButton.setForeground(textOnDark);
            signUpButton.setFont(buttonFont);
            signUpButton.setOpaque(true);
            signUpButton.setFocusPainted(false);
            Dimension btnDim = new Dimension(CONTROL_WIDTH, BUTTON_HEIGHT);
            signUpButton.setPreferredSize(btnDim);
            signUpButton.setMaximumSize(btnDim);
            signUpButton.setHorizontalAlignment(SwingConstants.CENTER);
            signUpButton.setMargin(new Insets(8, 12, 8, 12));
            // add small gap after the guidance line then add the SignUp button
            SignUpPanel.add(Box.createVerticalStrut(V_SPACE / 2));
            SignUpPanel.add(signUpButton);

            backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            backButton.setBackground(danger);
            backButton.setForeground(textOnDark);
            backButton.setFont(buttonFont);
            backButton.setOpaque(true);
            backButton.setFocusPainted(false);
            Dimension btnDim2 = new Dimension(CONTROL_WIDTH, BUTTON_HEIGHT);
            backButton.setPreferredSize(btnDim2);
            backButton.setMaximumSize(btnDim2);
            backButton.setHorizontalAlignment(SwingConstants.CENTER);
            backButton.setMargin(new Insets(8, 12, 8, 12));
            // small gap between SignUp and Back buttons
            SignUpPanel.add(Box.createVerticalStrut(10));
            SignUpPanel.add(backButton);
        } else {
            // fallback: single button styling
            if (signUpButton != null) {
                styleControl.accept(signUpButton);
                signUpButton.setBackground(primary);
                signUpButton.setForeground(textOnDark);
            }
            if (backButton != null) {
                styleControl.accept(backButton);
                backButton.setBackground(danger);
                backButton.setForeground(textOnDark);
            }
        }

        // finish: ensure no glass pane, request focus to first input
        try { Component glass = getGlassPane(); if (glass != null) { glass.setVisible(false); glass.setEnabled(false); } } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            if (UsernameInput != null) {
                UsernameInput.requestFocusInWindow();
            } else if (EmailInput != null) {
                EmailInput.requestFocusInWindow();
            }
        });

        SignUpPanel.revalidate();
        SignUpPanel.repaint();
    }

    public static void main(String[] args) {
        new SignUp();
    }

}
