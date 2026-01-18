import java.io.*;
import java.net.*;

public class SimpleHttpServer {
    public static void main(String[] args) {
        int port = 8080;  // Use 8080 to avoid root privileges needed for 80
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on http://localhost:" + port);
            while (true) {  // Infinite loop to handle multiple clients
                try (Socket clientSocket = serverSocket.accept()) {  // Accept connection
                    // Get input/output streams (TCP socket pipes)
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    OutputStream out = clientSocket.getOutputStream();

                    // Read request line (e.g., "GET / HTTP/1.1")
                    String requestLine = in.readLine();
                    if (requestLine != null) {
                        String[] parts = requestLine.split(" ");
                        String method = parts[0];
                        String path = parts[1];

                        // Simple handling: Only GET, ignore path for now
                        if ("GET".equals(method)) {
                            // Send response
                            String responseBody = "<html><body>Hello, World!</body></html>";
                            String headers = "HTTP/1.1 200 OK\r\n" +
                                    "Content-Type: text/html\r\n" +
                                    "Content-Length: " + responseBody.length() + "\r\n" +
                                    "\r\n";  // Blank line separates headers from body
                            out.write(headers.getBytes());
                            out.write(responseBody.getBytes());
                        } else {
                            // Basic error for non-GET
                            out.write("HTTP/1.1 405 Method Not Allowed\r\n\r\n".getBytes());
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error handling client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}