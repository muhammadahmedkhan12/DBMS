package Seat;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

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
        try (Scanner scanner = new Scanner(new File("seats.txt"))) {
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                if (data.length == 3) {
                    seats.add(new Seat(data[0],Boolean.parseBoolean(data[1])));
                }
            }
        } catch (Exception e) {

        }
    }

    public void saveseats() {
        try (FileWriter writer = new FileWriter("seats.txt")) {
            for (Seat s : seats) {
                writer.write(s.getSeatid() + "," + "," + s.isIsbooked() + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
