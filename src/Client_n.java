import java.io.*;
import java.net.Socket;

/**
 * Created by insanekillah on 2017-04-10.
 */
public class Client_n {

    private BufferedReader console;
    private InputStreamReader in;
    SocketPortal portal;
    Socket socket;

    public Client_n() throws Exception {
        this.in = new InputStreamReader(System.in);
        this.console = new BufferedReader(this.in);
        this.socket = new Socket("localhost", 16000);
        portal = new SocketPortal(socket);

        // keygen and share
        portal.send_key_client();

        // auth
        System.out.println("Please type your username: ");
        String username = System.console().readLine();
        System.out.println("Please type your password: ");
        String password = System.console().readLine();
        String message = username+","+password;
        msg m = new msg(1, message.getBytes());
        portal.send(m);

        // access or nah
        msg accessgrantedornah = portal.receive();
        if (accessgrantedornah.type == 2) {
            System.out.println("Access has been granted to the Server, request files.");
            requestFiles();
        } else if (accessgrantedornah.type == 3) {
            System.out.println("Access has not beed granted to the Server, quit.");
        }
    }

    public void requestFiles() {
        while (true) {

            // Get message from User
            System.out.println("What's your filename?: ");
            String input = System.console().readLine();
            byte[] message_bytes = input.getBytes();
            msg m = new msg(4, message_bytes);

            sendMessage(m, input);
        }
    }

    // send message
    public void sendMessage(msg msgobject, String mess) {
        portal.send(msgobject);
        this.receiveFile(mess);
    }

    // receive file
    public void receiveFile(String mess) {

        msg newmsgobject = portal.receive();

        if (newmsgobject.type == 5) {
            // message found
            msg actualmsgobject = portal.receive();
            String newfilename = new String(actualmsgobject.message);
            try {
                FileOutputStream downloader = new FileOutputStream("clientfiles/" + mess);
                downloader.write(actualmsgobject.message);
                System.out.println("Your file is now in the clientfiles/ folder");
            } catch (FileNotFoundException e ) {
                System.out.println("idk why it would throw");
            } catch (IOException e ) {
                System.out.println("unable to write to make the file.");
            }
        }

        if (newmsgobject.type == 6) {
            // file not found
            System.out.println("Your file was not found on the server.");
        }
    }

    public static void main(String[] args) throws Exception {
        new Client_n();
    }
}
