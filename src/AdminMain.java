import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Movie.MovieManager;
import Movie.Movie;
import Cinema.CinemaManager;
import Cinema.Cinema;
import Screen.Screen;
import Show.Show;
import Show.ShowManager;
import Screen.ScreenManager;
import java.util.ArrayList;

public class AdminMain extends JFrame {
    private JPanel MyPanel;
    private JTable movieTable;
    private JScrollPane tableScrollPane;
    private DefaultTableModel tableModel;
    private JButton NewAdminButton;
    private JLabel WelcomeLabel;
    private JButton Deletemoviebutton;
    private JButton editcinemabutton;
    private JButton Addnewcinema;
    private JButton deletecinemabutton;
    private JButton EditMoviesbutton;
    private JButton Addnewmoviesbutton;
    private JButton AddScreenButton;
    private JButton DeleteScreenButton;
    private JButton backButton;

    private static final int BUTTON_PREF_WIDTH = 140;

    public AdminMain() {
        MyPanel = new JPanel();
        setContentPane(MyPanel);

        setSize(1300, 900);
        setLocationRelativeTo(null);

        final int INNER_MAX_WIDTH = 1000;

        tableModel = new DefaultTableModel(new Object[]{"Movie ID", "Name", "Rating", "Add Movie to Show", "Edit Movie"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 3 || column == 4;
            }
        };
        movieTable = new JTable(tableModel);
        movieTable.setRowHeight(30);
        tableScrollPane = new JScrollPane(movieTable);
        tableScrollPane.setPreferredSize(new Dimension(INNER_MAX_WIDTH, 360));
        tableScrollPane.setMaximumSize(new Dimension(INNER_MAX_WIDTH, Integer.MAX_VALUE));
        MyPanel.setLayout(new BorderLayout());
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(tableScrollPane);
        MyPanel.add(centerWrapper, BorderLayout.CENTER);

        loadMoviesToTable();

        movieTable.getColumn("Add Movie to Show").setCellRenderer(new ButtonRenderer());
        movieTable.getColumn("Add Movie to Show").setCellEditor(new ButtonEditor(new JCheckBox(), "add"));
        movieTable.getColumn("Edit Movie").setCellRenderer(new ButtonRenderer());
        movieTable.getColumn("Edit Movie").setCellEditor(new ButtonEditor(new JCheckBox(), "edit"));

        JPanel controlsPanel = new JPanel(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JPanel backPanel = new JPanel(new BorderLayout());
        backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(BUTTON_PREF_WIDTH, backButton.getPreferredSize().height));
        backButton.setHorizontalAlignment(SwingConstants.CENTER);
        backPanel.add(backButton, BorderLayout.CENTER);

        NewAdminButton = new JButton("New Admin");
        EditMoviesbutton = new JButton("Edit Shows");
        Addnewmoviesbutton = new JButton("Add New Movie");
        Deletemoviebutton = new JButton("Delete Show");
        editcinemabutton = new JButton("Edit Cinema");
        Addnewcinema = new JButton("Add New Cinema");
        deletecinemabutton = new JButton("Delete Cinema");
        AddScreenButton = new JButton("Add Screen");
        DeleteScreenButton = new JButton("Delete Screen");
        buttonPanel.add(NewAdminButton);
        buttonPanel.add(EditMoviesbutton);
        buttonPanel.add(Addnewmoviesbutton);
        buttonPanel.add(Deletemoviebutton);
        buttonPanel.add(editcinemabutton);
        buttonPanel.add(Addnewcinema);
        buttonPanel.add(deletecinemabutton);
        buttonPanel.add(AddScreenButton);
        buttonPanel.add(DeleteScreenButton);

        JPanel outerButtonsPanel = new JPanel();
        outerButtonsPanel.setOpaque(false);
        outerButtonsPanel.setLayout(new BoxLayout(outerButtonsPanel, BoxLayout.Y_AXIS));
        JPanel rowWrapper = new JPanel(new GridBagLayout());
        rowWrapper.setOpaque(false);
        rowWrapper.add(buttonPanel);
        outerButtonsPanel.add(rowWrapper);
        outerButtonsPanel.add(Box.createVerticalStrut(8));
        JPanel backRowWrapper = new JPanel(new GridBagLayout());
        backRowWrapper.setOpaque(false);
        backRowWrapper.add(backPanel);
        outerButtonsPanel.add(backRowWrapper);

        controlsPanel.add(outerButtonsPanel, BorderLayout.NORTH);

        MyPanel.add(controlsPanel, BorderLayout.SOUTH);

        NewAdminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MakeNewAdmin();
            }
        });

        EditMoviesbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EditShows();
            }
        });
        Addnewmoviesbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddMovieOnlyFrame();
            }
        });
        Deletemoviebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DeleteMovie();
            }
        });
        editcinemabutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Editcinemas();
            }
        });
        Addnewcinema.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddnewCinema();
            }
        });
        deletecinemabutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DeleteCinemas();
            }
        });
        AddScreenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddScreenFrame();
            }
        });

        DeleteScreenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DeleteScreenFrame();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AdminSignIn();
            }
        });

        applyTheme();
        setVisible(true);
    }

    private void loadMoviesToTable() {
        tableModel.setRowCount(0);
        ArrayList<Movie> movies = MovieManager.getManager().getMovies();
        for (Movie m : movies) {
            tableModel.addRow(new Object[]{m.getMovieid(), m.getMoviename(), m.getRating(), "Add Movie to Show", "Edit Movie"});
        }
    }

    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private String label;
        private String actionType;
        private int selectedRow;
        private final JButton editorButton;

        public ButtonEditor(JCheckBox checkBox, String actionType) {
            super(checkBox);
            this.actionType = actionType;
            this.editorButton = new JButton();
            this.editorButton.setOpaque(true);
            // single listener attached to the editor button
            this.editorButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // use selectedRow captured from getTableCellEditorComponent
                    int movieId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                    if (actionType.equals("add")) {
                        new AddMovieToScreenFrame(movieId);
                    } else if (actionType.equals("edit")) {
                        new EditMovieFrame(movieId);
                    }
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            selectedRow = row;
            editorButton.setText(label);
            return editorButton;
        }

        @Override
        public Object getCellEditorValue() {
            // return the label string so the table cell keeps the proper text after editing
            return label;
        }
    }

    class AddMovieToScreenFrame extends JFrame {
        public AddMovieToScreenFrame(int movieId) {
            setTitle("Add Movie to Screen");
            setSize(400, 350);
            setLocationRelativeTo(AdminMain.this);
            setLayout(new GridLayout(7, 2));

            Movie movie = MovieManager.getManager().getMovieById(movieId);

            add(new JLabel("Movie ID:"));
            JTextField movieIdField = new JTextField(String.valueOf(movie.getMovieid()));
            movieIdField.setEditable(false);
            add(movieIdField);

            add(new JLabel("Movie Name:"));
            JTextField movieNameField = new JTextField(movie.getMoviename());
            movieNameField.setEditable(false);
            add(movieNameField);

            add(new JLabel("Rating:"));
            JTextField ratingField = new JTextField(movie.getRating());
            ratingField.setEditable(false);
            add(ratingField);

            add(new JLabel("Select Cinema:"));
            JComboBox<String> cinemaCombo = new JComboBox<>();
            for (Cinema c : CinemaManager.getCinemaManager().getCinemas()) {
                cinemaCombo.addItem(c.getName());
            }
            add(cinemaCombo);

            add(new JLabel("Select Screen:"));
            JComboBox<String> screenCombo = new JComboBox<>();
            if (cinemaCombo.getItemCount() > 0) {
                Cinema selectedCinema = CinemaManager.getCinemaManager().getCinemas().get(cinemaCombo.getSelectedIndex());


                ScreenManager.loadScreens(selectedCinema);

                screenCombo.removeAllItems();
                for (Screen s : selectedCinema.getScreens()) {
                    screenCombo.addItem(s.getScreenId());
                }
            }

            add(screenCombo);

            cinemaCombo.addActionListener(e -> {
                int idx = cinemaCombo.getSelectedIndex();
                if (idx >= 0) {
                    Cinema selectedCinema = CinemaManager.getCinemaManager().getCinemas().get(idx);

                    // load screens from DB
                    ScreenManager.loadScreens(selectedCinema);

                    // repopulate screenCombo
                    screenCombo.removeAllItems();
                    for (Screen s : selectedCinema.getScreens()) {
                        screenCombo.addItem(s.getScreenId());
                    }
                }
            });


            add(new JLabel("Showtime:"));
            JTextField showtimeField = new JTextField();
            add(showtimeField);

            JButton addButton = new JButton("Add Movie");
            addButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int cinemaIdx = cinemaCombo.getSelectedIndex();
                    int screenIdx = screenCombo.getSelectedIndex();
                    String showtime = showtimeField.getText().trim();
                    if (cinemaIdx < 0 || screenIdx < 0 || showtime.isEmpty()) {
                        JOptionPane.showMessageDialog(AddMovieToScreenFrame.this, "Please select cinema, screen, and enter showtime.");
                        return;
                    }
                    Cinema cinema = CinemaManager.getCinemaManager().getCinemas().get(cinemaIdx);
                    Screen screen = cinema.getScreens().get(screenIdx);
                    CinemaManager.getCinemaManager().addMovieToCinema(cinema, screen, movie, showtime);
                    JOptionPane.showMessageDialog(AddMovieToScreenFrame.this, "Movie added to screen.");
                    dispose();
                }
            });
            addButton.setPreferredSize(new Dimension(BUTTON_PREF_WIDTH, addButton.getPreferredSize().height));
            add(addButton);

            JButton cancelButton = new JButton("Cancel");
            cancelButton.setPreferredSize(new Dimension(BUTTON_PREF_WIDTH, cancelButton.getPreferredSize().height));
            cancelButton.addActionListener(e -> dispose());
            add(cancelButton);

            setVisible(true);
        }
    }

    class EditMovieFrame extends JFrame {
        public EditMovieFrame(int movieId) {
            setTitle("Edit Movie");
            setSize(400, 250);
            setLocationRelativeTo(AdminMain.this);
            setLayout(new GridLayout(5, 2));

            Movie movie = MovieManager.getManager().getMovieById(movieId);

            add(new JLabel("Movie ID:"));
            JTextField movieIdField = new JTextField(String.valueOf(movie.getMovieid()));
            movieIdField.setEditable(false);
            add(movieIdField);

            add(new JLabel("Current Name:"));
            JTextField currentNameField = new JTextField(movie.getMoviename());
            currentNameField.setEditable(false);
            add(currentNameField);

            add(new JLabel("Current Rating:"));
            JTextField currentRatingField = new JTextField(movie.getRating());
            currentRatingField.setEditable(false);
            add(currentRatingField);

            add(new JLabel("New Name:"));
            JTextField newNameField = new JTextField();
            add(newNameField);

            add(new JLabel("New Rating:"));
            JTextField newRatingField = new JTextField();
            add(newRatingField);

            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(e -> {
                String newName = newNameField.getText().trim();
                String newRating = newRatingField.getText().trim();

                if (newName.isEmpty() || newRating.isEmpty()) {
                    JOptionPane.showMessageDialog(EditMovieFrame.this, "Please enter new name and rating.");
                    return;
                }

                boolean success = MovieManager.getManager().editMovie(movieId, newName, newRating);
                if (!success) {
                    JOptionPane.showMessageDialog(EditMovieFrame.this, "Movie id not found.");
                    return;
                }

                for (Cinema cinema : CinemaManager.getCinemaManager().getCinemas()) {
                    for (Screen screen : cinema.getScreens()) {
                        for (Show show : screen.getShows()) {
                            if (show.getMovie().getMovieid() == movieId) {
                                show.getMovie().setMoviename(newName);
                                show.getMovie().setRating(newRating);
                            }
                        }
                        ShowManager.saveShows(screen);
                    }
                }

                JOptionPane.showMessageDialog(EditMovieFrame.this, "Movie updated successfully.");
                loadMoviesToTable();
                dispose();
            });

            saveButton.setPreferredSize(new Dimension(BUTTON_PREF_WIDTH, saveButton.getPreferredSize().height));
            add(saveButton);

            JButton cancelButton = new JButton("Cancel");
            cancelButton.setPreferredSize(new Dimension(BUTTON_PREF_WIDTH, cancelButton.getPreferredSize().height));
            cancelButton.addActionListener(ev -> dispose());
            add(cancelButton);

            setVisible(true);
        }
    }

    // ✅ FIXED: Moved AddScreenFrame outside of EditMovieFrame
    class AddScreenFrame extends JFrame {
        public AddScreenFrame() {
            setTitle("Add Screen");
            setSize(350, 220);
            setLocationRelativeTo(AdminMain.this);
            setLayout(new GridLayout(5, 2));

            add(new JLabel("Screen Type (2D/3D):"));
            JTextField screenTypeField = new JTextField();
            add(screenTypeField);

            add(new JLabel("Number of Seats:"));
            JTextField seatsField = new JTextField();
            add(seatsField);

            add(new JLabel("Select Cinema:"));
            JComboBox<String> cinemaCombo = new JComboBox<>();
            for (Cinema c : CinemaManager.getCinemaManager().getCinemas()) {
                cinemaCombo.addItem(c.getName());
            }
            add(cinemaCombo);

            JButton addButton = new JButton("Add Screen");
            addButton.addActionListener(e -> {
                String screenType = screenTypeField.getText().trim();
                String seatsText = seatsField.getText().trim();
                int cinemaIdx = cinemaCombo.getSelectedIndex();
                if (screenType.isEmpty() || seatsText.isEmpty() || cinemaIdx < 0) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields.");
                    return;
                }

                int numberOfSeats;
                try {
                    numberOfSeats = Integer.parseInt(seatsText);
                    if (numberOfSeats <= 0) throw new NumberFormatException();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid positive number for seats.");
                    return;
                }

                Cinema cinema = CinemaManager.getCinemaManager().getCinemas().get(cinemaIdx);

                String screenId = Screen.getNextScreenId();

                Screen screen = new Screen(screenId, cinema.getCinemaid(), screenType, numberOfSeats);
                cinema.addScreen(screen);

                // ✅ Save only the screen using ScreenManager
                ScreenManager.saveScreen(screen);

                JOptionPane.showMessageDialog(this, "Screen added to cinema: " + cinema.getName());
                dispose();
            });
            addButton.setPreferredSize(new Dimension(BUTTON_PREF_WIDTH, addButton.getPreferredSize().height));
            add(addButton);

            JButton backButton = new JButton("Back");
            backButton.setPreferredSize(new Dimension(BUTTON_PREF_WIDTH, backButton.getPreferredSize().height));
            backButton.addActionListener(e -> dispose());
            add(backButton);

            JButton cancelButton = new JButton("Cancel");
            cancelButton.setPreferredSize(new Dimension(BUTTON_PREF_WIDTH, cancelButton.getPreferredSize().height));
            cancelButton.addActionListener(e -> dispose());
            add(cancelButton);

            setVisible(true);
        }
    }


    class DeleteScreenFrame extends JFrame {
        public DeleteScreenFrame() {
            setTitle("Delete Screen");
            setSize(500, 400);
            setLocationRelativeTo(AdminMain.this);
            setLayout(new BorderLayout());

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

            for (Cinema cinema : CinemaManager.getCinemaManager().getCinemas()) {
                JPanel cinemaPanel = new JPanel(new BorderLayout());
                cinemaPanel.add(new JLabel("Cinema: " + cinema.getName()), BorderLayout.NORTH);

                JPanel screensPanel = new JPanel(new GridLayout(0, 2, 10, 5));
                for (Screen screen : cinema.getScreens()) {
                    screensPanel.add(new JLabel("Screen: " + screen.getScreenId() + " (" + screen.getScreenType() + ")"));

                    JButton deleteBtn = new JButton("Delete");
                    deleteBtn.addActionListener(e -> {
                        // ✅ Remove screen from DB using ScreenManager
                        ScreenManager.deleteScreen(screen);

                        // Remove from cinema object
                        cinema.getScreens().remove(screen);

                        JOptionPane.showMessageDialog(DeleteScreenFrame.this, "Screen deleted from cinema: " + cinema.getName());
                        dispose();
                    });
                    screensPanel.add(deleteBtn);
                }

                cinemaPanel.add(screensPanel, BorderLayout.CENTER);
                mainPanel.add(cinemaPanel);
            }

            JScrollPane scrollPane = new JScrollPane(mainPanel);
            add(scrollPane, BorderLayout.CENTER);

            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));

            JButton backButton = new JButton("Back");
            backButton.setPreferredSize(new Dimension(BUTTON_PREF_WIDTH, backButton.getPreferredSize().height));
            backButton.addActionListener(e -> dispose());
            bottomPanel.add(backButton);

            JButton closeButton = new JButton("Close");
            closeButton.setPreferredSize(new Dimension(BUTTON_PREF_WIDTH, closeButton.getPreferredSize().height));
            closeButton.addActionListener(e -> dispose());
            bottomPanel.add(closeButton);

            add(bottomPanel, BorderLayout.SOUTH);
            setVisible(true);
        }
    }


    class AddMovieOnlyFrame extends JFrame {

        public AddMovieOnlyFrame() {
            setTitle("Add New Movie");
            setSize(350, 200);
            setLocationRelativeTo(AdminMain.this);
            setLayout(new GridLayout(3, 2));

            add(new JLabel("Movie Name:"));
            JTextField movieNameField = new JTextField();
            add(movieNameField);

            add(new JLabel("Rating:"));
            JTextField ratingField = new JTextField();
            add(ratingField);

            JButton addButton = new JButton("Add Movie");
            addButton.addActionListener(e -> {

                String movieName = movieNameField.getText().trim();
                String rating = ratingField.getText().trim();

                if (movieName.isEmpty() || rating.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields.");
                    return;
                }

                for (Movie m : MovieManager.getManager().getMovies()) {
                    if (m.getMoviename().equalsIgnoreCase(movieName)) {
                        JOptionPane.showMessageDialog(this, "Movie name already exists.");
                        return;
                    }
                }

                Movie movie = new Movie(movieName, rating);
                int assignedId = movie.getMovieid();

                MovieManager.getManager().addMovie(movie);

                JOptionPane.showMessageDialog(
                        this,
                        "Movie added successfully with ID: " + assignedId
                );

                loadMoviesToTable();
                dispose();
            });

            addButton.setPreferredSize(
                    new Dimension(BUTTON_PREF_WIDTH, addButton.getPreferredSize().height)
            );
            add(addButton);

            JButton cancelButton = new JButton("Cancel");
            cancelButton.setPreferredSize(
                    new Dimension(BUTTON_PREF_WIDTH, cancelButton.getPreferredSize().height)
            );
            cancelButton.addActionListener(e -> dispose());
            add(cancelButton);

            setVisible(true);
        }
    }


    private void applyTheme() {
        Color bg = new Color(45, 62, 80);
        Color fg = Color.WHITE;
        Color primary = new Color(0, 150, 136);
        Color danger = new Color(220, 80, 80);
        Color btnColor = new Color(30, 45, 60);
        Font headerFont = new Font("Segoe UI", Font.BOLD, 20);
        Font controlFont = new Font("Segoe UI", Font.PLAIN, 14);

        if (MyPanel == null) return;

        MyPanel.setBackground(bg);
        MyPanel.setOpaque(true);

        if (WelcomeLabel == null) {
            WelcomeLabel = new JLabel("Welcome, Admin");
        }
        WelcomeLabel.setFont(headerFont);
        WelcomeLabel.setForeground(fg);
        WelcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        WelcomeLabel.setBorder(new EmptyBorder(18, 0, 18, 0));
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER));
        header.setOpaque(false);
        header.add(WelcomeLabel);

        MyPanel.add(header, BorderLayout.NORTH);

        java.util.List<JButton> primaryBtns = java.util.Arrays.asList(
                NewAdminButton, EditMoviesbutton, Addnewmoviesbutton, editcinemabutton, Addnewcinema, AddScreenButton
        );
        java.util.List<JButton> dangerBtns = java.util.Arrays.asList(
                Deletemoviebutton, deletecinemabutton, DeleteScreenButton
        );

        java.util.function.Consumer<JButton> stylePrimary = b -> {
            if (b == null) return;
            b.setFont(controlFont);
            b.setBackground(btnColor);
            b.setForeground(fg);
            b.setOpaque(true);
            b.setFocusPainted(false);
            b.setHorizontalAlignment(SwingConstants.CENTER);
            b.setHorizontalTextPosition(SwingConstants.CENTER);
            b.setMargin(new Insets(6, 12, 6, 12));
        };
        java.util.function.Consumer<JButton> styleDanger = b -> {
            if (b == null) return;
            b.setFont(controlFont);
            b.setBackground(btnColor);
            b.setForeground(fg);
            b.setOpaque(true);
            b.setFocusPainted(false);
            b.setHorizontalAlignment(SwingConstants.CENTER);
            b.setHorizontalTextPosition(SwingConstants.CENTER);
            b.setMargin(new Insets(6, 12, 6, 12));
        };

        if (backButton != null) {
            backButton.setFont(controlFont);
            backButton.setForeground(fg);
            backButton.setBackground(btnColor);
            backButton.setOpaque(true);
            backButton.setFocusPainted(false);
            backButton.setHorizontalAlignment(SwingConstants.CENTER);
            backButton.setHorizontalTextPosition(SwingConstants.CENTER);
            backButton.setMargin(new Insets(6, 12, 6, 12));
            backButton.setPreferredSize(new Dimension(BUTTON_PREF_WIDTH, backButton.getPreferredSize().height));
            backButton.setContentAreaFilled(false);
            backButton.setOpaque(true);
        }

        for (JButton b : primaryBtns) stylePrimary.accept(b);
        for (JButton b : dangerBtns) styleDanger.accept(b);

        JTableHeader th = movieTable.getTableHeader();
        th.setBackground(new Color(30, 45, 60));
        th.setForeground(fg);
        th.setFont(controlFont.deriveFont(Font.BOLD));
        movieTable.setFont(controlFont);
        movieTable.setForeground(Color.BLACK);
        movieTable.setSelectionBackground(primary);
        movieTable.setSelectionForeground(Color.WHITE);

        for (Component comp : MyPanel.getComponents()) {
            if (comp instanceof JPanel) {
                comp.setBackground(bg);
                comp.setForeground(fg);
            }
        }

        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminMain());
    }
}

