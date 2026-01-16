import Cinema.Cinema;
import Cinema.CinemaManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddnewCinema extends JFrame {
    private JTextField cinemaidtextfield;
    private JTextField cinemanametextfiled;
    private JTextField screentypetextfield;
    private JLabel cinemaidlabel;
    private JLabel Cinemanamelabel;
    private JLabel cinemascreentypelabel;
    private JButton addcinemabutton;
    private JPanel MyPanel;
    private JButton backButton;

    CinemaManager cinemaManager = new CinemaManager();

    public AddnewCinema(){
        setContentPane(MyPanel);
        
        setSize(500, 300);
        setLocationRelativeTo(null);

        applyTheme();

        addcinemabutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cinemaId = cinemaidtextfield.getText().trim();
                String cinemaName = cinemanametextfiled.getText().trim();

                if (cinemaId.isEmpty() || cinemaName.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.");
                    return;
                }
                if(cinemaManager.cinemaExists(cinemaId)) {
                    JOptionPane.showMessageDialog(null, "Cinema ID already exists. Please use a different ID.");
                    return;
                }

                Cinema newCinema = new Cinema(cinemaId, cinemaName);
                cinemaManager.addcinema(newCinema);
                JOptionPane.showMessageDialog(null, "Cinema added successfully!");
                dispose();
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminMain();
                dispose();
            }
        });

        // show frame last
        setVisible(true);
    }

    private void applyTheme() {
        final int INNER_MAX_WIDTH = 450;
        final int CONTROL_MAX_WIDTH = 350;
        final int V_SPACE = 18;

        Color bg = new Color(45, 62, 80);
        Color fg = Color.WHITE;
        Color primary = new Color(0, 150, 136);
        Color danger = new Color(220, 80, 80);
        Font headerFont = new Font("Segoe UI", Font.BOLD, 22);
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font controlFont = new Font("Segoe UI", Font.PLAIN, 14);

        // MyPanel must exist (designer binding)
        if (MyPanel == null) return;

        // avoid duplicate rebuilds
        if (Boolean.TRUE.equals(MyPanel.getClientProperty("themedAddCinema"))) return;
        MyPanel.putClientProperty("themedAddCinema", Boolean.TRUE);

        // clear and rebuild MyPanel with header -> center column -> buttons at bottom
        MyPanel.removeAll();
        MyPanel.setLayout(new BorderLayout());
        MyPanel.setBackground(bg);
        MyPanel.setOpaque(true);

        // --- center column for labels/fields ---
        JPanel centerCol = new JPanel();
        centerCol.setOpaque(false);
        centerCol.setLayout(new BoxLayout(centerCol, BoxLayout.Y_AXIS));
        centerCol.setBorder(new EmptyBorder(20, 20, 20, 20));
        centerCol.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerCol.setMaximumSize(new Dimension(INNER_MAX_WIDTH, Integer.MAX_VALUE));

        // cinema id
        if (cinemaidlabel != null) {
            cinemaidlabel.setForeground(fg);
            cinemaidlabel.setFont(labelFont);
            cinemaidlabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            cinemaidlabel.setBorder(new EmptyBorder(V_SPACE/4, 0, V_SPACE/4, 0));
            centerCol.add(cinemaidlabel);
            centerCol.add(Box.createVerticalStrut(6));
        }
        if (cinemaidtextfield != null) {
            Dimension d = cinemaidtextfield.getPreferredSize();
            cinemaidtextfield.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, d.height));
            cinemaidtextfield.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH, d.height));
            cinemaidtextfield.setAlignmentX(Component.CENTER_ALIGNMENT);
            cinemaidtextfield.setFont(controlFont);
            cinemaidtextfield.setBorder(new EmptyBorder(6,8,6,8));
            centerCol.add(cinemaidtextfield);
            centerCol.add(Box.createVerticalStrut(12));
        }

        // cinema name
        if (Cinemanamelabel != null) {
            Cinemanamelabel.setForeground(fg);
            Cinemanamelabel.setFont(labelFont);
            Cinemanamelabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            Cinemanamelabel.setBorder(new EmptyBorder(V_SPACE/4, 0, V_SPACE/4, 0));
            centerCol.add(Cinemanamelabel);
            centerCol.add(Box.createVerticalStrut(6));
        }
        if (cinemanametextfiled != null) {
            Dimension d = cinemanametextfiled.getPreferredSize();
            cinemanametextfiled.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, d.height));
            cinemanametextfiled.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH, d.height));
            cinemanametextfiled.setAlignmentX(Component.CENTER_ALIGNMENT);
            cinemanametextfiled.setFont(controlFont);
            cinemanametextfiled.setBorder(new EmptyBorder(6,8,6,8));
            centerCol.add(cinemanametextfiled);
            centerCol.add(Box.createVerticalStrut(12));
        }

        // screen type
        if (cinemascreentypelabel != null) {
            cinemascreentypelabel.setForeground(fg);
            cinemascreentypelabel.setFont(labelFont);
            cinemascreentypelabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            cinemascreentypelabel.setBorder(new EmptyBorder(V_SPACE/4, 0, V_SPACE/4, 0));
            centerCol.add(cinemascreentypelabel);
            centerCol.add(Box.createVerticalStrut(6));
        }
        if (screentypetextfield != null) {
            Dimension d = screentypetextfield.getPreferredSize();
            screentypetextfield.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH, d.height));
            screentypetextfield.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH, d.height));
            screentypetextfield.setAlignmentX(Component.CENTER_ALIGNMENT);
            screentypetextfield.setFont(controlFont);
            screentypetextfield.setBorder(new EmptyBorder(6,8,6,8));
            centerCol.add(screentypetextfield);
            centerCol.add(Box.createVerticalStrut(12));
        }

        // place centerCol into a wrapper so it stays compact when MyPanel is placed in the frame
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(centerCol);
        MyPanel.add(centerWrapper, BorderLayout.CENTER);

        // --- button row at bottom of MyPanel ---
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 12));
        buttonRow.setOpaque(false);

        java.util.function.Consumer<AbstractButton> styleBtn = b -> {
            b.setFont(controlFont);
            b.setForeground(Color.WHITE);
            b.setOpaque(true);
            b.setFocusPainted(false);
            b.setHorizontalAlignment(SwingConstants.CENTER);
            b.setHorizontalTextPosition(SwingConstants.CENTER);
            b.setMargin(new Insets(8, 16, 8, 16));
            b.setMaximumSize(new Dimension(CONTROL_MAX_WIDTH / 2 + 20, b.getPreferredSize().height));
            b.setPreferredSize(new Dimension(CONTROL_MAX_WIDTH / 2 + 20, b.getPreferredSize().height));
        };

        if (addcinemabutton != null) {
            styleBtn.accept(addcinemabutton);
            addcinemabutton.setBackground(primary);
            buttonRow.add(addcinemabutton);
        }
        if (backButton != null) {
            styleBtn.accept(backButton);
            backButton.setBackground(danger);
            buttonRow.add(backButton);
        }

        // add a small bottom padding panel so buttons sit visually at the bottom edge of MyPanel
        JPanel bottomContainer = new JPanel(new BorderLayout());
        bottomContainer.setOpaque(false);
        bottomContainer.setBorder(new EmptyBorder(12, 0, 18, 0)); // extra bottom spacing
        bottomContainer.add(buttonRow, BorderLayout.CENTER);
        MyPanel.add(bottomContainer, BorderLayout.SOUTH);

        // constrain MyPanel width so controls don't stretch full window
        MyPanel.setMaximumSize(new Dimension(INNER_MAX_WIDTH, Integer.MAX_VALUE));
        MyPanel.setPreferredSize(new Dimension(INNER_MAX_WIDTH, MyPanel.getPreferredSize().height));

        // wrap MyPanel inside a centered wrapper (only once)
        Container current = getContentPane();
        boolean alreadyWrapped = false;
        if (current instanceof JComponent) {
            Object flag = ((JComponent) current).getClientProperty("wrappedAddCinema");
            alreadyWrapped = Boolean.TRUE.equals(flag);
        }

        if (!alreadyWrapped) {
            JPanel wrapper = new JPanel(new GridBagLayout());
            wrapper.setBackground(bg);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.NONE;
            wrapper.add(MyPanel, gbc);
            // mark to avoid double-wrapping
            wrapper.putClientProperty("wrappedAddCinema", Boolean.TRUE);
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
        SwingUtilities.invokeLater(() -> new AddnewCinema());
    }
}
