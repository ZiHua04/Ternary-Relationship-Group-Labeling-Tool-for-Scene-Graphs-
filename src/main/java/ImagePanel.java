

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImagePanel extends JPanel {
    private MyImage myImage;
    private int startX, startY, endX, endY, existX, existY;
    public boolean isDraw;
    private List<Annotation> annotations = new ArrayList<>();
    private Category category;

    public ImagePanel(MyImage myImage) {
        this.myImage = myImage;
        this.annotations = JsonUtil.getAnnotationsById(this.myImage.getId());
        this.setSize((int)myImage.getRelWidth(), (int)myImage.getRelHeight());
        this.startX = 0;
        this.startY = 0;
        this.endX = 0;
        this.endY = 0;
        addMouseMotionListener(new MyMouseAdapter());
        addMouseListener(new MyMouserListener());
//        addMouseListener(new MyMouseAdapter());


        JSONArray imageArray = JsonUtil.trainObject.getJSONArray("images");
        for (int i = 0; i < imageArray.size()-1; i++) {
            this.nextImage(ImageLoader.readNextImage(),false);
        }
        if(imageArray.size() > myImage.getId()){return;}
        JSONObject imageObject = new JSONObject();
        imageObject.put("file_name", myImage.getFile_name());
        imageObject.put("height",myImage.getHeight());
        imageObject.put("width", myImage.getWidth());
        imageObject.put("id",myImage.getId());
        JsonUtil.add(imageObject,"images","train.json");


    }
    class MyMouserListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            startX = e.getX();
            startY = e.getY();
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            isDraw = false;
            //System.out.println("释放鼠标，现在isDraw："+ isDraw);
            endX = e.getX();
            endY = e.getY();

            // 计算框的位置和大小
            int x = Math.min(startX, endX);
            int y = Math.min(startY, endY);
            int width = Math.abs(endX - startX);
            int height = Math.abs(endY - startY);

            // 打印框的位置和大小
            Annotation annotation = new Annotation();
            int[] bbox = new int[4];
            bbox[0] = (int)(x / myImage.getIndex());
            bbox[1] = (int)(y / myImage.getIndex());
            bbox[2] = (int)((x + width) / myImage.getIndex());
            bbox[3] = (int)((y + height) / myImage.getIndex());
            annotation.setBbox(bbox);
            annotation.setImage_id(myImage.getId());
            annotation.setArea((int)(width * height / myImage.getIndex() / myImage.getIndex()));
            annotation.setCategory_id(category.getId());
            addAnnotation(annotation);
//            annotations.add(annotation);
            System.out.println("框的位置：(" + x + ", " + y + "), 大小：" + width + " x " + height);
            repaint();
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
    class MyMouseAdapter extends MouseAdapter {
        @Override
        public void mouseDragged(MouseEvent e){
            isDraw = true;
            existX = e.getX();
            existY = e.getY();
            //System.out.println("X:" + existX + "Y:" + existY);
            repaint();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 绘制图片
        g.drawImage(myImage.getImage(), 0, 0, (int) myImage.getRelWidth(), (int) myImage.getRelHeight(), null);

        // 绘制框
        g.setColor(Color.RED);
        for(Annotation each : annotations){
            int x = (int)(each.getBbox()[0] * myImage.getIndex());
            int y = (int)(each.getBbox()[1] * myImage.getIndex());
            int m = (int)(each.getBbox()[2] * myImage.getIndex());
            int n = (int)(each.getBbox()[3] * myImage.getIndex());
            int width = Math.abs(m - x);
            int height = Math.abs(n - y);
            g.drawRect(x, y, width, height);
            g.drawString(Integer.toString(each.getCategory_id()),x,y);
            g.drawString(Arrays.toString(each.getBbox()),m,n);
            g.drawString(Integer.toString(each.getArea()),m,y);
            //g.drawString(each.toString(),0,0);
        }
        if(isDraw){
            // 计算框的位置和大小
            int x = Math.min(startX, existX);
            int y = Math.min(startY, existY);
            int width = Math.abs(existX - startX);
            int height = Math.abs(existY - startY);
            g.drawRect(x,y,width,height);
            g.setColor(Color.BLUE);
            g.setFont(new Font("宋体",Font.BOLD, 30));
            g.drawString(category.getName(),x,y);
        }
    }
    public void deleteAnnotation(){
        if(this.annotations == null || this.annotations.isEmpty()) return;
        JSONArray annotationsArray = JsonUtil.trainObject.getJSONArray("annotations");
        annotationsArray.remove(annotationsArray.size()-1);
        JsonUtil.trainObject.put("annotations",annotationsArray);
        JsonUtil.setObject(JsonUtil.trainObject,"train.json");
        this.annotations.remove(this.annotations.size()-1);
        repaint();
    }
    public void addAnnotation(Annotation annotation)
    {
        this.annotations.add(annotation);
        JSONObject jsonObject = new JSONObject();
        JSONArray annotationsArray = JsonUtil.trainObject.getJSONArray("annotations");
        if(annotationsArray.size() == 0){ jsonObject.put("id", 0);}
        else{
            JSONObject lastObject = annotationsArray.getJSONObject(annotationsArray.size()-1);
            jsonObject.put("id", lastObject.getIntValue("id") + 1);

        }

        jsonObject.put("segmentation", null);
        jsonObject.put("area", annotation.getArea());
        jsonObject.put("bbox",annotation.getBbox());
        jsonObject.put("iscrowd",0);
        jsonObject.put("image_id",annotation.getImage_id());
        jsonObject.put("category_id", annotation.getCategory_id());
        JsonUtil.add(jsonObject, "annotations", "train.json");
        System.out.println(jsonObject.toJSONString());
    }


    public void nextImage(MyImage myImage, boolean isSave) {
        this.myImage = myImage;
        this.setSize((int)myImage.getRelWidth(), (int)myImage.getRelHeight());
        annotations.clear();
        this.annotations = JsonUtil.getAnnotationsById(this.myImage.getId());
        this.repaint();
        if(!isSave) return;
        JSONObject imageObject = new JSONObject();
        imageObject.put("file_name", myImage.getFile_name());
        imageObject.put("height",myImage.getHeight());
        imageObject.put("width", myImage.getWidth());
        imageObject.put("id",myImage.getId());
        JsonUtil.add(imageObject,"images","train.json");
        repaint();
    }
    public void setCategory(Category category){
        this.category = category;
    }
    public List<Annotation> getAnnotations(){
        return this.annotations;
    }
    public MyImage getMyImage(){
        return this.myImage;
    }

}
