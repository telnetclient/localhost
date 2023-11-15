import java.io.*;
import java.net.*;

public class ChatClient {
    public static void main(String[] args) {

        BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Chat With Praveen. ");
        try {
            String serverIP = "127.0.0.1";
            int serverPort = 65534;
            String messageToSend = "";

            while (true) {

                Socket socket = new Socket(serverIP, serverPort);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                System.out.print("You: ");
                messageToSend = userInputReader.readLine();

                if (messageToSend.equals("exit")) {
                    socket.close();
                    System.out.println("Praveen: See you!!");
                    return; }
                out.println(messageToSend);
                String response = in.readLine();

                System.out.println("Praveen: " + response);
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
