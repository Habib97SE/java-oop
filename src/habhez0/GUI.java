package habhez0;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.io.FileReader;


/**
 * @author: Habib Hezarehee (habhez-0)
 * @email: habhez-0@student.ltu.se
 * @version: 1.0
 * @since: 2023-11-10
 * <h1>GUI</h1>
 * <p>
 * This class is responsible for creating the GUI for the application. <br />
 * The GUI is created using Swing. <br />
 * The GUI includes a menu bar with three menus: File, Accounts and Customer. <br />
 * And to divide each view, this class uses BorderLayout, and the program has 4 panels for each view. <br />
 * These panel are:
 *     <ol>
 *         <li>Header: This panel shows a welcome message and the customer's name if the customer is logged in.</li>
 *         <li>Sidebar: This panel shows a list of social media links, an image and a link to the bank's website.</li>
 *         <li>Main panel: The core functionality of each view like deposit, withdraw and etc. is inside this panel.</li>
 *         <li>Footer: Shows the bank's address, email and phone number.</li>
 *     </ol>
 *     The GUI class has a BankLogic object to access the bank's logic. <br />
 * </p>
 */
public class GUI {

    private int width;
    private int height;
    private JMenuBar menuBar;
    private JFrame frame;
    private boolean isUserLoggedIn = false;
    private String socialSecurityNumber;
    BankLogic bankLogic = new BankLogic();
    JMenuItem setCustomerItem = new JMenuItem("Välj kund");
    private final String bankName = "Banken AB";

    public GUI(int width, int height) {
        this.width = width;
        this.height = height;
        menuBar = new JMenuBar();
    }

    public GUI() {
        this.width = 800;
        this.height = 900;
        menuBar = new JMenuBar();
    }

    /**
     * Initializes and displays the main JFrame for the banking application.
     * Sets up the frame with the specified title, size, close operation,
     * menu bar, and icon. The method also adds various menus to the menu bar
     * and sets the main content pane of the frame.
     */
    public void start() {
        frame = new JFrame("Huvudsida" + " - " + bankName);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addFileMenu();
        addAccountMenu();
        addCustomerMenu();
        addBankClerkMenu();
        ImageIcon icon = createImageIcon();
        frame.setIconImage(icon.getImage());
        frame.setJMenuBar(menuBar);
        setMainFrame();
        frame.setVisible(true);
    }

    /**
     * Loads and returns the ImageIcon object from the path specified.
     *
     * @return ImageIcon object: icon.jpg from the path specified, null if the path is invalid or the image is not found.
     */
    public ImageIcon createImageIcon() {
        String path = "/habhez0_files/icon.jpg";
        URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        }
        return null;
    }

    /**
     * Load and return provided image from the habhez-0_files directory.
     *
     * @param fileName String: name of the file to load
     * @return Image object: image from the path specified, null if the path is invalid or the image is not found.
     */
    public Image loadImage(String fileName) {
        final String path = "/habhez0_files/" + fileName;
        URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL).getImage();
        }
        return null;
    }

    /**
     * Creates a JMenu for bank clerks functions that are only available to bank clerks. <br /><br />
     *
     * <p style="line-height: 1.5;">
     * The Bank Clerk menu includes four menu items:
     *     <ol>
     *         <li>Import Customers: Import customers from a file. {@link #createImportCustomersView()} ()}</li>
     *         <li>Export Customers: Export customers to a file. {@link #createExportCustomersView()} ()}</li>
     *    </ol>
     * </p>
     */
    public void addBankClerkMenu() {
        JMenu menu = new JMenu("Banktjänsteman");
        JMenuItem importCustomers = new JMenuItem("Importera kunder");
        JMenuItem exportCustomers = new JMenuItem("Exportera kunder");
        JMenuItem listAllCustomers = new JMenuItem("Lista alla kunder");

        /* action listener to importCustomers  */
        importCustomers.addActionListener(e -> {
            try {
                createImportCustomersView();
            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        /* action listener to exportCustomers  */
        exportCustomers.addActionListener(e -> {
            createExportCustomersView();
        });


        listAllCustomers.addActionListener(e -> {
            createListAllCustomersView();
        });

        // add menu items to menu
        menu.add(importCustomers);
        menu.add(exportCustomers);

        menu.add(listAllCustomers);

        // add menu to the menubar
        menuBar.add(menu);

    }

    private void createListAllCustomersView() {
        JPanel listAllCustomersPane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel panelTitleLabel = new JLabel("Lista alla kunder");
        panelTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        listAllCustomersPane.add(panelTitleLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel descriptionLabel = new JLabel("Här kan ni se alla kunder som finns i banken.");
        descriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        listAllCustomersPane.add(descriptionLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        String[] customerTableHeader = {"#", "Personnummer", "Namn"};
        DefaultTableModel model = new DefaultTableModel(0, 5);
        model.setColumnIdentifiers(customerTableHeader);
        JTable customerTable = new JTable(model);
        customerTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
        customerTable.setFillsViewportHeight(true);

        for (int i = 0; i < bankLogic.getAllCustomers().size(); i++) {
            Customer customer = bankLogic.getAllCustomers().get(i);
            String name = customer.getFirstName() + ' ' + customer.getLastName();
            String[] customerDetails = {String.valueOf(i + 1), customer.getPersonalNumber(), name};
            model.addRow(customerDetails);
        }

        JScrollPane scrollPane = new JScrollPane(customerTable);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Span the width of the pane
        gbc.fill = GridBagConstraints.BOTH; // Allow both horizontal and vertical stretching
        listAllCustomersPane.add(scrollPane, gbc);

        frame.setTitle("Lista alla kunder" + " - " + bankName);
        frame.setContentPane(setWindowPanel(listAllCustomersPane));
        frame.revalidate();


    }


    /**
     * Creates and manages the import customers view. <br />
     * <p style="line-height: 1.5;">
     * <ul>
     *     <li>A bank clerk needs to export all customers to a data file called customers.dat.</li>
     *     <li>The data file "customers.dat" contains all necessary information about the customers.</li>
     *     <li>The data file is located in the habhez-0_files directory.</li>
     *     <li>The data file is a serialized object of the Customer class.</li>
     *     <li>The data file is created by the bank clerk.</li>
     *     <li>The data file is used to import customers.</li>
     * </ul>
     * </p>
     */
    private void createExportCustomersView() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exportera kunder");
        fileChooser.setFileFilter(new FileNameExtensionFilter("DAT Filer", "dat"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        Path path = Paths.get("src", "habhez0_files");
        fileChooser.setCurrentDirectory(path.toFile());

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();

            // Append ".dat" if it's not present
            if (!filePath.toLowerCase().endsWith(".dat")) {
                filePath += ".dat";
            }

            File file = new File(filePath);

            // check if file already exists
            if (file.exists()) {
                int result = JOptionPane.showConfirmDialog(frame, "Filen finns redan. Vill du ersätta den?", "Filen finns redan", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.NO_OPTION) {
                    return;
                }
            }


            boolean result = bankLogic.exportCustomers(filePath, frame);
            if (result) {
                JOptionPane.showMessageDialog(frame, "Kunder exporterade.", "Kunder exporterade", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Kunde inte exportera kunder.", "Fel", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Kunde inte exportera kunder.", "Fel", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Creates and manages the import customers view. <br />
     * <p style="line-height: 1.5;">
     * A bank clerk needs to import and load exisiting customer from a data file called customers.dat. <br />
     * <ul>
     *      <li>The data file "customers.dat" contains all necessary information about the customers.</li>
     *      <li>The data file is located in the habhez-0_files directory.</li>
     *      <li>The data file is a serialized object of the Customer class.</li>
     *      <li>The data file is created by the bank clerk.</li>
     *      <li>The data file is used to import customers.</li>
     * </ul>
     * </p>
     */
    private void createImportCustomersView() throws IOException, ClassNotFoundException {
        JFileChooser fileChooser = new JFileChooser();
        Path path = Paths.get("src", "habhez0_files");
        fileChooser.setCurrentDirectory(path.toFile());
        fileChooser.setDialogTitle("Välj en fil att importera");
        // let user choose only files with .dat extension
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Data files", "dat"));
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            String fileName = fileChooser.getSelectedFile().getName();
            if (bankLogic.importCustomers(fileName, frame)) {
                JOptionPane.showMessageDialog(frame, "Kunder importerade.", "Kunder importerade", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Kunde inte importera kunder.", "Fel", JOptionPane.ERROR_MESSAGE);
            }
        }

    }


    /**
     * <p>
     * Adds a JMenu to JMenuBar menuBar to display file menu. <br /><br />
     * A user can use the File menu to exit the program. <br />
     * </p>
     */
    public void addFileMenu() {
        JMenu menu = new JMenu("Arkiv");
        JMenuItem exit = new JMenuItem("Avsluta");
        exit.addActionListener(e -> System.exit(0));

        menu.add(exit);
        menuBar.add(menu);
    }


    /**
     * <p>
     * the method determine whether the accountId is a saving or credit account and return the account object based on that.
     * </p>
     * <br />
     *
     * @param accountId : the account number of the account
     * @return : the account object if the account is found else returns null
     */
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

    /**
     * Add JMenu for Accounts to the menuBar. <br /><br />
     * <p style="line-height: 1.5;">
     * The Accounts menu includes six menu items:
     * <ol>
     *    <li>Create Account: Creates a new account for the logged in customer {@link #createNewAccountView()} ()}. </li>
     *    <li>View Account Details: View all available (active) accounts in a table. {@link #createAccountDetailsView()} ()} </li>
     *    <li>Deposit: Deposit money to the bank account. {@link #createDepositView()}</li>
     *    <li>Withdraw: Withdraw money from the bank account. {@link #createWithdrawView()}</li>
     *    <li>Get Transactions: Get a list of all transactions for a bank account based in a table. {@link #createTransactionsView()}</li>
     *    <li>Close Account: Close an account {@link #createCloseAccountView()}</li>
     * </ol>
     * Each menu item opens a new view.
     * </p>
     */
    public void addAccountMenu() {
        JMenu menu = new JMenu("Konto");
        JMenuItem createAccountItem = new JMenuItem("Skapa konto");
        JMenuItem viewAccountDetails = new JMenuItem("Visa kontodetaljer");
        JMenuItem deposit = new JMenuItem("Insättning");
        JMenuItem withdraw = new JMenuItem("Uttag");
        JMenuItem getTransactions = new JMenuItem("Visa transaktioner");
        JMenuItem loadTransactions = new JMenuItem("Ladda upp transaktioner");
        JMenuItem closeAccount = new JMenuItem("Stäng konto");

        /* action listener to createSavingAccount  */
        createAccountItem.addActionListener(e -> {
            createNewAccountView();
        });

        /* action listener to viewAccountDetails  */
        viewAccountDetails.addActionListener(e -> {
            createAccountDetailsView();
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

        loadTransactions.addActionListener(e -> {
            createLoadTransactionsView();
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
        menu.add(loadTransactions);
        menu.add(closeAccount);

        menuBar.add(menu);
    }

    private void createLoadTransactionsView() {
        JPanel loadTransactionsPane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel titleLabel = new JLabel("Ladda upp transaktioner");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        loadTransactionsPane.add(titleLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel descriptionLabel = new JLabel("Du kan visa transaktioner genom att ladda upp en fil.");
        descriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        loadTransactionsPane.add(descriptionLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        loadTransactionsPane.add(new JLabel("Välj en fil: "), gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;

        JButton chooseFile = new JButton("Välj fil");
        loadTransactionsPane.add(chooseFile, gbc);

        chooseFile.addActionListener(e -> {
            File file = loadTextFile();
            if (file == null) {
                JOptionPane.showMessageDialog(frame, "Kunde inte ladda upp transaktioner.", "Fel", JOptionPane.ERROR_MESSAGE);
                return;
            }
            loadAndShowTransactions(file.getAbsolutePath());
        });


        frame.setTitle("Ladda upp transaktioner" + " - " + bankName);
        frame.setContentPane(setWindowPanel(loadTransactionsPane));
        frame.revalidate();
    }

    public File loadTextFile() {
        JFileChooser fileChooser = new JFileChooser();
        Path path = Paths.get("src", "habhez0_files");
        fileChooser.setCurrentDirectory(path.toFile());
        fileChooser.setDialogTitle("Välj en fil att importera");
        // let user choose only files with .dat extension
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Textfiler", "txt"));
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    public void loadAndShowTransactions(String filePath) {
        StringBuilder htmlContent = new StringBuilder("<html>");
        boolean isTransactionSection = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Kontoinnehavare:") || line.startsWith("Kontonummer:")) {
                    htmlContent.append(line).append("<br>");
                } else if (line.contains("=")) {
                    isTransactionSection = !isTransactionSection; // Toggle the flag at each line of '='
                    if (isTransactionSection) {
                        htmlContent.append("<table><tr><th>#</th><th>Datumn</th><th>Balans</th><th>Transaktionstyp</th><th>Balans</th></tr>");
                    } else {
                        htmlContent.append("</table>");
                    }
                } else if (isTransactionSection) {
                    htmlContent.append("<tr><td>").append(line.replace("\t", "</td><td>")).append("</td></tr>");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Fel i att ladda filen: " + e.getMessage());
            return;
        }

        htmlContent.append("</html>");
        JOptionPane.showMessageDialog(null, htmlContent.toString());
    }

    /**
     * Creates a JPanel with a BorderLayout and adds the provided panel to the center of the BorderLayout. <br /><br />
     * this method creates a view to manage closing account functionality. <br />
     */
    private void createCloseAccountView() {
        if (!isUserLoggedIn) {
            JOptionPane.showMessageDialog(frame, "Du måste logga in för att stänga ett konto.");
            return;
        }

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
            JOptionPane.showMessageDialog(frame, message, "Konto stängt", JOptionPane.INFORMATION_MESSAGE);
            setMainFrame();
        });

        reset.addActionListener(e -> {
            accountNumbers.setSelectedIndex(0);
        });
        frame.setTitle("Stäng konto" + " - " + bankName);
        frame.setContentPane(setWindowPanel(closeAccountPane));
        frame.revalidate();
    }

    /**
     * x
     * This method creates and manage the Transaction view. <br />
     */
    private void createTransactionsView() {
        if (!isUserLoggedIn) {
            JOptionPane.showMessageDialog(frame, "Du måste logga in för att se transaktioner.");
            return;
        }

        JPanel transactionsPane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel panelTitleLabel = new JLabel("Visa transaktioner");
        panelTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        transactionsPane.add(panelTitleLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel descriptionLabel = new JLabel("Välj ett konto för att visa transaktioner.");
        descriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        transactionsPane.add(descriptionLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        transactionsPane.add(new JLabel("Kontonummer:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        // show customer's all accounts in a drop-down menu
        Customer customer = bankLogic.findCustomerByPersonalNumber(socialSecurityNumber);
        String[] accounts = customer.getAccountsNumber().toArray(new String[0]);
        JComboBox<String> accountNumbers = new JComboBox<>(accounts);
        transactionsPane.add(accountNumbers, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 3;
        JButton submit = new JButton("Visa transaktioner");
        transactionsPane.add(submit, gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        JButton printTransactions = new JButton("Skriv ut");
        transactionsPane.add(printTransactions, gbc);


        submit.addActionListener(e -> {
            int accountNumberValue = Integer.parseInt(Objects.requireNonNull(accountNumbers.getSelectedItem()).toString());
            Account account = getAccount(accountNumberValue);
            if (account == null) {
                JOptionPane.showMessageDialog(frame, "Kontot finns inte.");
                return;
            }

            ArrayList<Transaction> transactions = account.getTransactions();
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

            model.setColumnIdentifiers(transactionsTableHeader);

            for (Transaction transaction : transactions) {
                // Date time in format: YYYY-MM-DD HH:MM:SS
                String dateTime = transaction.getDate().toString();
                dateTime = dateTime.replace("T", " ");
                dateTime = dateTime.substring(0, dateTime.length() - 4);

                String[] transactionDetails = {dateTime, transaction.getTransactionType(), transaction.getAmount().toString(), transaction.getNewBalance().toString()};
                model.addRow(transactionDetails);
            }

            transactionsTable.setModel(model);
            SwingUtilities.invokeLater(() -> {
                JScrollPane scrollPane = new JScrollPane(transactionsTable);
                gbc.gridx = 0;
                gbc.gridy = 4;
                gbc.gridwidth = 3; // Span the width of the pane
                gbc.fill = GridBagConstraints.BOTH; // Allow both horizontal and vertical stretching
                transactionsPane.add(scrollPane, gbc);

                transactionsPane.revalidate();
                transactionsPane.repaint();
            });

        });

        printTransactions.addActionListener(e -> {
            int accountNumberValue = Integer.parseInt(Objects.requireNonNull(accountNumbers.getSelectedItem()).toString());
            boolean result = bankLogic.writeToTextFile(accountNumberValue);
            if (result) {
                JOptionPane.showMessageDialog(frame, "Transaktioner skrivna till fil.", "Transaktioner skrivna till fil", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Kunde inte skriva transaktioner till fil.", "Fel", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setTitle("Transaktioner" + " - " + bankName);
        frame.setContentPane(setWindowPanel(transactionsPane));
        frame.revalidate();
    }

    /**
     * This method creates and manage the withdraw view. <br />
     * A customer needs to have at least one ACTIVE account to be able to use this functionality. <br />
     * To use this view: The customer needs to choose the right bank account from drop-down list and add the amount to withdraw. <br />
     * If the amount is greater than the balance of the account, the withdraw will fail. <br />
     */
    private void createWithdrawView() {
        if (!isUserLoggedIn) {
            JOptionPane.showMessageDialog(frame, "Du måste logga in för att göra ett uttag.");
            return;
        }

        JPanel withdrawPane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel panelTitleLabel = new JLabel("Uttag");
        panelTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        withdrawPane.add(panelTitleLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel descriptionLabel = new JLabel("Välj ett konto för att göra ett uttag.");
        descriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        withdrawPane.add(descriptionLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        withdrawPane.add(new JLabel("Kontonummer:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        // show customer's all accounts in a drop-down menu
        Customer customer = bankLogic.findCustomerByPersonalNumber(socialSecurityNumber);
        String[] accounts = customer.getAccountsNumber().toArray(new String[0]);
        JComboBox<String> accountNumbers = new JComboBox<>(accounts);
        withdrawPane.add(accountNumbers, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        withdrawPane.add(new JLabel("Belopp:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        JTextField amount = new JTextField(20);
        withdrawPane.add(amount, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 4;
        JButton submit = new JButton("Ta ut");
        withdrawPane.add(submit, gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
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
                JOptionPane.showMessageDialog(frame, "Beloppet måste vara större än 0.", "Fel", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Double maximumWithdraw = account.getBalance();
            if (amountDouble > maximumWithdraw) {
                JOptionPane.showMessageDialog(frame, "Du kan inte ta ut mer än vad du har på kontot.", "Fel", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean result = account.withdraw(amountDouble);
            if (result) {
                Double accountBalance = account.getBalance();
                String message = "Uttag lyckades.\nKontonummer: " + accountNumberValue + "\nNytt saldo: " + accountBalance;
                JOptionPane.showMessageDialog(frame, message, "Uttag lyckades", JOptionPane.INFORMATION_MESSAGE);
                setMainFrame();
            } else {
                JOptionPane.showMessageDialog(frame, "Uttag misslyckades.", "Fel", JOptionPane.ERROR_MESSAGE);
            }
        });

        reset.addActionListener(e -> {
            amount.setText("");
        });

        frame.setTitle("Uttag" + " - " + bankName);
        frame.setContentPane(setWindowPanel(withdrawPane));
        frame.revalidate();

    }

    /**
     * This method creates and manage the deposit view. <br />
     * A customer needs to have at least one ACTIVE account to be able to use this functionality. <br />
     * To use this view: The customer needs to choose the right bank account from drop-down list and add the amount to deposit. <br />
     * If the amount is less than 0, the deposit will fail. <br />
     * If the amount is greater than 0, the deposit will succeed. <br />
     * If the amount is 0, the deposit will fail. <br />
     */
    private void createDepositView() {
        if (!isUserLoggedIn) {
            JOptionPane.showMessageDialog(frame, "Du måste logga in för att göra en insättning.");
            return;
        }

        JPanel depositPane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel panelTitleLabel = new JLabel("Insättning");
        panelTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        depositPane.add(panelTitleLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel descriptionLabel = new JLabel("Välj ett konto för att göra en insättning.");
        descriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        depositPane.add(descriptionLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        depositPane.add(new JLabel("Kontonummer:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        // show customer's all accounts in a drop-down menu
        Customer customer = bankLogic.findCustomerByPersonalNumber(socialSecurityNumber);
        String[] accounts = customer.getAccountsNumber().toArray(new String[0]);
        JComboBox<String> accountNumbers = new JComboBox<>(accounts);
        depositPane.add(accountNumbers, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        depositPane.add(new JLabel("Belopp:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        JTextField amount = new JTextField(20);
        depositPane.add(amount, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 4;
        JButton submit = new JButton("Sätt in");
        depositPane.add(submit, gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        JButton reset = new JButton("Återställ");
        depositPane.add(reset, gbc);


        submit.addActionListener(e -> {
            String accountNumberValue = Objects.requireNonNull(accountNumbers.getSelectedItem()).toString();
            String amountValue = amount.getText();

            if (accountNumberValue.isEmpty() || amountValue.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Du måste fylla i alla fält.", "Fel", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int accountNumber = Integer.parseInt(accountNumberValue);
            double amountDouble = Double.parseDouble(amountValue);

            if (amountDouble <= 0) {
                JOptionPane.showMessageDialog(frame, "Beloppet måste vara större än 0.", "Fel", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Account account = getAccount(accountNumber);
            if (account == null) {
                JOptionPane.showMessageDialog(frame, "Kontot finns inte.", "Fel", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean result = account.deposit(amountDouble);
            if (result) {
                String message = "Insättning lyckades.\nKontonummer: " + accountNumberValue + "\nNytt saldo: " + account.getBalance();
                JOptionPane.showMessageDialog(frame, message, "Insättning lyckades", JOptionPane.INFORMATION_MESSAGE);
                setMainFrame();
            } else {
                JOptionPane.showMessageDialog(frame, "Insättning misslyckades.", "Fel", JOptionPane.ERROR_MESSAGE);
            }
        });

        reset.addActionListener(e -> {
            amount.setText("");
        });

        frame.setTitle("Insättning" + " - " + bankName);
        frame.setContentPane(setWindowPanel(depositPane));
        frame.revalidate();
    }

    /**
     * This method creates and manage the account details view. <br />
     * User needs to have at least one ACTIVE account to be able to use this functionality. <br />
     * All accounts' details will be listed in a table. <br />
     */
    private void createAccountDetailsView() {
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

        JPanel accountDetailsPane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel panelTitleLabel = new JLabel("Kontodetaljer");
        panelTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        accountDetailsPane.add(panelTitleLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel descriptionLabel = new JLabel("Här kan ni se alla era konton som är aktiv. Tänk på att konto som är \n" + "ej aktiv visas inte. I tabellen nedan, kan ni se kontonummer, balans, kontotyp och ränta.");
        descriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        accountDetailsPane.add(descriptionLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        String[] accountTableHeader = {"Kontonummer", "Balans", "Kontotyp", "Ränta"};
        DefaultTableModel model = new DefaultTableModel(0, 4);
        model.setColumnIdentifiers(accountTableHeader);
        JTable accountTable = new JTable(model);
        accountTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
        accountTable.setFillsViewportHeight(true);
        for (Account account : customer.getAccounts()) {
            String[] accountDetails = {String.valueOf(account.getCustomerAccountNumber()), String.valueOf(account.getBalance()), account.getAccountType(), String.valueOf(account.getInterestRate())};
            model.addRow(accountDetails);
        }

        JScrollPane scrollPane = new JScrollPane(accountTable);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Span the width of the pane
        gbc.fill = GridBagConstraints.BOTH; // Allow both horizontal and vertical stretching
        accountDetailsPane.add(scrollPane, gbc);


        frame.setTitle("Kontodetaljer" + " - " + bankName);
        frame.setContentPane(setWindowPanel(accountDetailsPane));
        frame.revalidate();
    }

    /**
     * Creates a JMenu for Customer and add it to the menuBar. <br /><br />
     * <p style="line-height: 1.5;">
     * The Customer menu includes five menu items:
     *     <ol>
     *         <li>Set Customer: Set the customer to perform actions on. {@link #createSetCustomerView(JMenuItem)} (JMenuItem)}</li>
     *         <li>Create Customer: Create a new customer. {@link #createNewCustomerView()} ()}</li>
     *         <li>View Customer Details: View the details of the customer. {JOptionsPane()}</li>
     *         <li>Edit Customer: Edit the details of the customer. {@link #createEditCustomerView()} ()}</li>
     *         <li>Delete Customer: Delete the customer. {@link #createDeleteCustomerView()} ()}</li>
     *     </ol>
     * </p>
     */
    private void addCustomerMenu() {
        JMenu customerMenu = new JMenu("Kund");
        if (!isUserLoggedIn) {
            setCustomerItem.setText("Välj kund");
        } else {
            setCustomerItem.setText("Byt kund");
        }
        setCustomerItem.setForeground(Color.RED);

        JMenuItem setCustomer = setCustomerItem;
        JMenuItem createCustomer = new JMenuItem("Skapa kund");
        JMenuItem viewCustomerDetails = new JMenuItem("Visa kunddetaljer");
        JMenuItem editCustomer = new JMenuItem("Redigera kund");
        JMenuItem deleteCustomer = new JMenuItem("Ta bort kund");

        /* action listener to setCustomer  */
        setCustomer.addActionListener(e -> {
            createSetCustomerView(setCustomer);
        });

        createCustomer.addActionListener(e -> {
            createNewCustomerView();
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
            String message = "Förnamn: " + customer.getFirstName() + "\nEfternamn: " + customer.getLastName() + "\nPersonnummer: " + customer.getPersonalNumber();
            JOptionPane.showMessageDialog(frame, message, "Kunddetaljer", JOptionPane.INFORMATION_MESSAGE);
        });

        editCustomer.addActionListener(e -> {
            createEditCustomerView();
        });

        deleteCustomer.addActionListener(e -> {
            createDeleteCustomerView();
        });

        customerMenu.add(setCustomer);
        customerMenu.addSeparator();
        customerMenu.add(createCustomer);
        customerMenu.add(viewCustomerDetails);
        customerMenu.add(editCustomer);
        customerMenu.add(deleteCustomer);

        menuBar.add(customerMenu);
    }

    /**
     * This method creates and manage the delete customer view. <br />
     * A customer needs to be logged in to be able to use this functionality. <br />
     * To use this view: The customer needs to click on the delete button. <br />
     */
    private void createDeleteCustomerView() {
        if (!isUserLoggedIn) {
            JOptionPane.showMessageDialog(frame, "Du måste logga in för att ta bort en kund.");
            return;
        }

        Customer customer = bankLogic.findCustomerByPersonalNumber(socialSecurityNumber);

        if (customer == null) {
            JOptionPane.showMessageDialog(frame, "Kunden finns inte.");
            return;
        }

        String yesText = "Ja";
        String noText = "Nej";

        int result = JOptionPane.showOptionDialog(frame, "Är du säker på att du vill ta bort kunden?", "Ta bort kund", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{yesText, noText}, yesText);

        if (result != JOptionPane.YES_OPTION) {
            return;
        }

        ArrayList<String> deleteResult = bankLogic.deleteCustomer(socialSecurityNumber);

        System.out.println(deleteResult.toString());

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

        frame.setTitle("Ta bort kund" + " - " + bankName);
        frame.setContentPane(setWindowPanel(deleteCustomerPane));
        frame.revalidate();
    }

    /**
     * This method creates and manage the edit customer view. <br />
     * A customer cannoot change their social security number (or personal number) after creating their account. <br />
     * The customer can change their first name and last name. <br />
     */
    private void createEditCustomerView() {
        if (!isUserLoggedIn) {
            JOptionPane.showMessageDialog(frame, "Du måste logga in för att redigera en kund.");
            return;
        }

        Customer customer = bankLogic.findCustomerByPersonalNumber(socialSecurityNumber);

        if (customer == null) {
            JOptionPane.showMessageDialog(frame, "Kunden finns inte.");
            return;
        }

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

            if (firstNameValue.length() < 2 || lastNameValue.length() < 2) {
                JOptionPane.showMessageDialog(frame, "Förnamn och efternamn måste vara minst 2 bokstäver.");
                return;
            }

            if (firstNameValue.matches(".*\\d.*") || lastNameValue.matches(".*\\d.*")) {
                JOptionPane.showMessageDialog(frame, "Förnamn och efternamn får inte innehålla siffror.");
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

        frame.setTitle("Redigera kund" + " - " + bankName);
        frame.setContentPane(setWindowPanel(editCustomerPane));
        frame.revalidate();
    }

    /**
     * This method creates and manage the create customer view. <br />
     * To create a new customer in the system, we need to have the customer's first name, last name and social security number. <br />
     * PS. The social security number is unique for each customer and cannot be changed later. <br />
     */
    private void createNewCustomerView() {

        JPanel newCustomerPane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel panelTitleLabel = new JLabel("Skapa kund");
        panelTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        newCustomerPane.add(panelTitleLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel descriptionLabel = new JLabel("Fyll i alla fält för att skapa en ny kund. Tänk på att personnummer måste vara unik, antingen 10- eller 12 siffror. ");
        descriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        newCustomerPane.add(descriptionLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel importantMessageLabel = new JLabel("Viktigt: Personnummer går inte att ändra senare. Vänligen dubbelklicka innan du klickar dig vidare");
        importantMessageLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        importantMessageLabel.setForeground(Color.RED);
        newCustomerPane.add(importantMessageLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        newCustomerPane.add(new JLabel("Förnamn:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        JTextField firstName = new JTextField(20);
        newCustomerPane.add(firstName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        newCustomerPane.add(new JLabel("Efternamn:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        JTextField lastName = new JTextField(20);
        newCustomerPane.add(lastName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        newCustomerPane.add(new JLabel("Personnummer:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 5;
        JTextField personalNumber = new JTextField(20);

        // add a focus listener to the personalNumber field
        personalNumber.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                personalNumber.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                String personalNumberValue = personalNumber.getText();
                if (personalNumberValue.length() == 10 || personalNumberValue.length() == 12) {
                    personalNumber.setText(personalNumberValue);
                } else {
                    personalNumber.setText("10 eller 12 siffror");
                }
            }
        });

        newCustomerPane.add(personalNumber, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 6;
        JButton reset = new JButton("Återställ");
        newCustomerPane.add(reset, gbc);
        gbc.gridx = 1;
        gbc.gridy = 6;
        JButton submit = new JButton("Skapa kund");
        newCustomerPane.add(submit, gbc);


        // add action listeners to the buttons
        submit.addActionListener(e -> {
            String firstNameValue = firstName.getText();
            String lastNameValue = lastName.getText();
            String personalNumberValue = personalNumber.getText();

            if (firstNameValue.isEmpty() || lastNameValue.isEmpty() || personalNumberValue.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Du måste fylla i alla fält.");
                return;
            }

            if (firstNameValue.length() < 2 || lastNameValue.length() < 2) {
                JOptionPane.showMessageDialog(frame, "Förnamn och efternamn måste vara minst 2 bokstäver.");
                return;
            }

            if (firstNameValue.matches(".*\\d.*") || lastNameValue.matches(".*\\d.*")) {
                JOptionPane.showMessageDialog(frame, "Förnamn och efternamn får inte innehålla siffror.");
                return;
            }

            if (bankLogic.findCustomerByPersonalNumber(personalNumberValue) != null) {
                JOptionPane.showMessageDialog(frame, "Kunden finns redan.");
                return;
            }

            boolean result = bankLogic.createCustomer(firstNameValue, lastNameValue, personalNumberValue);
            if (result) {
                JOptionPane.showMessageDialog(frame, "Kund skapad.");
                isUserLoggedIn = true;
                socialSecurityNumber = personalNumberValue;
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

        // set padding around the panel
        newCustomerPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // set the window panel as the content pane
        frame.setTitle("Skapa kund" + " - " + bankName);
        frame.setContentPane(setWindowPanel(newCustomerPane));
        frame.revalidate();
        frame.repaint();
    }

    /**
     * This method creates and manage the set customer view. <br />
     *
     * @param setCustomer The JMenuItem to set the customer. and change the text to "Byt kund" if the user is logged in. <br />
     *                    If the user is not logged in, the text will be "Välj kund". <br />
     */
    private void createSetCustomerView(JMenuItem setCustomer) {
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


    /**
     * This method creates and manage the create new account view. <br />
     * A customer needs to be logged in to be able to use this functionality. <br />
     * To use this view: The customer needs to choose the right account type {Saving, Credit} from drop-down list. <br />
     */
    private void createNewAccountView() {
        if (!isUserLoggedIn) {
            JOptionPane.showMessageDialog(frame, "Du måste logga in för att skapa ett konto.");
            return;
        }

        final String SAVING_ACCOUNT = "Sparkonto";
        final String CREDIT_ACCOUNT = "Kreditkonto";

        JPanel newAccountPane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel panelTitleLabel = new JLabel("Skapa konto");
        panelTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        newAccountPane.add(panelTitleLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel descriptionLabel = new JLabel("Välj ett konto för att göra en insättning.");
        descriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        newAccountPane.add(descriptionLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        newAccountPane.add(new JLabel("Kontotyp:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        String[] accountTypes = {SAVING_ACCOUNT, CREDIT_ACCOUNT};
        JComboBox<String> accountType = new JComboBox<>(accountTypes);
        newAccountPane.add(accountType, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 3;
        JButton submit = new JButton("Skapa konto");
        newAccountPane.add(submit, gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        JButton reset = new JButton("Återställ");
        newAccountPane.add(reset, gbc);


        submit.addActionListener(e -> {
            int accountNumber = 0;
            String accountTypeValue = Objects.requireNonNull(accountType.getSelectedItem()).toString();
            if (accountTypeValue.equals(SAVING_ACCOUNT)) {
                accountNumber = bankLogic.createSavingsAccount(socialSecurityNumber);
                String message = "Sparkonto skapat.\nKontonummer: " + accountNumber;
                if (accountNumber != -1) {
                    JOptionPane.showMessageDialog(frame, message, "Sparkonto skapat", JOptionPane.INFORMATION_MESSAGE);
                    setMainFrame();
                } else {
                    JOptionPane.showMessageDialog(frame, "Kunde inte skapa sparkonto.", "Fel", JOptionPane.ERROR_MESSAGE);
                }

            } else if (accountTypeValue.equals(CREDIT_ACCOUNT)) {
                accountNumber = bankLogic.createCreditAccount(socialSecurityNumber);
                if (accountNumber != -1) {
                    String message = "Kreditkonto skapat.\nKontonummer: " + accountNumber;
                    JOptionPane.showMessageDialog(frame, message, "Kreditkonto skapat", JOptionPane.INFORMATION_MESSAGE);
                    setMainFrame();
                } else {
                    JOptionPane.showMessageDialog(frame, "Kunde inte skapa sparkonto.", "Fel", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        reset.addActionListener(e -> {
            accountType.setSelectedIndex(0);
        });

        frame.setContentPane(setWindowPanel(newAccountPane));
        frame.setTitle("Skapa konto" + " - " + bankName);
        frame.revalidate();
        frame.repaint();  // Repaint to show the changes
    }

    /**
     * This method recreate all panels and set the main frame. <br />
     *
     * @param centerPanel The panel to be set in the center of the main frame. <br />
     * @return The main panel with all panels. <br />
     */
    private JPanel setWindowPanel(JPanel centerPanel) {
        JPanel windowPanel = new JPanel(new BorderLayout());
        windowPanel.add(centerPanel, BorderLayout.CENTER);
        windowPanel.add(createSidebar(), BorderLayout.WEST);
        windowPanel.add(createHeader(), BorderLayout.NORTH);
        windowPanel.add(createFooter(), BorderLayout.SOUTH);
        return windowPanel;
    }

    /**
     * This method creates and update the main frame {frame}. <br />
     * The main frame includes the sidebar, header and footer. <br />
     */
    private void setMainFrame() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(createSidebar(), BorderLayout.WEST);
        mainPanel.add(createHeader(), BorderLayout.NORTH);
        mainPanel.add(createFooter(), BorderLayout.SOUTH);
        frame.setContentPane(mainPanel);
        frame.revalidate();
    }


    /**
     * This method creates and manage the sidebar. <br />
     *
     * @return
     */
    private JPanel createSidebar() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel banksWebsite = createLink("Besök vår hemsida", "https://www.google.com");
        banksWebsite.setFont(new Font("SansSerif", Font.BOLD, 20));
        panel.add(banksWebsite, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        Image bankOffice = loadImage("bank-office.jpg");
        bankOffice = bankOffice.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        JLabel bankOfficeLabel = new JLabel(new ImageIcon(bankOffice));
        panel.add(bankOfficeLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel welcomeMessageLabel = new JLabel("<html><div style='text-align: center; font-family: sans-sarif'>Välkommen till " + bankName + "</div></html>");
        panel.add(welcomeMessageLabel, gbc);
        panel.setBackground(Color.CYAN);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Följ oss på sociala medier:"), gbc);

        // add banks social media icons and links to the panel
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(createIconLink("facebook.jpg", "https://www.facebook.com/"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(createIconLink("twitter.jpg", "https://twitter.com/"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(createIconLink("instagram.png", "https://www.instagram.com/"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(createIconLink("youtube.png", "https://www.youtube.com/"), gbc);


        return panel;
    }

    /**
     * This method creates a JLabel with a link. <br />
     *
     * @param text The text to be shown in the JLabel. <br />
     * @param link The link to be opened when the user clicks on the JLabel. <br />
     * @return The JLabel with the link. <br />
     */
    private JLabel createLink(String text, String link) {
        JLabel label = new JLabel("<html><a href='" + link + "'>" + text + "</a></html>");
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(link));
                } catch (IOException | URISyntaxException ex) {
                    ex.printStackTrace();
                }
            }
        });
        return label;
    }

    /**
     * This method creates a JLabel with an icon and a link. <br />
     *
     * @param iconPath The path to the icon. <br />
     * @param link     The link to be opened when the user clicks on the JLabel. <br />
     * @return The JLabel with the icon and the link. <br />
     */
    private JLabel createIconLink(String iconPath, String link) {
        try {
            Image icon = loadImage(iconPath);
            icon = icon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            JLabel iconLabel = new JLabel(new ImageIcon(icon));
            iconLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            iconLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        Desktop.getDesktop().browse(new URI(link));
                    } catch (IOException | URISyntaxException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            return iconLabel;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Kunde inte skapa ikon.", "Fel", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * This method creates the header, if a user logged in, the header will show the customer's name. <br />
     * Otherwise a welcome message
     *
     * @return The header panel. <br />
     */
    private JPanel createHeader() {
        try {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(Color.GREEN);
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JLabel titleLabel;
            if (isUserLoggedIn) {
                Customer loggedInCustomer = bankLogic.findCustomerByPersonalNumber(socialSecurityNumber);
                titleLabel = new JLabel(loggedInCustomer.getFirstName() + " " + loggedInCustomer.getLastName());
            } else {
                titleLabel = new JLabel("Välkommen till " + bankName);
            }

            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
            titleLabel.setForeground(Color.WHITE);
            panel.add(titleLabel, BorderLayout.EAST);

            return panel;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Kunde inte skapa header.", "Fel", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * This method creates the footer. <br />
     * The footer contains: bank name, address, phone number and email. <br />
     *
     * @return The footer panel. <br />
     */
    private JPanel createFooter() {
        try {
            JPanel panel = new JPanel(new BorderLayout());
            String footerText = "<html>" + bankName + " © 2023 <br>";
            footerText += "Adress: Stockholm, Sweden" + "<br>";
            footerText += "Telefon: 08-123 456 78" + "<br>";
            footerText += "E-post: mail@mail.com" + "</html>";
            JLabel footerLabel = new JLabel(footerText);
            footerLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
            footerLabel.setForeground(Color.WHITE);
            footerLabel.setHorizontalAlignment(SwingConstants.CENTER); // Set horizontal alignment to center
            panel.setBackground(Color.BLUE);
            panel.add(footerLabel, BorderLayout.CENTER);
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            return panel;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Kunde inte skapa footer.", "Fel", JOptionPane.ERROR_MESSAGE);
            return null;
        }

    }
}
