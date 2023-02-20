package com.yjiang.test;

import com.yjiang.base.core.util.ChromeDriveUtils;
import com.yjiang.base.core.util.PicRecognizeUtils;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONException;
import org.openqa.selenium.Alert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class XieWeiWeiShengReadExcel {

    public static void main(String[] args) throws Exception {

        ChromeDriver driver = init();
        login(driver);
//        pageAccess(driver);

    }

    public static void login(ChromeDriver driver) throws IOException, JSONException {

        driver.findElementById("dx_32_").sendKeys("dxd17");
        driver.findElementById("dx_33_").sendKeys("321");
        driver.findElementById("img_dx_30_").click();
    }

    public static void pageAccess(ChromeDriver driver){
        driver.findElementByXPath("//img[@src='icons/in_icons/54.png']").click();
        driver.findElementById("402881046d390069016d390319af0003").click();
        driver.findElementByXPath("//a[text()='成员登记']").click();
        driver.findElementByXPath("//div[@id='top']/h1").click();
    }

    public static void familyProcess(ChromeDriver driver){
        driver.switchTo().frame("mainframe");
        driver.findElementByXPath("//a[@onclick='dengji()']").click();
        driver.findElementByXPath("//table[@class='bdtable']/tbody/tr[2]/td[2]/span[1]/span[1]").click();
        sleep(1000);
        driver.findElementByXPath("//div[text()='沭阳县东小店乡谢圩村股份经济合作社']").click();
        sleep(1000);
        driver.findElementById("address").sendKeys("宿迁市沭阳县东小店乡谢圩村");
        clickById(driver, "chigu");
    }

    public static void sleep(long l){
        try {
            Thread.sleep(l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void clickById(ChromeDriver driver, String id){
        int i=5;
        while(i>0){
            try {
                driver.findElementById(id).click();
                i=0;
            }catch (Exception e){
                e.printStackTrace();
                i--;
            }
        }
    }

    public static Boolean personProcess(ChromeDriver driver, String name, boolean isHuzhu, String relation, String id) throws IOException {
        try {
            driver.findElementByXPath("//a[text()='新 增']").click();
            driver.findElementById("memberName").sendKeys(name);
            driver.findElementByXPath("//input[@name='isHuzhu'][@value='" + (isHuzhu ? 1 : 0) + "']").click();
            Select jtgxs = new Select(driver.findElementById("jtgxs"));
            jtgxs.selectByVisibleText(relation);
            driver.findElementById("sfzhm").sendKeys(id);
            driver.findElementById("cysx").click();
            Alert alert = driver.switchTo().alert();
            if (alert != null) {
                alert.accept();
                ChromeDriveUtils.screenShot(driver, "谢圩五组", name, name);
                driver.findElementByXPath("//a[text()='关闭']").click();
                driver.findElementByXPath("//a[text()='关闭']").click();
                return null;
            }
            driver.findElementByXPath("//form[@id='cgxzxx']/div/ul/li[text()='持股信息']").click();
            driver.findElementById("rkg").clear();
            driver.findElementById("rkg").sendKeys("1");
            driver.findElementById("cybc").click();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static String getRandCode(ChromeDriver driver, String path) throws IOException, JSONException {
        WebElement ele = driver.findElementById("randImage");
        File screenshot = ele.getScreenshotAs(OutputType.FILE);

        File screenshotLocation= new File(path);

        FileUtils.copyFile(screenshot, screenshotLocation);

        String s = PicRecognizeUtils.accurateGeneral(path);
        return s;
    }

    public static ChromeDriver init(){
        ChromeDriver driver;
        String diverPath = "C://temp/chromedriver81.exe";
        System.setProperty("webdriver.chrome.driver", diverPath);
        String activeProfile = "local";
        ChromeOptions options = new ChromeOptions();
        if (!"local".equals(activeProfile)) {
            options.addArguments("--headless", "--disable-gpu", "--whitelisted-ips");
        }
//        options.addArguments("--no-sandbox");

        ChromeDriverService.Builder builder = new ChromeDriverService.Builder();
        ChromeDriverService chromeService = builder.usingDriverExecutable(new File(diverPath))
                .usingPort(4444)
                .build();
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
        driver.get("http://172.17.0.146:8081/fjhis/start");
        return driver;
    }

    public static List<String> getColunmNames(){
        List<String> columnNames = new ArrayList<>();
        columnNames.add("zuming");
        columnNames.add("name");
        columnNames.add("relation");
        columnNames.add("sex");
        columnNames.add("huzhu");
        columnNames.add("count");
        columnNames.add("xxx");
        columnNames.add("xxx");
        columnNames.add("id");
        return columnNames;
    }

    public static List<Map<String,String>> readFile(String fileName, int sheetNum,  List<String> columnNames) {
        Workbook wb =null;
        Sheet sheet = null;
        Row row = null;
        List<Map<String,String>> list = null;
        String cellData = null;
        wb = ReadExcel.getWorkbook(fileName);
        if(wb != null){
            //用来存放表中数据
            list = new ArrayList<Map<String,String>>();
            //获取第一个sheet
            sheet = wb.getSheetAt(sheetNum);
            //获取最大行数
            int rownum = sheet.getPhysicalNumberOfRows();
            //获取第一行
            row = sheet.getRow(0);
            //获取最大列数
            int colnum = row.getPhysicalNumberOfCells();

            for (int i = 0; i<=rownum; i++) {
                Map<String, String> map = new LinkedHashMap<String, String>();
                row = sheet.getRow(i);
                if (row != null) {
                    for (int j = 0; j < colnum; j++) {
                        cellData = (String) ReadExcel.getCellFormatValue(row.getCell(j));
                        map.put(columnNames.get(j), cellData);
                    }
                } else {
                    break;
                }
                list.add(map);
            }
        }

        return list;

    }
}
