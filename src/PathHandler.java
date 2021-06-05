import java.io.File;

public class PathHandler {
    public static String getBasePath() {
        return new File("").getAbsolutePath();
    }

    public static String getRelativePath(String fullPath) {
        return fullPath.replace(getBasePath(), "");
    }

    public static String getFullPath(String relativePath) {
        return getBasePath().concat(relativePath).replace("/","\\");
    }
}