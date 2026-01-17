package Ticket;

import Database.DBConnection;

import java.sql.*;
import java.util.ArrayList;

public class TicketManager {
    private ArrayList<Ticket> tickets = new ArrayList<>();

    public TicketManager(){
        loadtickets();
    }


    public ArrayList<Ticket> getTicketusingusername(String username){
        ArrayList<Ticket> result = new ArrayList<>();
        for (Ticket t : tickets){
            if (t.getUsername().equals(username)){
                result.add(t);
            }
        }
        return result;
    }

    public void loadtickets() {
        tickets.clear();

        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT * FROM Tickets";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Ticket ticket = new Ticket(
                        rs.getString("TicketID"),
                        rs.getString("Username"),
                        rs.getString("SeatID"),
                        rs.getString("ShowtimeID"),
                        rs.getString("CinemaID"),
                        rs.getString("MovieID"),
                        rs.getString("PaymentMethod")
                );
                tickets.add(ticket);
            }
            System.out.println("Tickets loaded from database: " + tickets.size());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean saveTicket(String username, String movieName, String cinemaName,
                                     int seatNumber, String paymentMethod, String screenType) {
        String sql = "INSERT INTO Tickets " +
                "(Username, MovieName, CinemaName, SeatNumber, PaymentMethod, ScreenType) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection()){
             PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, movieName);
            ps.setString(3, cinemaName);
            ps.setInt(4, seatNumber);
            ps.setString(5, paymentMethod);
            ps.setString(6, screenType);

            ps.executeUpdate();
            System.out.println("Ticket saved successfully for user: " + username);
            return true;

        }
        catch (SQLException ex) {
            if (ex.getMessage().contains("UQ_Ticket")) {
                System.err.println("Seat " + seatNumber + " already booked.");
            }
            else {
                ex.printStackTrace();
            }
            return false;
        }
    }
}