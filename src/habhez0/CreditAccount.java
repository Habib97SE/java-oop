package habhez0;

public class CreditAccount extends Account
{
    private double creditLimit;
    private double debtRate;
    public CreditAccount (double balance, double interestRate, String accountType, double creditLimit, double debtRate)
    {
        // debtRate is always 7% and creditLimit = 5000SEK
        super(balance, interestRate, accountType);
        this.creditLimit = creditLimit;
        this.debtRate = debtRate;
    }

    public double getCreditLimit ()
    {
        return creditLimit;
    }

    public void setCreditLimit (double creditLimit)
    {
        this.creditLimit = creditLimit;
    }

    public double getDebtRate ()
    {
        return debtRate;
    }

    public void setDebtRate (double debtRate)
    {
        this.debtRate = debtRate;
    }

    public boolean withdraw(double amount)
    {
        if (amount <= 0)
            return true;
        if (this.getBalance() - amount > -5000)
        {
            this.setBalance(this.getBalance() - amount);
            amount -= amount * 2;
            this.addTransaction(amount);
            return true;
        }
        return false;
    }

    @Override
    public String toString()
    {
        double interestAmount;
        if (this.getBalance() < 0)
        {
            interestAmount = this.getBalance() * 1.07;
        } else {
            interestAmount = this.getBalance() * 1.005;
        }
        interestAmount -= this.getBalance();
        if (interestAmount != 0.00)
            interestAmount = interestAmount * -1;

        String finalResult = "";
        finalResult += this.getCustomerAccountNumber() + " ";
        finalResult += getAmountInLocalCurrency(this.getBalance()) + " ";
        finalResult += this.getAccountType() + " ";
        finalResult += this.getAmountInLocalCurrency(interestAmount);
        return finalResult;
    }

    @Override
    public String getAccountDetails ()
    {

        String finalResult = "";
        finalResult += this.getCustomerAccountNumber() + " ";
        finalResult += getAmountInLocalCurrency(this.getBalance()) + " ";
        finalResult += this.getAccountType() + " ";
        if (this.getBalance() < 0)
            finalResult += this.getDebtRate() + " %";
        else
            finalResult += this.getInterestRate() + " %";
        return finalResult;
    }
}
