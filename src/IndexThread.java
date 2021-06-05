import java.io.IOException;

public class IndexThread extends Thread{
    private int startIndex;

    public IndexThread(int startIndex) {
        this.startIndex = startIndex;
    }

    @Override
    public void run() {
        for(int i = startIndex; i < Main.index.allPaths.size(); i+= Main.THREAD_COUNT) {
            try {
                Main.index.AddWords(Main.index.allPaths.get(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}