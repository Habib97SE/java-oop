package habhez0;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class GUI {
    private String title;
    private int width;
    private int height;
    private JMenuBar menuBar;
    private JFrame frame;
    private boolean isUserLoggedIn = true;
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
        frame.setJMenuBar(menuBar);
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

        menu.add(createAccountItem);
        menu.add(viewAccountDetails);
        menu.add(deposit);
        menu.add(withdraw);
        menu.add(getTransactions);
        menu.add(closeAccount);

        menuBar.add(menu);
    }

    private void createAccount() {
        if (!isUserLoggedIn) {
            JOptionPane.showMessageDialog(frame, "Du måste logga in för att skapa ett konto.");
            return;
        }
        final String SAVING_ACCOUNT = "Sparkonto";
        final String CREDIT_ACCOUNT = "Kreditkonto";
        JPanel accountPanel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(0, 1));
        // a drop-down menu to select account type (savings or credit)
        String[] accountTypes = {SAVING_ACCOUNT, CREDIT_ACCOUNT};
        JComboBox<String> accountType = new JComboBox<>(accountTypes);
        // a button to submit the form
        JButton submit = new JButton("Skapa konto");
        // a button to cancel the form
        JButton cancel = new JButton("Avbryt");

        // if the user is logged in, we can get the social security number
        submit.addActionListener(e -> {
            String accountTypeValue = (String) accountType.getSelectedItem();
            int accountNumber = -1;
            if (Objects.equals(accountTypeValue, SAVING_ACCOUNT)) {
                accountNumber = bankLogic.createSavingsAccount(socialSecurityNumber);

            } else if (Objects.equals(accountTypeValue, CREDIT_ACCOUNT)) {
                accountNumber = bankLogic.createCreditAccount(socialSecurityNumber);
            } else {
                JOptionPane.showMessageDialog(frame, "Du måste välja ett konto.");
            }

            // if accountNumber is -1, it means that the account was not created successfully
            if (accountNumber != -1) {
                JOptionPane.showMessageDialog(frame, "Konto skapat. Kontonummer: " + accountNumber);
                // repaint the frame
                setMainFrame();
            } else {
                JOptionPane.showMessageDialog(frame, "Kunde inte skapa konto.");
            }

        });

        cancel.addActionListener(e -> {
            start();
        });

        formPanel.add(accountType);
        formPanel.add(submit);
        formPanel.add(cancel);
        accountPanel.add(formPanel, BorderLayout.CENTER);
        accountPanel.add(createSidebar(), BorderLayout.WEST);
        accountPanel.add(createHeader(), BorderLayout.NORTH);
        accountPanel.add(createFooter(), BorderLayout.SOUTH);
        frame.setContentPane(accountPanel);
        frame.revalidate();

    }

    private void setMainFrame() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Main frame"));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.removeAll();
        frame.setContentPane(panel);
        frame.revalidate();
        frame.repaint();
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
            return panel;
        }
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Bank"));
        panel.setBackground(Color.BLUE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return panel;
    }

    private JPanel createFooter() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Footer"));
        panel.setBackground(Color.BLUE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return panel;
    }
}
