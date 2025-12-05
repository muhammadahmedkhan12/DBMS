public class Payment {
    public static String processpayment(String method , String cardnumber , String cvv){
        if (method.equalsIgnoreCase("cash")){
            return "Payment successfull by cash";
        }
        else if (method.equalsIgnoreCase("card")) {
            if (cardnumber.length()==16 && cvv.length()==3){
                return "Payment successfull by credit card";
            }
            else {
                return "invalid card details";
            }
        }
        else {
            return "Invalid payment method";
        }
    }
}
