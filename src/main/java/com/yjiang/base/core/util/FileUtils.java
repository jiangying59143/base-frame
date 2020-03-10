package com.yjiang.base.core.util;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class FileUtils {
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
}
