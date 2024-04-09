import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Temp {
    public static JSONObject relObject = new JSONObject();
   // public static JSONObject trainObject = new JSONObject();
    public static void main(String[] args) {

        try{
            JSONReader reader = new JSONReader(new FileReader("resources/Json/rel.json"));

            relObject = (JSONObject)reader.readObject();
            //reader = new JSONReader(new FileReader("resources/Json/train.json"));
            //trainObject = (JSONObject) reader.readObject();
            reader.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        JSONObject trainObject = relObject.getJSONObject("train");
        for(int i = 0; i < 690; i++){
            JSONArray array = trainObject.getJSONArray(String.valueOf(i));
            if(array == null){
                System.out.println(i);

            }
            else{
                for(int j = 0; j < array.size(); j++){
                    JSONArray a = array.getJSONArray(j);
                    int temp = (int)a.get(2) + 1;
                    a.set(2, temp);
                    array.set(j, a);
                }
                trainObject.put(String.valueOf(i), array);
                relObject.put("train", trainObject);
            }
            JsonUtil.setObject(relObject, "rel.json");
        }

    }


}
