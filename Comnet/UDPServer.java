import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UDPServer {
    private static final int SERVER_PORT = 12113;
    private static final int MAX_CLIENTS = 10;

    private static List<ClientInfo> clients = new ArrayList<>();
    private static DatagramSocket socket = null; 
    private static int exitCount = 0; 

    public static void main(String[] args) {
        try {
            socket = new DatagramSocket(SERVER_PORT);
            System.out.println("UDP server started on port " + SERVER_PORT);

            while (true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);

                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                System.out.println("Received from " + clientAddress + ":" + clientPort + ": " + message);

                if (message.equalsIgnoreCase("exit")) {
                    handleExit(clientAddress, clientPort);
                } else {
                    ClientInfo newClient = new ClientInfo(clientAddress, clientPort);
                    if (!clients.contains(newClient)) {
                        clients.add(newClient);
                    }

                    broadcastMessage(message, clientAddress, clientPort);
                }
            }
        } catch (SocketException e) {
            System.err.println("Error: The socket is already in use.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }

    private static void broadcastMessage(String message, InetAddress senderAddress, int senderPort) throws IOException {
        byte[] sendData = message.getBytes();
        for (ClientInfo client : clients) {
            if (!client.getAddress().equals(senderAddress) || client.getPort() != senderPort) {
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, client.getAddress(), client.getPort());
                socket.send(sendPacket);
            }
        }
    }

    private static void handleExit(InetAddress clientAddress, int clientPort) {
        if (!clients.contains(new ClientInfo(clientAddress, clientPort))) {
            clients.add(new ClientInfo(clientAddress, clientPort));
        }

        boolean allClientsExited = true;
        for (ClientInfo client : clients) {
            if (!client.getAddress().equals(clientAddress) || client.getPort() != clientPort) {
                allClientsExited = false;
                break;
            }
        }

        if (allClientsExited) {
            System.out.println("All clients have exited. Server is exiting gracefully.");
            System.exit(0);
        }
    }

    private static class ClientInfo {
        private InetAddress address;
        private int port;

        public ClientInfo(InetAddress address, int port) {
            this.address = address;
            this.port = port;
        }

        public InetAddress getAddress() {
            return address;
        }

        public int getPort() {return port;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            ClientInfo that = (ClientInfo) obj;
            return port == that.port && address.equals(that.address);
        }

        @Override
        public int hashCode() {
            return Objects.hash(address, port);
        }
    }
}
