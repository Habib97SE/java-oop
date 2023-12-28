import habhez0.BankLogic;
import habhez0.GUI;

import java.io.*;


/**
 * Habib Hezarehee (habhez0) <br />
 * Main class
 * <br />
 *
 * @author habhez-0@student.ltu.se
 * @version 1.0
 * @since 2023-12-10
 */
public class App {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new GUI().start();
//        BankLogic bankLogic = new BankLogic();
//        boolean result = bankLogic.createCustomer("Habib", "Hezarehee", "199709100276");
//        if (result) {
//            System.out.println("Kunden har skapat.");
//        } else {
//            System.out.println("Problem med att skapa kunden.");
//        }
//        bankLogic.createCreditAccount("199709100276");
//        bankLogic.createSavingsAccount("199709100276");
//        bankLogic.closeAccount("199709100276", 1001);
////
////        FileOutputStream fos = new FileOutputStream("file.dat");
////        ObjectOutputStream oos = new ObjectOutputStream(fos);
////        oos.writeObject(bankLogic.getAllCustomers());
//
//        FileInputStream fis = new FileInputStream("file.dat");
//        ObjectInputStream ois = new ObjectInputStream(fis);
//        System.out.println(ois.readObject());

    }
}