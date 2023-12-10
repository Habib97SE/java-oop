/**
 * BankLogic class hanterar bankens logik. T ex hanterar konton, kunder, insättningar och uttag.
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

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.*;
// import LocalDateTime
import java.time.LocalDateTime;

public class BankLogic {
    private ArrayList<Customer> allCustomers = new ArrayList<>();

    /**
     * Check if customer with given pNo already exists in our system.
     *
     * @param pNo String which is the personal number that should be checked.
     * @return If customer exists with given pNo returns true else returns false.
     */
    private boolean customerExists(String pNo) {
        for (Customer allCustomer : allCustomers) {
            if (Objects.equals(allCustomer.getPersonalNumber(), pNo)) {
                return true;
            }
        }
        return false;
    }

    public Customer findCustomerByPersonalNumber(String pNo) {
        for (Customer customer : allCustomers) {
            if (Objects.equals(customer.getPersonalNumber(), pNo))
                return customer;
        }
        return null;
    }

    private Account findCustomerAccount(Customer customer, int accountNumber) {
        ArrayList<Account> accounts = new ArrayList<Account>();
        for (int i = 0; i < accounts.size(); i++) {
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
    public ArrayList<Customer> getAllCustomers() {
        return allCustomers;
    }

    /**
     * Create a new customer with given info and add it to allCustomers arrayList,
     *
     * @param name    String : the first name of the customer.
     * @param surname String : The last name of the customer.
     * @param pNo     String : The personal number of the customer.
     * @return if customer is created successfully returns true otherwise returns false.
     */
    public boolean createCustomer(String name, String surname, String pNo) {
        if (!customerExists(pNo)) {
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
    public ArrayList<String> getCustomer(String pNo) {
        ArrayList<String> customerDetails = new ArrayList<String>();
        for (Customer allCustomer : allCustomers) {
            if (Objects.equals(allCustomer.getPersonalNumber(), pNo)) {
                customerDetails.add(allCustomer.toString());
                for (CreditAccount creditAccount : allCustomer.getCreditAccounts()) {
                    if (creditAccount.getAccountIsActive())
                        customerDetails.add(creditAccount.getAccountDetails());
                }
                for (SavingsAccount savingsAccount : allCustomer.getSavingsAccounts()) {
                    if (savingsAccount.getAccountIsActive())
                        customerDetails.add(savingsAccount.getAccountDetails());
                }
                return customerDetails;
            }
        }
        return null;
    }

    public boolean changeCustomerName(String name, String surname, String pNo) {
        // By default, result is false because if the name and surname are empty and if
        // pNo is not found then we return false
        boolean result = false;
        if (!customerExists(pNo))
            return result;
        for (Customer allCustomer : allCustomers) {
            // if a customer with given pNo found
            if (Objects.equals(allCustomer.getPersonalNumber(), pNo)) {
                // check if name is not empty
                if (!Objects.equals(name, "")) { // if not empty change the name and change the result var
                    allCustomer.setFirstName(name);
                    result = true;
                }
                if (!Objects.equals(surname, "")) {
                    allCustomer.setLastName(surname);
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * Create a new savings account for a customer with provided pNo.
     *
     * @param pNo String : personal number of the customer
     * @return : if customer exists returns the account number of the new account otherwise returns -1
     */
    public int createSavingsAccount(String pNo) {
        if (!customerExists(pNo))
            return -1;
        for (Customer allCustomer : allCustomers) {
            if (Objects.equals(allCustomer.getPersonalNumber(), pNo))
                return allCustomer.createNewAccount(0.0, 1.2, "Sparkonto");
        }
        return -1;
    }

    public int createCreditAccount(String pNo) {
        if (!customerExists(pNo))
            return -1;
        for (Customer customer : allCustomers) {
            if (Objects.equals(customer.getPersonalNumber(), pNo)) {
                return customer.createNewAccount(0.0, 0.5, "Kreditkonto");
            }
        }
        return -1;
    }

    public ArrayList<Transaction> getTransactions(String pNo, int accountId) {
        ArrayList<Transaction> finalResult = new ArrayList<>();
        if (!customerExists(pNo))
            return null;
        boolean accountBelongsToCustomer = false;
        for (Customer customer : allCustomers) {
            if (Objects.equals(customer.getPersonalNumber(), pNo)) {
                for (CreditAccount creditAccount : customer.getCreditAccounts()) {
                    if (creditAccount.getCustomerAccountNumber() == accountId) {
                        finalResult = creditAccount.getTransactions();
                        accountBelongsToCustomer = true;
                    }

                }
                for (SavingsAccount savingsAccount : customer.getSavingsAccounts()) {
                    if (savingsAccount.getCustomerAccountNumber() == accountId) {
                        finalResult = savingsAccount.getTransactions();
                        accountBelongsToCustomer = true;
                    }
                }
            }
        }
        if (!accountBelongsToCustomer) {
            return null;
        }
        return finalResult;
    }

    public String getAccount(String pNo, int accountId) {
        if (!customerExists(pNo))
            return null;
        for (Customer allCustomer : allCustomers) {
            if (Objects.equals(allCustomer.getPersonalNumber(), pNo)) {
                for (CreditAccount creditAccount : allCustomer.getCreditAccounts()) {
                    if (creditAccount.getCustomerAccountNumber() == accountId) {
                        return creditAccount.getAccountDetails();
                    }
                }
                for (SavingsAccount savingsAccount : allCustomer.getSavingsAccounts()) {
                    if (savingsAccount.getCustomerAccountNumber() == accountId) {
                        return savingsAccount.getAccountDetails();
                    }
                }
            }
        }
        return null;
    }

    public String checkAccountType(String pNo, int accountId) {
        if (!customerExists(pNo))
            return null;
        for (Customer allCustomer : allCustomers) {
            if (Objects.equals(allCustomer.getPersonalNumber(), pNo)) {
                for (CreditAccount creditAccount : allCustomer.getCreditAccounts()) {
                    if (creditAccount.getCustomerAccountNumber() == accountId) {
                        return "Kreditkonto";
                    }
                }
                for (SavingsAccount savingsAccount : allCustomer.getSavingsAccounts()) {
                    if (savingsAccount.getCustomerAccountNumber() == accountId) {
                        return "Sparkonto";
                    }
                }
            }
        }
        return null;
    }

    public SavingsAccount getSavingAccount(String pNo, int accountId) {
        if (!customerExists(pNo))
            return null;
        for (Customer allCustomer : allCustomers) {
            if (Objects.equals(allCustomer.getPersonalNumber(), pNo)) {
                for (SavingsAccount savingsAccount : allCustomer.getSavingsAccounts()) {
                    if (savingsAccount.getCustomerAccountNumber() == accountId) {
                        return savingsAccount;
                    }
                }
            }
        }
        return null;
    }

    public CreditAccount getCreditAccount(String pNo, int accountId) {
        if (!customerExists(pNo))
            return null;
        for (Customer allCustomer : allCustomers) {
            if (Objects.equals(allCustomer.getPersonalNumber(), pNo)) {
                for (CreditAccount creditAccount : allCustomer.getCreditAccounts()) {
                    if (creditAccount.getCustomerAccountNumber() == accountId) {
                        return creditAccount;
                    }
                }
            }
        }
        return null;
    }

    public boolean deposit(String pNo, int accountId, int amount) {
        if (!customerExists(pNo))
            return false;
        for (Customer allCustomer : allCustomers) {
            if (Objects.equals(allCustomer.getPersonalNumber(), pNo))
                return allCustomer.depositIntoAccount(accountId, amount);
        }
        return false;
    }

    public boolean withdraw(String pNo, int accountId, int amount) {
        if (!customerExists(pNo))
            return false;
        for (Customer allCustomer : allCustomers) {
            if (Objects.equals(allCustomer.getPersonalNumber(), pNo)) {
                return allCustomer.withdrawFromAccount(accountId, amount);
            }
        }
        return false;
    }

    /**
     * Closes an account based on the provided personal number and account number. If the account is a savings account,
     * the remaining balance is transferred to the customer's credit account. If the account is a credit account, the
     * remaining balance is transferred to the customer's savings account.
     *
     * @param pNr       - A String representing the unique personal number of the customer.
     * @param accountId - An int representing the unique account number of the account to be closed.
     * @return A String containing details of the closed account. If the provided personal number does not match any existing customer,
     * or if the provided account number does not match any existing account, or if the account could not be closed,
     * null is returned.
     * The returned string pattern is as follows: "accountNumber balance accountType earnedInterest"
     */
    public String closeAccount(String pNr, int accountId) {
        if (!customerExists(pNr))
            return null;

        for (Customer allCustomer : allCustomers) {
            if (Objects.equals(allCustomer.getPersonalNumber(), pNr)) {
                for (SavingsAccount savingsAccount : allCustomer.getSavingsAccounts()) {
                    if (savingsAccount.getCustomerAccountNumber() == accountId)
                        return savingsAccount.deactivateAccount();
                }
                for (CreditAccount creditAccount : allCustomer.getCreditAccounts()) {
                    if (creditAccount.getCustomerAccountNumber() == accountId)
                        return creditAccount.deactivateAccount();
                }
            }
        }
        return null;
    }

    /**
     * Removes a customer and all associated accounts from the banking system based on the provided personal number.
     *
     * @param pNo - A String representing the unique personal number of the customer to be deleted.
     * @return An ArrayList containing details of the deleted customer and their accounts.
     * If the provided personal number does not match any existing customer or if the customer could not be deleted,
     * null is returned.
     */
    public ArrayList<String> deleteCustomer(String pNo) {
        if (!customerExists(pNo))
            return null;
        ArrayList<String> removedCustomer = new ArrayList<String>();
        Customer customer = findCustomerByPersonalNumber(pNo);

        removedCustomer.add(customer.toString());
        for (Customer existingCustomer : allCustomers) {
            if (Objects.equals(existingCustomer.getPersonalNumber(), pNo)) {
                for (CreditAccount creditAccount : existingCustomer.getCreditAccounts()) {
                    if (creditAccount.getAccountIsActive()) {
                        removedCustomer.add(creditAccount.toString());
                    }
                }
                for (SavingsAccount savingsAccount : existingCustomer.getSavingsAccounts()) {
                    if (savingsAccount.getAccountIsActive()) {
                        removedCustomer.add(savingsAccount.toString());
                    }
                }
                break; // Ensure that once the correct customer is processed, exit the loop
            }
        }
        allCustomers.remove(customer);

        return removedCustomer;
    }

    public boolean exportCustomers(String filePath, JFrame frame) {
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(allCustomers);
            oos.close();
            fos.close();
            return true;
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(frame, "Filen du försöker exportera till finns inte.", "Fel", JOptionPane.ERROR_MESSAGE);
            ioe.printStackTrace();
            return false;
        }
    }

    public void updateAccountNumber() {
        // loop through the allCustomers
        int accountNumber = 1000;
        for (Customer customer : allCustomers) {
            for (SavingsAccount savingsAccount : customer.getSavingsAccounts()) {
                accountNumber = savingsAccount.getCustomerAccountNumber();
            }
            for (CreditAccount creditAccount : customer.getCreditAccounts()) {
                accountNumber = creditAccount.getCustomerAccountNumber();
            }
        }
        accountNumber++;
        Account.accountNumber = accountNumber;
    }


    public Boolean importCustomers(String fileName, JFrame frame) throws IOException, ClassNotFoundException {
        Path path = Paths.get("src", "habhez0_files", fileName).normalize();
        String fullPath = path.toString();
        try {
            FileInputStream fis = new FileInputStream(fullPath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            allCustomers = (ArrayList<Customer>) ois.readObject();
            ois.close();
            fis.close();
            updateAccountNumber();
            return true;
        } catch (FileNotFoundException fnfe) {
            JOptionPane.showMessageDialog(frame, "Filen du försöker importera finns inte.", "Fel", JOptionPane.ERROR_MESSAGE);
            fnfe.printStackTrace();
            return false;
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(frame, "Filen du försöker importera finns inte.", "Fel", JOptionPane.ERROR_MESSAGE);
            ioe.printStackTrace();
            return false;
        } catch (ClassNotFoundException c) {
            JOptionPane.showMessageDialog(frame, "Filen du försöker importera är inte kompatibel med programmet.", "Fel", JOptionPane.ERROR_MESSAGE);
            c.printStackTrace();
            return false;
        }
    }

    public boolean writeToTextFile(Integer accountNumber) {
        String fileName = accountNumber + ".txt";
        for (Customer customer : allCustomers) {
            for (SavingsAccount savingsAccount : customer.getSavingsAccounts()) {
                if (savingsAccount.getCustomerAccountNumber() == accountNumber) {
                    return savingsAccount.writeTransactionsInTextFile(fileName, customer.getFirstName() + " " + customer.getLastName());
                }
            }
            for (CreditAccount creditAccount : customer.getCreditAccounts()) {
                if (creditAccount.getCustomerAccountNumber() == accountNumber) {
                    return creditAccount.writeTransactionsInTextFile(fileName, customer.getFirstName() + " " + customer.getLastName());
                }
            }
        }
        return false;
    }

    public void loadTransactions(File file, JFrame frame) {
        try {
            String fullString = "";
            Path path = Paths.get("src", "habhez0_files", file.getName()).normalize();
            String fullPath = path.toString();
            BufferedReader br = Files.newBufferedReader(Paths.get(fullPath));
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(" ");
                String pNo = values[0];
                int accountNumber = Integer.parseInt(values[1]);
                String accountType = values[2];
                int amount = Integer.parseInt(values[3]);
                String transactionType = values[4];
                String date = values[5];
                String time = values[6];
                LocalDateTime dateTime = LocalDateTime.parse(date + "T" + time);
            }
            br.close();
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(frame, "Filen du försöker importera finns inte.", "Fel", JOptionPane.ERROR_MESSAGE);
            ioe.printStackTrace();
        }
    }
}
