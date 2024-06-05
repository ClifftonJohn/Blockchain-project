import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private List<Socket> nodes = new ArrayList<>();

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                nodes.add(socket);
                new Thread(new NodeHandler(socket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class NodeHandler implements Runnable {
        private Socket socket;
        private ObjectInputStream in;

        public NodeHandler(Socket socket) {
            this.socket = socket;
            try {
                this.in = new ObjectInputStream(socket.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Block newBlock = (Block) in.readObject();
                    broadcastBlock(newBlock);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void broadcastBlock(Block block) {
            for (Socket node : nodes) {
                if (node != socket) {
                    try {
                        ObjectOutputStream out = new ObjectOutputStream(node.getOutputStream());
                        out.writeObject(block);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start(5001);
    }
}
