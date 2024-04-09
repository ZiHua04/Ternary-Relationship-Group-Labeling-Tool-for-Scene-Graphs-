
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    ImagePanel imagePanel;
    JPanel uiPanel;
    List<Category> categorylist = new ArrayList<>();
    Category category = new Category();

    JComboBox<String> categoryBox;
    MainFrame(){
        this.setTitle("Label");
        this.setSize(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setCenter();
        imagePanel = new ImagePanel(ImageLoader.readNextImage());

        this.add(imagePanel);

        uiPanel = new JPanel(new GridLayout(5,1));
        JButton backButton = new JButton("上一张(A)");
        backButton.addActionListener(new LastImage(this));

        JButton nextButton = new JButton("下一张(D)");
        nextButton.addActionListener(new NextImage(this));
        // 创建面板，并设置布局管理器为BorderLayout
        uiPanel.add(backButton);
        uiPanel.add(nextButton);

        CategoriesLoader.loadCategory(categorylist);
        categoryBox = new JComboBox<>();
        imagePanel.setCategory(categorylist.get(0));
        for(Category each : categorylist){
            categoryBox.addItem(each.getName());
        }
        categoryBox.addItemListener(new ListenCombox(this));
        uiPanel.add(categoryBox);
        JButton setButton = new JButton("选择关系(F)");
        setButton.addActionListener(new SetRelation(this));
        JButton delButton = new JButton("删除框(X)");
        delButton.addActionListener(new DeleteAnnotation(this));
        uiPanel.add(setButton);
        uiPanel.add(delButton);
        this.add(uiPanel,BorderLayout.EAST);
        this.setVisible(true);

        this.addKeyListener(new MyKeyListener());
        this.requestFocus();
        // 添加窗口焦点监听器

    }
    private void setCenter(){
        // 获取屏幕的尺寸
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // 计算 JFrame 的位置
        int x = (screenWidth - Config.SCREEN_WIDTH) / 2;
        int y = (screenHeight - Config.SCREEN_HEIGHT) / 2;

        this.setLocation(x, y);
    }
    private  class SetRelation implements ActionListener{
        JFrame frame;
        SetRelation(JFrame frame){
            this.frame = frame;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            List<Rel_category> list = new ArrayList<>();
            CategoriesLoader.loadRelCategory(list);
            new RelationPanel(imagePanel.getMyImage().getId(),imagePanel.getAnnotations(),list, imagePanel);
            System.out.println("选择关系");
            frame.requestFocus();
        }
    }
    public class DeleteAnnotation implements  ActionListener{
        JFrame frame;
        DeleteAnnotation(JFrame frame){
            this.frame = frame;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            imagePanel.deleteAnnotation();
            frame.requestFocus();
        }
    }
    public class LastImage implements ActionListener{
        JFrame frame;
        LastImage(JFrame frame){
            this.frame = frame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(ImageLoader.currentIndex > 0){
                imagePanel.lastImage(ImageLoader.readLastImage());
                frame.requestFocus();
            }
        }
    }
    public class NextImage implements ActionListener{
        JFrame frame;
        NextImage(JFrame frame){
            this.frame = frame;
        }
        @Override
        public void actionPerformed(ActionEvent e) {

            if(ImageLoader.currentIndex <= ImageLoader.savedIndex){
                imagePanel.nextImage(ImageLoader.readNextImage(), false);
            }
            else{
                imagePanel.nextImage(ImageLoader.readNextImage(), true);
            }
            frame.requestFocus();
        }
    }
    private class ListenCombox implements ItemListener {
        JFrame frame;
        ListenCombox(JFrame frame){
            this.frame = frame;
        }
        @Override
        public void itemStateChanged(ItemEvent e) {
            System.out.println(e.getItem());
            for(Category category1: categorylist){
                if(category1.getName() == e.getItem()){
                    category = category1;
                    imagePanel.setCategory(category1);
                    frame.requestFocus();
                    return;
                }
            }

        }
    }
    //监听键盘类
    private class MyKeyListener implements KeyListener{

        @Override
        public void keyTyped(KeyEvent e) {
            char charA=e.getKeyChar();
//            System.out.println("你按了《"+charA+"》键");
            switch (charA){
                case 'a':
                    if(ImageLoader.currentIndex > 0){
                        imagePanel.lastImage(ImageLoader.readLastImage());
                    }
                    break;
                case 'd':
                    if(ImageLoader.currentIndex <= ImageLoader.savedIndex){
                        imagePanel.nextImage(ImageLoader.readNextImage(), false);
                    }
                    else{
                        imagePanel.nextImage(ImageLoader.readNextImage(), true);
                    }
                    break;
                case 'f':
                    List<Rel_category> list = new ArrayList<>();
                    CategoriesLoader.loadRelCategory(list);
                    new RelationPanel(imagePanel.getMyImage().getId(),imagePanel.getAnnotations(),list, imagePanel);
                    System.out.println("选择关系");
                    break;
                case 'x':
                    imagePanel.deleteAnnotation();
                    break;
            }
            if (charA <= '9' && charA > '0'){
                int index = Character.valueOf(charA) - '0' - 1;
                if(index < categorylist.size()){
                    category = categorylist.get(index);
                    imagePanel.setCategory(category);
                    categoryBox.setSelectedIndex(index);
//                    categoryBox.setSelectedItem(category.getName());
                    imagePanel.repaint();
                }
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

}
