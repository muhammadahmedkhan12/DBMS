public class Ticket {
    private String ticketid;
    private String username;
    private String seatid;
    private String showtimeid;
    private String cinemaid;
    private String movieid;
    private String paymentmethod;

    public Ticket(String ticketid , String username , String seatid , String showtimeid , String cinemaid , String movieid , String paymentmethod){
      this.ticketid=ticketid;
      this.username=username;
      this.seatid=seatid;
      this.showtimeid=showtimeid;
      this.cinemaid=cinemaid;
      this.movieid=movieid;
      this.paymentmethod=paymentmethod;
    }

    public String getSeatid() {
        return seatid;
    }

    public String getTicketid() {
        return ticketid;
    }

    public String getShowtimeid() {
        return showtimeid;
    }

    public String getMovieid() {
        return movieid;
    }

    public String getCinemaid() {
        return cinemaid;
    }

    public String getUsername() {
        return username;
    }

    public String getPaymentmethod() {
        return paymentmethod;
    }

}
