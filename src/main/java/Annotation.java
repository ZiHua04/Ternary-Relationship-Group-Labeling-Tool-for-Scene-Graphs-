
public class Annotation {
    private Object segmentation = null;
    private int area;
    private int[] bbox = new int[4];
    private int iscrowd = 0;
    private int image_id;
    private int id ;
    private int category_id;

    public Object getSegmentation() {
        return segmentation;
    }

    public void setSegmentation(Object segmentation) {
        this.segmentation = segmentation;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public int[] getBbox() {
        return bbox;
    }

    public void setBbox(int[] bbox) {
        this.bbox = bbox;
    }

    public int getIscrowd() {
        return iscrowd;
    }

    public void setIscrowd(int iscrowd) {
        this.iscrowd = iscrowd;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }
}
