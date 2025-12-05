package Ticket;

import java.io.File;
import java.io.FileWriter;
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
        try {
            Scanner scanner = new Scanner(new File("tickets.txt"));
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                if (data.length == 7) {
                    tickets.add(new Ticket(data[0], data[1], data[2], data[3], data[4], data[5], data[6]));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

        public void savetickets() {
            try (FileWriter writer = new FileWriter("tickets.txt")) {
                for (Ticket t : tickets) {
                    writer.write(t.getTicketid() + "," + t.getUsername() + "," + t.getSeatid() + "," +
                            t.getShowtimeid() + "," + t.getCinemaid() + "," + t.getMovieid() + "," +
                            t.getPaymentmethod() + "\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



            }



