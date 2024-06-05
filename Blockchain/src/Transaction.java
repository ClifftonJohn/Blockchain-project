import java.io.Serializable;

public class Transaction  implements Serializable  {
    private String sender;
    private String receiver;
    private double amount;
    private long timestamp;
    private String transactionId;

    public Transaction(String sender, String receiver, double amount, long timestamp, String transactionId) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.timestamp = timestamp;
        this.transactionId = transactionId;
    }
    public boolean validateTransaction(Blockchain blockchain) {
        // Check balance from blockchain (mocked for now)
        double balance = blockchain.getBalance(sender);
    
        // Check if the sender has enough balance
        if (balance >= amount) {
            // Check consensus from other nodes
            if (blockchain.checkConsensus(sender, amount)) {
                return true; // Valid transaction
            } else {
                System.out.println("Transaction is not valid: Consensus not met.");
                return false; // Not enough nodes agree
            }
        } else {
            System.out.println("Transaction is not valid: Insufficient funds.");
            return false;
        }
    }
    

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public double getAmount() {
        return amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getTransactionId() {
        return transactionId;
    }
}
