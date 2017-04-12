import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

public class SocketPortal {
    Socket socket;
    DataOutputStream out = null;
    DataInputStream in = null;
    SecurityFunctions sec = new SecurityFunctions();

    public final static int FILE_SIZE = 6022386;

    public SocketPortal(Socket socket) {
        this.socket = socket;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Failed");
        }
    }

    public void send(msg m) {

        //System.out.println("attempting to send");// filename: " + m.message.toString());

        byte[] array = ByteBuffer.allocate(m.getTotalMessageLength())
                .putInt(m.type)
                .putInt(m.length)
                .put(m.message)
                .array();

        try {

            out.write(encrypt(array));
            out.flush();

        } catch (IOException e) {
            System.out.println("IO Exception in send");
        }
    }

    public msg receive() {

        //System.out.println("attempting to receive");

        byte[] type = new byte[4];
        byte[] length = new byte[4];

        try {

            in.readFully(type);
            in.readFully(length);

        } catch (IOException e) {
            System.out.println("IO Exception in receive");
        }

        type = decrypt(type);
        length = decrypt(length);

        byte[] message = new byte[ByteBuffer.wrap(length).getInt()];

        try {

            in.readFully(message);

        } catch (IOException e) {
            System.out.println("IO Exception in receive2");
        }

        message = decrypt(message);

        msg m = new msg(ByteBuffer.wrap(type).getInt(), message);

        return m;
    }

    public void send_key_client() {

        msg m = new msg(0, sec.getPublicKey());
        System.out.println("attempting to send key");

        byte[] array = ByteBuffer.allocate(m.getTotalMessageLength())
                .putInt(m.type)
                .putInt(m.length)
                .put(m.message)
                .array();

        try {

            out.write(array);
            out.flush();

        } catch (IOException e) {
            System.out.println("IO Exception in send");
        }

        msg othersPublicKey = receive_key_client();
        if (othersPublicKey.type == 0) {
            sec.makeSecretKey(othersPublicKey.message);
        }
    }

    public void send_key_server() {

        msg m = new msg(0, sec.getPublicKey());
        System.out.println("attempting to send key");

        byte[] array = ByteBuffer.allocate(m.getTotalMessageLength())
                .putInt(m.type)
                .putInt(m.length)
                .put(m.message)
                .array();

        try {

            out.write(array);
            out.flush();

        } catch (IOException e) {
            System.out.println("IO Exception in send");
        }
    }

    public msg receive_key_client() {

        System.out.println("attempting to receive key");

        byte[] type = new byte[4];
        byte[] length = new byte[4];

        try {

            in.readFully(type);
            in.readFully(length);

        } catch (IOException e) {
            System.out.println("IO Exception in receive");
        }

        byte[] message = new byte[ByteBuffer.wrap(length).getInt()];

        try {

            in.readFully(message);

        } catch (IOException e) {
            System.out.println("IO Exception in receive2");
        }

        msg m = new msg(ByteBuffer.wrap(type).getInt(), message);

        return m;
    }

    public  void receive_key_server() {
        System.out.println("attempting to receive key");

        byte[] type = new byte[4];
        byte[] length = new byte[4];

        try {

            in.readFully(type);
            in.readFully(length);

        } catch (IOException e) {
            System.out.println("IO Exception in receive");
        }

        byte[] message = new byte[ByteBuffer.wrap(length).getInt()];

        try {

            in.readFully(message);

        } catch (IOException e) {
            System.out.println("IO Exception in receive2");
        }

        msg m = new msg(ByteBuffer.wrap(type).getInt(), message);

        sec.makeSecretKey(m.message);
        send_key_server();
    }

    private byte[] decrypt(byte[] message) {
        sec.decrypt(message);
        return message;
    }

    private byte[] encrypt(byte[] message) {
        sec.encrypt(message);
        return  message;
    }
}
