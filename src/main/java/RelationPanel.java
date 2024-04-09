
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class RelationPanel extends JFrame {
    private int imageId;
    private List<Annotation> annotations;
    private List<Rel_category> rel_categories;
    private JComboBox obj1,obj2,relationBox;
    private JButton confirmButton = new JButton("确认添加");
    private ImagePanel imagePanel;
    private List<Category> categories = new ArrayList<>();
    RelationPanel(int imageId, List<Annotation> annotations, List<Rel_category> rel_categories, ImagePanel imagePanel){
        this.imagePanel = imagePanel;
        this.imageId = imageId;
        this.annotations = annotations;
        this.rel_categories = rel_categories;
        CategoriesLoader.loadCategory(categories);
        this.setLayout(new GridLayout(4,1));

        obj1 = new JComboBox<String>();
        obj2= new JComboBox<String>();
        relationBox = new JComboBox<String>();
        for(int index = 0; index < annotations.size(); index++){
            Annotation each = annotations.get(index);
            int cate_id = each.getCategory_id();
            String cate_name = "";
            for(Category i : categories){
                if(i.getId() == cate_id){
                    cate_name = String.valueOf(index) + "_" + i.getName();
                    break;
                }
            }
            obj1.addItem(cate_name);
            obj2.addItem(cate_name);
        }
        for(Rel_category relCategory : rel_categories){
            relationBox.addItem(relCategory.getRelation());
        }
        confirmButton.addActionListener(new Confirm());
        this.add(obj1);
        this.add(obj2);
        this.add(relationBox);
        this.add(confirmButton);

        this.setSize(800,800);
        this.setVisible(true);
    }
    private class Confirm implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int obj1Id = 0, obj2Id = 0;
//            for(Category each : categories){
//                if(each.getName() == obj1.getSelectedItem()){
//                    obj1Id = each.getId();
//                }
//                if(each.getName() == obj2.getSelectedItem()){
//                    obj2Id = each.getId();
//                }
//            }
            obj1Id = obj1.getSelectedIndex();
            obj2Id = obj2.getSelectedIndex();
            System.out.println("obj1: " + obj1.getSelectedItem() + " id: " + obj1Id);
            System.out.println("obj2: " + obj2.getSelectedItem() + " id: " + obj2Id);
            System.out.println("rel: " + relationBox.getSelectedItem() + " id: "+ relationBox.getSelectedIndex());
            JsonUtil.addTrouble(Integer.toString(imageId), new int[]{obj1Id,obj2Id, relationBox.getSelectedIndex()});
            imagePanel.refresh(imagePanel.getMyImage());

        }
    }
}
