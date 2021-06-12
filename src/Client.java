import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final int PORT = 7070;
    private final ObjectOutputStream outObject;
    private final ObjectInputStream inObject;

    public static void main(String[] args) throws Exception{
        new Client();
    }

    public Client() throws Exception{
        Socket socket = new Socket("localhost", PORT);
        this.outObject = new ObjectOutputStream(socket.getOutputStream());
        this.inObject = new ObjectInputStream(socket.getInputStream());
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Search indexed files:");
            Scanner in = new Scanner(System.in);
            String query = in.nextLine();

            if (query.equals("Stop")) break;

            outObject.writeObject(query);
            String[] result = (String[]) inObject.readObject();

            if(result.length != 0){
                System.out.println("Matching files:");
                for(String path : result){
                    System.out.println(PathHandler.getFullPath(path));
                }
            }
            else System.out.println("No matching files");
        }
        socket.close();
        inObject.close();
        outObject.close();
    }
}
