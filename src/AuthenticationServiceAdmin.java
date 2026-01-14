import Database.DBConnection;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

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
        String temporary = "";
        for (int i = 0; i < admins.size(); i++) {
            if (admins.get(i).getUsername().equals(username)&& admins.get(i).getPassword().equals(password)){
                LoggedInAdmin = admins.get(i);
                temporary = "login successfull";
                break;
            }
            else {
                temporary = "incorrect username and password";
            }
        }
        return temporary;
    }
    public String MakeNewAdmin(String username, String password) {
        if (LoggedInAdmin == null || !LoggedInAdmin.getUsername().equals("mushtaq12")){
            return "Only mushtaq can make new admins";
        }
        String temp = "";
        for (int i = 0; i < admins.size(); i++) {
            if (admins.get(i).getUsername().equals(username)) {
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
        }
        else if ((password.contains("@") || password.contains("#") || password.contains("$")) && hasNumber && hasCapital) {
            admins.clear();
            admins.add(new Admin(username, password));
            saveAdmins();
            return "New Admin Made Successfully";
        }
        else {
            return "password did not meet conditions";
        }
    }
    public void loadAdmins(){
        try(Connection con = DBConnection.getConnection()){
            String query = "SELECT * FROM admin";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
                Admin a = new Admin(
                        rs.getString("username"),
                        rs.getString("password")
                );
                admins.add(a);
            }


        }
        catch (Exception e) {

        }
    }
    public void saveAdmins(){
        try(Connection con = DBConnection.getConnection()){
            String query = "INSERT INTO admin (username,password) VALUES (?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(query);

            for (Admin a  : admins) {

                preparedStatement.setString(1, a.getUsername());
                preparedStatement.setString(2, a.getPassword());

                preparedStatement.executeUpdate();
            }
        }
        catch (Exception e){

        }
    }



}
