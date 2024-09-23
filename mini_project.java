package mini_project;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;

public class CashFlowMinimizer extends Frame implements ActionListener {
    // UI components
    TextField nameField, emailField, passwordField, loginNameField, loginPasswordField;
    Label signUpMessageLabel, loginMessageLabel, cashFlowLabel, warningLabel, welcomeLabel;
    CardLayout cardLayout = new CardLayout();
    Panel mainPanel = new Panel(cardLayout);
    Button summaryButton;

    // Stacks for user credentials and cash flow transactions
    Stack<User> userStack = new Stack<>();
    double netCashFlow = 0; // Tracks net cash flow
    double totalIncome = 0; // Total incoming money
    double totalExpenses = 0; // Total outgoing money
    String currentUser; // Tracks the current user
	private TextArea summaryTextArea;

    // User class
    static class User {
        String name;
        String password;

        User(String name, String password) {
            this.name = name;
            this.password = password;
        }
    }

    // Constructor to set up the UI
    CashFlowMinimizer() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });

        mainPanel.add(createSignUpPanel(), "SignUp");
        mainPanel.add(createLoginPanel(), "Login");
        mainPanel.add(createHomePanel(), "Home");
        mainPanel.add(createPropertyPanel(), "Property");
        mainPanel.add(createIncomePanel(), "Income");
        mainPanel.add(createLoansPanel(), "Loans");
        mainPanel.add(createSummaryPanel(), "Summary");

        add(mainPanel);
        cardLayout.show(mainPanel, "SignUp");

        setSize(500, 400);
        setResizable(true);
        setVisible(true);
    }

    private Panel createSignUpPanel() {
        Panel panel = new Panel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        nameField = new TextField(20);
        emailField = new TextField(20);
        passwordField = new TextField(20);
        passwordField.setEchoChar('*');
        Button signUpButton = new Button("Sign Up");
        signUpButton.addActionListener(this);

        Button loginButton = new Button("Go to Login");
        loginButton.addActionListener(e -> cardLayout.show(mainPanel, "Login"));

        signUpMessageLabel = new Label();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new Label("Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new Label("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(emailField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new Label("Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(passwordField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(signUpButton, gbc);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(signUpMessageLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 4; panel.add(loginButton, gbc);

        return panel;
    }

    private Panel createLoginPanel() {
        Panel panel = new Panel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        loginNameField = new TextField(20);
        loginPasswordField = new TextField(20);
        loginPasswordField.setEchoChar('*');
        Button loginButton = new Button("Login");
        loginButton.addActionListener(this);

        loginMessageLabel = new Label();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new Label("Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(loginNameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new Label("Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(loginPasswordField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(loginButton, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(loginMessageLabel, gbc);

        return panel;
    }

    private Panel createHomePanel() {
        Panel panel = new Panel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        welcomeLabel = new Label("Welcome, " + currentUser + "!");
        cashFlowLabel = new Label("Net Cash Flow: $" + netCashFlow);
        Button propertyButton = new Button("Property");
        Button incomeButton = new Button("Income");
        Button loansButton = new Button("Loans");

        propertyButton.addActionListener(this);
        incomeButton.addActionListener(this);
        loansButton.addActionListener(this);

        // Create the summary button
        summaryButton = new Button("View Summary");
        summaryButton.addActionListener(e -> cardLayout.show(mainPanel, "Summary"));

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(welcomeLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(cashFlowLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(propertyButton, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(incomeButton, gbc);
        gbc.gridx = 0; gbc.gridy = 4; panel.add(loansButton, gbc);
        gbc.gridx = 0; gbc.gridy = 5; panel.add(summaryButton, gbc);

        return panel;
    }

    private Panel createPropertyPanel() {
        Panel panel = new Panel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        Choice propertyChoice = new Choice();
        propertyChoice.add("House");
        propertyChoice.add("Condo");
        propertyChoice.add("Apartment");
        propertyChoice.add("Land");

        TextField propertyExpenseField = new TextField(20);
        Button propertySubmit = new Button("Add Property Expense");
        propertySubmit.addActionListener(e -> {
            double expense = Double.parseDouble(propertyExpenseField.getText());

            if (totalIncome == expense) {  // If matching, clear the transaction
                totalIncome = 0;
                netCashFlow -= expense;
            } else {
                totalExpenses += expense;
                netCashFlow -= expense;
            }

            cashFlowLabel.setText("Net Cash Flow: $" + netCashFlow);
            checkIncomeLimit();
        });

        Button propertyBackButton = new Button("Back");
        propertyBackButton.addActionListener(e -> cardLayout.show(mainPanel, "Home"));

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new Label("Property Type:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(propertyChoice, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new Label("Property Expense:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(propertyExpenseField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(propertySubmit, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(propertyBackButton, gbc);

        return panel;
    }

    private Panel createIncomePanel() {
        Panel panel = new Panel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        TextField salaryField = new TextField(20);
        TextField familyExpenseField = new TextField(20);

        Button incomeSubmit = new Button("Add Income and Expenses");
        incomeSubmit.addActionListener(e -> {
            double income = Double.parseDouble(salaryField.getText());
            double expense = Double.parseDouble(familyExpenseField.getText());

            if (expense == income) {  // If matching, clear the transaction
                totalExpenses = 0;
                netCashFlow += income;
            } else {
                totalIncome += income;
                totalExpenses += expense;
                netCashFlow += income - expense;
            }

            cashFlowLabel.setText("Net Cash Flow: $" + netCashFlow);
            checkIncomeLimit();
        });

        Button incomeBackButton = new Button("Back");
        incomeBackButton.addActionListener(e -> cardLayout.show(mainPanel, "Home"));

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new Label("Salary:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(salaryField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new Label("Family Expense:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(familyExpenseField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(incomeSubmit, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(incomeBackButton, gbc);

        return panel;
    }

    private Panel createLoansPanel() {
        Panel panel = new Panel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        TextField loanBorrowedField = new TextField(20);
        TextField loanSettledField = new TextField(20);

        Button loansSubmit = new Button("Submit Loan Info");
        loansSubmit.addActionListener(e -> {
            double borrowed = Double.parseDouble(loanBorrowedField.getText());
            double settled = Double.parseDouble(loanSettledField.getText());

            if (borrowed == settled) {  // If matching, clear the transaction
                borrowed = 0;
            } else {
                netCashFlow += borrowed - settled;
            }

            cashFlowLabel.setText("Net Cash Flow: $" + netCashFlow);
        });

        Button loansBackButton = new Button("Back");
        loansBackButton.addActionListener(e -> cardLayout.show(mainPanel, "Home"));

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new Label("Loan Borrowed:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(loanBorrowedField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new Label("Loan Settled:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(loanSettledField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(loansSubmit, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(loansBackButton, gbc);

        return panel;
    }

    private Panel createSummaryPanel() {
        Panel panel = new Panel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        Label summaryLabel = new Label("Summary of Transactions:");
        TextArea summaryTextArea = new TextArea(10, 30);

        // Back to home button
        Button backButton = new Button("Back to Home");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Home"));

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(summaryLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(summaryTextArea, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(backButton, gbc);

        // Method to dynamically update the summary
        summaryButton.addActionListener(e -> {
            updateSummaryText(summaryTextArea); // Refresh the summary text
            cardLayout.show(mainPanel, "Summary");
        });

        return panel;
    }

    // Method to update the summary text with current values
    private void updateSummaryText(TextArea summaryTextArea) {
        StringBuilder summary = new StringBuilder();
        summary.append("Net Cash Flow: $").append(netCashFlow).append("\n")
               .append("Total Income: $").append(totalIncome).append("\n")
               .append("Total Expenses: $").append(totalExpenses).append("\n");

        // You can add more detailed transactions if required
        summaryTextArea.setText(summary.toString());
    }


    public void actionPerformed(ActionEvent ae) {
        String command = ae.getActionCommand();
        if (command.equals("Sign Up")) {
            currentUser = nameField.getText();
            String password = passwordField.getText();
            userStack.push(new User(currentUser, password));
            signUpMessageLabel.setText("Sign Up Successful!");
            cardLayout.show(mainPanel, "Login");
        } else if (command.equals("Login")) {
            String name = loginNameField.getText();
            String password = loginPasswordField.getText();

            if (!userStack.isEmpty() && userStack.peek().name.equals(name) && userStack.peek().password.equals(password)) {
                currentUser = name;
                loginMessageLabel.setText("Login Successful!");
                welcomeLabel.setText("Welcome, " + currentUser + "!");
                cardLayout.show(mainPanel, "Home");
            } else {
                loginMessageLabel.setText("Invalid credentials!");
            }
        } else if (command.equals("Property")) {
            cardLayout.show(mainPanel, "Property");
        } else if (command.equals("Income")) {
            cardLayout.show(mainPanel, "Income");
        } else if (command.equals("Loans")) {
            cardLayout.show(mainPanel, "Loans");
        } else if (command.equals("View Summary")) {
            updateSummaryText(summaryTextArea);  // Call this to update summary
            cardLayout.show(mainPanel, "Summary");
        }
    }


    private void checkIncomeLimit() {
        if (totalExpenses > totalIncome) {
            warningLabel.setText("Warning: Expenses exceed income!");
        } else {
            warningLabel.setText("");
        }
    }

    public static void main(String[] args) {
        new CashFlowMinimizer();
    }
}
