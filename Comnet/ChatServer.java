import java.io.*;
import java.net.*;

public class ChatServer {
    public static void main(String[] args) {
        final String SERVER_IP = "127.0.0.1"; 
        final int SERVER_PORT = 65534;       
        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT, 50, InetAddress.getByName(SERVER_IP));
            System.out.println("Server listening on " + SERVER_IP + ":" + SERVER_PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedReader scanf = new BufferedReader(new InputStreamReader(System.in));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String Message = in.readLine();
                if (Message == null || "exit".equals(Message)) {
                    System.out.println("Exited.");
                    clientSocket.close();
                    return;
                }

                System.out.println("Syed_Sahil: " + Message);
                System.out.print("You: ");
                String message = scanf.readLine();

                out.println(message);
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
