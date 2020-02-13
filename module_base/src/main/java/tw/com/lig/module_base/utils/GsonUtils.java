package tw.com.lig.module_base.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by liuwangping on 2017/12/27.
 */

public class GsonUtils {
    private static Gson gson;

    private GsonUtils() {
    }

    public static Gson getGson() {
        if (gson == null) synchronized (GsonUtils.class) {
            if (gson == null) gson = new Gson();
        }
        return gson;
    }

    public static String toJson(Object o) {
        return getGson().toJson(o);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return getGson().fromJson(json, typeOfT);
    }
    public static <T> T fromJson2(String json, Class<T> ClassName) {
        return getGson().fromJson(json, ClassName);
    }
}
