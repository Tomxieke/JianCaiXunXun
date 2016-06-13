package com.ciist.xunxun.app.util;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by xieke on 2016/6/2 0002.
 */
public class HttpUtil {

    /**
     * 将json文件写入本地储存
     * @param context
     * @param string
     * @param file  文件名
     * @return
     */
    public static boolean saveObject(Context context,String string, String file) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = context.openFileOutput(file, context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(string);
            oos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
            }
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }


    /**
     * 从本地文件读取json
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static String readObject(Context context,String file) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = context.openFileInput(file);
            ois = new ObjectInputStream(fis);
            return (String) ois.readObject();
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
            }
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * 缓存文件超过CACHE_TIME毫秒才去访问网络取数据
     * @param context
     * @param cachefile
     * @return
     */
    private static int CACHE_TIME = 600000;  //缓存文件超过10分钟才访问网络取数据，10分钟以内直接读取缓存文件中的数据
    public static boolean isCacheDataFailure(Context context,String cachefile) {
        boolean failure = false;
        File data = context.getFileStreamPath(cachefile);
        if (data.exists()
                && (System.currentTimeMillis() - data.lastModified()) > CACHE_TIME)
            failure = true;
        else if (!data.exists())
            failure = true;
        return failure;
    }
}
