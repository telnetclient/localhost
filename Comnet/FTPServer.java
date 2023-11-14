import java.io.*;
import java.net.*;

class FTPServer {
    public static void main (String[] args) {
        int port = 53979;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("FTP Server started on port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepter Connection from " + clientSocket.getInetAddress());
                Thread clientThread = new Thread (new ClientHandler(clientSocket));
                clientThread.start();
            }
        }
        catch (Exception e) { e.printStackTrace(); }
    }
    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        public ClientHandler (Socket clientSocket) {
            this.clientSocket = clientSocket;
        }
        @Override 
        public void run () {
            try (
                InputStream inputStream = clientSocket.getInputStream();
                OutputStream outputStream = clientSocket.getOutputStream();
                DataInputStream dataInputStream = new DataInputStream(inputStream); 
                DataOutputStream dataOutputStream = new DataOutputStream(outputStream); 
            ) {
                dataOutputStream.writeUTF("Type your file name (path if not located in same directory) here: ");
                dataOutputStream.flush();
                String fileName = dataInputStream.readUTF();
                if (fileName.equalsIgnoreCase("exit")) 
                    System.exit(0);
                long fileSize = dataInputStream.readLong();
                System.out.printf("\nReceiving file: %s (Size: %d bytes).\n",fileName,fileSize);
                try (FileOutputStream fileOutputStream = new FileOutputStream("received_" + fileName)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer,0,bytesRead);
                        System.out.write(buffer,0,bytesRead);
                    }
                    System.out.println();
                }
                System.out.println("File Received Successfully.");
            }
            catch (Exception e) { e.printStackTrace(); }               
        }
    }
}
