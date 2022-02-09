import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import habhez0.BankLogic;

/**
 * Den här klassen innehåller min lösning för uppgift0.
 * ltu-id: habhez-0
 * Namn: Habiballah Hezarehee
 */
public class App
{


   public static void main (String[] args)
   {
      BankLogic bank = new BankLogic();
      bank.createCustomer("Habiballah", "Hezarehee", "1234567890");
      bank.createSavingsAccount("1234567890");
      if (bank.deposit("1234567890", 1001, 100))
      {
         System.out.println("Deposit successful");
      }
      System.out.println(bank.getCustomer("1234567890"));
      if (bank.withdraw("1234567890", 1001, 100))
      {
         System.out.println("Withdraw successful");
      }
      System.out.println(bank.getCustomer("1234567890"));


   }
}

