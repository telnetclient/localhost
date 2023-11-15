import java.io.*;
import java.net.*;

public class TCPEchoServer {
    public static void main(String[] args) { 
        int port = 54321;

        try (ServerSocket serverSocket = new ServerSocket(port)) { 
            System.out.println("Echo Server is running on port " + port);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                    System.out.println("Accepted connection from " + clientSocket.getInetAddress()); 
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println("Received: " + inputLine); 
                        out.println("Server Echo: " + inputLine);
                    }
                } catch (IOException e) { e.printStackTrace(); }
            }
        } catch (IOException e) { e.printStackTrace();
    }
}
}
