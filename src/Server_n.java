import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by insanekillah on 2017-04-10.
 */
public class Server_n extends Thread {


    private BufferedReader console;
    private InputStreamReader in;
    SocketPortal portal;
    Socket socket;

    public Server_n(Socket s) throws Exception {
        this.in = new InputStreamReader(System.in);
        this.console = new BufferedReader(this.in);

        this.socket = s;  //new Socket("localhost", 16000);
        this.portal = new SocketPortal(this.socket);

        // keygen and share
        portal.receive_key_server();

        receiveMessages();
    }

    // receive file
    public void receiveMessages() {

        System.out.println("Now receiving messages :D");

        while (true) {
            msg newmsgobject = null;
            newmsgobject = portal.receive();

            System.out.println("received! msg of type " + newmsgobject.type);

            if (newmsgobject.type == 4) {
                // get file
                String filename = new String(newmsgobject.message);
                try {
                     this.sendFile(filename);
                } catch (FileNotFoundException e ) {
                    System.out.println("idk why it would throw");
                } catch (IOException e ) {
                    System.out.println("Error with receiving file name");
                }
            }

            if (newmsgobject.type == 1) {
                // get usernamepassword
                String usernamepassword = new String(newmsgobject.message);
                String username = usernamepassword.split(",")[0];
                String password = usernamepassword.split(",")[1];

                // check username and password for authentication
                if (true) {
                    portal.send(new msg(2));
                } else {
                    portal.send(new msg(3));
                }
            }
        }
    }

    // send message
    public void sendFile(String filename) throws IOException {

        msg m;
        System.out.println("the file: " + filename);

        if (new File("serverfiles/" + filename).exists()) {
            m = new msg(5);
            portal.send(m);

            m = null;
            Path path = Paths.get("serverfiles/" + filename);
            byte[] data = Files.readAllBytes(path);

            m = new msg(5, data);

        } else {
            m = new msg(6);
        }

        portal.send(m);
    }


}
