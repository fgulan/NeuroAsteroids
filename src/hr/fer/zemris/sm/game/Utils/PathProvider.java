package hr.fer.zemris.sm.game.Utils;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Fredi Šarić on 19.01.16.
 */
public class PathProvider {

    private static final boolean IS_WINDOWS = System.getProperty("os.name").contains("indow");


    public static Path getResourcesPath(String resources) {
        Path path = null;
        try {
            String strPath = PathProvider.class.getClassLoader().getResource(resources).toURI().toString();
            path = Paths.get(strPath);
        }catch (Exception ignorable) {}

        return path;
    }


}
