package tw.com.lig.module_base.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import tw.com.lig.module_base.data.SPConstant;
import tw.com.lig.module_base.global.AppContext;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by ${刘佳阔} on 2016/8/23.
 * SharedPreferences 的封装類
 */
public class SPutils {
    /**
     * 保存在手機裡面的文件名
     */
    public static final String FILE_NAME = "share_data";
    private static final String TAG = "SPutils";

    /**
     * 保存資料的方法，通过传入的值保存不同類型資料
     * 支持 int string  boolean float long Serializable子類
     *
     * @param context
     * @param key     名称
     * @param object  支持 int string  boolean float long Serializable子類
     */
    public static void put(Context context, String key, Object object) {

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else if (object instanceof Serializable) {
            putObj(context, key, (Serializable) object);
        } else {
            editor.putString(key, (String) object);
        }

        SharedPreferencesCompat.apply(editor);
    }

    public static void put( String key, Object object) {

        SharedPreferences sp = AppContext.getContext().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else if (object instanceof Serializable) {
            putObj(AppContext.getContext(), key, (Serializable) object);
        } else {
            editor.putString(key, (String) object);
        }

        SharedPreferencesCompat.apply(editor);
    }


    /**
     * 得到保存資料的方法，我们根据{默认值}得到保存的資料的具体類型，然後調用相對于的方法獲取值
     * 支持 int string  boolean float long Serializable子類
     * 注意 默认值的類型直接影响到返回值的類型
     *
     * @param context
     * @param key           键的名称
     * @param defaultObject 默认值
     * @return 结果
     */
    public static Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        } else if (defaultObject instanceof Serializable) {
            return getObj(context, key, defaultObject);
        }
        return null;
    }

    /**
     * 存贮object類型的資料
     *
     * @param context
     * @param key
     * @param vlaue
     */
    private static void putObj(Context context, String key, Serializable vlaue) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        // 创建位元組輸出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 创建對象輸出流，并封装位元組流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 將對象寫入位元組流
            oos.writeObject(vlaue);
            // 將位元組流编碼成base64的字元窜 -没什么必要
            String object = new String(Base64.encode(baos
                    .toByteArray(), Base64.DEFAULT));
            //      String object = new String(baos.toByteArray());
            editor.putString(key, object);
            editor.apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到儲存的 object 對象
     *
     * @param context
     * @param key
     * @param dafaultObj
     * @return
     */
    private static Object getObj(Context context, String key, Object dafaultObj) {
        ByteArrayInputStream bais;
        ObjectInputStream bis;
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        String res = sp.getString(key, "");
        // LogUtils.d(TAG, "getObj="+res);
        Object object = dafaultObj;
        if (!res.equals("")) {
            byte[] bytes = Base64.decode(res.getBytes(), Base64.DEFAULT);
            //  byte[] bytes = res.getBytes();
            //封装到位元組流
            bais = new ByteArrayInputStream(bytes);
            try {
                //再次封装
                bis = new ObjectInputStream(bais);
                try {
                    //讀取對象
                    object = bis.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bais != null) {
                    try {
                        bais.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        return object;
    }

    /**
     * 移除某個key值已经對應的值
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有資料
     *
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);

    }

    /**
     * 查询某個key是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值對
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 创建一個解决SharedPreferencesCompat.apply方法的一個兼容類
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            editor.commit();
        }
    }

    /**
     * 已经登陆了
     * @return
     */
    public static boolean hasLogin(){
        String  token = (String) get(AppContext.getContext(), SPConstant.KEY_TOKEN, "");
        return !TextUtils.isEmpty(token);
    }
    public static Object get( String key, Object defaultObject) {
        SharedPreferences sp = AppContext.getContext().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        } else if (defaultObject instanceof Serializable) {
            return getObj(AppContext.getContext(), key, defaultObject);
        }
        return null;
    }

    public static boolean getBoolean(String key){
        SharedPreferences sp = AppContext.getContext().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getBoolean(key,false);
    }
    public static String  getString(String key){
        SharedPreferences sp = AppContext.getContext().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getString(key,"");
    }
    public static int  getInt(String key){
        SharedPreferences sp = AppContext.getContext().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getInt(key,0);
    }
   /* public  static void put( String key, Serializable vlaue) {
        SharedPreferences sp = AppContext.getContext().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        // 创建位元組輸出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 创建對象輸出流，并封装位元組流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 將對象寫入位元組流
            oos.writeObject(vlaue);
            // 將位元組流编碼成base64的字元窜 -没什么必要
            String object = new String(Base64.encode(baos
                    .toByteArray(), Base64.DEFAULT));
            //      String object = new String(baos.toByteArray());
            editor.putString(key, object);
            editor.apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

}
