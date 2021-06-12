import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InvertedIndex {
    public Map<String, List<String>> indexMap;
    public List<Path> allPaths;

    public InvertedIndex() {
        indexMap = new ConcurrentHashMap<>();
        allPaths = Collections.synchronizedList(new ArrayList());
    }

    public void getAllPaths(Path root) {
        try (Stream<Path> paths = Files.walk(root)) {
            allPaths = paths.parallel()
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void AddWords(Path path) throws IOException {
        try (BufferedReader read = Files.newBufferedReader(path)) {
            String line;
            while((line = read.readLine()) != null) {
                String[] words = Split(line);
                for(String word : words) {
                    if(!word.isEmpty()) {
                        Add(word,
                                PathHandler.getRelativePath(path.toString()).replace("\\", "/"));
                    }
                }
            }
        }
    }

    private void Add(String word, String path) {
        indexMap.putIfAbsent(word, Collections.synchronizedList(new ArrayList()));

        if(!indexMap.get(word).contains(path)) {
            indexMap.get(word).add(path);
        }
    }

    public static String[] Split(String text) {
        text = Clean(text);
        String [] words;
        if(!(text.length() == 0)) {
            words = text.split("(?U)\\p{Space}+");
        }
        else {
            words = new String [] {};
        }
        return words;
    }

    private static String Clean(String word) {
        word = word.toLowerCase().trim();
        word = word.replaceAll("\\<.*?\\>", " ");
        word = word.replaceAll("(?U)[^\\p{Alnum}\\p{Space}]+", "");
        return word;
    }
}