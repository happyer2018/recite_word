package com.hp.gre3000.utils.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GsonFix {
    private volatile static GsonFix _instance;
    private Gson gson;

    private GsonFix() {
        gson = new GsonBuilder()
                .addSerializationExclusionStrategy(new SerializeExclusionStrategy())
                .addDeserializationExclusionStrategy(new DeserializeExclusionStrategy())
                .registerTypeAdapter(new TypeToken<Map<String, Object>>() {
                }.getType(), new MapDeserializerDoubleAsIntFix())
                .registerTypeAdapter(int.class, new IntegerDefault0Adapter())
                .create();
    }

    public static GsonFix getInstance() {
        if (_instance == null) {
            synchronized (GsonFix.class) {
                if (_instance == null) {
                    _instance = new GsonFix();
                }
            }
        }
        return _instance;
    }

    public Gson getGson() {
        return gson;
    }

    public <T> T get(String string, Class<T> classOfT) {
        T t = null;
        try {
            t = getGson().fromJson(string, classOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    public <T> List<T> gets(String string, Class<T> clazz) {
        List<T> lst = new ArrayList<>();
        try {
            JsonArray array = new JsonParser().parse(string).getAsJsonArray();
            for (final JsonElement elem : array) {
                lst.add(getGson().fromJson(elem, clazz));
            }
        } catch (Exception e) {
        }
        return lst;
    }

//    Map<String, Object> requestMap = new Gson().fromJson(requestString, new TypeToken<HashMap<String, Object>>() {}.getType());
//                        for (String key : requestMap.keySet()) {
//        if (requestMap.get(key) instanceof Double) {
//            Double doubleV = (Double) requestMap.get(key);
//            Integer integerV = doubleV.intValue();
//            if(doubleV == integerV.doubleValue()){
//                requestMap.put(key,integerV);
//            }
//        }
//    }

    class SerializeExclusionStrategy implements ExclusionStrategy {

        public boolean shouldSkipClass(Class<?> arg0) {
            return false;
        }

        public boolean shouldSkipField(FieldAttributes f) {

            Ignore annotation = f.getAnnotation(Ignore.class);
            if (annotation != null && annotation.serialize()) {
                return true;
            }
            return false;
        }

    }

    class DeserializeExclusionStrategy implements ExclusionStrategy {

        public boolean shouldSkipClass(Class<?> arg0) {
            return false;
        }

        public boolean shouldSkipField(FieldAttributes f) {

            Ignore annotation = f.getAnnotation(Ignore.class);
            if (annotation != null && annotation.deserialize()) {
                return true;
            }
            return false;
        }

    }
}
