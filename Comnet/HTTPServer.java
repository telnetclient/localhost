import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.Headers;

import java.io.*;
import java.net.InetSocketAddress;

public class HTTPServer {
    public static void main(String[] args) throws IOException {
        int port = 8080; // Replace with your desired port
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/upload", new UploadHandler());
        server.setExecutor(null); // Use the default executor
        server.start();
        System.out.println("HTTP Server started on port " + port);
    }

    private static class UploadHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(200, 0);

            try (InputStream inputStream = exchange.getRequestBody();
                 DataInputStream dataInputStream = new DataInputStream(inputStream);
                 OutputStream outputStream = exchange.getResponseBody();
                 DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {

                String fileName = dataInputStream.readUTF();
                if (fileName.equalsIgnoreCase("exit"))
                    System.exit(0);
                long fileSize = dataInputStream.readLong();
                System.out.printf("\nReceiving file: %s (Size: %d bytes).\n\n", fileName, fileSize);

                try (FileOutputStream fileOutputStream = new FileOutputStream("received_" + fileName)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead);
                        System.out.write(buffer, 0, bytesRead);
                    }
                    System.out.println();
                }

                System.out.println("\nFile Received Successfully.");
                dataOutputStream.writeBytes("File received successfully.");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                exchange.close();
            }
        }
    }
}
