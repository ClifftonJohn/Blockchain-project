import java.io.*;
import java.net.*;
import java.util.Scanner;

public class P2PClient {
    private String clientName;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public P2PClient(String clientName, String serverAddress, int port) throws IOException {
        this.clientName = clientName;
        // Establish a connection to the server
        socket = new Socket(serverAddress, port);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());

        // Display the IP address and port of the connected server
        System.out.println("Connected to server at " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
    }

    public void sendData(Object data) throws IOException {
        outputStream.writeObject(data);
    }

    public void receiveData() throws IOException, ClassNotFoundException {
        Object data = inputStream.readObject();
        System.out.println("Received data: " + data);
        saveData(data);
    }

    private void saveData(Object data) throws IOException {
        File file = new File(clientName + ".dat");
        boolean append = file.exists(); // Append if file exists
        try (FileOutputStream fos = new FileOutputStream(file, append);
             ObjectOutputStream oos = append ? new AppendingObjectOutputStream(fos) : new ObjectOutputStream(fos)) {
            oos.writeObject(data);
        }
    }

    public void close() throws IOException {
        inputStream.close();
        outputStream.close();
        socket.close();
    }

    // Custom ObjectOutputStream to handle appending to an existing file
    private static class AppendingObjectOutputStream extends ObjectOutputStream {
        public AppendingObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            // do not write a header, but reset:
            reset();
        }
    }

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter client name: ");
            String clientName = scanner.nextLine();
            System.out.print("Enter server IP address: ");
            String serverIP = scanner.nextLine();
            P2PClient client = new P2PClient(clientName, serverIP, 5000);

            System.out.println("Connected to server. Enter data to send: ");
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                client.sendData(data);
                client.receiveData();
            }
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
