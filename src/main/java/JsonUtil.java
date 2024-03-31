
import com.alibaba.fastjson.*;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class JsonUtil {
    public static JSONObject relObject;
    public static JSONObject trainObject;
    public static JSONObject testObject;
    public static JSONObject valObject;
    JsonUtil(){
        try {
            // 创建 JSONReader 对象并指定文件路径
            JSONReader reader = new JSONReader(new FileReader("resources/Json/rel.json"));
            JSONReader reader1 = new JSONReader(new FileReader("resources/Json/train.json"));
            JSONReader reader2 = new JSONReader(new FileReader("resources/Json/test.json"));
            JSONReader reader3 = new JSONReader(new FileReader("resources/Json/val.json"));

            // 开始解析 JSON 文件，将内容转换为 JSONObject
            relObject = (JSONObject) reader.readObject();
            trainObject = (JSONObject) reader1.readObject();
            testObject = (JSONObject) reader2.readObject();
            valObject = (JSONObject) reader3.readObject();

            // 关闭 JSONReader
            reader.close();
            //System.out.println(relObject.toJSONString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Rel_category> list = new ArrayList<>();
        CategoriesLoader.loadRelCategory(list);
        JSONArray array = new JSONArray();
        for (Rel_category each: list) {
            array.add(each.getRelation());
        }
        relObject.put("rel_categories", array);
        setObject(relObject,"rel.json");

        List<Category> categoryList = new ArrayList<>();
        CategoriesLoader.loadCategory(categoryList);
        JSONArray categoryArray = new JSONArray();
        for (Category each : categoryList){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("supercategory", each.getSupercategory());
            jsonObject.put("id", each.getId());
            jsonObject.put("name", each.getName());
            categoryArray.add(jsonObject);
        }
        trainObject.put("categories", categoryArray);
        setObject(trainObject, "train.json");

    }
    public static List<Annotation> getAnnotationsById(int imageId){
        JSONArray annotationArray = trainObject.getJSONArray("annotations");
        JSONArray needArray = new JSONArray();
        for (int i = 0; i < annotationArray.size(); i++) {
            JSONObject object = annotationArray.getJSONObject(i);
            if(imageId == object.getIntValue("image_id")){
                needArray.add(object);
            }
        }
        List<Annotation> list = needArray.toJavaList(Annotation.class);
        return list;
    }
    public static void add (JSONObject jsonObject, String name, String filename){
        JSONObject object = new JSONObject();
        switch (filename){
            case "train.json":{
                object = trainObject;
                break;
            }
            case "test.json":{
                object = testObject;
                break;
            }
            case "val.json":{
                object = valObject;
                break;
            }
        }
        JSONArray array = object.getJSONArray(name);
        array.add(jsonObject);
        object.put(name, array);
        setObject(object,filename);
    }
    public static void setObject(JSONObject newObject, String filename){
        String head = "resources/Json/";
        try {
            // 创建 JSONWriter 对象并指定文件路径
            JSONWriter writer = new JSONWriter(new FileWriter(head + filename));
            writer.config(SerializerFeature.PrettyFormat, true);
            writer.config(SerializerFeature.WriteMapNullValue,true);
            // 将 JSONObject 写入到文件
            writer.writeObject(newObject);

            // 关闭 JSONWriter
            writer.close();
            // 创建 JSONReader 对象并指定文件路径
            JSONReader reader = new JSONReader(new FileReader("resources/Json/rel.json"));
            JSONReader reader1 = new JSONReader(new FileReader("resources/Json/train.json"));
            JSONReader reader2 = new JSONReader(new FileReader("resources/Json/test.json"));
            JSONReader reader3 = new JSONReader(new FileReader("resources/Json/val.json"));

            // 开始解析 JSON 文件，将内容转换为 JSONObject
            relObject = (JSONObject) reader.readObject();
            trainObject = (JSONObject) reader1.readObject();
            testObject = (JSONObject) reader2.readObject();
            valObject = (JSONObject) reader3.readObject();
            System.out.println("filename 已成功保存到文件！");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void addTrouble(String imageId, int[] trouble){
        JSONObject train = relObject.getJSONObject("train");
        JSONObject val = relObject.getJSONObject("val");
        JSONObject test = relObject.getJSONObject("test");
        if(train.containsKey(imageId)){
            JSONArray array = train.getJSONArray(imageId);
            array.add(trouble);
            train.put(imageId,array);
        } else if (val.containsKey(imageId)) {
            JSONArray array = val.getJSONArray(imageId);
            array.add(trouble);
            val.put(imageId,array);
        } else if (test.containsKey(imageId)) {
            JSONArray array = test.getJSONArray(imageId);
            array.add(trouble);
            test.put(imageId,array);
        }else{
            JSONArray array = new JSONArray();
            array.add(trouble);
            Random random = new Random();
            int num = random.nextInt(100);
            train.put(imageId,array);
        }
        relObject.put("train",train);
        relObject.put("test",test);
        relObject.put("val",val);
        setObject(relObject,"rel.json");


    }
}
