import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        final int PORT = 8000;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Waiting for connection on port " + PORT + "...");
            
            // Wait for a single client to connect
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client Accepted: " + clientSocket.getInetAddress().getHostAddress());

            // Use try-with-resources to automatically manage and close all streams
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true); // Added 'true' for auto-flush
                 BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {

                // --- RECEIVING THREAD ---
                // This thread's only job is to listen for client messages
                Thread receiveThread = new Thread(() -> {
                    try {
                        String clientMessage;
                        while ((clientMessage = in.readLine()) != null) {
                            System.out.println("Client: " + clientMessage);
                        }
                    } catch (IOException e) {
                        // This often happens when the client disconnects
                        System.out.println("Client disconnected or read error: " + e.getMessage());
                    }
                });
                
                // Start the receiving thread immediately
                receiveThread.start();

                // --- SENDING LOGIC (on Main Thread) ---
                // This loop reads from the server's console and sends to the client
                String serverMessage;
                System.out.println("Enter messages to send to the client (type '/exit' to stop):");
                
                while ((serverMessage = consoleReader.readLine()) != null) {
                    if ("/exit".equalsIgnoreCase(serverMessage)) {
                        break; // Exit the loop
                    }
                    out.println(serverMessage); // Send the correct message to the client
                }

            } // All client-related streams (in, out, consoleReader) and clientSocket are auto-closed here
              catch (IOException e) {
                System.err.println("I/O error with client: " + e.getMessage());
            }

        } catch (IOException e) {
            System.err.println("Server error (could not start): " + e.getMessage());
        }
        
        System.out.println("Server shutting down.");
    }
}


























