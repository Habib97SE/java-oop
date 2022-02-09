package habhez0;

import java.text.NumberFormat;
import java.util.*;


public class BankLogic
{
   //Create Map of two ArrayList of customers. First part is customers details, second part is accounts details
   // Customers: socialSecurityNumber, firstName, lastName
   // Account: accountNumber, accountType, balance, interestRate
   private static LinkedHashMap<ArrayList<String>, ArrayList<String>> bank = new LinkedHashMap<ArrayList<String>,
           ArrayList<String>>();
   final char SPACE = ' ';
   static int accountNumber = 1001;

   public BankLogic ()
   {
      Account account = new Account();
      Customer customer = new Customer();
   }


   /**
    * Get the customers data from the bank.
    *
    * @param pNo : String that contains the customer's personal number
    * @return : ArrayList<String> that contains the customer's data if the customer exists, otherwise return null.
    */
   private LinkedHashMap<ArrayList<String>, ArrayList<String>> getCustomerData (String pNo)
   {
      LinkedHashMap<ArrayList<String>, ArrayList<String>> customerData = new LinkedHashMap<ArrayList<String>,
              ArrayList<String>>();
      // Check if customer exists
      for (Map.Entry<ArrayList<String>, ArrayList<String>> customer : bank.entrySet())
      {
         ArrayList<String> customerDetails = customer.getKey();
         // Check if first element of customerDetails is equal to pNo

         if (customerDetails.get(0).equals(pNo))
         {
            customerData.put(customerDetails, customer.getKey());
            customerData.put(customerDetails, customer.getValue());
            return customerData;
         }
      }
      // If customer does not exist, return null
      return null;
   }

   private boolean putBackCustomer (Map<ArrayList<String>, ArrayList<String>> customerData)
   {
      // Put back customer and replace the old one
      return bank.put(customerData.keySet().iterator().next(), customerData.values().iterator().next()) != null;
   }

   /**
    * Returns an ArrayList <String> that contains a presentation of all the bank's customers as follows:
    * [8505221898 Karl Karlsson, 6911258876 Pelle Persson, 7505121231 Lotta Larsson]
    *
    * @return: ArrayList <String> that contains a presentation of all the bank's customers
    * If no customers exist, return an empty ArrayList [].
    */
   public ArrayList<String> getAllCustomers ()
   {
      ArrayList<String> allCustomers = new ArrayList<>();

      // Get all keySet from bank
      for (ArrayList<String> customer : bank.keySet())
      {
         // Check if customer is already added to allCustomers ArrayList
         if (!allCustomers.contains(customer.get(0) + SPACE + customer.get(1) + SPACE + customer.get(2)))
         {
            allCustomers.add(customer.get(0) + SPACE + customer.get(1) + SPACE + customer.get(2));
         }
      }
      ;
      return allCustomers;
   }

   /**
    * Create a new customer
    *
    * @param name     : String that contains the customer's name
    * @param surname: String that contains the customer's surname
    * @param pNo:     String that contains the customer's personal number
    * @return : true if the customer was created, false if the customer already exists
    */
   public boolean createCustomer (String name, String surname, String pNo)
   {
      // Check if customer already exists by pNo
      for (ArrayList<String> customer : bank.keySet())
      {
         // If customer exists, return false
         if (Objects.equals(customer.get(0), pNo))
         {
            return false;
         }
      }
      // Create new customer
      ArrayList<String> customer = new ArrayList<String>();
      ArrayList<String> accounts = new ArrayList<String>();
      customer.add(pNo);
      customer.add(name);
      customer.add(surname);
      bank.put(customer, accounts);
      return true;
   }

   /**
    * Get all accounts, balances and interest rates for a customer
    *
    * @param pNo: String that contains the customer's personal number
    * @return: ArrayList <String> that contains a presentation of all the customer's accounts as follows:
    * [pNo firstName lastName, accountNumber balance kr accountType interestRate %]
    * Returns null if the customer does not exist
    */
   public ArrayList<String> getCustomer (String pNo)
   {
      LinkedHashMap<ArrayList<String>, ArrayList<String>> customerData = getCustomerData(pNo);
      if (customerData == null)
         return null;

      ArrayList<String> customer = customerData.keySet().iterator().next();
      ArrayList<String> accounts = customerData.values().iterator().next();

      ArrayList<String> customerAccounts = new ArrayList<>();
      customerAccounts.add(customer.get(0) + SPACE + customer.get(1) + SPACE + customer.get(2));

      //Order accounts by accountNumber from low to high
      for (int i = 0; i < accounts.size(); i++)
      {
         for (int j = 0; j < accounts.size() - 1; j++)
         {
            if (Integer.parseInt(accounts.get(j).split(" ")[0]) > Integer.parseInt(accounts.get(j + 1).split(" ")[0]))
            {
               String temp = accounts.get(j);
               accounts.set(j, accounts.get(j + 1));
               accounts.set(j + 1, temp);
            }
         }
      }

      for (String account : accounts)
      {
         if (account.split(" ").length == 5)
            continue;
         String[] accountData = account.split(" ");
         accountData[1] =
                 NumberFormat.getCurrencyInstance(new Locale("sv", "SE")).format(Double.parseDouble(accountData[1]));
         String accountType = "";
         accountType += accountData[0] + SPACE + accountData[1] + SPACE + accountData[2] + SPACE + accountData[3] +
                 " %";
         customerAccounts.add(accountType);
      }
      return customerAccounts;
   }

   /**
    * Change customers name, surname. If the customer does not exist, return false.
    * If name or surname is empty, keep the old one.
    *
    * @param name:    String that contains the customer's new name
    * @param surname: String that contains the customer's new surname
    * @param pNo:     String that contains the customer's personal number
    * @return : true if the customer's name was changed, false if the customer does not exist
    */
   public boolean changeCustomerName (String name, String surname, String pNo)
   {
      if (name.isEmpty() && surname.isEmpty())
         return false;
      for (Map.Entry<ArrayList<String>, ArrayList<String>> entry : bank.entrySet())
      {
         if (entry.getKey().get(0).equals(pNo))
         {
            if (!name.equals(""))
               entry.getKey().set(1, name);
            if (!surname.equals(""))
               entry.getKey().set(2, surname);
            return true;
         }
      }
      return false;
   }

   /**
    * Create a new savings account for a customer, if the customer does not exist, return -1, otherwise account number.
    *
    * @param pNo: String that contains the customer's personal number
    * @return : int that contains the account number if the account was created, -1 if the customer does not exist
    */
   public int createSavingsAccount (String pNo)
   {
      Map<ArrayList<String>, ArrayList<String>> customerData = getCustomerData(pNo);
      if (customerData == null)
         return -1;
      ArrayList<String> account = new ArrayList<String>();
      // Save the value part of the customerData
      account = customerData.values().iterator().next();

      String accountInfo = accountNumber + " " + "0.00" + " " + "Sparkonto" + " " + "1.2";
      account.add(accountInfo);
      ArrayList<String> customer = new ArrayList<String>();
      customer = customerData.keySet().iterator().next();
      // replace the old customerData with the new one

      bank.put(customer, account);
      int accountNo = accountNumber;
      accountNumber++;
      return accountNo;
   }

   /**
    * Get the customer's info and return it as a string. If the customer does not exist, return null.
    *
    * @param pNo:       String that contains the customer's personal number
    * @param accountId: int that contains the account number
    * @return : String that contains the customer's info if the customer exists, null if the customer does not exist
    */
   public String getAccount (String pNo, int accountId)
   {
      LinkedHashMap<ArrayList<String>, ArrayList<String>> customerData = getCustomerData(pNo);
      if (customerData == null)
         return null;

      ArrayList<String> accounts = customerData.values().iterator().next();
      for (String account : accounts)
      {
         String[] accountDetails = account.split(" ");
         if (accountDetails[0].equals(Integer.toString(accountId)))
         {
            String accountInfo = "";
            accountDetails[1] = NumberFormat.getCurrencyInstance(new Locale("sv", "SE")).format(Double.parseDouble(accountDetails[1]));
            accountInfo =
                    accountDetails[0] + SPACE + accountDetails[1] + SPACE + accountDetails[2] + SPACE + accountDetails[3] + " %";
            return accountInfo;
         }
      }
      return null;
   }


   /**
    * Make deposit to the account, If the customer does not exist, return false, otherwise return true.
    *
    * @param pNo       : String that contains the customer's personal number
    * @param accountId : int that contains the account number
    * @param amount    : int that contains the amount to deposit
    * @return : boolean that contains true if the deposit was made, false if the customer does not exist
    */
   public boolean deposit (String pNo, int accountId, int amount)
   {
      if (amount <= 0)
         return false;
      LinkedHashMap<ArrayList<String>, ArrayList<String>> customerData = getCustomerData(pNo);
      if (customerData == null)
         return false;

      ArrayList<String> accounts = customerData.values().iterator().next();
      for (String account : accounts)
      {
         String[] accountDetails = account.split(" ");
         if (accountDetails[0].equals(Integer.toString(accountId)))
         {
            double balance = Double.parseDouble(accountDetails[1]);
            balance += amount;
            accountDetails[1] = Double.toString(balance);
            // replace the old balanace with the new one in accounts
            accounts.remove(account);
            accounts.add(accountDetails[0] + SPACE + accountDetails[1] + SPACE + accountDetails[2] + SPACE + accountDetails[3]);
            ArrayList<String> customer = customerData.keySet().iterator().next();
            bank.put(customer, accounts);
            return true;
         }
      }
         return false;
   }

   /**
    * Make withdrawal from the account, If the customer does not exist, return false, otherwise return true.
    *
    * @param pNo       : String that contains the customer's personal number
    * @param accountId : int that contains the account number
    * @param amount    : int that contains the amount to withdraw
    * @return : boolean that contains true if the withdrawal was made, false if the customer does not exist
    */
   public boolean withdraw (String pNo, int accountId, int amount)
   {
      if (amount <= 0)
         return false;
      LinkedHashMap<ArrayList<String>, ArrayList<String>> customerData = getCustomerData(pNo);
      if (customerData == null)
         return false;

      ArrayList<String> accounts = customerData.values().iterator().next();
      for (String account : accounts)
      {
         String[] accountDetails = account.split(" ");
         if (accountDetails[0].equals(Integer.toString(accountId)))
         {
            double balance = Double.parseDouble(accountDetails[1]);
            if (balance < amount)
               return false;
            balance -= amount;
            accountDetails[1] = Double.toString(balance);
            // replace the old balanace with the new one in accounts
            accounts.remove(account);
            accounts.add(accountDetails[0] + SPACE + accountDetails[1] + SPACE + accountDetails[2] + SPACE + accountDetails[3]);
            ArrayList<String> customer = customerData.keySet().iterator().next();
            bank.put(customer, accounts);
            return true;
         }
      }
      return false;
   }


   /**
    * Close the account, If the customer does not exist, return null, otherwise return the customer's account info
    *
    * @param pNr       : String that contains the customer's personal number (pNr)
    * @param accountId : int that contains the account number (accountId)
    * @return : String that contains the account info if the account was closed, null if the customer does not exist.
    * Calculate the interest and add it to the balance.
    * Return string format is: "Account number Balance kr Account Type Interest rate"
    */
   public String closeAccount (String pNr, int accountId)
   {
      LinkedHashMap<ArrayList<String>, ArrayList<String>> customerData = getCustomerData(pNr);
      if (customerData == null)
         return null;
      ArrayList<String> customer = new ArrayList<String>();
      ArrayList<String> accounts = new ArrayList<String>();

      customer = customerData.keySet().iterator().next();
      accounts = customerData.values().iterator().next();
      for (String account : accounts)
      {
         String[] accountDetailArray = account.split(" ");
         if (accountDetailArray[0].equals(Integer.toString(accountId)))
         {
            float balance = Float.parseFloat(accountDetailArray[1]);
            float interestRate = Float.parseFloat(accountDetailArray[3]);
            float interest = balance * interestRate / 100;
            // Convert balance and interest to local currency

            String balanceStr =
                    NumberFormat.getCurrencyInstance(new Locale("sv", "SE")).format(Double.parseDouble(String.valueOf(balance)));
            String interestStr =
                    NumberFormat.getCurrencyInstance(new Locale("sv", "SE")).format(Double.parseDouble(String.valueOf(interest)));
            String accountInfo =
                    accountDetailArray[0] + SPACE + balance + SPACE + accountDetailArray[2] + SPACE + interestRate + SPACE + "closed";
            accounts.set(accounts.indexOf(account), accountInfo);
            return accountDetailArray[0] + SPACE + balanceStr + SPACE + accountDetailArray[2] + SPACE + interestStr;
         }
      }
      return null;
   }

   /**
    * If the account has been closed return true, otherwise return false
    *
    * @param account : String that contains the account info
    * @return : boolean that indicates if the account has been closed or not
    */
   private boolean accountIsClosed (String account)
   {
      if (account.split(" ").length == 4)
         return true;
      return account.split(" ")[4].equals("closed");
   }


   /**
    * Delete a customer and its bank accounts from the bank.
    *
    * @param pNo : String that contains the customer's personal number
    * @return : An arraylist of strings that contains the account info if the customer was deleted, null if the customer does not exist.
    */
   public ArrayList<String> deleteCustomer (String pNo)
   {
      ArrayList<String> finalList = new ArrayList<String>();
      LinkedHashMap<ArrayList<String>, ArrayList<String>> customerData = getCustomerData(pNo);

      if (customerData == null)
         return null;

      ArrayList<String> customer = new ArrayList<String>();
      ArrayList<String> accounts = new ArrayList<String>();

      customer = customerData.keySet().iterator().next();
      accounts = customerData.values().iterator().next();

      finalList.add(customer.get(0) + SPACE + customer.get(1) + SPACE + customer.get(2));
      for (String account : accounts)
      {
         if (account.split(" ").length == 4)
         {
            String[] accountDetailArray = account.split(" ");
            float balance = Float.parseFloat(accountDetailArray[1]);
            float interestRate = Float.parseFloat(accountDetailArray[3]);
            float interest = balance * interestRate / 100;
            // Convert balance and interest to local currency
            String balanceStr = NumberFormat.getCurrencyInstance(new Locale("sv", "SE")).format(Double.parseDouble(String.valueOf(balance)));
            String interestStr = NumberFormat.getCurrencyInstance(new Locale("sv", "SE")).format(Double.parseDouble(String.valueOf(interest)));
            finalList.add(accountDetailArray[0] + SPACE + balanceStr + SPACE + accountDetailArray[2] + SPACE + interestStr);
         }
      }
      bank.remove(customer);
      return finalList;
   }
}


