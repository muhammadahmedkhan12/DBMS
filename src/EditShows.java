import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Cinema.CinemaManager;
import Cinema.Cinema;
import Screen.Screen;
import Screen.ScreenManager;
import Show.Show;
import Show.ShowManager;

public class EditShows extends JFrame {
    private JComboBox<String> cinemaCombo;
    private JComboBox<String> screenCombo;
    private JTable showsTable;
    private DefaultTableModel tableModel;
    private JButton loadShowsButton;
    private JButton backButton;
    private Cinema selectedCinema;
    private Screen selectedScreen;

    private static final int BUTTON_PREF_WIDTH = 140;

    public EditShows() {
        setTitle("Edit Shows");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Top Panel - Cinema and Screen Selection
        JPanel selectionPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        selectionPanel.add(new JLabel("Select Cinema:"));
        cinemaCombo = new JComboBox<>();
        for (Cinema c : CinemaManager.getCinemaManager().getCinemas()) {
            cinemaCombo.addItem(c.getName());
        }
        selectionPanel.add(cinemaCombo);

        selectionPanel.add(new JLabel("Select Screen:"));
        screenCombo = new JComboBox<>();
        selectionPanel.add(screenCombo);

        loadShowsButton = new JButton("Load Shows");
        selectionPanel.add(new JLabel()); // Empty cell
        selectionPanel.add(loadShowsButton);

        add(selectionPanel, BorderLayout.NORTH);

        // Center Panel - Shows Table
        tableModel = new DefaultTableModel(
                new Object[]{"Show ID", "Movie Name", "Showtime", "Edit Time", "Delete Show"},
                0
        ) {
            public boolean isCellEditable(int row, int column) {
                return column == 3 || column == 4; // Only action buttons are editable
            }
        };

        showsTable = new JTable(tableModel);
        showsTable.setRowHeight(30);

        showsTable.getColumn("Edit Time").setCellRenderer(new ButtonRenderer());
        showsTable.getColumn("Edit Time").setCellEditor(new ButtonEditor(new JCheckBox(), "edit"));
        showsTable.getColumn("Delete Show").setCellRenderer(new ButtonRenderer());
        showsTable.getColumn("Delete Show").setCellEditor(new ButtonEditor(new JCheckBox(), "delete"));

        JScrollPane scrollPane = new JScrollPane(showsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel - Back Button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(BUTTON_PREF_WIDTH, backButton.getPreferredSize().height));
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Event Listeners
        cinemaCombo.addActionListener(e -> updateScreenCombo());

        loadShowsButton.addActionListener(e -> loadShows());

        backButton.addActionListener(e -> dispose());

        // Initialize screen combo for first cinema
        if (cinemaCombo.getItemCount() > 0) {
            updateScreenCombo();
        }

        applyTheme();
        setVisible(true);
    }

    private void updateScreenCombo() {
        int cinemaIdx = cinemaCombo.getSelectedIndex();
        if (cinemaIdx < 0) return;

        selectedCinema = CinemaManager.getCinemaManager().getCinemas().get(cinemaIdx);

        // Load screens from DB for this cinema
        ScreenManager.loadScreens(selectedCinema);

        screenCombo.removeAllItems();
        for (Screen s : selectedCinema.getScreens()) {
            screenCombo.addItem(s.getScreenId());
        }
    }

    private void loadShows() {
        int screenIdx = screenCombo.getSelectedIndex();
        if (screenIdx < 0) {
            JOptionPane.showMessageDialog(this, "Please select a screen.");
            return;
        }

        selectedScreen = selectedCinema.getScreens().get(screenIdx);

        if (selectedScreen.getShows() != null) {
            selectedScreen.getShows().clear();
        }

        ShowManager.loadShows(selectedScreen);

        tableModel.setRowCount(0);
        for (Show show : selectedScreen.getShows()) {
            tableModel.addRow(new Object[]{
                    show.getShowId(),
                    show.getMovie().getMoviename(),
                    show.getShowTime(),
                    "Edit Time",
                    "Delete Show"
            });
        }

        if (selectedScreen.getShows().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No shows found for this screen.");
        }
    }

    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
    class ButtonEditor extends DefaultCellEditor {
        private String label;
        private String actionType;
        private int selectedRow;
        private final JButton editorButton;
        private boolean actionPerformed = false;

        public ButtonEditor(JCheckBox checkBox, String actionType) {
            super(checkBox);
            this.actionType = actionType;
            this.editorButton = new JButton();
            this.editorButton.setOpaque(true);

            this.editorButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    actionPerformed = true;
                    int showId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());

                    // Stop editing first to prevent the error
                    SwingUtilities.invokeLater(() -> {
                        if (showsTable.isEditing()) {
                            showsTable.getCellEditor().cancelCellEditing();
                        }
                    });

                    if (actionType.equals("edit")) {
                        editShowTime(showId);
                    } else if (actionType.equals("delete")) {
                        deleteShow(showId);
                    }
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            selectedRow = row;
            actionPerformed = false;
            editorButton.setText(label);
            return editorButton;
        }

        @Override
        public Object getCellEditorValue() {
            return label;
        }

        @Override
        protected void fireEditingStopped() {
            // Only fire if action wasn't performed (prevents the error)
            if (!actionPerformed) {
                super.fireEditingStopped();
            }
        }
    }

    private void editShowTime(int showId) {
        // Reload shows to ensure we have fresh data
        if (selectedScreen == null) {
            JOptionPane.showMessageDialog(this, "Please select a screen first.");
            return;
        }

        // Find the show
        Show show = null;
        for (Show s : selectedScreen.getShows()) {
            if (s.getShowId() == showId) {
                show = s;
                break;
            }
        }

        if (show == null) {
            JOptionPane.showMessageDialog(this, "Show not found. Please reload shows.");
            return;
        }

        final Show selectedShow = show; // Make it effectively final for lambda

        // Create edit dialog
        JDialog editDialog = new JDialog(this, "Edit Showtime", true);
        editDialog.setSize(350, 180);
        editDialog.setLocationRelativeTo(this);
        editDialog.setLayout(new GridLayout(3, 2, 10, 10));
        ((JPanel)editDialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        editDialog.add(new JLabel("Current Time:"));
        JTextField currentTimeField = new JTextField(selectedShow.getShowTime());
        currentTimeField.setEditable(false);
        editDialog.add(currentTimeField);

        editDialog.add(new JLabel("New Time:"));
        JTextField newTimeField = new JTextField();
        editDialog.add(newTimeField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String newTime = newTimeField.getText().trim();
            if (newTime.isEmpty()) {
                JOptionPane.showMessageDialog(editDialog, "Please enter a new time.");
                return;
            }

            selectedShow.setShowTime(newTime);
            ShowManager.updateShow(selectedShow);

            JOptionPane.showMessageDialog(editDialog, "Show time updated successfully.");
            editDialog.dispose();
            loadShows();
        });
        editDialog.add(saveButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> editDialog.dispose());
        editDialog.add(cancelButton);

        editDialog.setVisible(true);
    }

    private void deleteShow(int showId) {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this show?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // Find and remove the show
        Show showToRemove = null;
        for (Show s : selectedScreen.getShows()) {
            if (s.getShowId() == showId) {
                showToRemove = s;
                break;
            }
        }

        if (showToRemove != null) {
            selectedScreen.getShows().remove(showToRemove);
            ShowManager.deleteShowFromDB(showId);

            JOptionPane.showMessageDialog(this, "Show deleted successfully.");
            loadShows(); // Refresh the table
        } else {
            JOptionPane.showMessageDialog(this, "Show not found.");
        }
    }

    private void applyTheme() {
        Color bg = new Color(45, 62, 80);
        Color fg = Color.WHITE;
        Color btnColor = new Color(30, 45, 60);
        Font controlFont = new Font("Segoe UI", Font.PLAIN, 14);

        getContentPane().setBackground(bg);

        // Style all panels
        for (Component comp : getContentPane().getComponents()) {
            if (comp instanceof JPanel) {
                comp.setBackground(bg);
                stylePanel((JPanel) comp, fg, controlFont);
            }
        }

        // Style buttons
        loadShowsButton.setFont(controlFont);
        loadShowsButton.setBackground(btnColor);
        loadShowsButton.setForeground(fg);
        loadShowsButton.setFocusPainted(false);

        backButton.setFont(controlFont);
        backButton.setBackground(btnColor);
        backButton.setForeground(fg);
        backButton.setFocusPainted(false);

        // Style combos and table
        cinemaCombo.setFont(controlFont);
        screenCombo.setFont(controlFont);

        showsTable.setFont(controlFont);
        showsTable.setForeground(Color.BLACK);
        showsTable.setSelectionBackground(new Color(0, 150, 136));
        showsTable.setSelectionForeground(Color.WHITE);
        showsTable.getTableHeader().setBackground(btnColor);
        showsTable.getTableHeader().setForeground(fg);
        showsTable.getTableHeader().setFont(controlFont.deriveFont(Font.BOLD));
    }

    private void stylePanel(JPanel panel, Color fg, Font font) {
        panel.setBackground(new Color(45, 62, 80));
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JLabel) {
                comp.setForeground(fg);
                comp.setFont(font);
            } else if (comp instanceof JPanel) {
                stylePanel((JPanel) comp, fg, font);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EditShows());
    }
}