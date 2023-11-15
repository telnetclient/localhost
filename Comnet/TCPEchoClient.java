import java.net.*; 
import java.io.*;
public class TCPEchoClient { 
    public static void main(String[] args) {
        String serverAddress = "localhost"; // Change to your server's IP or hostname 
        int serverPort = 54321; // Match the server's port

        try (Socket socket = new Socket(serverAddress, serverPort);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                System.out.println("Connected to the Echo Server. Type exit to terminate.");
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in)); String userMessage;
                System.out.print("Enter a message: ");
                while ((userMessage = userInput.readLine()) != null) { 
                    if (userMessage.equalsIgnoreCase("exit")) 
                        break;
                    out.println(userMessage);

                    String response = in.readLine();
                    System.out.println("Server says: " + response);
                    System.out.print("\nEnter a message: ");
                }
            } catch (IOException e) { e.printStackTrace();
        }
    }
}
