/**
 * Customer klass hanterar kundens information, såsom för- och efternamn, konto osv.
 *
 * @email habhez-0@student.ltu.se
 * @author Habiballah Hezarehee (habhez-0)
 * @version 1.0
 */
package habhez0;

import java.util.ArrayList;
import java.util.Objects;


public class Customer
{
    private String firstName;
    private String lastName;
    final private String personalNumber;
    private ArrayList<SavingsAccount> savingsAccounts = new ArrayList<>();
    private ArrayList<CreditAccount> creditAccounts = new ArrayList<>();

    public Customer (String personalNumber, String firstName, String lastName)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalNumber = personalNumber;
    }

    public int createNewAccount (double balance, double interestRate, String accountType)
    {
        int accountNumber = 0;
        if (Objects.equals(accountType, "Sparkonto"))
        {
            SavingsAccount savingsAccount = new SavingsAccount(balance, interestRate, accountType);
            savingsAccounts.add(savingsAccount);
            accountNumber = savingsAccount.getCustomerAccountNumber();
        }
        if (Objects.equals(accountType, "Kreditkonto"))
        {
            CreditAccount creditAccount = new CreditAccount(balance, interestRate, accountType, 5000, 7);
            creditAccounts.add(creditAccount);
            accountNumber = creditAccount.getCustomerAccountNumber();
        }
        return accountNumber;
    }

    public ArrayList<String> getAccounts ()
    {
        ArrayList<String> accounts = new ArrayList<>();
        for (CreditAccount creditAccount : creditAccounts)
            accounts.add(creditAccount.toString());
        for (SavingsAccount savingAccount : savingsAccounts)
            accounts.add(savingAccount.toString());
        return accounts;
    }

    public ArrayList<String> getCustomerAccounts ()
    {
        return getAccounts();
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

    public boolean depositIntoAccount (int accountNumber, int amount)
    {
        for (SavingsAccount savingsAccount : savingsAccounts )
        {
            if (savingsAccount.getCustomerAccountNumber() == accountNumber)
                return savingsAccount.deposit(amount);
        }
        for (CreditAccount creditAccount : creditAccounts)
        {
            if (creditAccount.getCustomerAccountNumber() == accountNumber)
                return creditAccount.deposit(amount);
        }

        return false;
    }


    public boolean withdrawFromAccount (int accountNumber, int amount)
    {
        for (SavingsAccount savingsAccount : savingsAccounts )
        {
            if (savingsAccount.getCustomerAccountNumber() == accountNumber)
            {
                return savingsAccount.withdraw(amount);
            }
        }
        for (CreditAccount creditAccount : creditAccounts)
        {
            if (creditAccount.getCustomerAccountNumber() == accountNumber)
            {
                return creditAccount.withdraw(amount);
            }
        }
        return false;
    }

    public String removeAccount (int accountNumber)
    {
        String result= "";
        for (SavingsAccount savingsAccount : savingsAccounts )
        {
            if (savingsAccount.getCustomerAccountNumber() == accountNumber)
            {
                result = savingsAccount.toString();
                savingsAccounts.remove(savingsAccount);
                return result;
            }
        }
        for (CreditAccount creditAccount : creditAccounts)
        {
            if (creditAccount.getCustomerAccountNumber() == accountNumber)
            {
                result = creditAccount.toString();
                creditAccounts.remove(creditAccount);
                return result;
            }
        }
        return null;
    }

    public ArrayList<SavingsAccount> getSavingsAccounts ()
    {
        return savingsAccounts;
    }

    public void setSavingsAccounts (ArrayList<SavingsAccount> savingsAccounts)
    {
        this.savingsAccounts = savingsAccounts;
    }

    public ArrayList<CreditAccount> getCreditAccounts ()
    {
        return creditAccounts;
    }

    public void setCreditAccounts (ArrayList<CreditAccount> creditAccounts)
    {
        this.creditAccounts = creditAccounts;
    }

    @Override
    public String toString ()
    {
        return this.personalNumber + " " + this.firstName + " " + this.lastName;
    }

    public ArrayList<String> getAccountsNumber() {
        ArrayList<String> accounts = new ArrayList<>();
        for (CreditAccount creditAccount : creditAccounts)
            accounts.add(String.valueOf(creditAccount.getCustomerAccountNumber()));
        for (SavingsAccount savingAccount : savingsAccounts)
            accounts.add(String.valueOf(savingAccount.getCustomerAccountNumber()));
        return accounts;
    }
}
