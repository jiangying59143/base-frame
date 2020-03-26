package com.yjiang.base.share;

import com.alibaba.druid.util.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableScheduling
public class ShareServiceImpl implements ShareService {

    private WebDriver driver;

    @Override
    public void init() {
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        driver.get("http://quote.eastmoney.com/center/gridlist.html#hs_a_board");
        WebElement tableElement = driver.findElement(By.id("table_wrapper-table"));
        List<WebElement> trs = tableElement.findElements(By.tagName("tr"));
        for (WebElement tr : trs) {
            List<WebElement> tds = tr.findElements(By.tagName("td"));
            for (WebElement td : tds) {
                System.out.println(td.getText());
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver","D://java_projects/base-frame/src/main/resources/driver/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        WebDriver driver = new ChromeDriver(options);
        driver.get("http://quote.eastmoney.com/center/gridlist.html#hs_a_board");

        int maxPage = 0;
        List<WebElement> pages =  driver.findElements(By.xpath("//span[@class='paginate_page']/a"));
        for (WebElement page : pages) {
            if(StringUtils.isNumber(page.getText()) && Integer.parseInt(page.getText()) > maxPage) {
                maxPage = Integer.parseInt(page.getText());
         }
        }

        Integer cur = 0;
        for (int i = 1; i <= maxPage; i++) {
            if(i < maxPage) {
                while(cur == isContinue(driver, cur)){
                    Thread.sleep(500);
                }

                cur += 20;

                processSinglePage(driver);
                WebElement page = driver.findElement(By.xpath("//span[@class='paginate_page']/a[@data-index='" + (i + 1) + "']"));
                page.click();
            }else{
                Thread.sleep(3000);
                processSinglePage(driver);
            }
        }
        driver.close();
    }

    private static int isContinue(WebDriver driver, Integer itemNumber){
        try {
            WebElement tableElement = driver.findElement(By.id("table_wrapper-table"));
            WebElement tbody = tableElement.findElement(By.tagName("tbody"));
            if (tbody != null) {
                List<WebElement> trs = tbody.findElements(By.tagName("tr"));
                if (CollectionUtils.isNotEmpty(trs) && trs.size() == 20) {
                    WebElement tr1 = trs.get(19);
                    if (tr1 != null) {
                        List<WebElement> tds = tr1.findElements(By.tagName("td"));
                        if (CollectionUtils.isNotEmpty(tds)) {
                            if (tds.size() == 19) {
                                return itemNumber + 20;
                            }
                        }
                    }
                }
            }
        }catch (StaleElementReferenceException se){
            return itemNumber;
        }
        return itemNumber;
    }

    private static void processSinglePage(WebDriver driver) throws InterruptedException {
        WebElement tableElement = driver.findElement(By.id("table_wrapper-table"));
        WebElement tbody = tableElement.findElement(By.tagName("tbody"));
        if(tbody != null) {
            List<WebElement> trs = tbody.findElements(By.tagName("tr"));
            if(CollectionUtils.isNotEmpty(trs)) {
                for (WebElement tr : trs) {
                    List<WebElement> tds = tr.findElements(By.tagName("td"));
                    for (WebElement td : tds) {
                        System.out.print(td.getText() + "|");
                    }
                    System.out.println();
                }
            }
        }
    }
}
