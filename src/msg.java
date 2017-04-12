/**
 * Created by insanekillah on 2017-04-10.
 */
public class msg {
    int type;
    int length;
    byte[] message;

    // key share 0
    // auth 1
    // access granted 2
    // access denied 3
    // file req 4
    // file found 5
    // file not found 6

    public msg(int t) {
        type = t;
        message = new String("").getBytes();
        length = message.length;
    }

    public msg(int t, byte[] m) {
        type = t;
        length = m.length;
        message = m;
    }

    public int getTotalMessageLength() {
        return length + 4*2;
    }
}
