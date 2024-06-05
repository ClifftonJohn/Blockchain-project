/**
 * Created by knghi
 */
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Node {
    private List<Block> blockchain = new ArrayList<>();
    private List<Node> network = new ArrayList<>();
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Node(String serverAddress, int serverPort) {
        try {
            this.socket = new Socket(serverAddress, serverPort);
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addBlock(Block block) {
        if (blockchain.isEmpty() || block.getPrevHash() == blockchain.get(blockchain.size() - 1).getBlockHash()) {
            blockchain.add(block);
            broadcastBlock(block);
        } else {
            System.out.println("Invalid block");
        }
    }

    public void broadcastBlock(Block block) {
        try {
            out.writeObject(block);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void receiveBlock(Block block) {
        addBlock(block);
    }

    public List<Block> getBlockchain() {
        return blockchain;
    }

    public void mineBlock(Transaction[] transactions) {
        int prevHash = blockchain.isEmpty() ? 0 : blockchain.get(blockchain.size() - 1).getBlockHash();
        Block newBlock = new Block(prevHash, transactions);
        addBlock(newBlock);
    }

    public void listenForBlocks() {
        new Thread(() -> {
            while (true) {
                try {
                    Block newBlock = (Block) in.readObject();
                    receiveBlock(newBlock);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
