import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
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
        
        setSize(1300, 900);
        setLocationRelativeTo(null);

        // apply theme/layout before attaching listeners
        applyTheme();

        // attach listeners after theming (null-safe)
        if (MakeNewAdmin != null) {
            MakeNewAdmin.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String username = (Userinput != null) ? Userinput.getText() : "";
                    String password = (PasswordInput != null) ? new String(PasswordInput.getPassword()) : "";
                    String result = AuthenticationServiceAdmin.getInstance().MakeNewAdmin(username,password);
                    JOptionPane.showMessageDialog(MakeNewAdmin.this,result);
                }
            });
        }

        if (backButton != null) {
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new AdminMain();
                    dispose();
                }
            });
        }

        // show last
        setVisible(true);
    }

    // new helper: apply shared theme and layout, keep all bound names intact
    private void applyTheme() {
        final int INNER_MAX_WIDTH = 450;
        final int CONTROL_MAX_WIDTH = 350;
        final int V_SPACE = 20;

        Color bg = new Color(45, 62, 80);
        Color fg = Color.WHITE;
        Color primary = new Color(0, 150, 136);
        Color danger = new Color(220, 80, 80);
        Font headerFont = new Font("Segoe UI", Font.BOLD, 22);
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font controlFont = new Font("Segoe UI", Font.PLAIN, 14);

        if (MyPanel == null) return;

        // idempotent: skip if already themed
        if (Boolean.TRUE.equals(MyPanel.getClientProperty("themedMakeNewAdmin"))) return;
        MyPanel.putClientProperty("themedMakeNewAdmin", Boolean.TRUE);

        // rebuild MyPanel: header at top, center column for labels/fields, buttons at bottom
        MyPanel.removeAll();
        MyPanel.setLayout(new BorderLayout());
        MyPanel.setBackground(bg);
        MyPanel.setOpaque(true);

        // Header: welcome label top-center with vertical gap
        JPanel headerPanelRef = null;
        if (WelcomeLabel != null) {
            JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            header.setOpaque(false);
            WelcomeLabel.setForeground(fg);
            WelcomeLabel.setFont(headerFont);
            // center text inside label
            WelcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
            // remove right padding, add small top and bottom gap
            WelcomeLabel.setBorder(new EmptyBorder(V_SPACE, 0, V_SPACE / 2, 240));
            header.add(WelcomeLabel);
            // Do NOT add header to MyPanel here — we'll add it to the outer wrapper so it appears top-center across the window
            headerPanelRef = header;
        }


        // Center column: labels and inputs stacked and centered
        JPanel centerCol = new JPanel();
        centerCol.setOpaque(false);
        centerCol.setLayout(new BoxLayout(centerCol, BoxLayout.Y_AXIS));
        centerCol.setBorder(new EmptyBorder(12, 16, 12, 16));
        centerCol.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerCol.setMaximumSize(new Dimension(INNER_MAX_WIDTH, Integer.MAX_VALUE));

        if (Username != null) {
            Username.setForeground(fg);
            Username.setFont(labelFont);
            Username.setAlignmentX(Component.CENTER_ALIGNMENT);
            Username.setBorder(new EmptyBorder(0,0,8,0));
            centerCol.add(Username);
            centerCol.add(Box.createVerticalStrut(6));
        }
        if (Userinput != null) {
            Userinput.setFont(controlFont);
            Dimension d1 = Userinput.getPreferredSize();
            Userinput.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, d1.height));
            Userinput.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH, d1.height));
            Userinput.setAlignmentX(Component.CENTER_ALIGNMENT);
            Userinput.setBorder(new EmptyBorder(6,8,6,8));
            centerCol.add(Userinput);
            centerCol.add(Box.createVerticalStrut(12));
        }

        if (Password != null) {
            Password.setForeground(fg);
            Password.setFont(labelFont);
            Password.setAlignmentX(Component.CENTER_ALIGNMENT);
            Password.setBorder(new EmptyBorder(0,0,8,0));
            centerCol.add(Password);
            centerCol.add(Box.createVerticalStrut(6));
        }
        if (PasswordInput != null) {
            PasswordInput.setFont(controlFont);
            Dimension d2 = PasswordInput.getPreferredSize();
            PasswordInput.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, d2.height));
            PasswordInput.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH, d2.height));
            PasswordInput.setAlignmentX(Component.CENTER_ALIGNMENT);
            PasswordInput.setBorder(new EmptyBorder(6,8,6,8));
            centerCol.add(PasswordInput);
            centerCol.add(Box.createVerticalStrut(20)); // gap before buttons
        }

        // center wrapper to keep the column compact
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(centerCol);
        MyPanel.add(centerWrapper, BorderLayout.CENTER);

        // Bottom: buttons row centered, with vertical gap from fields
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        buttonRow.setOpaque(false);

        java.util.function.Consumer<AbstractButton> styleBtn = b -> {
            b.setFont(controlFont);
            b.setForeground(Color.WHITE);
            b.setOpaque(true);
            b.setFocusPainted(false);
            b.setHorizontalAlignment(SwingConstants.CENTER);
            b.setHorizontalTextPosition(SwingConstants.CENTER);
            b.setMargin(new Insets(8, 16, 8, 16));
            b.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH/2 + 20, b.getPreferredSize().height));
            b.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH/2 + 20, b.getPreferredSize().height));
        };

        if (MakeNewAdmin != null) {
            styleBtn.accept(MakeNewAdmin);
            MakeNewAdmin.setBackground(primary);
            buttonRow.add(MakeNewAdmin);
        }
        if (backButton != null) {
            styleBtn.accept(backButton);
            backButton.setBackground(danger);
            buttonRow.add(backButton);
        }

        JPanel bottomContainer = new JPanel(new BorderLayout());
        bottomContainer.setOpaque(false);
        bottomContainer.setBorder(new EmptyBorder(18, 0, 18, 0)); // vertical gap above buttons and bottom padding
        bottomContainer.add(buttonRow, BorderLayout.CENTER);
        MyPanel.add(bottomContainer, BorderLayout.SOUTH);

        // constrain MyPanel width so controls don't stretch full window
        MyPanel.setMaximumSize(new Dimension(INNER_MAX_WIDTH, Integer.MAX_VALUE));
        MyPanel.setPreferredSize(new Dimension(INNER_MAX_WIDTH, MyPanel.getPreferredSize().height));

        // wrap MyPanel inside a centered wrapper (only once) and put header at the top of wrapper so title is truly top-center
        Container current = getContentPane();
        boolean alreadyWrapped = false;
        if (current instanceof JComponent) {
            Object flag = ((JComponent) current).getClientProperty("wrappedMakeNewAdmin");
            alreadyWrapped = Boolean.TRUE.equals(flag);
        }

        if (!alreadyWrapped) {
            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setBackground(bg);

            if (headerPanelRef != null) {
                wrapper.add(headerPanelRef, BorderLayout.NORTH);
            }

            JPanel centerGrid = new JPanel(new GridBagLayout());
            centerGrid.setOpaque(false);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.NONE;
            centerGrid.add(MyPanel, gbc);

            wrapper.add(centerGrid, BorderLayout.CENTER);

            wrapper.putClientProperty("wrappedMakeNewAdmin", Boolean.TRUE);
            setContentPane(wrapper);
        } else {
            if (getContentPane() instanceof JComponent) {
                ((JComponent) getContentPane()).setBackground(bg);
            }
        }

        // ensure content pane background matches
        if (getContentPane() instanceof JComponent) {
            ((JComponent) getContentPane()).setBackground(bg);
        }

        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MakeNewAdmin());
    }


}
