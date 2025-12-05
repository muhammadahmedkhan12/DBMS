import Database.DBConnection;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class AuthenticationService {
    public ArrayList<User> users;
    private User LoggedInUser = null;
    private static AuthenticationService authenticateuser = new AuthenticationService();



    public static AuthenticationService getAuthenticateuser() {
        return authenticateuser;
    }

    public AuthenticationService() {
        users = new ArrayList<>();
//        if (new File("users.txt").length() == 0 ){
//            users.add(new User("mushtaq", "mushtaq", "mushtaq@gmail.com", "0011223344"));
//            saveUsers();
//        }
//        else{
//            loadUsers();
//        }
        loadUsers();

    }

    public String SignUp(String username, String password, String email, String contactno) {
        loadUsers();
        String temp = "";
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)) {
                return "Username already exists";
            }
        }
        try {
            if (!chkEmail(email)) {
                return "Invalid or already used email.";
            }

            if (!chkContact(contactno)) {
                return "Invalid or already used contact number.";
            }
        }
        catch (IOException e) {
            return "Error validating email/contact.";
        }

        boolean hasCapital = false;
        boolean hasNumber = false;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isUpperCase(password.charAt(i))) {
                hasCapital = true;
            }
            else if (Character.isDigit(password.charAt(i))) {
                hasNumber = true;
            }
        }
        if (username.length() < 6) {
            return "Username could not be less than 6";
        }
        else if (password.length() < 8) {
            return "password could not be less than 8";
        }
        else if ((password.contains("@") || password.contains("#") || password.contains("$")) && hasNumber && hasCapital) {
            users.add(new User(username, password, email, contactno));
            saveUsers();
            return "Sign up successful";
        } else {
            return "password did not meet conditions";
        }
    }

    public String login(String username, String password) {
        loadUsers();
        String temporary = "";
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username) && users.get(i).getPassword().equals(password)) {
                temporary = "login successfull";
                LoggedInUser = users.get(i);
                break;
            } else {
                temporary = "incorrect username and password";
            }
        }
        return temporary;
    }

    public void loadUsers() {
        users.clear();

        try(Connection con = DBConnection.getConnection()){
            String query = "SELECT * FROM movieUsers";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()){
                User u = new User(
                        rs.getString("Username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("Number")

                );
                users.add(u);
            }

        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

//        try (Scanner filescanner = new Scanner(new File("users.txt"))) {
//            while (filescanner.hasNextLine()) {
//                String[] data = filescanner.nextLine().split(",");
//                if (data.length == 4) {
//                    String username = data[0];
//                    String password = data[1];
//                    String email = data[2];
//                    String contactno = data[3];
//                    users.add(new User(username, password, email, contactno));
//                }
//            }
//
//        } catch (Exception e) {
//
//        }
    }

    public void saveUsers() {
        try (Connection con = DBConnection.getConnection()) {

            String query = "INSERT INTO movieUsers (UserName, Password, Email, Number) VALUES (?, ?, ?, ?)";

            for (User u : users) {
                // Only insert users that don't exist (avoid duplicates)
                if (!userExists(u.getEmail(),  con)) {

                    PreparedStatement stmt = con.prepareStatement(query);

                    stmt.setString(1, u.getUsername());
                    stmt.setString(2, u.getPassword());
                    stmt.setString(3, u.getEmail());
                    stmt.setString(4, u.getContactno());

                    stmt.executeUpdate();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String changepassword(String currentusername,String currentpassword, String newpassword) {
        System.out.println("Logged in user: " + LoggedInUser.getUsername());
        System.out.println("Current password: " + LoggedInUser.getPassword());
        boolean hasCapital = false;
        boolean hasNumber = false;
        for (int i = 0; i < newpassword.length(); i++) {
            if (Character.isUpperCase(newpassword.charAt(i))) {
                hasCapital = true;
            } else if (Character.isDigit(newpassword.charAt(i))) {
                hasNumber = true;
            }
        }
        String temp = "";
        if (LoggedInUser == null) {
            temp =  "No user is currently logged in.";
            return temp;
        }
        if ((!currentpassword.equals(LoggedInUser.getPassword())||!currentusername.equals(LoggedInUser.getUsername()))){
            temp =  "Current password or username is not correct";
        }
        else if (newpassword.length() < 8) {
            temp =  "Password could not be less than 8";
        }
        else if (!((newpassword.contains("@") || newpassword.contains("#") || newpassword.contains("$")) && hasNumber && hasCapital)) {
            temp = "New password does not meet conditions";
        }
        else {
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getUsername().equals(LoggedInUser.getUsername())){
                    users.get(i).setPassword(newpassword);
                    LoggedInUser.setPassword(newpassword);
                    temp =  "Password Changed";
                    saveUsers();
                    break;
                }
            }

        }
        return temp;
    }


    public static boolean chkEmail(String email) {
        if (email == null || email.isEmpty()) return false;

        int atInd = email.indexOf('@');
        if (atInd == -1 || atInd != email.lastIndexOf('@')) return false;
        if (atInd == 0 || atInd == email.length() - 1) return false;

        String local = email.substring(0, atInd);
        String domain = email.substring(atInd + 1);
        if (local.isEmpty() || domain.isEmpty()) return false;
        if (!domain.contains(".") || domain.startsWith(".") || domain.endsWith(".")) return false;

        String allowed = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!email.matches(allowed)) return false;




        return true;
    }


    public static boolean chkContact(String contact) throws IOException {
        if (contact == null || contact.isEmpty()) return false;
        if (contact.length() != 11) return false;

        for (int i = 0; i < contact.length(); i++) {
            char c = contact.charAt(i);
            if (!Character.isDigit(c)) return false;
        }

        return true;
    }

    private boolean userExists(String email,  Connection con) {
        String query = "SELECT * FROM movieUsers WHERE Email = ? ";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
        catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }


    public static void main(String[] args) {
        AuthenticationService au = new AuthenticationService();
    }
}