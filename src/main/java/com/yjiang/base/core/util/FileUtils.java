package com.yjiang.base.core.util;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FileUtils {
    private static final Map<String, String> ballPicMap = new HashMap<>();
    static{
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902501531.png","b1");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902101600.png","r1");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902102212.png","r2");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902502184.png","b2");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902103176.png","r3");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902503880.png","b3");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902104855.png","r4");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902504143.png","b4");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902505688.png","b5");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902105626.png","r5");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902106488.png","r6");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902506270.png","b6");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902107839.png","r7");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902507239.png","b7");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902108769.png","r8");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902508548.png","b8");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902509772.png","b9");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902109210.png","r9");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902510405.png","b10");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902110435.png","r10");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902511276.png","b11");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902111316.png","r11");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902512964.png","b12");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902112341.png","r12");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902513798.png","b13");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902113809.png","r13");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902114700.png","r14");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902514377.png","b14");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902115788.png","r15");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902515166.png","b15");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902116463.png","r16");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902516449.png","b16");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902117946.png","r17");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902118438.png","r18");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902119334.png","r19");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902120376.png","r20");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902121785.png","r21");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902122325.png","r22");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902123264.png","r23");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902124365.png","r24");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902125143.png","r25");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902126490.png","r26");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902127951.png","r27");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902128235.png","r28");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902129619.png","r29");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902130131.png","r30");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902131493.png","r31");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902132554.png","r32");
        ballPicMap.put("https://www.cjcp.com.cn/js/kj_js_css/img/20180820032902133669.png","r33");
    }
    /**
     * 网络图片下载
     * @param imageUrl 图片url
     * @param formatName 文件格式名称
     * @param localFile 下载到本地文件
     * @return 下载是否成功
     */
    public static boolean downloadImage(String imageUrl, String formatName, File localFile) {
        boolean isSuccess = false;
        URL url = null;
        try {
            url = new URL(imageUrl);
            isSuccess = ImageIO.write(ImageIO.read(url), formatName, localFile);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    public static void main(String[] args) {
        ballPicMap.entrySet().parallelStream()
                .forEach(e->downloadImage(e.getKey(), "png",
                        new File("D:\\java_projects\\base-frame\\src\\main\\webapp\\static\\img\\lottery\\" + e.getValue() + ".png")));
    }
}
