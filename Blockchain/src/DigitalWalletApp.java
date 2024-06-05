import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class DigitalWalletApp {
    private static Map<String, User> users;
    private static Blockchain blockchain = new Blockchain();
    
    public static void main(String[] args) {

        // Load the blockchain
        blockchain = BlockchainUtils.loadBlockchain("blockdata.dat");
        if (blockchain == null) {
            blockchain = new Blockchain(); // Initialize a new blockchain if none exists
        }

        // Load users from CSV file
        users = UserCSVUtil.loadUsers();

        JFrame frame = new JFrame("Digital Wallet");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Set CardLayout for the frame
        CardLayout cardLayout = new CardLayout();
        frame.setLayout(cardLayout);

        //Create the login panel background
        // Login panel
        
        //JPanel loginPanel = new JPanel(new GridLayout(3, 2));
        ImagePanel loginPanel = new ImagePanel("src//background.jpg");
        loginPanel.setLayout(new GridLayout(3, 2));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        
        JButton loginButton = new JButton("Login");

        //loginPanel.add(new JLabel("Username:"));
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);  // Change color to blue
        //usernameLabel.setHorizontalAlignment(JLabel.CENTER); // Horizontally centers the text
        //usernameLabel.setVerticalAlignment(JLabel.CENTER); // Vertically centers the text (if needed)
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);  // Change color to blue
        //passwordLabel.setHorizontalAlignment(JLabel.CENTER); // Horizontally centers the text
        passwordLabel.setVerticalAlignment(JLabel.CENTER); // Vertically centers the text (if needed

        //usernameField.setOpaque(false);
        //passwordField.setOpaque(false);
        usernameLabel.setOpaque(false);
        passwordLabel.setOpaque(false);
        loginButton.setOpaque(false);

        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        //loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(new JLabel(""));
        loginPanel.add(loginButton);

        // Wallet panel
        //JPanel walletPanel = new JPanel(new GridLayout(4, 2));
        // Wallet panel setup with BoxLayout for vertical stacking
        JPanel walletPanel = new JPanel();
        walletPanel.setLayout(new BoxLayout(walletPanel, BoxLayout.Y_AXIS));

        // Balance Panel
        JPanel balancePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel balanceLabel = new JLabel("Balance: ");
        JLabel balanceValue = new JLabel();
        balancePanel.add(balanceLabel);
        balancePanel.add(balanceValue);

        // Receiver Panel
        JPanel receiverPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel receiverLabel = new JLabel("Receiver:");
        JTextField receiverField = new JTextField(20);  // Sizing the text field
        receiverPanel.add(receiverLabel);
        receiverPanel.add(receiverField);

        // Amount Panel
        JPanel amountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel amountLabel = new JLabel("Amount:");
        JTextField amountField = new JTextField(20);  // Sizing the text field
        amountPanel.add(amountLabel);
        amountPanel.add(amountField);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton transferButton = new JButton("Transfer");
        JButton logoutButton = new JButton("Logout");
        JButton viewBlockchainButton = new JButton("View Blockchain");
        buttonsPanel.add(transferButton);
        buttonsPanel.add(logoutButton);
        buttonsPanel.add(viewBlockchainButton);

        // Adding sub-panels to the main wallet panel
        walletPanel.add(balancePanel);
        walletPanel.add(receiverPanel);
        walletPanel.add(amountPanel);
        walletPanel.add(buttonsPanel);

        frame.add(walletPanel);
       
        //JTextField receiverField = new JTextField();
        //JTextField amountField = new JTextField();

        //JButton transferButton = new JButton("Transfer");
        //JButton logoutButton = new JButton("Logout");
        //JButton viewBlockchainButton = new JButton("View Blockchain");  // Create the view blockchain button

        // walletPanel.add(new JLabel("Balance:"));
        // walletPanel.add(balanceValue);
        // walletPanel.add(new JLabel("Receiver:"));
        // walletPanel.add(receiverField);
        // walletPanel.add(new JLabel("Amount:"));
        // walletPanel.add(amountField);
        // walletPanel.add(transferButton);
        // walletPanel.add(logoutButton);
        // walletPanel.add(viewBlockchainButton); // Add the new button to the panel

        // View blockchain button action
        viewBlockchainButton.addActionListener(new ActionListener() {
        // @Override
        // public void actionPerformed(ActionEvent e) {
        // blockchain.printBlockchain();  // Print the blockchain to the console
        // }
        // });
        @Override
        public void actionPerformed(ActionEvent e) {
            // This assumes blockchain is accessible here; ensure it is final or effectively final
            EventQueue.invokeLater(() -> {
                BlockchainViewer viewer = new BlockchainViewer(blockchain);
                viewer.setVisible(true);
            });
        }
    }); 
        
        // Add panels to the frame
        frame.add(loginPanel, "login");
        frame.add(walletPanel, "wallet");
        cardLayout.show(frame.getContentPane(), "login");  // Make sure the login panel is shown first
        
        // Get the CardLayout from the frame
        CardLayout cl = (CardLayout) (frame.getContentPane().getLayout());

        // Login button action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                User user = users.get(username);

                if (user != null && user.getPassword().equals(password)) {
                    balanceValue.setText(String.valueOf(user.getWallet().getBalance()));
                    cl.show(frame.getContentPane(), "wallet");
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid login", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Transfer button action
        // transferButton.addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {
        //         String receiverUsername = receiverField.getText();
        //         double amount = Double.parseDouble(amountField.getText());

        //         //Update to check via blockchain before transaction
        //         try {
        //             amount = Double.parseDouble(amountField.getText());
        //         } catch (NumberFormatException ex) {
        //             JOptionPane.showMessageDialog(frame, "Invalid amount", "Error", JOptionPane.ERROR_MESSAGE);
        //             return;
        //         }

        //         User sender = users.get(usernameField.getText());
        //         User receiver = users.get(receiverUsername);

        //         if (receiver != null && sender.getWallet().transfer(receiver.getWallet(), amount)) {
        //             balanceValue.setText(String.valueOf(sender.getWallet().getBalance()));

        //             // Create and add a new transaction to the blockchain
        //             Transaction transaction = new Transaction(sender.getUsername(), receiver.getUsername(), amount, System.currentTimeMillis(), "tx" + System.currentTimeMillis());
        //             blockchain.mineBlock(new Transaction[]{transaction});
                    
        //             // Save the updated users to CSV file
        //             UserCSVUtil.saveUsers(users);
        //         } else {
        //             JOptionPane.showMessageDialog(frame, "Transfer failed", "Error", JOptionPane.ERROR_MESSAGE);
        //         }
        //             // After successful transfer
        //             BlockchainUtils.saveBlockchain(blockchain, "blockdata.dat");
        //     }
        // });

            // Assuming these fields are class-level variables
HashSet<String> activeTransactions = new HashSet<>();

transferButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String transactionId = UUID.randomUUID().toString();
        if (activeTransactions.add(transactionId)) {  // Check if the transaction can be added (i.e., is not currently active)
            try {
                String receiverUsername = receiverField.getText();
                double amount = Double.parseDouble(amountField.getText());  // Parsing the amount only once

                User sender = users.get(usernameField.getText());
                User receiver = users.get(receiverUsername);

                if (receiver != null && sender.getWallet().transfer(receiver.getWallet(), amount)) {
                    balanceValue.setText(String.valueOf(sender.getWallet().getBalance()));
                    // Prepare the confirmation message with transaction details
                    String message = String.format("Do you want to transfer $%.2f to %s?", amount, receiver.getUsername());
                    int response = JOptionPane.showConfirmDialog(frame, message, "Confirm Transaction", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if(response == JOptionPane.YES_OPTION){
                    // Create and add a new transaction to the blockchain
                    Transaction transaction = new Transaction(sender.getUsername(), receiver.getUsername(), amount, System.currentTimeMillis(), "tx" + System.currentTimeMillis());
                    blockchain.mineBlock(new Transaction[]{transaction});

                    // Save the updated users to CSV file
                    UserCSVUtil.saveUsers(users);

                    // Save the blockchain state
                    BlockchainUtils.saveBlockchain(blockchain, "blockdata.dat");

                    JOptionPane.showMessageDialog(frame, "Transaction successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // If user cancels, do nothing
                    JOptionPane.showMessageDialog(frame, "Transaction canceled", "Transaction Canceled", JOptionPane.INFORMATION_MESSAGE);
                }} else {
                    JOptionPane.showMessageDialog(frame, "Transfer failed", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid amount", "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                activeTransactions.remove(transactionId);  // Remove the transaction ID whether success or fail
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Transaction is already in progress", "Transaction Error", JOptionPane.WARNING_MESSAGE);
        }
    }
});


        // Logout button to Log in 
        // frame.addWindowListener(new java.awt.event.WindowAdapter() {
        //     public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        //         BlockchainUtils.saveBlockchain(blockchain, "blockdata.dat"); // Save when app is closing
        //         // Transfer - Log out 
        //         //Log out -> To log in!
        //     }
        // });

        // Logout button action
logoutButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Clear user input fields
        usernameField.setText("");
        passwordField.setText("");

        // Show the login panel again
        cl.show(frame.getContentPane(), "login");

        // Optionally, you may also want to clear other fields or perform cleanup
        balanceValue.setText("");
        receiverField.setText("");
        amountField.setText("");
    }
});
frame.setVisible(true);
    }
}
