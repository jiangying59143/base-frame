package com.yjiang.base.core.util;

import com.google.common.io.Files;
import com.yjiang.base.modular.health.service.impl.HealthCareServiceImpl;
import com.yjiang.base.modular.system.model.HealthUsers;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ChromeDriveUtils {

    public static void main(String[] args) throws Exception {
        String diverPath = "D:\\root\\driver\\chromedriver91.exe";

        String url = "http://www.jscdc.cn/KABP2011/business/index1.jsp";
        System.setProperty("webdriver.chrome.driver", diverPath);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--no-sandbox", "--disable-gpu", "--whitelisted-ips");
        ChromeDriverService.Builder builder = new ChromeDriverService.Builder();
        ChromeDriverService chromeService = builder.usingDriverExecutable(new File(diverPath)).usingPort(3333).build();
        try {
            chromeService.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ChromeDriver driver = new ChromeDriver(chromeService, options);
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.MINUTES);
        //定位对象时给10s 的时间, 如果10s 内还定位不到则抛出异常 不注释会报org.openqa.selenium.TimeoutException: timeout
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(3, TimeUnit.SECONDS);
        driver.get(url);

        HealthUsers h = new HealthUsers(0, "test", "xx", "男", "xx", "xx", "xx", 0, 0);

        HealthCareServiceImpl healthCareService = new HealthCareServiceImpl();
        healthCareService.singlePersonProcess(h, false, false);

        driver.close();
    }

    public static void screenShotLong(ChromeDriver driver, String appName, String folderName, String fileName) throws IOException {
        driver.manage().window().maximize();
        String js_height = "return document.body.clientHeight";
        JavascriptExecutor driver_js= ((JavascriptExecutor) driver);
        Object height = driver_js.executeScript(js_height);
        int k = 1;
        while (true) {
            if (k * 500 < Integer.parseInt(height.toString())) {
                String js_move = String.format("window.scrollTo(0,%s)", String.valueOf(k * 200));
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
