package com.yjiang.base.modular.health.service.impl;

import com.yjiang.base.modular.health.service.IXieWeiService;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service("XieWeiServiceImpl")
@EnableScheduling
public class XieWeiServiceImpl implements IXieWeiService {

    private static Logger logger = LoggerFactory.getLogger(HealthCareServiceImpl.class);

    @Value("${spring.profiles.active}")
    private String activeProfile;

    protected ChromeDriver driver;

    protected String appName = "nccqjy";

    String diverPath = "C://temp/chromedriver.exe";

//    String diverPath = "/root/driver/chromedriver";

    String url = "http://app3.jsnc.gov.cn:8080/nccqjy/main.do?method=2&pid=10000#";

    int port = 4444;

//    @Scheduled(cron="0 32 7 * * ?")
    public void scheduleProcess(){

    }

    public String getAppName(){
        return appName;
    }

    public void init() throws Exception {
        try {
            System.setProperty("webdriver.chrome.driver", diverPath);
            ChromeOptions options = new ChromeOptions();
            if (!"local".equals(activeProfile)) {
                options.addArguments("--headless", "--no-sandbox", "--disable-gpu", "--whitelisted-ips");
            }

            ChromeDriverService.Builder builder = new ChromeDriverService.Builder();
            ChromeDriverService chromeService = builder.usingDriverExecutable(new File(diverPath)).usingPort(port).build();
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


}
