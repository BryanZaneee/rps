import java.io.*;
import java.net.*;

public class TCPclient {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java TCPclient <server address> <server_port>");
            return;
        }

        String SERVER_ADDRESS = args[0];
        int SERVER_PORT = Integer.parseInt(args[1]);

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             DataOutputStream output = new DataOutputStream(socket.getOutputStream());
             DataInputStream input = new DataInputStream(socket.getInputStream());
             BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Client> Connected to the server.");

            while (true) {
                // Client enters their choice
                System.out.print("Enter your choice (rock/paper/scissors): ");
                String clientChoice = reader.readLine();
                output.writeUTF(clientChoice);
                output.flush();

                // Server sends its choice
                String serverChoice = input.readUTF();
                System.out.println("Server's choice: " + serverChoice);

                // Server sends the result
                String result = input.readUTF();
                System.out.println(result);
            }
        } catch (UnknownHostException ex) {
            System.out.println("Client> Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Client> I/O error: " + ex.getMessage());
        }
    }
}
