

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadJsonFile
{
    public static JSONObject relObject;
    public static JSONObject trainObject;
    public static JSONObject testObject;
    public static JSONObject valObject;
    public static void main(String[] args) {
        new JsonUtil();
        try {
            // 创建 JSONReader 对象并指定文件路径
            JSONReader reader = new JSONReader(new FileReader("resources/Json/rel.json"));
            JSONReader reader1 = new JSONReader(new FileReader("resources/Json/train.json"));
            JSONReader reader2 = new JSONReader(new FileReader("resources/Json/test.json"));
            JSONReader reader3 = new JSONReader(new FileReader("resources/Json/val.json"));
            JSONReader reader4 = new JSONReader(new FileReader("resources/Json/rel.json"));
            // 开始解析 JSON 文件，将内容转换为 JSONObject
            relObject = (JSONObject) reader.readObject();
            trainObject = (JSONObject) reader1.readObject();
            testObject = (JSONObject) reader2.readObject();
            valObject = (JSONObject) reader3.readObject();
            relObject = (JSONObject) reader4.readObject();
            // 关闭 JSONReader
            reader.close();
            //System.out.println(relObject.toJSONString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONArray trainImages = trainObject.getJSONArray("images");
        JSONArray trainAnnotations = trainObject.getJSONArray("annotations");
        JSONArray trainCategories = trainObject.getJSONArray("categories");
        System.out.println("当前train中有" + trainImages.size() + "张图片，" + trainAnnotations.size() + "个标签," + trainCategories.size() + "个种类。");
        JSONArray testImages = testObject.getJSONArray("images");
        JSONArray testAnnotations = testObject.getJSONArray("annotations");
        JSONArray testCategories = testObject.getJSONArray("categories");
        System.out.println("当前test中有" + testImages.size() + "张图片，" + testAnnotations.size() + "个标签," + testCategories.size() + "个种类。");
        JSONArray valImages =valObject.getJSONArray("images");
        JSONArray valAnnotations = valObject.getJSONArray("annotations");
        JSONArray valCategories = valObject.getJSONArray("categories");
        System.out.println("当前val中有" + valImages.size() + "张图片，" + valAnnotations.size() + "个标签," + valCategories.size() + "个种类。");
        //train： val ： test = 8：1：1
        int valNum = trainImages.size() / 10;
//        valNum = valNum==0? 1 : valNum;
        int testNum = trainImages.size() / 10;
//        testNum = testNum==0? 1 : testNum;
        int trainNum = trainImages.size() - valNum - testNum;
        System.out.println("转换后train，val，test图片的比例应为：" + trainNum + ":"+ valNum + ":" + testNum);

        JSONArray newArray = new JSONArray();
        for(int i = 0; i < valNum; i++){
            JsonUtil.add(trainImages.getJSONObject(0), "images", "val.json");

            int imageId = trainImages.getJSONObject(0).getIntValue("id");

            JSONObject relTrain = relObject.getJSONObject("train");
            JSONObject relVal = relObject.getJSONObject("val");
            JSONArray relImage = relTrain.getJSONArray(String.valueOf(imageId));
            relVal.put(String.valueOf(imageId), relImage);
            relTrain.remove(String.valueOf(imageId));
            relObject.put("val", relVal);
            relObject.put("train", relTrain);
            JsonUtil.setObject(relObject, "rel.json");

            for(int j = 0; j < trainAnnotations.size(); j++){
                JSONObject annotation = trainAnnotations.getJSONObject(j);
                if(annotation.getIntValue("image_id") == imageId){
                    newArray.add(annotation);
//                    System.out.println(annotation);
                    JsonUtil.add(annotation,"annotations", "val.json");
                }


            }
            trainImages.remove(0);

        }
//        System.out.println(newArray);
        for(int i = 0; i < newArray.size(); i++){
            int id = newArray.getJSONObject(i).getIntValue("id");
            for (int j = 0; j < trainAnnotations.size(); j++) {
                if(trainAnnotations.getJSONObject(j).getIntValue("id") == id){
                    trainAnnotations.remove(j);
                    break;
                }
            }
        }
        JsonUtil.setObject(trainObject, "train.json");
        JSONArray categoriesArray = trainObject.getJSONArray("categories");
        for (int i = 0; i < categoriesArray.size(); i++) {
            JsonUtil.add(categoriesArray.getJSONObject(i), "categories", "val.json");
        }

        newArray = new JSONArray();
        for(int i = 0; i < testNum; i++){
            JsonUtil.add(trainImages.getJSONObject(0), "images", "test.json");

            int imageId = trainImages.getJSONObject(0).getIntValue("id");

            JSONObject relTrain = relObject.getJSONObject("train");
            JSONObject relTest = relObject.getJSONObject("test");
            JSONArray relImage = relTrain.getJSONArray(String.valueOf(imageId));
            relTest.put(String.valueOf(imageId), relImage);
            relTrain.remove(String.valueOf(imageId));
            relObject.put("test", relTest);
            relObject.put("train", relTrain);
            JsonUtil.setObject(relObject, "rel.json");

            for(int j = 0; j < trainAnnotations.size(); j++){
                JSONObject annotation = trainAnnotations.getJSONObject(j);
                if(annotation.getIntValue("image_id") == imageId){
                    newArray.add(annotation);
//                    System.out.println(annotation);
                    JsonUtil.add(annotation,"annotations", "test.json");
                }
            }
            trainImages.remove(0);
        }
        for(int i = 0; i < newArray.size(); i++){
            int id = newArray.getJSONObject(i).getIntValue("id");
            for (int j = 0; j < trainAnnotations.size(); j++) {
                if(trainAnnotations.getJSONObject(j).getIntValue("id") == id){
                    trainAnnotations.remove(j);
                    break;
                }
            }
        }
        JsonUtil.setObject(trainObject, "train.json");
        for (int i = 0; i < categoriesArray.size(); i++) {
            JsonUtil.add(categoriesArray.getJSONObject(i), "categories", "test.json");
        }

    }

}
