import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Blockchain implements Serializable {
    private List<Block> blockchain = new ArrayList<>();
    private int difficulty = 4;  // Define the difficulty level

    public void addBlock(Block block) {
        if (blockchain.isEmpty() || block.getPrevHash() == blockchain.get(blockchain.size() - 1).getBlockHash()) {
            if (isValidProof(block)) {  // Check if block satisfies the PoW
                blockchain.add(block);
                System.out.println("Block added: " + block.getBlockHash());
            } else {
                System.out.println("Invalid PoW for block.");
            }
        } else {
            System.out.println("Invalid block: Previous hash does not match.");
        }
    }

    private boolean isValidProof(Block block) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        return String.format("%032x", block.getBlockHash()).substring(0, difficulty).equals(target);
    }

    public List<Block> getBlockchain() {
        return blockchain;
    }

    public void mineBlock(Transaction[] transactions) {
        int prevHash = blockchain.isEmpty() ? 0 : blockchain.get(blockchain.size() - 1).getBlockHash();
        Block newBlock = new Block(prevHash, transactions);
        System.out.println("Mining block...");
        while (!isValidProof(newBlock)) {
            newBlock.incrementNonce();  // Simulate mining by incrementing nonce until valid hash is found
        }
        System.out.println("Block mined with nonce: " + newBlock.getNonce());
        addBlock(newBlock);
        printBlockchain();
    }
    // Method to get balance for a given address
    public double getBalance(String address) {
        double balance = 0;
        for (Block block : blockchain) {
            for (Transaction transaction : block.getTransactions()) {
                if (transaction.getReceiver().equals(address)) {
                    balance += transaction.getAmount();
                }
                if (transaction.getSender().equals(address)) {
                    balance -= transaction.getAmount();
                }
            }
        }
        return balance;
    }

    // Method to check consensus among nodes
    public boolean checkConsensus(String sender, double amount) {
        // Placeholder for consensus check (mocked for now)
        int consensusThreshold = (int) Math.ceil(0.9 * blockchain.size());  // 90% of nodes must agree
        int agreementCount = 0;
        // Logic to simulate node agreement (mocked for now)
        for (int i = 0; i < blockchain.size(); i++) {
            if (Math.random() > 0.1) {  // Simulate 90% probability of agreement
                agreementCount++;
            }
        }
        return agreementCount >= consensusThreshold;
    }

    public void printBlockchain() {
        System.out.println("Current Blockchain:");
        for (Block block : blockchain) {
            System.out.println("------------------------------------------------");
            System.out.println("Block Hash: " + block.getBlockHash());
            System.out.println("Previous Hash: " + block.getPrevHash());
            System.out.println("Nonce: " + block.getNonce());
            System.out.println("Transactions:");
            for (Transaction transaction : block.getTransactions()) {
                System.out.println("    Transaction ID: " + transaction.getTransactionId());
                System.out.println("    Sender: " + transaction.getSender());
                System.out.println("    Receiver: " + transaction.getReceiver());
                System.out.println("    Amount: " + transaction.getAmount());
                System.out.println("    Timestamp: " + transaction.getTimestamp());
            }
        }
    }
    
}
