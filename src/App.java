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

   static LinkedHashMap<ArrayList<String>, ArrayList<String>> customerData = new LinkedHashMap<ArrayList<String>,
           ArrayList<String>>();

   private void removeDuplicateCustomer ()
   {
      for (Map.Entry<ArrayList<String>, ArrayList<String>> customer : customerData.entrySet())
      {
         ArrayList<String> customerDetails = customer.getKey();
         String pNo = customerDetails.get(0);
         //Check if customer exists twice
         for (Map.Entry<ArrayList<String>, ArrayList<String>> customer2 : customerData.entrySet())
         {
            ArrayList<String> customerDetails2 = customer2.getKey();
            if (customerDetails2.get(0).equals(pNo))
            {
               //Remove the duplicate customer
               customerData.remove(customerDetails2);
            }
         }
      }
   }


   public static void main (String[] args)
   {

      ArrayList<String> customer = new ArrayList<String>();
      ArrayList<String> accounts = new ArrayList<String>();

      customer.add("123456789");
      customer.add("Habiballah");
      customer.add("Hezarehee");

      accounts.add("1001 1000.00 Sparkonto 1.2%");
      customerData.put(customer, accounts);

      customer.clear();
      accounts.clear();

      customer.add("123456789");
      customer.add("Habiballah");
      customer.add("rezaie");

      customerData.put(customer, accounts);

      System.out.println(customerData);

   }
}

