package Seat;

import Database.DBConnection;

import java.sql.*;
import java.util.ArrayList;

public class SeatManager {
    private ArrayList<Seat> seats = new ArrayList<>();

    public SeatManager(){
        loadSeats();
    }
    public void addseat(Seat s ){
        seats.add(s);
        saveseats();
    }

    public ArrayList<Seat> getseatsusingshowtimeid(String Seatid){
        ArrayList<Seat> result = new ArrayList<>();
        for (Seat s : seats){
            if (s.getSeatid().equals(Seatid)){
                result.add(s);
            }
        }
        return result;
    }

    public void bookseat(String seatid){
        for (Seat s : seats){
            if (s.getSeatid().equals(seatid)){
                s.setIsbooked(true);
                break;
            }
        }
        saveseats();
    }

    public void loadSeats() {
        seats.clear();
        try (Connection con = DBConnection.getConnection()) {
            String q = "SELECT seat_id, screen_id, seat_label FROM dbo.seat";
            try (PreparedStatement ps = con.prepareStatement(q);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("seat_id");
                    String screenId = rs.getString("screen_id");
                    String label = rs.getString("seat_label");
                    // represent seatid as screenId + ":" + label
                    Seat s = new Seat(screenId + ":" + label, false);
                    seats.add(s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveseats() {
        // For simplicity keep seats table authoritative; insert any new seats from in-memory list
        try (Connection con = DBConnection.getConnection()) {
            String select = "SELECT seat_id FROM dbo.seat WHERE screen_id = ? AND seat_label = ?";
            String insert = "INSERT INTO dbo.seat (screen_id, seat_label) VALUES (?, ?)";
            try (PreparedStatement psSelect = con.prepareStatement(select);
                 PreparedStatement psInsert = con.prepareStatement(insert)) {
                for (Seat s : seats) {
                    String[] parts = s.getSeatid().split(":", 2);
                    if (parts.length != 2) continue;
                    String screenId = parts[0];
                    String label = parts[1];

                    psSelect.setString(1, screenId);
                    psSelect.setString(2, label);
                    try (ResultSet rs = psSelect.executeQuery()) {
                        if (!rs.next()) {
                            psInsert.setString(1, screenId);
                            psInsert.setString(2, label);
                            psInsert.executeUpdate();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
