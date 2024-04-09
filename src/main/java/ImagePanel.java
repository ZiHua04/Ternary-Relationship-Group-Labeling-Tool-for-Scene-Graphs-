

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
    private int startX, startY, endX, endY, existX, existY, nowX, nowY;
    public boolean isDraw;
    private List<Annotation> annotations = new ArrayList<>();
    private JSONArray categoryList = new JSONArray();
    private JSONArray relList = new JSONArray();
    private JSONArray relArray = new JSONArray();
    private Category category;

    public ImagePanel(MyImage myImage) {

        this.myImage = myImage;
        this.relList = JsonUtil.relObject.getJSONArray("rel_categories");
        this.categoryList = JsonUtil.trainObject.getJSONArray("categories");
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
//        for (int i = 0; i < imageArray.size()-1; i++) {
//            this.nextImage(ImageLoader.readNextImage(),false);
//        }
        this.nextImage(ImageLoader.loadImageByIndex(imageArray.size()-1), false);
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
        @Override
        public void mouseMoved(MouseEvent e){
            nowX = e.getX();
            nowY = e.getY();
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
            g.setColor(Color.CYAN);
            g.setFont(new Font("宋体", Font.BOLD, 30));
            g.drawString(categoryList.getJSONObject(each.getCategory_id()-1).getString("name"), x+10 ,y);
            g.setFont(new Font("宋体", Font.PLAIN, 14));

            g.setColor(Color.RED);
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
        if(!isDraw){
            g.drawString(category.getName(), nowX, nowY);
        }
        if(relArray == null) return;
        for (int i = 0; i < relArray.size(); i++) {
            JSONArray rel = relArray.getJSONArray(i);
            g.setColor(Color.GREEN);
            Annotation startAnnotation = annotations.get((Integer) rel.get(0));
            Annotation endAnnotation =  annotations.get((Integer) rel.get(1));
            int startX = (int)(startAnnotation.getBbox()[0] * myImage.getIndex());
            int startY = (int)(startAnnotation.getBbox()[1] * myImage.getIndex());
            int endX = (int)(endAnnotation.getBbox()[0] * myImage.getIndex());
            int endY = (int)(endAnnotation.getBbox()[1] * myImage.getIndex());
            g.drawLine(startX,startY, endX, endY);
            g.setColor(Color.MAGENTA);
            g.setFont(new Font("宋体", Font.BOLD, 30));
            g.drawString((String) this.relList.get((Integer) rel.get(2)), (startX+endX)/2, (startY+endY)/2);
            g.setFont(new Font("宋体", Font.PLAIN, 14));
        }
        g.setColor(Color.RED);
    }
    public void deleteAnnotation(){
        if(this.annotations == null || this.annotations.isEmpty()) return;
        JSONArray annotationsArray = JsonUtil.trainObject.getJSONArray("annotations");


        for (int i = 0; i < annotationsArray.size(); i++) {
            if(annotationsArray.getJSONObject(i).getIntValue("id") == this.annotations.get(this.annotations.size()-1).getId()){
                annotationsArray.remove(i);
                break;
            }
        }
        JsonUtil.trainObject.put("annotations",annotationsArray);
        JsonUtil.setObject(JsonUtil.trainObject,"train.json");
        JSONObject relTrain = JsonUtil.relObject.getJSONObject("train");
        JSONArray relArray = relTrain.getJSONArray(Integer.toString(myImage.getId()));
        if(relArray != null){
            JSONArray newArray = new JSONArray();
            for(int i = 0; i < relArray.size(); i++){
                if((Integer) relArray.getJSONArray(i).get(0) != this.annotations.size()-1 && (Integer) relArray.getJSONArray(i).get(1) != this.annotations.size()-1){
                    newArray.add(relArray.getJSONArray(i));
                }
            }
            relTrain.put(Integer.toString(myImage.getId()), newArray);
            JsonUtil.relObject.put("train", relTrain);
            JsonUtil.setObject(JsonUtil.relObject, "rel.json");
        }


        this.annotations.remove(this.annotations.size()-1);
        this.refresh(myImage);
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
    public void refresh(MyImage myImage){
        this.myImage = myImage;
        this.relArray = JsonUtil.relObject.getJSONObject("train").getJSONArray(Integer.toString(myImage.getId()));
        this.setSize((int)myImage.getRelWidth(), (int)myImage.getRelHeight());
        annotations.clear();
        this.annotations = JsonUtil.getAnnotationsById(this.myImage.getId());
        this.repaint();
    }
    public void lastImage(MyImage myImage){
        this.myImage = myImage;
        this.relArray = JsonUtil.relObject.getJSONObject("train").getJSONArray(Integer.toString(myImage.getId()));
        this.setSize((int)myImage.getRelWidth(), (int)myImage.getRelHeight());
        annotations.clear();
        this.annotations = JsonUtil.getAnnotationsById(this.myImage.getId());
        this.repaint();
    }
    public void nextImage(MyImage myImage, boolean isSave) {
        this.myImage = myImage;
        this.relArray = JsonUtil.relObject.getJSONObject("train").getJSONArray(Integer.toString(myImage.getId()));
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
