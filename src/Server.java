import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Server {
    static final int PORT = 7070;
    static String RELATIVE_PATH = "/data";
    static String OUTPUT = "output.json";
    static int THREAD_COUNT = 4;
    static InvertedIndex index = new InvertedIndex();

    public static void main(String[] args) throws IOException, InterruptedException {
        WriteToFile();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is up and running on port: " + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client has been accepted");
                ServerHandler User = new ServerHandler(socket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void WriteToFile() throws InterruptedException, IOException {
        // Fill allPaths
        index.getAllPaths(Paths.get(PathHandler.getFullPath(RELATIVE_PATH)));

        // Index files to indexMap with threads
        IndexThread[] TreadArray = new IndexThread[THREAD_COUNT];
        long m = System.currentTimeMillis();
        for(int i = 0; i < THREAD_COUNT; i++) {
            TreadArray[i] = new IndexThread(i);
            TreadArray[i].start();
        }
        for(int i = 0; i < THREAD_COUNT; i++) {
            TreadArray[i].join();
        }
        System.out.println("Indexing time with " + THREAD_COUNT + " threads: " + (System.currentTimeMillis() - m) + " ms");

        // Write to JSON
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter file = new FileWriter(OUTPUT);
        gson.toJson(index.indexMap, file);
        file.close();
    }
}