import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Movie.MovieManager;
import Movie.Movie;
import Cinema.CinemaManager;
import Cinema.Cinema;
import Cinema.Screen;
import java.util.ArrayList;

public class AdminMain extends JFrame{
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
    private JButton EditMovies;
    private JButton DeleteMovieButton;
    private JButton Addcinemabutton;
    private JButton editCinemaButton;
    private JButton Deletecinemabutton;
    private JButton AddScreenButton;
    private JButton DeleteScreenButton;
    private JButton backButton;


    public AdminMain() {
        MyPanel = new JPanel();
        setContentPane(MyPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        tableModel = new DefaultTableModel(new Object[]{"Movie ID", "Name", "Rating", "Add Movie", "Edit Movie"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 3 || column == 4;
            }
        };
        movieTable = new JTable(tableModel);
        movieTable.setRowHeight(30);
        tableScrollPane = new JScrollPane(movieTable);
        MyPanel.setLayout(new BorderLayout());
        MyPanel.add(tableScrollPane, BorderLayout.CENTER);

        loadMoviesToTable();

        movieTable.getColumn("Add Movie").setCellRenderer(new ButtonRenderer());
        movieTable.getColumn("Add Movie").setCellEditor(new ButtonEditor(new JCheckBox(), "add"));
        movieTable.getColumn("Edit Movie").setCellRenderer(new ButtonRenderer());
        movieTable.getColumn("Edit Movie").setCellEditor(new ButtonEditor(new JCheckBox(), "edit"));

        JPanel controlsPanel = new JPanel(new BorderLayout()); ////

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JPanel backPanel = new JPanel(new BorderLayout()); ////
        backButton = new JButton("Back");
        backPanel.add(backButton, BorderLayout.CENTER);

        NewAdminButton = new JButton("New Admin");
        EditMoviesbutton = new JButton("Edit Movies");
        Addnewmoviesbutton = new JButton("Add New Movie");
        Deletemoviebutton = new JButton("Delete Movie");
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

        buttonPanel.setPreferredSize(new Dimension(1200, 60));
        controlsPanel.add(buttonPanel, BorderLayout.NORTH);
        controlsPanel.add(backPanel, BorderLayout.SOUTH);

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
                new EditMovies();
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



    }

    private void loadMoviesToTable() {
        tableModel.setRowCount(0);
        ArrayList<Movie> movies = MovieManager.getManager().getMovies();
        for (Movie m : movies) {
            tableModel.addRow(new Object[]{m.getMovieid(), m.getMoviename(), m.getRating(), "Add Movie", "Edit Movie"});
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

        public ButtonEditor(JCheckBox checkBox, String actionType) {
            super(checkBox);
            this.actionType = actionType;
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            selectedRow = row;
            JButton button = new JButton(label);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int movieId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                    if (actionType.equals("add")) {
                        new AddMovieToScreenFrame(movieId);
                    } else if (actionType.equals("edit")) {
                        new EditMovieFrame(movieId);
                    }
                    fireEditingStopped();
                }
            });
            return button;
        }
    }

    class AddMovieToScreenFrame extends JFrame {
        public AddMovieToScreenFrame(int movieId) {
            setTitle("Add Movie to Screen");
            setSize(400, 350);
            setLocationRelativeTo(AdminMain.this);
            setLayout(new GridLayout(7, 2));

            Movie movie = MovieManager.getManager().getmoviebyid(movieId);

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
                for (Screen s : selectedCinema.getScreens()) {
                    screenCombo.addItem(s.getScreenid());
                }
            }
            add(screenCombo);

            cinemaCombo.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    screenCombo.removeAllItems();
                    int idx = cinemaCombo.getSelectedIndex();
                    if (idx >= 0) {
                        Cinema selectedCinema = CinemaManager.getCinemaManager().getCinemas().get(idx);
                        for (Screen s : selectedCinema.getScreens()) {
                            screenCombo.addItem(s.getScreenid());
                        }
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
                    CinemaManager.getCinemaManager().addmovietocinema(cinema, screen, movie, showtime);
                    JOptionPane.showMessageDialog(AddMovieToScreenFrame.this, "Movie added to screen.");
                    dispose();
                }
            });
            add(addButton);

            JButton cancelButton = new JButton("Cancel");
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

            Movie movie = MovieManager.getManager().getmoviebyid(movieId);

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
            saveButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String newName = newNameField.getText().trim();
                    String newRating = newRatingField.getText().trim();
                    if (newName.isEmpty() || newRating.isEmpty()) {
                        JOptionPane.showMessageDialog(EditMovieFrame.this, "Please enter new name and rating.");
                        return;
                    }
                    boolean success = MovieManager.getManager().editmovies(movieId, newName, newRating);
                    if (success) {
                        for (int i = 0; i < CinemaManager.getCinemaManager().getCinemas().size(); i++) {
                            Cinema cinema = CinemaManager.getCinemaManager().getCinemas().get(i);
                            for (int j = 0; j < cinema.getScreens().size(); j++) {
                                Screen screen = cinema.getScreens().get(j);
                                for (int k = 0; k < screen.getMovies().size(); k++) {
                                    Movie m = screen.getMovies().get(k);
                                    if (m.getMovieid() == movieId) {
                                        m.setMoviename(newName);
                                        m.setRating(newRating);
                                    }
                                }
                            }
                        }
                        CinemaManager.getCinemaManager().savecinemas();
                        JOptionPane.showMessageDialog(EditMovieFrame.this, "Movie updated successfully.");
                        loadMoviesToTable();
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(EditMovieFrame.this, "Movie id not found.");
                    }
                }
            });
            add(saveButton);

            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(e -> dispose());
            add(cancelButton);

            setVisible(true);
        }
    }

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
            addButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String screenType = screenTypeField.getText().trim();
                    String seatsText = seatsField.getText().trim();
                    int cinemaIdx = cinemaCombo.getSelectedIndex();
                    if (screenType.isEmpty() || seatsText.isEmpty() || cinemaIdx < 0) {
                        JOptionPane.showMessageDialog(AddScreenFrame.this, "Please fill all fields.");
                        return;
                    }
                    int numberOfSeats;
                    try {
                        numberOfSeats = Integer.parseInt(seatsText);
                        if (numberOfSeats <= 0) throw new NumberFormatException();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(AddScreenFrame.this, "Please enter a valid positive number for seats.");
                        return;
                    }
                    Cinema cinema = CinemaManager.getCinemaManager().getCinemas().get(cinemaIdx);
                    if (cinema.getScreens().isEmpty()) {
                        Screen.resetScreenNumber();
                    }
                    cinema.addScreen(new Screen(screenType, numberOfSeats));
                    CinemaManager.getCinemaManager().savecinemas();
                    JOptionPane.showMessageDialog(AddScreenFrame.this, "Screen added to cinema.");
                    dispose();
                }
            });
            add(addButton);

            JButton backButton = new JButton("Back");
            backButton.addActionListener(e -> dispose());
            add(backButton);

            JButton cancelButton = new JButton("Cancel");
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
                JPanel cinemaPanel = new JPanel();
                cinemaPanel.setLayout(new BorderLayout());
                cinemaPanel.add(new JLabel("Cinema: " + cinema.getName()), BorderLayout.NORTH);

                JPanel screensPanel = new JPanel();
                screensPanel.setLayout(new GridLayout(0, 2, 10, 5));
                for (Screen screen : cinema.getScreens()) {
                    screensPanel.add(new JLabel("Screen: " + screen.getScreenid() + " (" + screen.getScreentype() + ")"));
                    JButton deleteBtn = new JButton("Delete");
                    deleteBtn.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            cinema.getScreens().remove(screen);
                            CinemaManager.getCinemaManager().savecinemas();
                            JOptionPane.showMessageDialog(DeleteScreenFrame.this, "Screen deleted.");
                            dispose();
                        }
                    });
                    screensPanel.add(deleteBtn);
                }
                cinemaPanel.add(screensPanel, BorderLayout.CENTER);
                mainPanel.add(cinemaPanel);
            }

            JScrollPane scrollPane = new JScrollPane(mainPanel);
            add(scrollPane, BorderLayout.CENTER);

            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

            JButton backButton = new JButton("Back");
            backButton.addActionListener(e -> dispose());
            bottomPanel.add(backButton);

            JButton closeButton = new JButton("Close");
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
            addButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String movieName = movieNameField.getText().trim();
                    String rating = ratingField.getText().trim();
                    if (movieName.isEmpty() || rating.isEmpty()) {
                        JOptionPane.showMessageDialog(AddMovieOnlyFrame.this, "Please fill all fields.");
                        return;
                    }
                    Movie movie = new Movie(movieName, rating);
                    boolean exists = false;
                    for (Movie m : MovieManager.getManager().getMovies()) {
                        if (m.getMoviename().equalsIgnoreCase(movieName)) {
                            exists = true;
                            break;
                        }
                    }
                    if (exists) {
                        JOptionPane.showMessageDialog(AddMovieOnlyFrame.this, "Movie name already exists.");
                        return;
                    }
                    MovieManager.getManager().addmovie(movie);
                    JOptionPane.showMessageDialog(AddMovieOnlyFrame.this, "Movie added to movies.txt with ID: " + movie.getMovieid());
                    loadMoviesToTable();
                    dispose();
                }
            });
            add(addButton);
            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(e -> dispose());
            add(cancelButton);

            setVisible(true);
        }
    }

    public static void main(String[] args) {
        new AdminMain();
    }
}
