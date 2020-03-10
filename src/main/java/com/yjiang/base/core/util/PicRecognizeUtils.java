package com.yjiang.base.core.util;

import com.alibaba.fastjson.JSON;
import com.baidu.aip.ocr.AipOcr;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

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
//        Map<String, Object> data =new HashMap<>();
//        //循环转换
//        Iterator it =res.keys();
//        while (it.hasNext()) {
//            String key = (String) it.next();
//            data.put(key, res.get(key));
//        }
//
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

    public static void main(String[] args) {
        System.out.println(Integer.parseInt(basicGeneralUrl("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902103176.png")));
    }
}
