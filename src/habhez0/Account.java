/**
 * Den här klassen innehåller metoder som hanterar konton.
 *
 * @email habhez-0@student.ltu.se
 * @author Habiballah Hezarehee (habhez-0)
 * @version 1.0
 */
package habhez0;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public abstract class Account
{
    private double balance;
    private double interestRate;
    public static int accountNumber = 1001;
    private String accountType;
    private int customerAccountNumber;
    private boolean accountIsActive;
    private ArrayList<Transaction> transactions = new ArrayList<>();

    public Account (double balance, double interestRate, String accountType)
    {
        this.balance = balance;
        this.interestRate = interestRate;
        this.accountType = accountType;
        this.customerAccountNumber = accountNumber;
        accountNumber++;
        this.accountIsActive = true;
    }

    public String getAmountInLocalCurrency (double amount)
    {
        return NumberFormat.getCurrencyInstance(new Locale("sv", "SE")).format(amount);
    }

    public double getBalance ()
    {
        return balance;
    }

    public void setBalance (double balance)
    {
        this.balance = balance;
    }

    public double getInterestRate ()
    {
        return interestRate;
    }

    public void setInterestRate (double interestRate)
    {
        this.interestRate = interestRate;
    }

    public int getCustomerAccountNumber ()
    {
        return this.customerAccountNumber;
    }

    public void setCustomerAccountNumber (int accountNumber)
    {
        this.customerAccountNumber = accountNumber;
    }

    public String getAccountType ()
    {
        return accountType;
    }

    public void setAccountType (String accountType)
    {
        this.accountType = accountType;
    }

    public boolean deposit (double amount)
    {
        if (amount > 0)
        {
            this.balance += amount;
            this.addTransaction(amount);
            return true;
        }
        return false;
    }

    public abstract boolean withdraw (double amount);

    public boolean addTransaction(double amount)
    {
        LocalDateTime date = LocalDateTime.now();
        Transaction transaction = new Transaction(date, amount, this.balance);
        this.transactions.add(transaction);
        return true;
    }

    public ArrayList<Transaction> getTransactions() { return this.transactions; }

    public double calculateInterest ()
    {
        return (this.balance * this.interestRate) / 100;
    }

    /**
     * check if an account has not been closed
     *
     * @return : if account active returns true else returns false
     */
    public boolean getAccountIsActive ()
    {
        return this.accountIsActive;
    }

    public String getAccountDetails ()
    {
        String finalResult = "";
        finalResult += this.customerAccountNumber + " ";
        finalResult += getAmountInLocalCurrency(this.getBalance()) + " ";
        finalResult += this.accountType + " ";
        finalResult += this.getInterestRate() + " %";
        return finalResult;
    }

    /**
     * Deactivate an account if it has not been closed before and return the account details
     * @return : if account has not been closed before returns account details else returns null
     * The account details are returned as a string in this format: accountNumber balance accountType earnedInterest
     */
    public String deactivateAccount ()
    {
        if (this.accountIsActive)
        {
            this.accountIsActive = false;
            return toString();
        }
        return null;
    }

    @Override
    public String toString ()
    {
        return this.customerAccountNumber + " " + getAmountInLocalCurrency(this.getBalance()) + " " + this.accountType + " " + this.getAmountInLocalCurrency(this.calculateInterest());
    }
}
