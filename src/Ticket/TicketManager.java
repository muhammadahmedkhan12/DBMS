package Ticket;

import Database.DBConnection;

import java.sql.*;
import java.util.ArrayList;

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
        tickets.clear();
        try(Connection con = DBConnection.getConnection()) {
            String query = "SELECT ticketid, username, seatid, showtimeid, cinemaid, movieid, paymentmethod FROM ticket";
            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
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
            e.printStackTrace();
        }
    }

    public void savetickets() {
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try (Statement stmt = con.createStatement()) {
                // clear existing tickets to match in-memory state
                stmt.executeUpdate("DELETE FROM ticket");
            }

            String query = "INSERT INTO ticket (ticketid, username, seatid, showtimeid, cinemaid, movieid, paymentmethod) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement ps = con.prepareStatement(query)) {
                for (Ticket t : tickets) {
                    ps.setString(1, t.getTicketid());
                    ps.setString(2, t.getUsername());
                    ps.setString(3, t.getSeatid());
                    ps.setString(4, t.getShowtimeid());
                    ps.setString(5, t.getCinemaid());
                    ps.setString(6, t.getMovieid());
                    ps.setString(7, t.getPaymentmethod());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            con.commit();
            con.setAutoCommit(true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int reserveSeat(String username, Integer movieId, String cinemaId, String screenId, String seatLabel, String showtime, String paymentMethod, Double price) throws SQLException {
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                // 1) find or create seat (handle concurrent inserts)
                Integer seatId = null;
                String selectSeat = "SELECT seat_id FROM dbo.seat WHERE screen_id = ? AND seat_label = ?";
                try (PreparedStatement ps = con.prepareStatement(selectSeat)) {
                    ps.setString(1, screenId);
                    ps.setString(2, seatLabel);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) seatId = rs.getInt(1);
                    }
                }

                if (seatId == null) {
                    String insertSeat = "INSERT INTO dbo.seat (screen_id, seat_label) OUTPUT INSERTED.seat_id VALUES (?, ?)";
                    try (PreparedStatement ps = con.prepareStatement(insertSeat)) {
                        ps.setString(1, screenId);
                        ps.setString(2, seatLabel);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) seatId = rs.getInt(1);
                        }
                    } catch (SQLException se) {
                        // If another transaction inserted the seat concurrently, select it again
                        if (se.getErrorCode() == 2627 || se.getErrorCode() == 2601) {
                            try (PreparedStatement ps2 = con.prepareStatement(selectSeat)) {
                                ps2.setString(1, screenId);
                                ps2.setString(2, seatLabel);
                                try (ResultSet rs2 = ps2.executeQuery()) {
                                    if (rs2.next()) seatId = rs2.getInt(1);
                                }
                            }
                        } else {
                            throw se;
                        }
                    }
                }

                if (seatId == null) {
                    // something unexpected happened
                    con.rollback();
                    con.setAutoCommit(true);
                    return -1;
                }

                // 2) insert ticket; unique index on (screen_id, seat_id, showtime) prevents double booking
                String insertTicket = "INSERT INTO dbo.ticket (username, movie_id, cinema_id, screen_id, seat_id, showtime, payment_method, price) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = con.prepareStatement(insertTicket, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, username);
                    if (movieId == null) ps.setNull(2, java.sql.Types.INTEGER); else ps.setInt(2, movieId);
                    ps.setString(3, cinemaId);
                    ps.setString(4, screenId);
                    ps.setInt(5, seatId);
                    ps.setString(6, showtime);
                    ps.setString(7, paymentMethod);
                    if (price == null) ps.setNull(8, java.sql.Types.DECIMAL); else ps.setDouble(8, price);

                    ps.executeUpdate();
                    try (ResultSet gk = ps.getGeneratedKeys()) {
                        int id = gk.next() ? gk.getInt(1) : -1;
                        con.commit();
                        con.setAutoCommit(true);
                        return id;
                    }
                } catch (SQLException se) {
                    // Ticket insert failed - likely unique constraint violation (seat already booked for that showtime)
                    con.rollback();
                    con.setAutoCommit(true);
                    if (se.getErrorCode() == 2627 || se.getErrorCode() == 2601) {
                        // seat already booked
                        return -1;
                    }
                    throw se;
                }

            } catch (SQLException ex) {
                con.rollback();
                con.setAutoCommit(true);
                throw ex;
            } finally {
                if (!con.getAutoCommit()) {
                    try { con.setAutoCommit(true); } catch (Exception ignored) {}
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
