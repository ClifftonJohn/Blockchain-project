import java.util.Arrays;
import java.util.Random;
import java.io.Serializable;
public class Block  implements Serializable  {
    private int prevHash;
    private int blockHash;
    private Transaction[] transactions;
    private int nonce; // Nonce for the proof-of-work algorithm

    public Block(int prevHash, Transaction[] transactions) {
        this.prevHash = prevHash;
        this.transactions = transactions;
        //this.nonce = 0; // Start nonce at 0
        this.nonce = new Random().nextInt(Integer.MAX_VALUE);
        this.blockHash = computeHash(); // Initial hash calculation
    }

    public int getPrevHash() {
        return prevHash;
    }

    public int getBlockHash() {
        return blockHash;
    }

    public Transaction[] getTransactions() {
        return transactions;
    }

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    // Compute hash by considering the transactions, previous hash, and nonce
    public int computeHash() {
        int[] contents = {Arrays.hashCode(transactions), prevHash, nonce};
        return Arrays.hashCode(contents);
    }

    // Method to increment nonce and recompute the hash
    public void incrementNonce() {
        this.nonce++;
        this.blockHash = computeHash(); // Recompute the hash as the nonce changes
    }
}
