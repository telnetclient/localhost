import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

class HTTPClient {
    public static void main(String[] args) throws IOException {
        URL url = new URL("http://localhost:8080/upload");
        String filePath;
        System.out.println("Welcome to my HTTP File Transferer. Type 'exit' to terminate.");

        BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));
        do {
            System.out.print("Type your file name (path if not located in the same directory) here: ");
            filePath = bfr.readLine();

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());

            try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
                File file = new File(filePath);

                dataOutputStream.writeUTF(file.getName());
                dataOutputStream.writeLong(file.length());

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    dataOutputStream.write(buffer, 0, bytesRead);
                }

                System.out.println("File Sent Successfully.");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    System.out.println("Server response: File received successfully.");
                } else {
                    System.out.println("Server response: " + responseCode);
                }
            } catch (Exception e) {
                dataOutputStream.writeUTF("exit");
                return;
            }
        } while (true);
    }
}
