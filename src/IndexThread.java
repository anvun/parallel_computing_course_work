import java.io.IOException;

public class IndexThread extends Thread{
    private int startIndex;

    public IndexThread(int startIndex) {
        this.startIndex = startIndex;
    }

    @Override
    public void run() {
        for(int i = startIndex; i < Server.index.allPaths.size(); i+= Server.THREAD_COUNT) {
            try {
                Server.index.AddWords(Server.index.allPaths.get(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}