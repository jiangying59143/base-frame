package com.yjiang.base.core.util;

import com.alibaba.fastjson.JSON;
import com.baidu.aip.ocr.AipOcr;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 文字识别应用地址
 * https://console.bce.baidu.com/ai/?_=1593442649267&fromai=1&locale=zh-cn#/ai/ocr/overview/index
 */
public class PicRecognizeUtils {
    //设置APPID/AK/SK
    public static final String APP_ID = "xxx";
    public static final String API_KEY = "xxx";
    public static final String SECRET_KEY = "xxx";
    static int callCount = 0;

    static class WordsResult{
        private String words;

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }
    }

    public static String basicGeneralUrl(String url) throws JSONException {
        // 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//        client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//        client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
//        System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

        // 调用接口
        JSONObject res = client.basicGeneralUrl(url, new HashMap<String, String>());
        List<WordsResult> list = new ArrayList<>();
        JSONArray array = res.getJSONArray("words_result");
        for(int i=0; i<array.length();i++){
            Object s = array.get(i);
            WordsResult wordsResult = JSON.parseObject(s.toString(), WordsResult.class);
            list.add(wordsResult);
        }
//        System.out.println("map对象:"+data.toString());
        callCount++;
        return list.get(0).getWords();
    }

    public static String accurateGeneral(String url) throws JSONException {
        // 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//        client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//        client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
//        System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

        // 调用接口
        JSONObject res1 = client.accurateGeneral(readFileByBytes(url), new HashMap<String, String>());
        List<WordsResult> list = new ArrayList<>();
        JSONArray array = res1.getJSONArray("words_result");
        for(int i=0; i<array.length();i++){
            Object s = array.get(i);
            WordsResult wordsResult = JSON.parseObject(s.toString(), WordsResult.class);
            list.add(wordsResult);
        }
//        System.out.println("map对象:"+data.toString());
        callCount++;
        return list.get(0).getWords();
    }

    public static byte[] readFileByBytes(String fileName) {
        File file = new File(fileName);
        InputStream in = null;
        int count = 0;
        try {
            System.out.println("以字节为单位读取文件内容，一次读一个字节：");
// 一次读一个字节
            in = new FileInputStream(file);
            count = in.available();
            int tempbyte;
            while ((tempbyte = in.read()) != -1) {
                System.out.write(tempbyte);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        byte[] tempbytes = null;
        try {
            System.out.println("以字节为单位读取文件内容，一次读多个字节：");
// 一次读多个字节
            tempbytes = new byte[count];
            int byteread = 0;
            in = new FileInputStream(fileName);
// 读入多个字节到字节数组中，byteread为一次读入的字节数
            while ((byteread = in.read(tempbytes)) != -1) {
                System.out.write(tempbytes, 0, byteread);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                }
            }
        }
        return tempbytes;
    }

    public static void main(String[] args) {
        System.out.println(Integer.parseInt(accurateGeneral("D://testimg.png")));
    }
}
