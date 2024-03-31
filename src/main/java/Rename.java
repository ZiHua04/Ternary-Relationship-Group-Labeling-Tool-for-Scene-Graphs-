
import java.io.File;

public class Rename {
    private static int startIndex = 0;
    private static final int NUM_DIGITS = 6; // Number of digits for the file name

    public static void main(String[] args) {
        File folder = new File("resources\\images");
        File[] listOfFiles = folder.listFiles();
        int count = startIndex;

        for (File file : listOfFiles) {
            if (file.isFile() && isImageFile(file)) {
                String newFileName = String.format("%s\\%0" + NUM_DIGITS + "d%s", folder, count, getFileExtension(file));
                File newFile = new File(newFileName);
                boolean success = file.renameTo(newFile);
                if (success)
                    count++;
            }
        }
    }

    public static boolean isImageFile(File file) {
        String[] imageTypes = {".jpg", ".png", ".gif", ".jpeg", ".bmp", ".tiff", ".ico"};
        String fileName = file.getName().toLowerCase();

        for (String type : imageTypes) {
            if (fileName.endsWith(type))
                return true;
        }

        return false;
    }

    public static String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
    }
}
