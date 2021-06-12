import com.google.gson.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Search {
    public static String[] Find(String query) throws IOException {
        String [] queryWords = InvertedIndex.Split(query);

        JsonParser parser = new JsonParser();

        JsonObject json = (JsonObject) parser.parse(new FileReader(Server.OUTPUT));

        ArrayList<ArrayList<String>> listOfLists = new ArrayList<>();
        for (String word : queryWords) {
            JsonArray paths = (JsonArray) json.get(word);
            ArrayList<String> list = new ArrayList<>();

            if (paths != null) {
                for (int i = 0; i < paths.size(); i++) {
                    list.add(paths.get(i).getAsString());
                }
            }
            listOfLists.add(list);
        }

        return Intersection(listOfLists);
    }

    public static String[] Intersection(ArrayList<ArrayList<String>> lists) {
        HashSet<String> intersectionSet = new HashSet<>(lists.get(0));
        for (int i = 1; i < lists.size(); i++) {
            HashSet<String> set = new HashSet<>(lists.get(i));
            intersectionSet.retainAll(set);
        }
        return intersectionSet.toArray(new String[0]);//new ArrayList<>(intersectionSet);
    }
}