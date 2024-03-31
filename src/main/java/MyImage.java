

import java.awt.*;

public class MyImage {

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    private Image image;
    private String file_name;
    private int height;

    public float getIndex() {
        return index;
    }

    public void setIndex(float index) {
        this.index = index;
    }

    private float index;//缩放比例：rel:real

    public float getRelWidth() {
        return relWidth;
    }

    public void setRelWidth(float relWidth) {
        this.relWidth = relWidth;
    }

    public float getRelHeight() {
        return relHeight;
    }

    public void setRelHeight(float relHeight) {
        this.relHeight = relHeight;
    }

    private float relWidth;
    private float relHeight;

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int width;
    private int id;
}
