/**
 * Den här klassen innehåller metoder som hanterar konton.
 *
 * @email habhez-0@student.ltu.se
 * @author Habiballah Hezarehee (habhez-0)
 * @version 1.0
 */
package habhez0;

import java.text.NumberFormat;
import java.util.Locale;

public class Account
{
    private double balance;
    private double interestRate;
    public static int accountNumber = 1001;
    private String accountType;
    private int customerAccountNumber;
    private boolean accountIsActive;

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
            return true;
        }
        return false;
    }

    public boolean withdraw (double amount)
    {
        if (amount > 0 && amount <= this.balance)
        {
            this.balance -= amount;
            return true;
        }
        return false;
    }

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

    public String deactivateAccount ()
    {
        if (this.accountIsActive)
        {
            this.accountIsActive = false;
            return toString();
        }
        return null;
    }

    public String getAccountDetails ()
    {
        return this.customerAccountNumber + " " + getAmountInLocalCurrency(this.getBalance()) + " " + this.accountType + " " + this.getInterestRate() + " %";
    }

    @Override
    public String toString ()
    {
        return this.customerAccountNumber + " " + getAmountInLocalCurrency(this.getBalance()) + " " + this.accountType + " " + this.getAmountInLocalCurrency(this.calculateInterest());
    }
}
