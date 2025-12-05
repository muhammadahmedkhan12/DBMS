import java.io.*;
import java.util.Scanner;

public class CreditCardValidations {
    public static String validateCardDetails(String cardNumber, String expiry, String cvv) {
        if (isDuplicateCard(cardNumber)) {
            return "This card number is already registered.";
        }

        boolean validCard = cardNumber != null && cardNumber.length() == 16;
        if (validCard) {
            for (int i = 0; i < cardNumber.length(); i++) {
                if (cardNumber.charAt(i) < '0' || cardNumber.charAt(i) > '9') {
                    validCard = false;
                    break;
                }
            }
        }
        boolean validExpiry = false;
        String expiryMsg = "";
        if (expiry != null && expiry.length() == 5 && expiry.charAt(2) == '/') {
            char m1 = expiry.charAt(0);
            char m2 = expiry.charAt(1);
            char y1 = expiry.charAt(3);
            char y2 = expiry.charAt(4);
            int mm = (m1 - '0') * 10 + (m2 - '0');
            int yy = (y1 - '0') * 10 + (y2 - '0');
            int curYear = 25;
            int curMonth = 6;
            int maxYear = curYear + 10;
            if (mm >= 1 && mm <= 12) {
                if (yy < curYear || yy > maxYear) {
                    expiryMsg = "Expiry year must be within 10 years from now.\n";
                    validExpiry = false;
                } else if (yy > curYear) {
                    validExpiry = true;
                } else if (yy == curYear && mm >= curMonth) {
                    validExpiry = true;
                } else if (yy == curYear && mm < curMonth) {
                    expiryMsg = "Card is expired. Month has already passed.\n";
                    validExpiry = false;
                }
            }
            else {
                expiryMsg = "Month must be between 01 and 12.\n";
                validExpiry = false;
            }
        } else {
            expiryMsg = "Expiry must be in MM/YY format.\n";
        }

        boolean validCVV = cvv != null && cvv.length() == 3;
        if (validCVV) {
            for (int i = 0; i < cvv.length(); i++) {
                if (cvv.charAt(i) < '0' || cvv.charAt(i) > '9') {
                    validCVV = false;
                    break;
                }
            }
        }

        String temp = "";
        if (!validCard) temp += "Card number must be 16 digits.\n";
        if (!validExpiry) temp += "Expiry must be MM/YY, valid and not expired.\n";
        temp += expiryMsg;
        if (!validCVV) temp += "CVV must be in 3 digits.\n";

        if (temp.equals("")) {
            return null;
        } else {
            return temp.trim();
        }
    }

    private static boolean isDuplicateCard(String cardNumber) {
        try (Scanner sc = new Scanner(new File("creditcards.txt"))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.contains(cardNumber)) {
                    return true;
                }
            }
        } catch (IOException e) {}
        return false;
    }
}