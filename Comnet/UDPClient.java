import java.io.*;
import java.net.*;

public class UDPClient {
    public static void main(String[] args) {
        final String serverHost = "192.168.109.35"; 
        final int serverPort = 12113; 

        try {
            DatagramSocket socket = new DatagramSocket();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter your name: ");
            String clientName = reader.readLine();

            System.out.println("Enter your messages below!.\n");
            String joinMessage = clientName + " has entered the chat.";
            byte[] joinData = joinMessage.getBytes();
            DatagramPacket joinPacket = new DatagramPacket(joinData, joinData.length, InetAddress.getByName(serverHost), serverPort);
            socket.send(joinPacket);

            Thread receiveThread = new Thread(() -> {
                try {
                    byte[] receiveData = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                    while (true) {
                        socket.receive(receivePacket);
                        String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            receiveThread.start();

            while (true) {
                String message = reader.readLine();

                if (message.equalsIgnoreCase("exit")) {
                    socket.close();
                    System.out.println("\nYou are exited from the chat!\nSee you!.");
                    return;
                }

                String combinedMessage = clientName + ": " + message;
                byte[] sendData = combinedMessage.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(serverHost), serverPort);
                socket.send(sendPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
