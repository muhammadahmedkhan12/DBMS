import Cinema.Cinema;
import Cinema.CinemaManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Editcinemas extends JFrame {
    private JTextField cinemaidtextfield;
    private JTextField Newnametextfield;
    private JTextField newscreentextfield;
    private JLabel Cinemaidlabel;
    private JLabel Newnamelabel;
    private JLabel Newscreentypelabel;
    private JButton editecinemabutton;
    private JPanel MyPanel;
    private JButton backButton;

    CinemaManager cinemaManager = CinemaManager.getCinemaManager();

    public Editcinemas(){
        setContentPane(MyPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);

        // apply theme/layout before attaching listeners and before showing
        applyTheme();

        // attach listeners (null-safe)
        if (editecinemabutton != null) {
            editecinemabutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String cinemaid = (cinemaidtextfield != null) ? cinemaidtextfield.getText() : "";
                    String newname = (Newnametextfield != null) ? Newnametextfield.getText() : "";
                    String newScreen = (newscreentextfield != null) ? newscreentextfield.getText() : "";

                    boolean found = false;
                    ArrayList<Cinema> cinemas = cinemaManager.getCinemas();

                    for (int i = 0; i < cinemas.size(); i++) {
                        Cinema c = cinemas.get(i);
                        if (c.getCinemaid().equals(cinemaid)) {
                            c.setName(newname);
//                            c.setScreentype(newScreen);
                            found = true;
                            break;
                        }
                    }

                    if (found) {
                        cinemaManager.savecinemas();
                        JOptionPane.showMessageDialog(Editcinemas.this, "Cinema updated successfully!");
                    } else {
                        JOptionPane.showMessageDialog(Editcinemas.this, "Cinema ID not found.");
                    }
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

    private void applyTheme() {
        final int INNER_MAX_WIDTH = 450;
        final int CONTROL_MAX_WIDTH = 350;
        final int V_SPACE = 20;

        Color bg = new Color(45, 62, 80);
        Color fg = Color.WHITE;
        Color primary = new Color(0, 150, 136);
        Color danger = new Color(220, 80, 80);
        Font headerFont = new Font("Segoe UI", Font.BOLD, 20);
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font controlFont = new Font("Segoe UI", Font.PLAIN, 14);

        if (MyPanel == null) return;

        // skip if already themed/wrapped
        Container cp = getContentPane();
        if (cp instanceof JComponent) {
            Object flag = ((JComponent) cp).getClientProperty("wrappedEditcinemas");
            if (Boolean.TRUE.equals(flag)) {
                // still update colors/fonts for existing components
                MyPanel.setBackground(bg);
                // style controls below even if already wrapped
            }
        }

        // style labels and text fields (keep names intact)
        if (Cinemaidlabel != null) {
            Cinemaidlabel.setForeground(fg);
            Cinemaidlabel.setFont(labelFont);
            Cinemaidlabel.setBorder(new EmptyBorder(0,0,8,0));
        }
        if (Newnamelabel != null) {
            Newnamelabel.setForeground(fg);
            Newnamelabel.setFont(labelFont);
            Newnamelabel.setBorder(new EmptyBorder(0,0,8,0));
        }
        if (Newscreentypelabel != null) {
            Newscreentypelabel.setForeground(fg);
            Newscreentypelabel.setFont(labelFont);
            Newscreentypelabel.setBorder(new EmptyBorder(0,0,8,0));
        }

        java.util.function.Consumer<JTextField> styleField = f -> {
            if (f == null) return;
            f.setFont(controlFont);
            Dimension d = f.getPreferredSize();
            f.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, d.height));
            f.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH, d.height));
            f.setAlignmentX(Component.CENTER_ALIGNMENT);
            f.setBorder(new EmptyBorder(6,8,6,8));
        };
        styleField.accept(cinemaidtextfield);
        styleField.accept(Newnametextfield);
        styleField.accept(newscreentextfield);

        // style buttons
        java.util.function.Consumer<AbstractButton> stylePrimaryBtn = b -> {
            if (b == null) return;
            b.setFont(controlFont);
            b.setBackground(primary);
            b.setForeground(fg);
            b.setOpaque(true);
            b.setFocusPainted(false);
            b.setHorizontalAlignment(SwingConstants.CENTER);
            b.setHorizontalTextPosition(SwingConstants.CENTER);
            b.setMargin(new Insets(8, 16, 8, 16));
            b.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, b.getPreferredSize().height));
            b.setPreferredSize(new Dimension(200, b.getPreferredSize().height));
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
        };
        java.util.function.Consumer<AbstractButton> styleDangerBtn = b -> {
            if (b == null) return;
            b.setFont(controlFont);
            b.setBackground(danger);
            b.setForeground(fg);
            b.setOpaque(true);
            b.setFocusPainted(false);
            b.setHorizontalAlignment(SwingConstants.CENTER);
            b.setHorizontalTextPosition(SwingConstants.CENTER);
            b.setMargin(new Insets(8, 16, 8, 16));
            b.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, b.getPreferredSize().height));
            b.setPreferredSize(new Dimension(140, b.getPreferredSize().height));
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
        };
        stylePrimaryBtn.accept(editecinemabutton);
        styleDangerBtn.accept(backButton);

        // set panel background
        MyPanel.setBackground(bg);
        MyPanel.setOpaque(true);

        // build an outer wrapper with header at top and MyPanel centered
        // idempotent: only wrap once
        boolean alreadyWrapped = false;
        if (getContentPane() instanceof JComponent) {
            Object flag = ((JComponent) getContentPane()).getClientProperty("wrappedEditcinemas");
            alreadyWrapped = Boolean.TRUE.equals(flag);
        }

        if (!alreadyWrapped) {
            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setBackground(bg);

            // header at top center
            JLabel header = new JLabel("Edit Cinema");
            header.setFont(headerFont);
            header.setForeground(fg);
            header.setHorizontalAlignment(SwingConstants.CENTER);
            header.setBorder(new EmptyBorder(V_SPACE, 0, V_SPACE, 0));
            JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            headerPanel.setOpaque(false);
            headerPanel.add(header);
            wrapper.add(headerPanel, BorderLayout.NORTH);

            // center MyPanel inside a GridBagLayout so it stays centered and constrained
            JPanel centerGrid = new JPanel(new GridBagLayout());
            centerGrid.setOpaque(false);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.CENTER;
            centerGrid.add(MyPanel, gbc);

            // constrain MyPanel maximum width so controls don't stretch entire window
            MyPanel.setMaximumSize(new Dimension(INNER_MAX_WIDTH, Integer.MAX_VALUE));
            MyPanel.setPreferredSize(new Dimension(INNER_MAX_WIDTH, MyPanel.getPreferredSize().height));

            wrapper.add(centerGrid, BorderLayout.CENTER);

            // mark wrapped
            if (wrapper instanceof JComponent) {
                ((JComponent) wrapper).putClientProperty("wrappedEditcinemas", Boolean.TRUE);
            }
            setContentPane(wrapper);
        } else {
            // ensure background colors are applied when already wrapped
            if (getContentPane() instanceof JComponent) {
                ((JComponent) getContentPane()).setBackground(bg);
            }
        }

        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Editcinemas());
    }
}
