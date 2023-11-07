package habhez0;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class GUI {
    private String title;
    private int width;
    private int height;
    private JMenuBar menuBar;
    private JFrame frame;
    private boolean isUserLoggedIn = false;
    private String socialSecurityNumber;
    BankLogic bankLogic = new BankLogic();

    public GUI(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
        menuBar = new JMenuBar();
    }

    public void start() {
        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addFileMenu();
        addAccountMenu();
        addCustomerMenu();
        addHelpMenu();
        frame.setJMenuBar(menuBar);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void addNewMenu(String name, String[] items) {
        JMenu menu = new JMenu(name);
        for (String item : items) {
            addNewMenuItem(menu, item);
        }
        menuBar.add(menu);
    }

    private JMenuItem addNewMenuItem(JMenu menu, String name) {
        JMenuItem menuItem = new JMenuItem(name);
        menu.add(menuItem);
        return menuItem;
    }

    public void addFileMenu() {
        // add menu items
        JMenu menu = new JMenu("Arkiv");
        JMenuItem exit = new JMenuItem("Avsluta");

        // add menu items functionalities (action listeners)
        exit.addActionListener(e -> System.exit(0));

        menu.add(exit);
        menuBar.add(menu);
    }

    public void addHelpMenu() {
        JMenu helpMenu = new JMenu("Hjälp");
        JMenuItem about = new JMenuItem("Om");
        JMenuItem help = new JMenuItem("Hjälp");

        about.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Banken AB");
        });

        help.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Hjälp");
        });

        helpMenu.add(about);
        helpMenu.add(help);

        menuBar.add(helpMenu);
    }

    private Account getAccount(int accountId) {
        String accountType = bankLogic.checkAccountType(socialSecurityNumber, accountId);
        if (Objects.equals(accountType, "Sparkonto")) {
            return bankLogic.getSavingAccount(socialSecurityNumber, accountId);
        } else if (Objects.equals(accountType, "Kreditkonto")) {
            return bankLogic.getCreditAccount(socialSecurityNumber, accountId);
        } else {
            return null;
        }
    }

    public void addAccountMenu() {
        JMenu menu = new JMenu("Konto");
        JMenuItem createAccountItem = new JMenuItem("Skapa konto");
        JMenuItem viewAccountDetails = new JMenuItem("Visa kontodetaljer");
        JMenuItem deposit = new JMenuItem("Insättning");
        JMenuItem withdraw = new JMenuItem("Uttag");
        JMenuItem getTransactions = new JMenuItem("Visa transaktioner");
        JMenuItem closeAccount = new JMenuItem("Stäng konto");

        /* action listener to createSavingAccount  */
        createAccountItem.addActionListener(e -> {
            createAccount();
        });

        /* action listener to viewAccountDetails  */
        viewAccountDetails.addActionListener(e -> {
            viewAccountsDetails();
        });

        /* action listener to deposit  */
        deposit.addActionListener(e -> {
            createDepositView();
        });

        /* action listener to withdraw  */
        withdraw.addActionListener(e -> {
            createWithdrawView();
        });

        /* action listener to getTransactions  */
        getTransactions.addActionListener(e -> {
            createTransactionsView();
        });

        /* action listener to closeAccount  */
        closeAccount.addActionListener(e -> {
            createCloseAccountView();
        });

        menu.add(createAccountItem);
        menu.add(viewAccountDetails);
        menu.add(deposit);
        menu.add(withdraw);
        menu.add(getTransactions);
        menu.add(closeAccount);

        menuBar.add(menu);
    }

    private void createCloseAccountView() {
        if (!isUserLoggedIn) {
            JOptionPane.showMessageDialog(frame, "Du måste logga in för att stänga ett konto.");
            return;
        }

        JPanel windowPanel = new JPanel(new BorderLayout());
        JPanel closeAccountPane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        closeAccountPane.add(new JLabel("Kontonummer:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        // show customer's all accounts in a drop-down menu
        Customer customer = bankLogic.findCustomerByPersonalNumber(socialSecurityNumber);
        String[] accounts = customer.getAccountsNumber().toArray(new String[0]);
        JComboBox<String> accountNumbers = new JComboBox<>(accounts);
        closeAccountPane.add(accountNumbers, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        JButton submit = new JButton("Stäng konto");
        closeAccountPane.add(submit, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        JButton reset = new JButton("Återställ");
        closeAccountPane.add(reset, gbc);

        submit.addActionListener(e -> {
            String accountNumberValue = Objects.requireNonNull(accountNumbers.getSelectedItem()).toString();

            if (accountNumberValue.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Du måste fylla i alla fält.");
                return;
            }

            int accountNumber = Integer.parseInt(accountNumberValue);

            Account account = getAccount(accountNumber);
            if (account == null) {
                JOptionPane.showMessageDialog(frame, "Kontot finns inte.");
                return;
            }
            String result = bankLogic.closeAccount(socialSecurityNumber, accountNumber);
            if (result == null) {
                JOptionPane.showMessageDialog(frame, "Kunde inte stänga konto.");
                return;
            }
            String message = "Konto stängt. \n" + result;
            JOptionPane.showMessageDialog(frame, message);
            setMainFrame();
        });

        reset.addActionListener(e -> {
            accountNumbers.setSelectedIndex(0);
        });

        windowPanel.add(closeAccountPane, BorderLayout.CENTER);
        windowPanel.add(createSidebar(), BorderLayout.WEST);
        windowPanel.add(createHeader(), BorderLayout.NORTH);
        windowPanel.add(createFooter(), BorderLayout.SOUTH);

        frame.setContentPane(windowPanel);
        frame.revalidate();
    }

    private void createTransactionsView() {
        if (!isUserLoggedIn) {
            JOptionPane.showMessageDialog(frame, "Du måste logga in för att se transaktioner.");
            return;
        }

        JPanel windowPanel = new JPanel(new BorderLayout());
        JPanel transactionsPane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        transactionsPane.add(new JLabel("Kontonummer:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        // show customer's all accounts in a drop-down menu
        Customer customer = bankLogic.findCustomerByPersonalNumber(socialSecurityNumber);
        String[] accounts = customer.getAccountsNumber().toArray(new String[0]);
        JComboBox<String> accountNumbers = new JComboBox<>(accounts);
        transactionsPane.add(accountNumbers, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        JButton submit = new JButton("Visa");
        transactionsPane.add(submit, gbc);

        submit.addActionListener(e -> {
            int accountNumberValue = Integer.parseInt(Objects.requireNonNull(accountNumbers.getSelectedItem()).toString());
            Account account = getAccount(accountNumberValue);
            if (account == null) {
                JOptionPane.showMessageDialog(frame, "Kontot finns inte.");
                return;
            }

            ArrayList<String> transactions = account.getTransactions();
            if (transactions.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Det finns inga transaktioner.");
                return;
            }

            // Show all transactions in a JTable
            JPanel transactionsPanel = new JPanel(new BorderLayout());
            JPanel transactionsTablePane = new JPanel(new GridBagLayout());
            GridBagConstraints gbc2 = new GridBagConstraints();

            gbc2.gridwidth = 3;
            gbc2.fill = GridBagConstraints.HORIZONTAL;
            gbc2.insets = new Insets(5, 5, 5, 5);

            JTable transactionsTable = new JTable();
            transactionsTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
            transactionsTable.setFillsViewportHeight(true);

            String[] transactionsTableHeader = {"Datum", "Transaktionstyp", "Belopp", "Saldo"};

            DefaultTableModel model = new DefaultTableModel(0, 4);
            model.addRow(transactionsTableHeader);

            for (String transaction : transactions) {
                String[] transactionDetails = transaction.split(" ");
                model.addRow(transactionDetails);
            }

            transactionsTable.setModel(model);

        });

        windowPanel.add(transactionsPane, BorderLayout.CENTER);
        windowPanel.add(createSidebar(), BorderLayout.WEST);
        windowPanel.add(createHeader(), BorderLayout.NORTH);
        windowPanel.add(createFooter(), BorderLayout.SOUTH);

        frame.setContentPane(windowPanel);
        frame.revalidate();
    }

    private void createWithdrawView() {
        if (!isUserLoggedIn) {
            JOptionPane.showMessageDialog(frame, "Du måste logga in för att göra ett uttag.");
            return;
        }

        JPanel windowPanel = new JPanel(new BorderLayout());
        JPanel withdrawPane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        withdrawPane.add(new JLabel("Kontonummer:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        // show customer's all accounts in a drop-down menu
        Customer customer = bankLogic.findCustomerByPersonalNumber(socialSecurityNumber);
        String[] accounts = customer.getAccountsNumber().toArray(new String[0]);
        JComboBox<String> accountNumbers = new JComboBox<>(accounts);
        withdrawPane.add(accountNumbers, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        withdrawPane.add(new JLabel("Belopp:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        JTextField amount = new JTextField(20);
        withdrawPane.add(amount, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        JButton submit = new JButton("Spara");
        withdrawPane.add(submit, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        JButton reset = new JButton("Återställ");
        withdrawPane.add(reset, gbc);

        submit.addActionListener(e -> {
            String accountNumberValue = Objects.requireNonNull(accountNumbers.getSelectedItem()).toString();
            String amountValue = amount.getText();

            if (accountNumberValue.isEmpty() || amountValue.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Du måste fylla i alla fält.");
                return;
            }

            int accountNumber = Integer.parseInt(accountNumberValue);
            double amountDouble = Double.parseDouble(amountValue);

            Account account = getAccount(accountNumber);
            if (account == null) {
                JOptionPane.showMessageDialog(frame, "Kontot finns inte.");
                return;
            }

            if (amountDouble <= 0) {
                JOptionPane.showMessageDialog(frame, "Beloppet måste vara större än 0.");
                return;
            }

            boolean result = account.withdraw(amountDouble);
            if (result) {
                Double accountBalance = account.getBalance();
                String message = "Uttag lyckades. Kontonummer: " + accountNumberValue + "\nNytt saldo: " + accountBalance;
                JOptionPane.showMessageDialog(frame, message);
                setMainFrame();
            } else {
                JOptionPane.showMessageDialog(frame, "Uttag misslyckades.");
            }
        });

    }

    private void createDepositView() {
        if (!isUserLoggedIn) {
            JOptionPane.showMessageDialog(frame, "Du måste logga in för att göra en insättning.");
            return;
        }

        JPanel windowPanel = new JPanel(new BorderLayout());
        JPanel depositPane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        depositPane.add(new JLabel("Kontonummer:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        // show customer's all accounts in a drop-down menu
        Customer customer = bankLogic.findCustomerByPersonalNumber(socialSecurityNumber);
        String[] accounts = customer.getAccountsNumber().toArray(new String[0]);
        JComboBox<String> accountNumbers = new JComboBox<>(accounts);
        depositPane.add(accountNumbers, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        depositPane.add(new JLabel("Belopp:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        JTextField amount = new JTextField(20);
        depositPane.add(amount, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        JButton submit = new JButton("Spara");
        depositPane.add(submit, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        JButton reset = new JButton("Återställ");
        depositPane.add(reset, gbc);

        submit.addActionListener(e -> {
            String accountNumberValue = Objects.requireNonNull(accountNumbers.getSelectedItem()).toString();
            String amountValue = amount.getText();

            if (accountNumberValue.isEmpty() || amountValue.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Du måste fylla i alla fält.");
                return;
            }

            int accountNumber = Integer.parseInt(accountNumberValue);
            double amountDouble = Double.parseDouble(amountValue);

            Account account = getAccount(accountNumber);
            if (account == null) {
                JOptionPane.showMessageDialog(frame, "Kontot finns inte.");
                return;
            }

            boolean result = account.deposit(amountDouble);
            if (result) {
                String message = "Insättning lyckades. Kontonummer: " + accountNumberValue + "\nNytt saldo: " + account.getBalance();
                JOptionPane.showMessageDialog(frame, message);
                setMainFrame();
            } else {
                JOptionPane.showMessageDialog(frame, "Insättning misslyckades.");
            }
        });

        reset.addActionListener(e -> {
            amount.setText("");
        });

        windowPanel.add(depositPane, BorderLayout.CENTER);
        windowPanel.add(createSidebar(), BorderLayout.WEST);
        windowPanel.add(createHeader(), BorderLayout.NORTH);
        windowPanel.add(createFooter(), BorderLayout.SOUTH);

        frame.setContentPane(windowPanel);
        frame.revalidate();
    }

    private void viewAccountsDetails() {
        if (!isUserLoggedIn) {
            JOptionPane.showMessageDialog(frame, "Du måste logga in för att se kontodetaljer.");
            return;
        }
        // show customer's all accounts in a JTable
        Customer customer = bankLogic.findCustomerByPersonalNumber(socialSecurityNumber);
        if (customer == null) {
            JOptionPane.showMessageDialog(frame, "Kunden finns inte.");
            return;
        }

        JPanel windowPanel = new JPanel(new BorderLayout());
        JPanel viewAccountDetailsPane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTable accountTable = new JTable();
        accountTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
        accountTable.setFillsViewportHeight(true);

        String[] accountTableHeader = {"Kontonummer", "Balans", "Kontotyp", "Ränta"};

        DefaultTableModel model = new DefaultTableModel(0, 4);
        model.addRow(accountTableHeader);

        for (String account : customer.getAccounts()) {
            String[] accountDetails = account.split(" ");
            model.addRow(accountDetails);
        }

        accountTable.setModel(model);

        gbc.gridx = 0;
        gbc.gridy = 0;
        viewAccountDetailsPane.add(accountTable, gbc);

        windowPanel.add(viewAccountDetailsPane, BorderLayout.CENTER);
        windowPanel.add(createSidebar(), BorderLayout.WEST);
        windowPanel.add(createHeader(), BorderLayout.NORTH);
        windowPanel.add(createFooter(), BorderLayout.SOUTH);

        frame.setContentPane(windowPanel);
        frame.revalidate();
    }

    private void addCustomerMenu() {
        JMenu customerMenu = new JMenu("Kund");
        JMenuItem setCustomer;

        if (isUserLoggedIn) {
            setCustomer = new JMenuItem("Byt kund");
        } else {
            setCustomer = new JMenuItem("Välj kund");
        }

        setCustomer.setForeground(Color.RED);
        JMenuItem createCustomer = new JMenuItem("Skapa kund");
        JMenuItem viewCustomerDetails = new JMenuItem("Visa kunddetaljer");
        JMenuItem editCustomer = new JMenuItem("Redigera kund");
        JMenuItem deleteCustomer = new JMenuItem("Ta bort kund");

        /* action listener to setCustomer  */
        setCustomer.addActionListener(e -> {
            setCustomer(setCustomer);
        });

        createCustomer.addActionListener(e -> {
            createNewCustomer();
        });

        viewCustomerDetails.addActionListener(e -> {
            if (!isUserLoggedIn) {
                JOptionPane.showMessageDialog(frame, "Du måste logga in för att se kunddetaljer.");
                return;
            }
            Customer customer = bankLogic.findCustomerByPersonalNumber(socialSecurityNumber);
            if (customer == null) {
                JOptionPane.showMessageDialog(frame, "Kunden finns inte.");
                return;
            }
            JOptionPane.showMessageDialog(frame, customer.getFirstName() + " " + customer.getLastName() + " " + customer.getPersonalNumber());
        });

        editCustomer.addActionListener(e -> {
            editCustomerView();
        });

        deleteCustomer.addActionListener(e -> {
            deleteCustomerView();
        });

        customerMenu.add(setCustomer);
        customerMenu.addSeparator();
        customerMenu.add(createCustomer);
        customerMenu.add(viewCustomerDetails);
        customerMenu.add(editCustomer);
        customerMenu.add(deleteCustomer);

        menuBar.add(customerMenu);
    }

    private void deleteCustomerView() {
        if (!isUserLoggedIn) {
            JOptionPane.showMessageDialog(frame, "Du måste logga in för att ta bort en kund.");
            return;
        }

        Customer customer = bankLogic.findCustomerByPersonalNumber(socialSecurityNumber);

        if (customer == null) {
            JOptionPane.showMessageDialog(frame, "Kunden finns inte.");
            return;
        }

        // show JOptionPane to confirm deletion
        int result = JOptionPane.showConfirmDialog(frame, "Är du säker på att du vill ta bort kunden?", "Ta bort kund", JOptionPane.YES_NO_OPTION);
        if (result != JOptionPane.YES_OPTION) {
            return;
        }

        ArrayList<String> deleteResult = bankLogic.deleteCustomer(socialSecurityNumber);

        System.out.println(deleteResult.toString());

        JPanel windowPanel = new JPanel(new BorderLayout());
        JPanel deleteCustomerPane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // create an array of customer detail in deleteResult[0]
        String[] customerDetails = deleteResult.get(0).split(" ");
        gbc.gridx = 0;
        gbc.gridy = 0;
        deleteCustomerPane.add(new JLabel(customerDetails[0]), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        deleteCustomerPane.add(new JLabel(customerDetails[1]), gbc);
        gbc.gridx = 2;
        gbc.gridy = 0;
        deleteCustomerPane.add(new JLabel(customerDetails[2]), gbc);

        String[] accountTableHeader = {"Kontonummer", "Balans", "Kontotyp", "Ränta"};

        DefaultTableModel model = new DefaultTableModel(0, 4);
        model.addRow(accountTableHeader);

        JTable accountTable = new JTable(model);
        accountTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
        accountTable.setFillsViewportHeight(true);

        for (int i = 1; i < deleteResult.size(); i++) {
            String[] accountDetails = deleteResult.get(i).split(" ");
            model.addRow(accountDetails);
        }

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        deleteCustomerPane.add(accountTable, gbc);


        windowPanel.add(deleteCustomerPane, BorderLayout.CENTER);
        windowPanel.add(createSidebar(), BorderLayout.WEST);
        windowPanel.add(createHeader(), BorderLayout.NORTH);
        windowPanel.add(createFooter(), BorderLayout.SOUTH);

        frame.setContentPane(windowPanel);
        frame.revalidate();
    }

    private void editCustomerView() {
        if (!isUserLoggedIn) {
            JOptionPane.showMessageDialog(frame, "Du måste logga in för att redigera en kund.");
            return;
        }

        Customer customer = bankLogic.findCustomerByPersonalNumber(socialSecurityNumber);

        if (customer == null) {
            JOptionPane.showMessageDialog(frame, "Kunden finns inte.");
            return;
        }

        JPanel windowPanel = new JPanel(new BorderLayout());
        JPanel editCustomerPane = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        editCustomerPane.add(new JLabel("Förnamn:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        JTextField firstName = new JTextField(customer.getFirstName(), 20);
        editCustomerPane.add(firstName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        editCustomerPane.add(new JLabel("Efternamn:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        JTextField lastName = new JTextField(customer.getLastName(), 20);
        editCustomerPane.add(lastName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        editCustomerPane.add(new JLabel("Personnummer:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        JTextField personalNumber = new JTextField(customer.getPersonalNumber(), 20);
        personalNumber.setEditable(false);
        editCustomerPane.add(personalNumber, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 3;
        JButton submit = new JButton("Spara");
        editCustomerPane.add(submit, gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        JButton reset = new JButton("Återställ");
        editCustomerPane.add(reset, gbc);


        // add action listeners to the buttons
        submit.addActionListener(e -> {
            String firstNameValue = firstName.getText();
            String lastNameValue = lastName.getText();

            if (firstNameValue.isEmpty() || lastNameValue.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Du måste fylla i alla fält.");
                return;
            }


            boolean result = bankLogic.changeCustomerName(firstNameValue, lastNameValue, socialSecurityNumber);
            if (result) {
                JOptionPane.showMessageDialog(frame, "Kund redigerad.");
                setMainFrame();
            } else {
                JOptionPane.showMessageDialog(frame, "Kunde inte redigera kund.");
            }
        });

        reset.addActionListener(e -> {
            firstName.setText("");
            lastName.setText("");
        });

        windowPanel.add(editCustomerPane, BorderLayout.CENTER);
        windowPanel.add(createSidebar(), BorderLayout.WEST);
        windowPanel.add(createHeader(), BorderLayout.NORTH);
        windowPanel.add(createFooter(), BorderLayout.SOUTH);

        frame.setContentPane(windowPanel);
        frame.revalidate();
    }

    private void createNewCustomer() {

        JPanel windowPanel = new JPanel(new BorderLayout());
        JPanel newCustomerPane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // add 2 columns in each row
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // add first name label
        gbc.gridx = 0;
        gbc.gridy = 0;
        newCustomerPane.add(new JLabel("Förnamn:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        JTextField firstName = new JTextField("", 20);
        newCustomerPane.add(firstName, gbc);

        // add last name label
        gbc.gridx = 0;
        gbc.gridy = 1;
        newCustomerPane.add(new JLabel("Efternamn:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        JTextField lastName = new JTextField();
        newCustomerPane.add(lastName, gbc);

        // add personal number label
        gbc.gridx = 0;
        gbc.gridy = 2;
        newCustomerPane.add(new JLabel("Personnummer:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        JTextField personalNumber = new JTextField();
        newCustomerPane.add(personalNumber, gbc);

        // add submit and reset buttons
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 3;
        JButton submit = new JButton("Skapa kund");
        newCustomerPane.add(submit, gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        JButton reset = new JButton("Återställ");
        newCustomerPane.add(reset, gbc);

        // add action listeners to the buttons
        submit.addActionListener(e -> {
            String firstNameValue = firstName.getText();
            String lastNameValue = lastName.getText();
            String personalNumberValue = personalNumber.getText();

            if (firstNameValue.isEmpty() || lastNameValue.isEmpty() || personalNumberValue.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Du måste fylla i alla fält.");
                return;
            }

            if (bankLogic.findCustomerByPersonalNumber(personalNumberValue) != null) {
                JOptionPane.showMessageDialog(frame, "Kunden finns redan.");
                return;
            }

            boolean result = bankLogic.createCustomer(firstNameValue, lastNameValue, personalNumberValue);
            if (result) {
                JOptionPane.showMessageDialog(frame, "Kund skapad.");
                setMainFrame();
            } else {
                JOptionPane.showMessageDialog(frame, "Kunde inte skapa kund.");
            }
        });

        reset.addActionListener(e -> {
            firstName.setText("");
            lastName.setText("");
            personalNumber.setText("");
        });

        // add the new customer panel to the window panel
        windowPanel.add(newCustomerPane, BorderLayout.CENTER);
        windowPanel.add(createSidebar(), BorderLayout.WEST);
        windowPanel.add(createHeader(), BorderLayout.NORTH);
        windowPanel.add(createFooter(), BorderLayout.SOUTH);

        // set the window panel as the content pane
        frame.setContentPane(windowPanel);
        frame.revalidate();
    }

    private void setCustomer(JMenuItem setCustomer) {
        if (setCustomer.getText().equals("Byt kund")) {
            isUserLoggedIn = false;
            socialSecurityNumber = null;
            JOptionPane.showMessageDialog(frame, "Du är nu utloggad. Välj ny kund.");
            setCustomer.setText("Välj kund");
        } else {
            String socialSecurityNumber = JOptionPane.showInputDialog(frame, "Ange personnummer:");
            if (socialSecurityNumber == null) {
                JOptionPane.showMessageDialog(frame, "Du måste ange ett personnummer.");
                return;
            }
            if (bankLogic.findCustomerByPersonalNumber(socialSecurityNumber) != null) {
                this.socialSecurityNumber = socialSecurityNumber;
                isUserLoggedIn = true;
                setMainFrame();
                setCustomer.setText("Byt kund");
            } else {
                JOptionPane.showMessageDialog(frame, "Kunden finns inte.");
            }
        }
    }


    private void createAccount() {
        if (!isUserLoggedIn) {
            JOptionPane.showMessageDialog(frame, "Du måste logga in för att skapa ett konto.");
            return;
        }

        final String SAVING_ACCOUNT = "Sparkonto";
        final String CREDIT_ACCOUNT = "Kreditkonto";

        JPanel windowPanel = new JPanel(new BorderLayout());
        JPanel newAccountPane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // add 2 columns in each row
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // add account type label
        gbc.gridx = 0;
        gbc.gridy = 0;
        newAccountPane.add(new JLabel("Kontotyp:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        String[] accountTypes = {SAVING_ACCOUNT, CREDIT_ACCOUNT};
        JComboBox<String> accountType = new JComboBox<>(accountTypes);
        newAccountPane.add(accountType, gbc);

        // add buttons
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        JButton submit = new JButton("Skapa konto");
        newAccountPane.add(submit, gbc);

        submit.addActionListener(e -> {
            int accountNumber = 0;
            String accountTypeValue = Objects.requireNonNull(accountType.getSelectedItem()).toString();
            if (accountTypeValue.equals(SAVING_ACCOUNT)) {
                accountNumber = bankLogic.createSavingsAccount(socialSecurityNumber);

                if (accountNumber != -1) {
                    JOptionPane.showMessageDialog(frame, "Sparkonto skapat.");
                    setMainFrame();
                } else {
                    JOptionPane.showMessageDialog(frame, "Kunde inte skapa sparkonto.");
                }

            } else if (accountTypeValue.equals(CREDIT_ACCOUNT)) {
                accountNumber = bankLogic.createCreditAccount(socialSecurityNumber);
                JOptionPane.showMessageDialog(frame, "Kreditkonto skapat.");
                if (accountNumber != -1) {
                    JOptionPane.showMessageDialog(frame, "Sparkonto skapat.");
                    setMainFrame();
                } else {
                    JOptionPane.showMessageDialog(frame, "Kunde inte skapa sparkonto.");
                }
            }
        });

        windowPanel.add(newAccountPane, BorderLayout.CENTER);
        windowPanel.add(createSidebar(), BorderLayout.WEST);
        windowPanel.add(createHeader(), BorderLayout.NORTH);
        windowPanel.add(createFooter(), BorderLayout.SOUTH);

        frame.setContentPane(windowPanel);
        frame.revalidate();
    }

    private void setMainFrame() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(createSidebar(), BorderLayout.WEST);
        mainPanel.add(createHeader(), BorderLayout.NORTH);
        mainPanel.add(createFooter(), BorderLayout.SOUTH);
        frame.setContentPane(mainPanel);
        frame.revalidate();
    }

    private JPanel createSidebar() {
        if (isUserLoggedIn) {
            JPanel panel = new JPanel();
            panel.add(new JLabel("Sidebar"));
            panel.setBackground(Color.BLUE);
            panel.add(new JLabel("Hej"));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            return panel;
        } else {
            JPanel panel = new JPanel();
            panel.add(new JLabel("Sidebar"));
            panel.setBackground(Color.RED);
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            panel.setForeground(Color.WHITE);
            return panel;
        }
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel();
        if (isUserLoggedIn) {
            JLabel customerName = new JLabel(bankLogic.findCustomerByPersonalNumber(socialSecurityNumber).getFirstName() + " " + bankLogic.findCustomerByPersonalNumber(socialSecurityNumber).getLastName());
            panel.add(customerName, BorderLayout.NORTH);
            panel.setBackground(Color.BLUE);
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            panel.setForeground(Color.WHITE);
            return panel;
        }
        panel.add(new JLabel("Header"));
        panel.setBackground(Color.RED);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return panel;
    }

    private JPanel createFooter() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Footer"));
        panel.setBackground(Color.BLUE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setForeground(Color.WHITE);
        return panel;
    }
}
