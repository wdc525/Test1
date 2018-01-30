package com.example.administrator.test1.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2017-11-15.
 */

public class CacheUitls {
    private static String result;

    public static boolean getBoolean(Context context, String key) {

        SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);

        return sp.getBoolean(key, false);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();

    }


    public static String getString(Context context, String key) {
        //判断sdcard是否挂载
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //保存图片在/mnt/sdcard/beijingnews/http://192.168.21.165:8080/xsxxxx.png
            //保存图片在/mnt/sdcard/beijingnews/llkskljskljklsjklsllsl
            try {
                String fileName = MD5Encoder.encode(key);//llkskljskljklsjklsllsl

                ///mnt/sdcard/beijingnews/llkskljskljklsjklsllsl
                File file = new File(Environment.getExternalStorageDirectory() + "/beijingnews/files", fileName);


                if (file.exists()) {

                    FileInputStream is = new FileInputStream(file);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) != -1) {
                        stream.write(buffer, 0, length);
                    }
                    is.close();
                    stream.close();
                    result = stream.toString();


                }

            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e("图片获取失败");
            }
        } else {
            SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
            result = sp.getString(key, "");

        }
        return result;
    }

    public static void putString(Context context, String key, String value) {
        //判断sdcard是否挂载
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //保存图片在/mnt/sdcard/beijingnews/http://192.168.21.165:8080/xsxxxx.png
            //保存图片在/mnt/sdcard/beijingnews/llkskljskljklsjklsllsl
            try {
                String fileName = MD5Encoder.encode(key);//llkskljskljklsjklsllsl

                ///mnt/sdcard/beijingnews/files/llkskljskljklsjklsllsl
                File file = new File(Environment.getExternalStorageDirectory() + "/beijingnews/files", fileName);

                File parentFile = file.getParentFile();//mnt/sdcard/beijingnews/files
                if (!parentFile.exists()) {
                    //创建目录
                    parentFile.mkdirs();
                }


                if (!file.exists()) {
                    file.createNewFile();
                }
                //保存文本数据
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(value.getBytes());
                fileOutputStream.close();

            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e("文本本地缓存失败");
            }
        } else {
            SharedPreferences sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
            sp.edit().putString(key, value).commit();
        }

    }
}
