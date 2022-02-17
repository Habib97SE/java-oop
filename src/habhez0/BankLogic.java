/**
 * BankLogic class hanterar bankens logik. T ex hanterar konton, kunder, insättningar och uttag.
 *
 * @email habhez-0@student.ltu.se
 * @author Habiballah Hezarehee (habhez-0)
 * @version 1.0
 */
package habhez0;

import habhez0.Account;
import habhez0.Customer;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

public class BankLogic
{
    // ArrayList som innehåller en ArrayList av String och kommer att hålla data om kundernas och kontons uppgifter.
    // Såsom kontonummer, belopp, namn osv.
    ArrayList<ArrayList<String>> bank = new ArrayList<ArrayList<String>>();
    final char SPACE = ' ';
    // accountNumber är kontonummer som plusas upp för varje gång ett nytt konto skapas.
    private static int accountNumber = 1001;

    public BankLogic ()
    {
        Account account = new Account();
        Customer customer = new Customer();
    }

    private BigDecimal convertToBigDecimal(String amount)
    {
        return new BigDecimal(amount);
    }

    /**
     * Calculate the interest for given account and return it as a String
     * @param accountInfo : Array of account details
     * @return : A String of the interest for the given account
     */
    private String calculateInterest (String[] accountInfo)
    {
        BigDecimal balance = convertToBigDecimal(accountInfo[1]);
        BigDecimal interestRate = convertToBigDecimal(accountInfo[2]);
        BigDecimal result = balance.multiply(interestRate);
        result = result.divide(new BigDecimal(100));
        return convertToSwedishCurrency(result);
    }

    /**
     * Convert double to Swedish currency format (e.g. 1 000,00 kr)
     * @param amount : double to convert to Swedish currency
     * @return : String with Swedish currency format
     */
    private String convertToSwedishCurrency (BigDecimal amount)
    {
        return NumberFormat.getCurrencyInstance(new Locale("sv", "SE")).format(amount);
    }

    /**
     * @override convertToSwedishCurrency ( )
     * @param amount
     * @return
     */
    private String convertToSwedishCurrency (double amount)
    {
        return NumberFormat.getCurrencyInstance(new Locale("sv", "SE")).format(amount);
    }


    /**
     * Return name, surname and personal number of all customers
     * @return : ArrayList with all customers information
     */
    public ArrayList<String> getAllCustomers ()
    {
        ArrayList<String> finalList = new ArrayList<>();
        for (ArrayList<String> customer : bank)
        {
            finalList.add(customer.get(0));
        }
        return finalList;
    }

    /**
     * If already customer returns True, otherwise False
     *
     * @param pNo : Personal number of customer to search for
     * @return : True if customer exists, otherwise False
     */
    private boolean isAlreadyCustomer (String pNo)
    {
        boolean result = false;
        for (ArrayList<String> customer : bank)
        {
            String[] customerInfo = customer.get(0).split(" ");
            if (Objects.equals(customerInfo[0], pNo))
                return true;
        }
        return false;
    }

    /**
     * Add new customer to bank database and return true if successful, otherwise false
     * @param name : Name of customer to add
     * @param surname : Surname of customer to add
     * @param pNo : Personal number of customer to add
     * @return : True if customer was added successfully to bank, otherwise false
     */
    public boolean createCustomer (String name, String surname, String pNo)
    {
        ArrayList<String> newCustomer = new ArrayList<>();
        String customerInfo = pNo + SPACE + name + SPACE + surname;
        if (!isAlreadyCustomer(pNo))
        {
            newCustomer.add(customerInfo);
            bank.add(newCustomer);
            return true;
        }
        return false;
    }


    /**
     * Return customer's details and accounts if customer exists, otherwise return null
     * @param pNo : Personal number of customer to search for
     * @return : ArrayList with customer information, first element is customer details, the rest is accounts
     */
    public ArrayList<String> getCustomer (String pNo)
    {
        for (ArrayList<String> customer : bank)
        {
            String[] customerInfo = customer.get(0).split(" ");
            if (Objects.equals(customerInfo[0], pNo))
            {
                ArrayList<String> finalList = new ArrayList<>(customer);
                for (int i = 1; i < finalList.size(); i++)
                {
                    String[] account = finalList.get(i).split(" ");
                    String accountInfo =
                            account[0] + SPACE + convertToSwedishCurrency(Double.parseDouble(account[1])) + SPACE + account[2] + SPACE + account[3] + " %";
                    finalList.set(i, accountInfo);
                }
                return finalList;
            }
        }
        return null;
    }

    /**
     * Change the customer's name and surname if customer exists, otherwise return false
     * @param name : New name of customer to change to (if empty, name will not be changed)
     * @param surname : New surname of customer to change to (if empty, surname will not be changed)
     * @param pNo: Personal number of customer to search and find the customer
     * @return :  True if customer's details were changed successfully, otherwise false
     */
    public boolean changeCustomerName (String name, String surname, String pNo)
    {
        if (name.isEmpty() && surname.isEmpty())
            return false;
        for (ArrayList<String> customer : bank)
        {
            String[] customerInfo = customer.get(0).split(" ");
            if (Objects.equals(customerInfo[0], pNo))
            {
                if (!name.isEmpty())
                    customerInfo[1] = name;
                if (!surname.isEmpty())
                    customerInfo[2] = surname;
                customer.set(0, customerInfo[0] + SPACE + customerInfo[1] + SPACE + customerInfo[2]);
                return true;
            }
        }
        return false;
    }

    /**
     * Create a new savings account for the customer and return its account number if successful, otherwise return -1
     * @param pNo : Personal number of customer to search for and store the new account details in
     * @return : Account number of new savings account if successful, otherwise -1
     */
    public int createSavingsAccount (String pNo)
    {
        for (ArrayList<String> customer : bank)
        {
            String[] customerInfo = customer.get(0).split(" ");
            if (Objects.equals(customerInfo[0], pNo))
            {
                String accountInfo = Integer.toString(accountNumber) + SPACE + "0.00" + SPACE + "Sparkonto" + SPACE + "1.2";
                customer.add(accountInfo);
                accountNumber++;
                return accountNumber - 1;
            }
        }
        return -1;
    }

    /**
     * Find the customer via given pNo and if there is and existance account with the given account number,
     * return the account details, otherwise return null
     * @param pNo : Personal number of customer to search for
     * @param accountId : Account number of account to return details of
     * @return : ArrayList with account details if successful, otherwise null
     */
    public String getAccount (String pNo, int accountId)
    {
        String result = "";
        for (ArrayList<String> customer : bank)
        {
            String[] customerInfo = customer.get(0).split(" ");
            if (Objects.equals(customerInfo[0], pNo))
            {
                for (int i = 1; i < customer.size(); i++)
                {
                    String[] account = customer.get(i).split(" ");
                    if (Objects.equals(account[0], Integer.toString(accountId)))
                    {
                        account[1] = convertToSwedishCurrency(Double.parseDouble(account[1]));
                        return account[0] + SPACE + account[1] + SPACE + account[2] + SPACE + account[3] + " %";
                    }
                }
            }
        }
        return null;
    }

    /**
     * Make a deposit to the account with the given account number for the given customer pNo
     *
     * @param pNo : Personal number of customer to search for
     * @param accountId : Account number of account to deposit to
     * @param amount : Amount to deposit
     * @return : True if deposit was successful, otherwise false
     */
    public boolean deposit (String pNo, int accountId, int amount)
    {
        if (amount <= 0)
            return false;
        for (ArrayList<String> customer : bank)
        {
            String[] customerInfo = customer.get(0).split(" ");
            if (Objects.equals(customerInfo[0], pNo))
            {
                for (int i = 1; i < customer.size(); i++)
                {
                    String[] account = customer.get(i).split(" ");
                    if (Objects.equals(account[0], Integer.toString(accountId)))
                    {
                        BigDecimal balance = new BigDecimal(account[1]);
                        balance = balance.add(new BigDecimal(amount));
                        account[1] = balance.toString();
                        customer.set(i, account[0] + SPACE + account[1] + SPACE + account[2] + SPACE + account[3]);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Make a withdrawal from the account with the given account number for the given customer pNo
     * @param pNo : Personal number of customer to search for
     * @param accountId : Account number of account to withdraw from
     * @param amount  : Amount to withdraw
     * @return : True if withdrawal was successful, otherwise false
     */
    public boolean withdraw (String pNo, int accountId, int amount)
    {
        if (amount <= 0)
            return false;
        for (ArrayList<String> customer : bank)
        {
            String[] customerInfo = customer.get(0).split(" ");
            if (Objects.equals(customerInfo[0], pNo))
            {
                for (int i = 1; i < customer.size(); i++)
                {
                    String[] account = customer.get(i).split(" ");
                    if (Objects.equals(account[0], Integer.toString(accountId)))
                    {
                        BigDecimal balance = new BigDecimal(account[1]);
                        if (balance.compareTo(new BigDecimal(amount)) >= 0)
                        {
                            balance = balance.subtract(new BigDecimal(amount));
                            account[1] = balance.toString();
                            customer.set(i, account[0] + SPACE + account[1] + SPACE + account[2] + SPACE + account[3]);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Close the account with the given account number for the given customer pNo
     * @param pNr : Personal number of customer to search for
     * @param accountId : Account number of account to close
     * @return : A String of accounts details and interest if successful, otherwise null
     */
    public String closeAccount (String pNr, int accountId)
    {
        for (ArrayList<String> customer : bank)
        {
            String[] customerInfo = customer.get(0).split(" ");
            if (Objects.equals(customerInfo[0], pNr))
            {
                for (int i = 1; i < customer.size(); i++)
                {
                    String[] account = customer.get(i).split(" ");
                    if (Objects.equals(account[0], Integer.toString(accountId)))
                    {
                        String[] accountInfo = customer.remove(i).split(" ");

                        String balanceStr = convertToSwedishCurrency(new BigDecimal(accountInfo[1]));
                        String interestStr = calculateInterest(accountInfo);
                        return accountInfo[0] + SPACE + balanceStr + SPACE + accountInfo[2] + SPACE + interestStr;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Delete a customer from the bank with all regarded accounts
     * @param pNo : Personal number of customer to delete
     * @return : ArrayList of all deleted accounts and customer's detail
     */
    public ArrayList<String> deleteCustomer (String pNo)
    {
        for (ArrayList<String> customer : bank)
        {
            String[] customerInfo = customer.get(0).split(" ");
            if (Objects.equals(customerInfo[0], pNo))
            {
                ArrayList<String> accountInfo = new ArrayList<>();
                accountInfo.add(customer.get(0));
                for (int i = 1; i < customer.size(); i++)
                {
                    String[] account = customer.get(i).split(" ");
                    if (account.length == 4)
                    {
                        String balanceStr = convertToSwedishCurrency(Double.parseDouble(account[1]));
                        String interestStr = calculateInterest(account);
                        accountInfo.add(account[0] + SPACE + balanceStr + SPACE + account[2] + SPACE + interestStr);
                    }
                }
                //Delete the whole customer ArrayList from bank
                bank.remove(customer);
                return accountInfo;
            }
        }
        return null;
    }
}
