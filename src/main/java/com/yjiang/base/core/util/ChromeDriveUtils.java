package com.yjiang.base.core.util;

import com.google.common.io.Files;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ChromeDriveUtils {
    public static void screenShotLong(ChromeDriver driver, String appName, String folderName, String fileName) throws IOException {
        driver.manage().window().maximize();
        String js_height = "return document.body.clientHeight";
        JavascriptExecutor driver_js= ((JavascriptExecutor) driver);
        Object height = driver_js.executeScript(js_height);
        int k = 1;
        while (true) {
            if (k * 500 < Integer.parseInt(height.toString())) {
                String js_move = String.format("window.scrollTo(0,%s)", String.valueOf(k * 500));
                driver_js.executeScript(js_move);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                k += 1;
            }else{
                break;
            }
        }
        Object scroll_width = driver_js.executeScript("return document.body.parentNode.scrollWidth");
        Object scroll_height = driver_js.executeScript("return document.body.parentNode.scrollHeight");
        driver.manage().window().setSize(new Dimension(Integer.parseInt(scroll_width.toString()), Integer.parseInt(scroll_height.toString())));
        screenShot(driver, appName, folderName, fileName);
    }

    public static void screenShot(ChromeDriver driver, String appName, String folderName, String fileName) throws IOException {
        String dir_path = "/root/" + appName + "/" + folderName;
        File dir = new File(dir_path);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        String pic_src = dir_path + "/"+ fileName + "_pic.png";
        File scrFile = driver.getScreenshotAs(OutputType.FILE);
        Files.copy(scrFile, new FileOutputStream(pic_src));
    }
}
