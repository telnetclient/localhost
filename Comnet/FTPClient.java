import java.io.*;
import java.net.*;

class FTPClient {
    public static void main (String[] args) throws IOException {
        String serverAddress = "localhost";
        int serverPort = 53979;
        String filePath;
        System.out.println("Welcome to my FTP File Transferer. Type exit to terminate.");
        BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));
        do {
            try (
                Socket socket = new Socket(serverAddress,serverPort);
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            ) {
                String promptMessage = dataInputStream.readUTF();
                System.out.print(promptMessage);
                filePath = bfr.readLine();
                if (filePath.equalsIgnoreCase("exit")) {
                    dataOutputStream.writeUTF(filePath);
                    socket.close();
                    return;
                }
                FileInputStream fileInputStream = new FileInputStream(filePath);
                File file = new File(filePath);
                dataOutputStream.writeUTF(file.getName());
                dataOutputStream.writeLong(file.length());
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) 
                    dataOutputStream.write(buffer,0,bytesRead);
                System.out.println("File Sent Successfully.");
                fileInputStream.close();
            }
            catch (Exception e) { e.printStackTrace(); }
        } while (true);
    }
}
