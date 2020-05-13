package com.yjiang.base.modular.health.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yjiang.base.core.util.ChromeDriveUtils;
import com.yjiang.base.modular.HealthUsers.service.IHealthUsersService;
import com.yjiang.base.modular.health.service.HealthCareService;
import com.yjiang.base.modular.health.service.IHealthService;
import com.yjiang.base.modular.system.model.Health;
import com.yjiang.base.modular.system.model.HealthUsers;
import org.apache.commons.collections.CollectionUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@EnableScheduling
public class HealthCareServiceImpl implements HealthCareService {

    @Resource
    private IHealthService healthService;

    @Resource
    private IHealthUsersService healthUsersService;

    private ChromeDriver driver;

    private String appName = "health";

    private String url = "http://www.jscdc.cn/KABP2011/business/index1.jsp";

    private String diverPath = "/root/driver/chromedriver";

    public void init() {
        System.setProperty("webdriver.chrome.driver",diverPath);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless","--no-sandbox","--disable-gpu", "--whitelisted-ips");

        ChromeDriverService.Builder builder = new ChromeDriverService.Builder();
        ChromeDriverService chromeService = builder.usingDriverExecutable(new File(diverPath)).usingPort(3333).build();
        try{
            chromeService.start();
        }catch(IOException e){
            e.printStackTrace();
        }

        driver = new ChromeDriver(chromeService, options);
        driver.get(url);
    }

    @Override
    @Scheduled(cron="* 52 * * * ?")
//    @Scheduled(cron="0 0 7 * * ?")
    public void process() throws IOException {
        System.out.println("health care start");
        //等待时间,模拟任意时间 7-17
        Random random = new Random();
//        try {
//            Thread.sleep(random.nextInt(3600*10) * 1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        int personNum = random.nextInt(31) + 20;
        int personNum = 2;

        Wrapper<HealthUsers>  wrapper = new EntityWrapper<>();
        wrapper.and("`count` < 10").orderBy("id");

        List<HealthUsers> healthUsers = healthUsersService.selectPage(new Page<>(0, personNum), wrapper).getRecords();
        for (HealthUsers healthUser : healthUsers) {
            Map<String, String> personInfoMap = this.getPersonInfo(healthUser.getName(), healthUser.getOrgName());
            int count = healthUser.getCount();
            if(count >= 10){
                return;
            }
            for (int i = count+1; i <= 10; i++) {
                personInfoMap.put("index", String.valueOf(i));
                boolean fineFlag = atomOperation(personInfoMap);
                if(fineFlag) {
                    healthUser.setCount(healthUser.getCount() + 1);
                    healthUsersService.updateById(healthUser);
                }
            }
        }

        System.out.println("health care end");

    }

    private boolean atomOperation(Map<String, String> personInfoMap){
        boolean fineFlag = false;
        while(!fineFlag) {
            try {
                processSingle(personInfoMap);
                fineFlag = true;
            } catch (Exception e) {
                try{
                    driver.close();
                }finally {
                    fineFlag = false;
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return fineFlag;
    }

    public void processSingle(Map<String, String> personInfoMap) throws IOException {
        List<Health> questionBankList = healthService.selectList(new EntityWrapper<>());

        this.init();
        login(personInfoMap.get("name"),
                personInfoMap.get("age"),
                personInfoMap.get("sex"),
                personInfoMap.get("education"),
                personInfoMap.get("job"),
                personInfoMap.get("orgName"));
        int questionCount = Integer.parseInt(driver.findElementById("__subjectCount").getText());
        doTest(questionBankList, questionCount, getRandomNumbers(questionCount,1 + new Random().nextInt(questionCount/3 + 1)));
        submit(questionCount);
        this.processReport(questionCount, questionBankList);
        ChromeDriveUtils.screenShotLong(driver, appName, personInfoMap.get("name"), personInfoMap.get("index"));
        driver.close();

    }

    private Map<String, String> getPersonInfo(String name, String orgName){
        Map<String, String> map = new HashMap<>();
        Random random = new Random();
        String age = Arrays.asList("20～25岁以下", "25～30岁以下", "30～35岁以下", "35～40岁以下", "40～45岁以下").get(random.nextInt(5));
        String sex = Arrays.asList("男", "女").get(random.nextInt(2));
        String education = Arrays.asList("小学", "初中", "高中/职高/中专", "大专/本科").get(random.nextInt(2));
        String job = Arrays.asList("教师", "饮食服务", "商业服务", "医务人员", "公司管理").get(random.nextInt(5));
        map.put("name", name);
        map.put("age", age);
        map.put("sex", sex);
        map.put("education", education);
        map.put("job", job);
        map.put("orgName", orgName);
        return map;
    }


    public void login(String name, String age, String sex, String edu, String metier, String orgName){
        Select city = new Select(driver.findElementById("zone3"));
        city.selectByVisibleText("宿迁市");
        Select zone = new Select(driver.findElementById("zone4"));
        zone.selectByVisibleText("沭阳县");
        Select village = new Select(driver.findElementById("zone5"));
        village.selectByVisibleText("东小店乡");
        driver.findElementById("name").sendKeys(name);
        Select ageGroup = new Select(driver.findElementById("ageGroup"));
        ageGroup.selectByVisibleText(age);
        Select sexGroup = new Select(driver.findElementById("sex"));
        sexGroup.selectByVisibleText(sex);
        Select educationStatus = new Select(driver.findElementById("educationStatus"));
        educationStatus.selectByVisibleText(edu);
        Select metierGroup = new Select(driver.findElementById("metier"));
        metierGroup.selectByVisibleText(metier);
        driver.findElementById("orgName").sendKeys(orgName);
        driver.findElementById("log_img").click();
    }

    public void doTest(List<Health> questionBankList, int questionCount, List<Integer> wrongItems){
        for (int questionIndex = 1; questionIndex <= questionCount; questionIndex++) {
            Map<String, String> questionAndSelection = this.getTitleAndAnswers(questionIndex, false);
            List<String> correctSelections = this.getCorrectSelections(questionBankList, questionAndSelection);
            this.setAnswersToQuestion(correctSelections, questionIndex, wrongItems);
        }
    }

    public void submit(int totalQuestionCount){
        driver.findElementByXPath("//td[@id='btnAct" + totalQuestionCount + "']/div[1]/input[1]").click();
        Alert confirm = driver.switchTo().alert();
        confirm.accept();
    }

    public void processReport(int totalQuestionCount, List<Health> questionBankList){
        List<String> questionTitles = questionBankList.stream().map(Health::getTitle).collect(Collectors.toList());
        for (int questionIndex = 1;questionIndex <= totalQuestionCount; questionIndex++) {
            Map<String, String> questionAndSelection = this.getTitleAndAnswers(questionIndex, true);
            String title = questionAndSelection.get("title");
            String[] correctSelections = this.getCorrectSelections(questionIndex);
            List<String> correctSelectionDetails = this.getCorrectSelectionsDetails(questionAndSelection, correctSelections);

            if(!questionTitles.contains(title)) {
                Health health = new Health();
                health.setTitle(title);
                health.setAnswers(String.valueOf(correctSelectionDetails.toArray()));
                healthService.insert(health);
            }
        }
    }

    private String[] getCorrectSelections(int questionIndex){
        String answers = driver.findElementById("KWait" + questionIndex).getText();
        if(answers.contains("标准答案：")){
            answers = answers.split("您的答案：")[1].split("标准答案：")[1].trim();
        } else {
            answers = answers.split("您的答案：")[1].trim();
        }
        return answers.split("");
    }

    private List<String> getCorrectSelectionsDetails(Map<String, String> questionAndSelection, String[] correctSelections){
        questionAndSelection.remove("title");
        List<String> correctSelectionDetails = null;
        for (String correctSelection : correctSelections) {
            correctSelectionDetails = questionAndSelection.entrySet().stream()
                    .filter(e-> Arrays.asList(correctSelections).contains(e.getKey()))
                    .map(e->e.getValue())
                    .collect(Collectors.toList());
        }
        return correctSelectionDetails;
    }

    private Map<String, String> getTitleAndAnswers(int questionIndex, boolean isScoreReport) {
        Map<String, String> title_and_answers = new HashMap<>();
        String this_title_html = driver.findElementByXPath(
                "//li[@id='E" + questionIndex + "'][@style='display: block;']/table[1]/tbody[1]/tr[1]/td[1]/div[@class='ECnt']")
                .getAttribute("innerHTML");
        String[] this_title_and_answers = this_title_html.split("<br>");
        String this_title = this_title_and_answers[1].split("</b>")[1].replace("<b>" + questionIndex + ".</br>", "").trim();
        title_and_answers.put("title", this_title);
        for (int answerIndex = 2; answerIndex < this_title_and_answers.length; answerIndex++) {
            String[] selectionAndDetail = this_title_and_answers[answerIndex].split("、", 2);
            try {
                if (isScoreReport) {
                    title_and_answers.put(selectionAndDetail[0], selectionAndDetail[1]);
                }else{
                    title_and_answers.put(selectionAndDetail[1], selectionAndDetail[0]);
                }
            } catch (Exception e) {
                if("".equals(this_title_and_answers[answerIndex]) && this_title_and_answers[answerIndex].contains("、")){
                    title_and_answers.put(selectionAndDetail[0], selectionAndDetail[0]);
                }
            }
        }
        return title_and_answers;
    }

    private List<String> getCorrectSelections(List<Health> questionBankList, Map<String, String> questionAndSelection){
        List<String> correctSelections = null;
        for (Health health : questionBankList) {
            if(questionAndSelection.get("title").contains(health.getTitle())){
                questionAndSelection.remove("title");
                correctSelections = questionAndSelection.entrySet().stream()
                        .filter(e-> health.getSelections().contains(e.getKey()))
                        .map(e->e.getValue())
                        .collect(Collectors.toList());
                return correctSelections;
            }
        }
        return correctSelections;
    }

    private void setAnswersToQuestion(List<String> correctSelections, int questionIndex, List<Integer> wrongItems){
        String questionType = driver
                .findElementByXPath("//li[@id='E" + questionIndex + "'][@style='display: block;']/table[1]/tbody[1]/tr[1]/td[1]/div[@class='ECnt']/b[1]")
                .getText().trim();
        boolean multipleFlag = questionType.contains("多选题");
        if(CollectionUtils.isEmpty(correctSelections)){
            if(multipleFlag) {
                driver.findElementByXPath("//input[@type='checkbox'][@value='A" + questionIndex + "']").click();
                driver.findElementByXPath("//img[@id='Key_Next']").click();
            }else {
                driver.findElementByXPath("//input[@type='radio'][@value='A" + questionIndex + "']").click();
            }
        }else{
            Object[] preCorrectSelections = correctSelections.toArray();
            if(wrongItems.contains(questionIndex)){
                if(correctSelections.size() > 1 ) {
                    correctSelections.remove(0);
                }else{
                    correctSelections.set(0,
                            String.valueOf("A".equals(correctSelections.get(0)) ? (char)(correctSelections.get(0).charAt(0) + 1) : (char)(correctSelections.get(0).charAt(0) - 1)));
                }
            }
            if(multipleFlag){
                for(String selection : correctSelections) {
                    driver.findElementByXPath("//input[@type='checkbox'][@value='" + selection + questionIndex + "']").click();
                    driver.findElementByXPath("//img[@id='Key_Next']").click();
                }
            }else {
                driver.findElementByXPath("//input[@type='radio'][@value='" + correctSelections.get(0) + questionIndex + "']").click();
            }
        }
    }

    private List<Integer> getRandomNumbers(int max, int num){
        int min = 1;
        Integer[] numbers = new Integer[num];
        Random random = new Random();
        for(int i=0;i<num;i++){
            numbers[i] = random.nextInt(max)%(max-min)+min;
        }

        return Arrays.asList(numbers);
    }

}
