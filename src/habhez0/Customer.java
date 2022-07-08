/**
 * Customer klass hanterar kundens information, såsom för- och efternamn, konto osv.
 *
 * @email habhez-0@student.ltu.se
 * @author Habiballah Hezarehee (habhez-0)
 * @version 1.0
 */
package habhez0;

import habhez0.Account;

import java.util.ArrayList;


public class Customer
{
    private String firstName;
    private String lastName;
    final private String personalNumber;
    ArrayList<Account> accounts = new ArrayList<Account>();

    public Customer (String personalNumber, String firstName, String lastName)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalNumber = personalNumber;
    }

    public int createNewAccount (double balance, double interestRate, String accountType)
    {
        Account account1 = new Account(balance, interestRate, accountType);
        accounts.add(account1);
        return account1.getCustomerAccountNumber();
    }

   public ArrayList<Account> getAccounts(){
      return this.accounts;
   }

    public ArrayList<String> getCustomerAccounts ()
    {
        ArrayList<String> accountsStr = new ArrayList<String>();
        for (int i = 0; i < accounts.size(); i++)
        {
            String account = accounts.get(i).getAccountDetails();
            accountsStr.add(account);
        }
        return accountsStr;
    }

    public String getFirstName ()
    {
        return firstName;
    }

    public void setFirstName (String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName ()
    {
        return lastName;
    }

    public void setLastName (String lastName)
    {
        this.lastName = lastName;
    }

    public String getPersonalNumber ()
    {
        return personalNumber;
    }

    public boolean depositIntoAccount(int accountNumber, int amount)
    {
        for (Account account : accounts)
        {
            if (account.getCustomerAccountNumber() == accountNumber)
            {
                return account.deposit(amount);
            }
        }
        return false;
    }



    public boolean withdrawFromAccount(int accountNumber, int amount)
    {
        for (Account account : accounts)
        {
            if (account.getCustomerAccountNumber() == accountNumber)
            {
                return account.withdraw(amount);
            }
        }
        return false;
    }

    public String removeAccount(int accountNumber)
    {
        for (int i= 0; i < accounts.size(); i++)
        {
            if (accounts.get(i).getCustomerAccountNumber() == accountNumber)
            {
                String result = accounts.get(i).toString();
                accounts.remove(i);
                return result;
            }
        }
        return null;
    }

    @Override
    public String toString ()
    {
        return this.personalNumber + " " + this.firstName + " " + this.lastName;
    }

}
