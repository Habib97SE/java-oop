import habhez0.Account;
import habhez0.Customer;
import habhez0.BankLogic;
import habhez0.GUI;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class App {
    public static void main(String[] args) {
        GUI gui = new GUI("Bank", 600, 500);
        gui.start();
    }
}

