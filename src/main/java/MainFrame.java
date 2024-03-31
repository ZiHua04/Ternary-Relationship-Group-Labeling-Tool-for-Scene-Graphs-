
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    ImagePanel imagePanel;
    JPanel uiPanel;
    List<Category> categorylist = new ArrayList<>();
    Category category = new Category();
    MainFrame(){
        this.setTitle("UFO_GAME");
        this.setSize(Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setCenter();
        imagePanel = new ImagePanel(ImageLoader.readNextImage());

        this.add(imagePanel);

        uiPanel = new JPanel(new GridLayout(5,1));

        JButton nextButton = new JButton("下一张");
        nextButton.addActionListener(new NextImage());
        // 创建面板，并设置布局管理器为BorderLayout

        uiPanel.add(nextButton);

        CategoriesLoader.loadCategory(categorylist);
        JComboBox<String> categoryBox = new JComboBox<>();
        imagePanel.setCategory(categorylist.get(0));
        for(Category each : categorylist){
            categoryBox.addItem(each.getName());
        }
        categoryBox.addItemListener(new ListenCombox());
        uiPanel.add(categoryBox);
        JButton setButton = new JButton("选择关系");
        setButton.addActionListener(new SetRelation());
        JButton delButton = new JButton("删除框");
        delButton.addActionListener(new DeleteAnnotation());
        uiPanel.add(setButton);
        uiPanel.add(delButton);
        this.add(uiPanel,BorderLayout.EAST);
        this.setVisible(true);

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

        @Override
        public void actionPerformed(ActionEvent e) {
            List<Rel_category> list = new ArrayList<>();
            CategoriesLoader.loadRelCategory(list);
            new RelationPanel(imagePanel.getMyImage().getId(),imagePanel.getAnnotations(),list);
            System.out.println("选择关系");
        }
    }
    public class DeleteAnnotation implements  ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            imagePanel.deleteAnnotation();
        }
    }
    public class NextImage implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            imagePanel.nextImage(ImageLoader.readNextImage(), true);
        }
    }
    private class ListenCombox implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            System.out.println(e.getItem());
            for(Category category1: categorylist){
                if(category1.getName() == e.getItem()){
                    category = category1;
                    imagePanel.setCategory(category1);
                    return;
                }
            }
        }
    }
    //监听键盘类


}
