package Ticket;

import Database.DBConnection;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class TicketManager {
    private ArrayList<Ticket> tickets = new ArrayList<>();

    public TicketManager(){
        loadtickets();
    }

    public void addticket(Ticket t ){
        tickets.add(t);
        savetickets();
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
        try(Connection con = DBConnection.getConnection()) {
            String query = "SELECT * FROM ticket";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                for (Ticket t : tickets) {
                    Ticket ticket = new Ticket(
                            rs.getString("ticketid"),
                            rs.getString("username"),
                            rs.getString("seatid"),
                            rs.getString("showtimeid"),
                            rs.getString("cinemaid"),
                            rs.getString("movieid"),
                            rs.getString("paymentmethod")
                    );
                    tickets.add(ticket);
                }
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void savetickets() {
        try (Connection con = DBConnection.getConnection()) {
            String query = "INSERT INTO ticket (ticketid, username, seatid, showtimeid, cinemaid, movieid, paymentmethod) VALUES (?, ?, ?, ?, ?, ?, ?)";

            for (Ticket t : tickets) {
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, t.getTicketid());
                ps.setString(2, t.getUsername());
                ps.setString(3, t.getSeatid());
                ps.setString(4, t.getShowtimeid());
                ps.setString(5, t.getCinemaid());
                ps.setString(6, t.getMovieid());
                ps.setString(7, t.getPaymentmethod());
                ps.executeUpdate();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}



