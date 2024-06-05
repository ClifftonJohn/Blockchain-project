import java.io.*;

public class BlockchainUtils {
    
    public static void saveBlockchain(Blockchain blockchain, String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(blockchain);
            System.out.println("Blockchain saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving the blockchain: " + e.getMessage());
        }
    }

    public static Blockchain loadBlockchain(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (Blockchain) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading the blockchain: " + e.getMessage());
            return null; // Handle the error appropriately
        }
    }
}
