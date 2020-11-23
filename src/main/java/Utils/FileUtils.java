package Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
    public static void createNewDir(String dirName){
        Path path = Paths.get(dirName);
        if(!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeByteToImage(String dir, String name, byte[] img){
        Path destinationFile = Paths.get(dir, name);
        FileUtils.createNewDir(dir);
        try {
            Files.write(destinationFile, img);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getPath(String dir, String name){
        return String.format("%s//%s", dir, name);
    }
}