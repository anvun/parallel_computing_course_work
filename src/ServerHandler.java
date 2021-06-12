import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerHandler extends Thread {
    private final ObjectOutputStream outObject;
    private final ObjectInputStream inObject;
    public ServerHandler(Socket socket) throws Exception {
        this.inObject = new ObjectInputStream(socket.getInputStream());
        this.outObject = new ObjectOutputStream(socket.getOutputStream());
        start();
    }

    public void run() {
        try {
            while (true) {
                String query = (String) inObject.readObject();
                String[] result = Search.Find(query);
                outObject.writeObject(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
