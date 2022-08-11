/**
 * BankLogic class hanterar bankens logik. T ex hanterar konton, kunder, insättningar och uttag.
 *
 * @email habhez-0@student.ltu.se
 * @author Habiballah Hezarehee (habhez-0)
 * @version 1.0
 */
package habhez0;

import java.util.*;

public class BankLogic
{
    private ArrayList<Customer> allCustomers = new ArrayList<>();

    /**
     * Check if customer with given pNo already exists in our system.
     *
     * @param pNo String which is the personal number that should be checked.
     * @return If customer exists with given pNo returns true else returns false.
     */
    private boolean customerExists (String pNo)
    {
        for (Customer allCustomer : allCustomers)
        {
            if (Objects.equals(allCustomer.getPersonalNumber(), pNo))
            {
                return true;
            }
        }
        return false;
    }

    private Customer findCustomerByPersonalNumber (String pNo)
    {
        for (Customer customer : allCustomers)
        {
            if (Objects.equals(customer.getPersonalNumber(), pNo))
                return customer;
        }
        return null;
    }

    private Account findCustomerAccount (Customer customer, int accountNumber)
    {
        ArrayList<Account> accounts = new ArrayList<Account>();
        for (int i = 0; i < accounts.size(); i++)
        {
            if (accounts.get(i).getCustomerAccountNumber() == accountNumber)
                return accounts.get(i);
        }
        return null;
    }

    /**
     * This method will return all customers personal info (personalNumber firstName lastName)
     * in an arrayList.
     *
     * @return : An arrayList of all customers personal info
     */
    public ArrayList<String> getAllCustomers ()
    {
        ArrayList<String> allCustomersStr = new ArrayList<String>();
        for (Customer allCustomer : allCustomers)
            allCustomersStr.add(allCustomer.toString());

        return allCustomersStr;
    }

    /**
     * Create a new customer with given info and add it to allCustomers arrayList,
     *
     * @param name    String : the first name of the customer.
     * @param surname String : The last name of the customer.
     * @param pNo     String : The personal number of the customer.
     * @return if customer is created successfully returns true otherwise returns false.
     */
    public boolean createCustomer (String name, String surname, String pNo)
    {
        if (!customerExists(pNo))
        {
            Customer customer1 = new Customer(pNo, name, surname);
            allCustomers.add(customer1);
            return true;
        }
        return false;
    }

    /**
     * get a customer's info (personal details and accounts)
     *
     * @param pNo String : personal number of chosen customer
     * @return : an arraylist of all data we have for that specidic customer
     */
    public ArrayList<String> getCustomer (String pNo)
    {
        ArrayList<String> customerDetails = new ArrayList<String>();
        for (Customer allCustomer : allCustomers)
        {
            if (Objects.equals(allCustomer.getPersonalNumber(), pNo))
            {
                customerDetails.add(allCustomer.toString());
                for (CreditAccount creditAccount : allCustomer.getCreditAccounts())
                {
                    if (creditAccount.getAccountIsActive())
                        customerDetails.add(creditAccount.getAccountDetails());
                }
                for (SavingsAccount savingsAccount : allCustomer.getSavingsAccounts())
                {
                    if (savingsAccount.getAccountIsActive())
                        customerDetails.add(savingsAccount.getAccountDetails());
                }
                return customerDetails;
            }
        }
        return null;
    }

    public boolean changeCustomerName (String name, String surname, String pNo)
    {
        // By default, result is false because if the name and surname are empty and if
        // pNo is not found then we return false
        boolean result = false;
        if (!customerExists(pNo))
            return result;
        for (Customer allCustomer : allCustomers)
        {
            // if a customer with given pNo found
            if (Objects.equals(allCustomer.getPersonalNumber(), pNo))
            {
                // check if name is not empty
                if (!Objects.equals(name, ""))
                { // if not empty change the name and change the result var
                    allCustomer.setFirstName(name);
                    result = true;
                }
                if (!Objects.equals(surname, ""))
                {
                    allCustomer.setLastName(surname);
                    result = true;
                }
            }
        }
        return result;
    }

    public int createSavingsAccount (String pNo)
    {
        if (!customerExists(pNo))
            return -1;
        for (Customer allCustomer : allCustomers)
        {
            if (Objects.equals(allCustomer.getPersonalNumber(), pNo))
                return allCustomer.createNewAccount(0.0, 1.2, "Sparkonto");
        }
        return -1;
    }

    public int createCreditAccount (String pNo)
    {
        if (!customerExists(pNo))
            return -1;
        for (Customer customer : allCustomers)
        {
            if (Objects.equals(customer.getPersonalNumber(), pNo))
            {
                return customer.createNewAccount(0.0, 0.5, "Kreditkonto");
            }
        }
        return -1;
    }

    public ArrayList<String> getTransactions (String pNo, int accountId)
    {
        ArrayList<String> finalResult = new ArrayList<>();
        if (!customerExists(pNo))
            return null;
        boolean accountBelongsToCustomer = false;
        for (Customer customer : allCustomers)
        {
            if (Objects.equals(customer.getPersonalNumber(), pNo))
            {
                for (CreditAccount creditAccount : customer.getCreditAccounts())
                {
                    if (creditAccount.getCustomerAccountNumber() == accountId)
                    {
                        finalResult = creditAccount.getTransactions();
                        accountBelongsToCustomer = true;
                    }

                }
                for (SavingsAccount savingsAccount : customer.getSavingsAccounts())
                {
                    if (savingsAccount.getCustomerAccountNumber() == accountId)
                    {
                        finalResult = savingsAccount.getTransactions();
                        accountBelongsToCustomer = true;
                    }
                }
            }
        }
        if (!accountBelongsToCustomer)
        {
            return null;
        }
        return finalResult;
    }

    public String getAccount (String pNo, int accountId)
    {
        if (!customerExists(pNo))
            return null;
        for (Customer allCustomer : allCustomers)
        {
            if (Objects.equals(allCustomer.getPersonalNumber(), pNo))
            {
                for (CreditAccount creditAccount : allCustomer.getCreditAccounts())
                {
                    if (creditAccount.getCustomerAccountNumber() == accountId)
                    {
                        return creditAccount.getAccountDetails();
                    }
                }
                for (SavingsAccount savingsAccount : allCustomer.getSavingsAccounts())
                {
                    if (savingsAccount.getCustomerAccountNumber() == accountId)
                    {
                        return savingsAccount.getAccountDetails();
                    }
                }
            }
        }
        return null;
    }

    public boolean deposit (String pNo, int accountId, int amount)
    {
        if (!customerExists(pNo))
            return false;
        for (Customer allCustomer : allCustomers)
        {
            if (Objects.equals(allCustomer.getPersonalNumber(), pNo))
                return allCustomer.depositIntoAccount(accountId, amount);
        }
        return false;
    }

    public boolean withdraw (String pNo, int accountId, int amount)
    {
        if (!customerExists(pNo))
            return false;
        for (Customer allCustomer : allCustomers)
        {
            if (Objects.equals(allCustomer.getPersonalNumber(), pNo))
            {
                return allCustomer.withdrawFromAccount(accountId, amount);
            }
        }
        return false;
    }

    public String closeAccount (String pNr, int accountId)
    {
        if (!customerExists(pNr))
            return null;

        for (Customer allCustomer : allCustomers)
        {
            if (Objects.equals(allCustomer.getPersonalNumber(), pNr))
            {
                for (SavingsAccount savingsAccount : allCustomer.getSavingsAccounts())
                {
                    if (savingsAccount.getCustomerAccountNumber() == accountId)
                        return savingsAccount.deactivateAccount();
                }
                for (CreditAccount creditAccount : allCustomer.getCreditAccounts())
                {
                    if (creditAccount.getCustomerAccountNumber() == accountId)
                        return creditAccount.deactivateAccount();
                }
            }
        }
        return null;
    }

    public ArrayList<String> deleteCustomer (String pNo)
    {
        if (!customerExists(pNo))
            return null;
        ArrayList<String> removedCustomer = new ArrayList<String>();
        Customer customer = findCustomerByPersonalNumber(pNo);
        if (customer != null)
        {
            removedCustomer.add(customer.toString());
            for (Customer customer1 : allCustomers)
            {
                if (Objects.equals(customer1.getPersonalNumber(), pNo))
                {
                    for (CreditAccount creditAccount : customer1.getCreditAccounts())
                    {
                        if (creditAccount.getAccountIsActive())
                            removedCustomer.add(creditAccount.toString());
                    }
                    for (SavingsAccount savingsAccount : customer1.getSavingsAccounts())
                    {
                        if (savingsAccount.getAccountIsActive())
                            removedCustomer.add(savingsAccount.toString());
                    }
                }
            }
            allCustomers.remove(customer);
        }
        return removedCustomer;
    }

}
