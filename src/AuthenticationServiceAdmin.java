// java
/* leave existing package if any */

import Database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class AuthenticationServiceAdmin {
    public ArrayList<Admin> admins;
    private Admin LoggedInAdmin = null;
    private static AuthenticationServiceAdmin autheticateadmin = new AuthenticationServiceAdmin();

    public static AuthenticationServiceAdmin getInstance(){
        return autheticateadmin;
    }

    public AuthenticationServiceAdmin() {
        admins = new ArrayList<>();
    }

    public String login(String username , String password){
        loadAdmins();
        for (Admin a : admins) {
            if (a.getUsername().equals(username) && a.getPassword().equals(password)) {
                LoggedInAdmin = a;
                return "login successful";
            }
        }
        return "incorrect username and password";
    }

    public String MakeNewAdmin(String username, String password) {
        // ensure we operate on latest admins
        loadAdmins();

        if (LoggedInAdmin == null || !LoggedInAdmin.getUsername().equals("mushtaq12")){
            return "Only mushtaq can make new admins";
        }

        for (Admin existing : admins) {
            if (existing.getUsername().equals(username)) {
                return "Username already exists";
            }
        }

        boolean hasCapital = false;
        boolean hasNumber = false;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isUpperCase(password.charAt(i))) {
                hasCapital = true;
            } else if (Character.isDigit(password.charAt(i))) {
                hasNumber = true;
            }
        }
        if (username.length() < 6) {
            return "Username could not be less than 6";
        } else if (password.length() < 8) {
            return "password could not be less than 8";
        } else if ((password.contains("@") || password.contains("#") || password.contains("$")) && hasNumber && hasCapital) {
            // add new admin to in-memory list and persist
            admins.add(new Admin(username, password));
            saveAdmins();
            return "New Admin Made Successfully";
        } else {
            return "password did not meet conditions";
        }
    }

    public void loadAdmins(){
        admins.clear(); // avoid duplicates on repeated calls
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT username, password FROM admin";
            try (PreparedStatement ps = con.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()){
                    Admin a = new Admin(
                            rs.getString("username"),
                            rs.getString("password")
                    );
                    admins.add(a);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveAdmins(){
        // simple safe approach: delete all and insert current list inside a transaction
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try (Statement st = con.createStatement()) {
                st.executeUpdate("DELETE FROM admin");
            }

            String query = "INSERT INTO admin (username,password) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                for (Admin a  : admins) {
                    preparedStatement.setString(1, a.getUsername());
                    preparedStatement.setString(2, a.getPassword());
                    preparedStatement.executeUpdate();
                }
            }

            con.commit();
            con.setAutoCommit(true);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // optional: expose currently logged admin for callers
    public Admin getLoggedInAdmin() {
        return LoggedInAdmin;
    }
}
