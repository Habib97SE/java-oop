/**
 * Den här klassen hanterar konton.
 *
 * @author Habiballah Hezarehee
 * @
 */
package habhez0;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Account
{
    private int balance;
    private BigDecimal interestRate;
    private int accountNumber;
    private String accountType;


    public Account ()
    {
        balance = 0;
        interestRate = new BigDecimal(0.0);
        accountNumber = 0;
        accountType = "savings";
    }

    public Account (int balance, BigDecimal interestRate, String accountType)
    {
        this.balance = balance;
        this.interestRate = interestRate;
        this.accountType = accountType;
        //Get latest customer from BankLogic class

    }

    public Account (int balance, BigDecimal interestRate, int accountNumber)
    {
        this.balance = balance;
        this.interestRate = interestRate;
        this.accountNumber = accountNumber;
        this.accountType = "savings";
    }

    public int getBalance ()
    {
        return balance;
    }

    public BigDecimal getInterestRate ()
    {
        return interestRate;
    }

    public int getAccountNumber ()
    {
        return accountNumber;
    }

    public String getAccountType ()
    {
        return accountType;
    }

    public void setBalance (int balance)
    {
        this.balance = balance;
    }

    public void setInterestRate (BigDecimal interestRate)
    {
        this.interestRate = interestRate;
    }

    public void setAccountNumber (int accountNumber)
    {
        this.accountNumber = accountNumber;
    }

    public void setAccountType (String accountType)
    {
        this.accountType = accountType;
    }

    public boolean withdraw (int amount)
    {
        if (amount > balance)
        {
            System.out.println("Insufficient funds");
            return false;
        } else
        {
            balance -= amount;
        }
        return true;
    }

    public void deposit (int amount)
    {
        balance += amount;
    }

    public void addInterest ()
    {
        balance += (int) (balance * interestRate.doubleValue());
    }

    public String toString ()
    {
        return "Balance: " + balance + "\n" + "Interest Rate: " + interestRate + "\n" + "Account Number: " + accountNumber + "\n" + "Account Type: " + accountType;
    }

    /**
     * calculate and return Interest: balance * interestRate / 100
     *
     * @return
     */
    public BigDecimal calculateInterest ()
    {
        BigDecimal interest = new BigDecimal(balance);
        interest = interest.multiply(interestRate);
        interest = interest.divide(new BigDecimal(100));
        return interest;
    }


}
