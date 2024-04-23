import java.io.*;
import java.net.*;

public class TCPserver {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TCPserver <port>");
            return;
        }

        int PORT = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Server> Connected to client from " + socket.getInetAddress().getHostAddress());
                new ServerThread(socket).start();
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static class ServerThread extends Thread {
        private Socket socket;

        public ServerThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (DataInputStream input = new DataInputStream(socket.getInputStream());
                 DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

                String[] options = {"rock", "paper", "scissors"};

                while (true) {
                    // Wait for client's response first
                    String clientChoice = input.readUTF();

                    // Server randomly chooses an option
                    String serverChoice = options[(int) (Math.random() * options.length)];
                    output.writeUTF(serverChoice);
                    output.flush();

                    // Determine the winner
                    String result = determineWinner(serverChoice, clientChoice);
                    output.writeUTF(result);
                    output.flush();
                }
            } catch (IOException ex) {
                System.out.println("Server> Communication error: " + ex.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException ex) {
                    System.out.println("Server> Error closing socket: " + ex.getMessage());
                }
            }
        }

        private String determineWinner(String serverChoice, String clientChoice) {
            if (serverChoice.equals(clientChoice)) {
                return "It's a tie!";
            } else if (
                (serverChoice.equals("rock") && clientChoice.equals("scissors")) ||
                (serverChoice.equals("paper") && clientChoice.equals("rock")) ||
                (serverChoice.equals("scissors") && clientChoice.equals("paper"))
            ) {
                return "Server wins!";
            } else {
                return "Client wins!";
            }
        }
    }
}
