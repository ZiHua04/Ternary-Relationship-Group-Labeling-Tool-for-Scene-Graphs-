
// import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImageLoader {

    private static String folderPath = "resources/images";
    public static int currentIndex = 0;
    public static int savedIndex = 0;
    private static File[] imageFiles;
    public static MyImage readLastImage(){
        if (imageFiles == null) {
            File folder = new File(folderPath);
            if (folder.isDirectory()) {
                imageFiles = folder.listFiles(file -> isImageFile(file.getName()));
            } else {
                System.out.println("指定路径不是一个文件夹");
                return null;
            }
        }
        if (currentIndex > 0){
            currentIndex--;
            File file = imageFiles[currentIndex];
            try {
                BufferedImage image = ImageIO.read(file);
                MyImage myImage = new MyImage();
                myImage.setFile_name(file.getName());
                myImage.setImage(image);
                myImage.setHeight(image.getHeight());
                myImage.setWidth(image.getWidth());
                myImage.setId(currentIndex);
                if(myImage.getWidth() > Config.SCREEN_WIDTH){
                    myImage.setRelWidth(Config.SCREEN_HEIGHT);
                    myImage.setRelHeight(myImage.getRelWidth() * myImage.getHeight() / myImage.getWidth());
                    myImage.setIndex(myImage.getRelWidth() / myImage.getWidth());
                }
                else if(myImage.getHeight() > Config.SCREEN_HEIGHT){
                    myImage.setRelHeight(Config.SCREEN_HEIGHT);
                    myImage.setRelWidth(myImage.getRelHeight() * myImage.getWidth() / myImage.getHeight());
                    myImage.setIndex(myImage.getRelHeight() / myImage.getHeight());
                }
                else{
                    myImage.setIndex(1);
                    myImage.setRelWidth(myImage.getWidth());
                    myImage.setRelHeight(myImage.getHeight());
                }

                System.out.println("Loaded image: " + file.getName());
                savedIndex = JsonUtil.trainObject.getJSONArray("images").size()-1;
                return myImage;
            } catch (IOException e) {
                System.out.println("无法加载图片：" + file.getName());
                return null;
            }
        } else {
            System.out.println("已读取完所有图片");
            return null;
        }
    }

    public static MyImage readNextImage() {
        if (imageFiles == null) {
            File folder = new File(folderPath);
            if (folder.isDirectory()) {
                imageFiles = folder.listFiles(file -> isImageFile(file.getName()));
            } else {
                System.out.println("指定路径不是一个文件夹");
                return null;
            }
        }

        if (currentIndex < imageFiles.length) {
            
            File file = imageFiles[currentIndex];
            try {
                BufferedImage image = ImageIO.read(file);
                MyImage myImage = new MyImage();
                myImage.setFile_name(file.getName());
                myImage.setImage(image);
                myImage.setHeight(image.getHeight());
                myImage.setWidth(image.getWidth());
                myImage.setId(currentIndex);
                if(myImage.getWidth() > Config.SCREEN_WIDTH){
                    myImage.setRelWidth(Config.SCREEN_HEIGHT);
                    myImage.setRelHeight(myImage.getRelWidth() * myImage.getHeight() / myImage.getWidth());
                    myImage.setIndex(myImage.getRelWidth() / myImage.getWidth());
                }
                else if(myImage.getHeight() > Config.SCREEN_HEIGHT){
                    myImage.setRelHeight(Config.SCREEN_HEIGHT);
                    myImage.setRelWidth(myImage.getRelHeight() * myImage.getWidth() / myImage.getHeight());
                    myImage.setIndex(myImage.getRelHeight() / myImage.getHeight());
                }
                else{
                    myImage.setIndex(1);
                    myImage.setRelWidth(myImage.getWidth());
                    myImage.setRelHeight(myImage.getHeight());
                }

                System.out.println("Loaded image: " + file.getName());

                currentIndex++;
                savedIndex = JsonUtil.trainObject.getJSONArray("images").size()-1;
                return myImage;
            } catch (IOException e) {
                System.out.println("无法加载图片：" + file.getName());
                return null;
            }
        } else {
            System.out.println("已读取完所有图片");
            return null;
        }
    }
    public static MyImage loadImageByIndex(int index){
        if (imageFiles == null) {
            File folder = new File(folderPath);
            if (folder.isDirectory()) {
                imageFiles = folder.listFiles(file -> isImageFile(file.getName()));
            } else {
                System.out.println("指定路径不是一个文件夹");
                return null;
            }
        }

        if (index < imageFiles.length) {
            currentIndex = index;
           savedIndex = JsonUtil.trainObject.getJSONArray("images").size()-1;
            File file = imageFiles[currentIndex];
            try {
                BufferedImage image = ImageIO.read(file);
                MyImage myImage = new MyImage();
                myImage.setFile_name(file.getName());
                myImage.setImage(image);
                myImage.setHeight(image.getHeight());
                myImage.setWidth(image.getWidth());
                myImage.setId(currentIndex);
                if(myImage.getWidth() > Config.SCREEN_WIDTH){
                    myImage.setRelWidth(Config.SCREEN_HEIGHT);
                    myImage.setRelHeight(myImage.getRelWidth() * myImage.getHeight() / myImage.getWidth());
                    myImage.setIndex(myImage.getRelWidth() / myImage.getWidth());
                }
                else if(myImage.getHeight() > Config.SCREEN_HEIGHT){
                    myImage.setRelHeight(Config.SCREEN_HEIGHT);
                    myImage.setRelWidth(myImage.getRelHeight() * myImage.getWidth() / myImage.getHeight());
                    myImage.setIndex(myImage.getRelHeight() / myImage.getHeight());
                }
                else{
                    myImage.setIndex(1);
                    myImage.setRelWidth(myImage.getWidth());
                    myImage.setRelHeight(myImage.getHeight());
                }

                System.out.println("Loaded image: " + file.getName());

                return myImage;
            } catch (IOException e) {
                System.out.println("无法加载图片：" + file.getName());
                return null;
            }
        } else {
            System.out.println("已读取完所有图片");
            return null;
        }
    }

    private static boolean isImageFile(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        return extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("gif");
        // 添加其他图片格式的判断，如bmp、jpeg等
    }
}
