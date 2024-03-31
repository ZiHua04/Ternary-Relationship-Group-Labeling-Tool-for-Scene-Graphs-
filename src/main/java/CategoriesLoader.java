

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class CategoriesLoader {
     private static String cate_filepath = "resources/categories.txt";
     private static String rel_filepath = "resources/rel_categories.txt";
     public static void loadRelCategory(List<Rel_category> list){
          Path path = Paths.get(rel_filepath);
          try{
               Scanner scanner = new Scanner(path);
               int id = 0;
               while(scanner.hasNextLine()){
                    //process each line
                    String line = scanner.nextLine();
                    Rel_category rel_category = new Rel_category();
                    rel_category.setId(id);
                    rel_category.setRelation(line);
                    list.add(rel_category);
                    //System.out.println(line);

                    id++;
               }
               scanner.close();
          }catch (IOException e) {
               System.out.println("文件未找到：" + e.getMessage());
          }
     }
     public static void loadCategory(List<Category> list) {
          Path path = Paths.get(cate_filepath);
          try{
               Scanner scanner = new Scanner(path);
               int id = 1;
               while(scanner.hasNextLine()){
                    //process each line
                    String line = scanner.nextLine();
                    Category category = new Category();
                    category.setId(id);
                    category.setName(line);
                    category.setSupercategory(line);
                    list.add(category);
                    //System.out.println(line);

                    id++;
               }
               scanner.close();
          }catch (IOException e) {
               System.out.println("文件未找到：" + e.getMessage());
          }


     }
}
