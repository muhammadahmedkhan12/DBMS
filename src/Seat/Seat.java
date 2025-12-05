package Seat;

public class Seat {
    private String seatid;
    private boolean isbooked;

    public Seat(String seatid,boolean isbooked){
        this.seatid=seatid;
        this.isbooked=isbooked;
    }

    public void setIsbooked(boolean isbooked) {
        this.isbooked = isbooked;
    }

    public String getSeatid() {
        return seatid;
    }

    public boolean isIsbooked() {
        return isbooked;
    }
}
