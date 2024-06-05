import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

    public class BlockchainViewer extends JFrame {
    private Blockchain blockchain;

    public BlockchainViewer(Blockchain blockchain) {
        this.blockchain = blockchain;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Blockchain Viewer");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        DefaultListModel<String> listModel = new DefaultListModel<>();
        if (blockchain.getBlockchain().isEmpty()) {
            listModel.addElement("No blocks in the blockchain");
        } else {
            blockchain.getBlockchain().forEach(block -> listModel.addElement("Block: " + block.getBlockHash()));
        }
        
        JList<String> blockList = new JList<>(listModel);
        JScrollPane listScrollPane = new JScrollPane(blockList);
        contentPane.add(listScrollPane, BorderLayout.WEST);

        JTextArea blockDetails = new JTextArea();
        blockDetails.setEditable(false);
        if (blockchain.getBlockchain().isEmpty()) {
            blockDetails.setText("The blockchain is currently empty. No block information to display.");
        }
        JScrollPane detailsScrollPane = new JScrollPane(blockDetails);
        contentPane.add(detailsScrollPane, BorderLayout.CENTER);

        blockList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !blockchain.getBlockchain().isEmpty()) {
                int selectedIndex = blockList.getSelectedIndex();
                Block selectedBlock = blockchain.getBlockchain().get(selectedIndex);
                blockDetails.setText("Hash: " + selectedBlock.getBlockHash() + "\n"
                        + "Previous Hash: " + selectedBlock.getPrevHash() + "\n"
                        + "Nonce: " + selectedBlock.getNonce() + "\n"
                        + "Transactions: " + Arrays.stream(selectedBlock.getTransactions())
                            .map(t -> t.getTransactionId() + " - " + t.getSender() + " to " + t.getReceiver() + ": " + t.getAmount())
                            .collect(Collectors.joining("\n"))
                );
            }
        });
    }
    public static void main(String[] args) {
        final Blockchain blockchain; // Declare the variable
        Blockchain loadedBlockchain = BlockchainUtils.loadBlockchain("blockdata.dat");

        if (loadedBlockchain == null) {
            blockchain = new Blockchain(); // Initialize a new blockchain if none exists
            BlockchainUtils.saveBlockchain(blockchain, "blockdata.dat"); // Save the new, empty blockchain to file
        } else {
            blockchain = loadedBlockchain; // Set it directly if loaded successfully
        }

        EventQueue.invokeLater(() -> {
            new BlockchainViewer(blockchain).setVisible(true);
        });
    }
}
