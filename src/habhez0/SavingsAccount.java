package habhez0;

/**
 * @author: Habib Hezarehee (habhez-0)
 * @email: habhez-0@student.ltu.se
 */

public class SavingsAccount extends Account
{
    private boolean withdrawLimitReached;

    public SavingsAccount (double balance, double interestRate, String accountType)
    {
        super(balance, interestRate, accountType);
        this.withdrawLimitReached = false;
    }

    public boolean getWithdrawLimitReached ()
    {
        return this.withdrawLimitReached;
    }

    public void setWithdrawLimitReached (boolean withdrawLimitReached)
    {
        this.withdrawLimitReached =
                withdrawLimitReached;
    }

    public boolean withdraw (double amount)
    {
        if (amount <= 0)
            return false;
        if (withdrawLimitReached)
            amount += amount * 0.02;
        if (this.getBalance() > amount)
        {
            this.setWithdrawLimitReached(true);
            this.setBalance(this.getBalance() - amount);
            amount -= amount * 2;
            this.addTransaction(amount);
            return true;
        }
        return false;
    }
}
