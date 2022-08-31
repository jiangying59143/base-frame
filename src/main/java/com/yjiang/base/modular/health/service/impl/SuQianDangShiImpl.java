package com.yjiang.base.modular.health.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.yjiang.base.core.util.MailUtils;
import com.yjiang.base.modular.HealthUsers.service.IHealthUsersService;
import com.yjiang.base.modular.system.model.Health;
import com.yjiang.base.modular.system.model.HealthUsers;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class SuQianDangShiImpl{

    String diverPath = "D:/driver/msedgedriver.exe";

    String url = "https://suxinwen.wjx.cn/vm/QBcumeA.aspx";

    protected ChromeDriver driver;

    protected String appName = "health";

    public static void main(String[] args) throws Exception {
        SuQianDangShiImpl suQianDangShi = new SuQianDangShiImpl();
        suQianDangShi.init();
        suQianDangShi.processing();

    }

    public void init() throws Exception {
        try {
            System.setProperty("webdriver.chrome.driver", diverPath);
            ChromeOptions options = new ChromeOptions();
//            if (!"local".equals(activeProfile)) {
//                options.addArguments("--headless", "--no-sandbox", "--disable-gpu", "--whitelisted-ips");
//            }
//            options.addArguments("--proxy-server=193.112.128.212:8118");

            ChromeDriverService.Builder builder = new ChromeDriverService.Builder();
            ChromeDriverService chromeService = builder.usingDriverExecutable(new File(diverPath)).usingPort(4444).build();
            try {
                chromeService.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

            driver = new ChromeDriver(chromeService, options);
            driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.MINUTES);
            //定位对象时给10s 的时间, 如果10s 内还定位不到则抛出异常 不注释会报org.openqa.selenium.TimeoutException: timeout
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.manage().timeouts().setScriptTimeout(3, TimeUnit.SECONDS);
            driver.get(url);
        }catch(Exception e){
            throw new Exception("webdriver 初始化失败，应该是网站无法访问");
        }
    }

    public void processing() throws InterruptedException {
        driver.findElementById("select2-q1-container").click();
        driver.findElementByXPath("//li[contains(text(),'沭阳县')]").click();

        driver.findElementById("select2-q2-container").click();
        driver.findElementByXPath("//li[contains(text(),'章集街道')]").click();

        driver.findElementById("q10").sendKeys("13951245998");
        driver.findElementByXPath("//div[@for='q11_" + (getRandom(2) +1) + "']").click();
        String phoneExisted = driver.findElementByXPath("//div[@style='line-height: 38px; background: rgb(255, 229, 224); height: auto;']").getText();
        if(StringUtils.isNotBlank(phoneExisted)){
            System.out.println(phoneExisted);
            System.out.println("phone 存在");
            return;
        }
        driver.findElementByXPath("//div[contains(text(),'" + getAge() + "')]").click();
        driver.findElementByXPath("//div[contains(text(),'群众')]").click();
        driver.findElementByXPath("//div[contains(text(),'大专及以下')]").click();

        for (int i = 15; i <= 34; i++) {
            List<WebElement> list;
            if(Arrays.asList(6,7,8,12,13,14,15,16,17,18,24).contains(i-10)){
                list = driver.findElementByXPath("//div[@id='div" + i + "']").findElements(By.className("ui-checkbox"));
            }else{
                list = driver.findElementByXPath("//div[@id='div" + i + "']").findElements(By.className("ui-radio"));
            }
            list.get(getRandom(list.size())).click();
//            driver.findElementByXPath("//div[@id='div" + i + "']").findElements(By.xpath("//div[@for='q" + i + "_" + (getRandom(list.size()) +1) + "']")).get(0).click();
        }

        Thread.sleep(2000);
//        driver.findElementById("ctlNext").click();
//        driver.findElementById("rectBottom").click();

        WebElement webElement = driver.findElementById("SM_TXT_1");
        if(webElement != null){
            System.out.println("失败");
        }

    }

    private String getAge(){
        List<String> list = Arrays.asList("30岁以下", "31-45岁", "46-60岁");
        int x = getRandom(3);
        return list.get(x);
    }

    private String getGender(){
        List<String> list = Arrays.asList("男", "女");
        int x = getRandom(2);
        return list.get(x);
    }

    private int getRandom(int x){
        Random random = new Random();
        return random.nextInt(x);
    }

}
