import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Delete {
    public static JSONObject trainObject = new JSONObject();
    public static JSONObject relObject = new JSONObject();

    public static void main(String[] args) {
        try{
            JSONReader reader = new JSONReader(new FileReader("resources/Json/train.json"));
            trainObject = (JSONObject) reader.readObject();
            reader = new JSONReader(new FileReader("resources/Json/rel.json"));
            relObject = (JSONObject)reader.readObject();
            reader.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for(int i = 0; i < 690; i++){
            JSONArray array = relObject.getJSONObject("train").getJSONArray(String.valueOf(i));
            if(array != null){
                continue;
            }


            JSONArray newImages = new JSONArray();
            JSONArray images = trainObject.getJSONArray("images");
            for (int j = 0; j < images.size(); j++){
                if(images.getJSONObject(j).getIntValue("id") != i){
                    newImages.add(images.getJSONObject(j));
                }
            }
            trainObject.put("images", newImages);

            JSONArray annotations = trainObject.getJSONArray("annotations");
            JSONArray save = new JSONArray();
            for(int j = 0; j < annotations.size(); j++){
                JSONObject annotation = annotations.getJSONObject(j);
                if(annotation.getIntValue("image_id") != i){
                    save.add(annotation);
                }
            }
            trainObject.put("annotations", save);
        }
        JsonUtil.setObject(trainObject, "train.json");

    }
}
