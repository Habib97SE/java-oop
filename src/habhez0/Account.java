/**
 * Den här klassen innehåller metoder som hanterar konton.
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

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.sql.*;

public abstract class Account implements Serializable {
    private double balance;
    private double interestRate;
    public static int accountNumber = 1001;
    private String accountType;
    private int customerAccountNumber;
    private boolean accountIsActive;
    private ArrayList<Transaction> transactions = new ArrayList<>();

    public Account(double balance, double interestRate, String accountType) {
        this.balance = balance;
        this.interestRate = interestRate;
        this.accountType = accountType;
        this.customerAccountNumber = accountNumber;
        accountNumber++;
        this.accountIsActive = true;

    }

    public String getAmountInLocalCurrency(double amount) {
        return NumberFormat.getCurrencyInstance(new Locale("sv", "SE")).format(amount);
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public int getCustomerAccountNumber() {
        return this.customerAccountNumber;
    }

    public void setCustomerAccountNumber(int accountNumber) {
        this.customerAccountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public boolean deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
            this.addTransaction(amount);
            return true;
        }
        return false;
    }

    public abstract boolean withdraw(double amount);

    public String formatDateTime(String dateTimeStr) {
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    public boolean addTransaction(double amount) {
        LocalDateTime date = LocalDateTime.now();
        Transaction transaction = new Transaction(formatDateTime(String.valueOf(date)), amount, this.balance);
        this.transactions.add(transaction);
        return true;
    }

    public ArrayList<Transaction> getTransactions() {
        return this.transactions;
    }

    public double calculateInterest() {
        return (this.balance * this.interestRate) / 100;
    }

    /**
     * check if an account has not been closed
     *
     * @return : if account active returns true else returns false
     */
    public boolean getAccountIsActive() {
        return this.accountIsActive;
    }

    public String getAccountDetails() {
        String finalResult = "";
        finalResult += this.customerAccountNumber + " ";
        finalResult += getAmountInLocalCurrency(this.getBalance()) + " ";
        finalResult += this.accountType + " ";
        finalResult += this.getInterestRate() + " %";
        return finalResult;
    }

    public String cleanFileName(String fileName) {
        if (fileName.endsWith(".txt.txt")) {
            fileName = fileName.substring(0, fileName.length() - 4);
        }
        if (!fileName.endsWith(".txt")) {
            fileName += ".txt";
        }
        return fileName;
    }

    public Boolean writeTransactionsInTextFile(String fileName, String accountHolderName, Path filePath) {
        // create a .txt file with the name of the account holder, account number and account type.
        // add today's date and time into the file.
        // Create table with the following columns: #, Date, Amount, Transaction Type, Balance
        // add the transactions into the table
        try {
            fileName = cleanFileName(fileName);
            Path path = filePath.resolve(fileName);
            File file = new File(path.toString());
            if (!file.exists() && !file.createNewFile()) {
                throw new IOException("File could not be created");
            }

            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String currentDate = date.format(formatter);
            String header = "#\t" + "Datum\t" + "Belopp\t" + "Transaktionstyp\t" + "Balans\n";

            try (Writer fileWriter = new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8)) {
                fileWriter.write("Kontoinnehavare: " + accountHolderName + "\n");
                fileWriter.write("Kontonummer: " + this.getCustomerAccountNumber() + "\n");
                fileWriter.write("Kontotyp: " + this.getAccountType() + "\n");
                fileWriter.write("Datum: " + currentDate + "\n");
                fileWriter.write("Saldo: " + this.getAmountInLocalCurrency(this.getBalance()) + "\n");
                fileWriter.write("\n");
                if (this.accountType.equals("Kreditkonto")) {
                    fileWriter.write("Kreditgräns: " + this.getAmountInLocalCurrency(this.getBalance()) + "\n");
                    fileWriter.write("Ränta: " + this.getInterestRate() + " %\n");
                }
                if (this.accountType.equals("Sparkonto")) {
                    fileWriter.write("Ränta: " + this.getInterestRate() + " %\n");
                }
                fileWriter.write(header);
                fileWriter.write("================================================================\n");
                int count = 1;
                for (Transaction transaction : this.transactions) {
                    String index = count + "\t";
                    String transactionDate = transaction.getDate() + "\t";
                    String amount = this.getAmountInLocalCurrency(transaction.getAmount()) + "\t";
                    String transactionType = transaction.getTransactionType() + "\t";
                    String balance = this.getAmountInLocalCurrency(transaction.getNewBalance()) + "\n";
                    fileWriter.write(index + transactionDate + amount + transactionType + balance);
                    count++;
                }
                fileWriter.write("================================================================\n");
                fileWriter.write("Slut på rapporten.");
                fileWriter.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deactivate an account if it has not been closed before and return the account details
     *
     * @return : if account has not been closed before returns account details else returns null
     * The account details are returned as a string in this format: accountNumber balance accountType earnedInterest
     */
    public String deactivateAccount() {
        if (this.accountIsActive) {
            this.accountIsActive = false;
            return toString();
        }
        return null;
    }

    @Serial
    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        in.defaultReadObject();
    }

    @Override
    public String toString() {
        return this.customerAccountNumber + " " + getAmountInLocalCurrency(this.getBalance()) + " " + this.accountType + " " + this.getAmountInLocalCurrency(this.calculateInterest());
    }
}
