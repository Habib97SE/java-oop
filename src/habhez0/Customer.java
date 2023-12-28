/**
 * Customer klass hanterar kundens information, såsom för- och efternamn, konto osv.
 *
 * @email habhez-0@student.ltu.se
 * @author Habiballah Hezarehee (habhez-0)
 * @version 1.0
 */
package habhez0;

/**
 * @author: Habib Hezarehee (habhez-0)
 * @email: habhez-0@student.ltu.se
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;


public class Customer implements Serializable {
    private String firstName;
    private String lastName;
    final private String personalNumber;
    private ArrayList<SavingsAccount> savingsAccounts = new ArrayList<>();
    private ArrayList<CreditAccount> creditAccounts = new ArrayList<>();

    public Customer(String personalNumber, String firstName, String lastName) {
        this.firstName = firstName.toLowerCase();
        this.lastName = lastName.toLowerCase();
        this.personalNumber = personalNumber;
    }

    public int createNewAccount(double balance, double interestRate, String accountType) {
        int accountNumber = 0;
        if (Objects.equals(accountType, "Sparkonto")) {
            SavingsAccount savingsAccount = new SavingsAccount(balance, interestRate, accountType);
            savingsAccounts.add(savingsAccount);
            accountNumber = savingsAccount.getCustomerAccountNumber();
        }
        if (Objects.equals(accountType, "Kreditkonto")) {
            CreditAccount creditAccount = new CreditAccount(balance, interestRate, accountType, 5000, 7);
            creditAccounts.add(creditAccount);
            accountNumber = creditAccount.getCustomerAccountNumber();
        }
        return accountNumber;
    }

    public ArrayList<Account> getAccounts() {
        ArrayList<Account> accounts = new ArrayList<>();
        accounts.addAll(savingsAccounts);
        accounts.addAll(creditAccounts);
        return new ArrayList<>(accounts);
    }

    public String getFirstName() {
        return firstName.substring(0, 1).toUpperCase() + firstName.substring(1);
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName.toLowerCase();
    }

    public String getLastName() {
        return lastName.substring(0, 1).toUpperCase() + lastName.substring(1);
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.toLowerCase();
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public boolean depositIntoAccount(int accountNumber, int amount) {
        for (SavingsAccount savingsAccount : savingsAccounts) {
            if (savingsAccount.getCustomerAccountNumber() == accountNumber)
                return savingsAccount.deposit(amount);
        }
        for (CreditAccount creditAccount : creditAccounts) {
            if (creditAccount.getCustomerAccountNumber() == accountNumber)
                return creditAccount.deposit(amount);
        }

        return false;
    }


    public boolean withdrawFromAccount(int accountNumber, int amount) {
        for (SavingsAccount savingsAccount : savingsAccounts) {
            if (savingsAccount.getCustomerAccountNumber() == accountNumber) {
                return savingsAccount.withdraw(amount);
            }
        }
        for (CreditAccount creditAccount : creditAccounts) {
            if (creditAccount.getCustomerAccountNumber() == accountNumber) {
                return creditAccount.withdraw(amount);
            }
        }
        return false;
    }


    public ArrayList<SavingsAccount> getSavingsAccounts() {
        return savingsAccounts;
    }

    public void setSavingsAccounts(ArrayList<SavingsAccount> savingsAccounts) {
        this.savingsAccounts = savingsAccounts;
    }

    public ArrayList<CreditAccount> getCreditAccounts() {
        return creditAccounts;
    }

    public void setCreditAccounts(ArrayList<CreditAccount> creditAccounts) {
        this.creditAccounts = creditAccounts;
    }


    @Override
    public String toString() {
        return "[ " + this.getPersonalNumber() + " " + this.getFirstName() + " " + this.getLastName() +
                getAccounts() + " ]";
    }

    public ArrayList<String> getAccountsNumber() {
        ArrayList<String> accounts = new ArrayList<>();
        for (CreditAccount creditAccount : creditAccounts) {
            if (!creditAccount.getAccountIsActive())
                continue;
            accounts.add(String.valueOf(creditAccount.getCustomerAccountNumber()));
        }
        for (SavingsAccount savingAccount : savingsAccounts) {
            if (!savingAccount.getAccountIsActive())
                continue;
            accounts.add(String.valueOf(savingAccount.getCustomerAccountNumber()));
        }
        return accounts;
    }

    public boolean hasAccount(int accountNumber) {
        for (Account account : getAccounts()) {
            if (account.getCustomerAccountNumber() == accountNumber)
                return true;
        }
        return false;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
}
