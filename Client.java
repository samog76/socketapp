import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        // Define the server address and port
        final String SERVER_HOST = "localhost";
        final int SERVER_PORT = 8000;

        try (Socket clientSocket = new Socket(SERVER_HOST, SERVER_PORT); // Fixed variable name to clientSocket
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             Scanner pen = new Scanner(System.in)) {

            System.out.println("Connected to server on " + SERVER_HOST + ":" + SERVER_PORT);
            
            // Fix 1: The variable name 'recievThread' was misspelled.
            // Fix 2: The Lambda syntax for Runnable was incorrect.
            Thread receiveThread = new Thread(() -> { 
                try {
                    String message;
                    // The loop continues as long as there are messages from the server
                    while ((message = in.readLine()) != null) {
                        System.out.println("Server: " + message);
                    }
                // Handle the case where the server closes the connection
                } catch (SocketException se) {
                    System.out.println("Connection closed by server.");
                } catch (IOException e) {
                    System.err.println("Error receiving message: " + e.getMessage());
                }
            });
            receiveThread.start();

            // Part 3: Incomplete sending logic completed here
            String userInput;
            // The loop continues as long as the user provides input
            while (pen.hasNextLine()) {
                userInput = pen.nextLine();
                if (userInput.equalsIgnoreCase("exit")) {
                    break; // Exit the loop and close the socket
                }
                out.println(userInput); // Send the message to the server
            }

        } catch (IOException e) {
             // Catch and handle the specific IOException for better error reporting
             System.err.println("Client error: Could not connect to server or I/O error occurred: " + e.getMessage());
        }
        System.out.println("Client disconnected.");
    }
}
