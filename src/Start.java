import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

public class Start extends JFrame{
    private JLabel WelcomeLabel;
    private JButton Admin;
    private JButton User;
    private JPanel MyPanel;

    public Start() {
        setContentPane(MyPanel);
        setTitle("Cinema Ticket Booking System");
        
        setSize(1300, 900);
        setLocationRelativeTo(null);

        if (WelcomeLabel != null) {
            WelcomeLabel.setVisible(false);
            WelcomeLabel.setText(""); // optional: clear text as well
        }

        // apply same theme as AdminWelcome (delegates to static helper)
        applyTheme();

        setVisible(true);

        Admin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AdminWelcome();
            }
        });
        User.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new  WelcomePage();
            }
        });
    }

    private void applyTheme() {
        // delegate to centralized helper so other forms match AdminWelcome exactly
        Start.applyTheme(this, MyPanel, WelcomeLabel,
                new JButton[]{Admin, User},
                new JButton[]{});
    }

    // centralized reusable theme helper used by Start, AdminWelcome, WelcomePage, SignIn, etc.
    public static void applyTheme(JFrame frame, JPanel panel, JLabel header,
                                  JButton[] primaryButtons, JButton[] accentButtons) {
        if (panel == null) return; // nothing to style

        // theme values (match AdminWelcome)
        Color panelBg = new Color(34, 45, 62);
        Color primary = new Color(0, 150, 136);
        Color danger = new Color(220, 80, 80);
        Color textOnDark = Color.WHITE;
        Font headerFont = new Font("Segoe UI", Font.BOLD, 20);
        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Wrap/center the provided panel once and constrain inner width to ~450px for compact layout
        if (frame != null && panel.getClientProperty("wrapped") == null) {
            JPanel wrapper = new JPanel(new GridBagLayout());
            wrapper.setBackground(panelBg);
            wrapper.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.CENTER;
            wrapper.add(panel, gbc);

            panel.setMaximumSize(new Dimension(450, Integer.MAX_VALUE));
            panel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.putClientProperty("wrapped", Boolean.TRUE);

            frame.setContentPane(wrapper);
        } else if (frame != null) {
            frame.getContentPane().setBackground(panelBg);
        }

        // panel-level styling
        panel.setBackground(panelBg);
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        // header/title styling: center and add vertical padding (20px top & bottom)
        if (header != null) {
            header.setForeground(textOnDark);
            header.setFont(headerFont);
            header.setHorizontalAlignment(SwingConstants.CENTER);
            // Increased right padding from 20 -> 80 to provide a larger right margin for the title
            header.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 80));
            header.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        // style primary buttons
        if (primaryButtons != null) {
            for (JButton b : primaryButtons) {
                if (b == null) continue;
                b.setBackground(primary);
                b.setForeground(textOnDark);
                b.setFont(buttonFont);
                b.setOpaque(true);
                b.setFocusPainted(false);
                b.setBorder(BorderFactory.createEmptyBorder(10,12,10,12));
                b.setAlignmentX(Component.CENTER_ALIGNMENT);
                b.setMaximumSize(new Dimension(350, 44));
                b.setPreferredSize(new Dimension(350, 44));
                b.setMinimumSize(new Dimension(100, 30));
                // uniform vertical spacing
                b.setBorder(BorderFactory.createCompoundBorder(
                        b.getBorder(),
                        BorderFactory.createEmptyBorder(20,0,20,0)));
            }
        }
        // style accent (back/danger) buttons
        if (accentButtons != null) {
            for (JButton b : accentButtons) {
                if (b == null) continue;
                b.setBackground(danger);
                b.setForeground(textOnDark);
                b.setFont(buttonFont);
                b.setOpaque(true);
                b.setFocusPainted(false);
                b.setBorder(BorderFactory.createEmptyBorder(10,12,10,12));
                b.setAlignmentX(Component.CENTER_ALIGNMENT);
                b.setMaximumSize(new Dimension(350, 44));
                b.setPreferredSize(new Dimension(350, 44));
                b.setMinimumSize(new Dimension(100, 30));
                b.setBorder(BorderFactory.createCompoundBorder(
                        b.getBorder(),
                        BorderFactory.createEmptyBorder(20,0,20,0)));
            }
        }

        // ensure frame background matches
        if (frame != null) {
            frame.getContentPane().setBackground(panelBg);
        }
    }

    public static void main(String[] args) {
        // create UI on EDT
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Start();
            }
        });
    }
}
