/* ECE 422 Project 2
 * Jenna Hatchard
 * Server Manager
 */

import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;

public class ServerManager {

    public static void main(String[] args) throws IOException {
        int portNumber = 16000;
        Server_n server;
        ServerSocket serverSocket = new ServerSocket(portNumber);
        try {
            while (true) {
                //try to connect to a client
                System.out.println("Trying to connect");
                new Server_n(serverSocket.accept()).start();
                System.out.println("Connected");
            }
        } catch (Exception e) {
            System.out.println("new Server fail");
        } finally {
            serverSocket.close();
        }
    }

}
