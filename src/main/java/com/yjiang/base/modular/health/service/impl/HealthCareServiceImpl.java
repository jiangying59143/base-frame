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
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@EnableScheduling
public class HealthCareServiceImpl implements HealthCareService {
    private static Logger logger = LoggerFactory.getLogger(HealthCareServiceImpl.class);

    @Resource
    private IHealthService healthService;

    @Resource
    private IHealthUsersService healthUsersService;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    private ChromeDriver driver;

    private String appName = "health";

    private String url = "http://www.jscdc.cn/KABP2011/business/index1.jsp";

    private String diverPath = "/root/driver/chromedriver";

    @Scheduled(cron="0 0 7 * * ?")
    public void scheduleProcess(){
        System.out.println("health care automation started");
        //等待时间,模拟任意时间 7-17
        Random random = new Random();
        try {
            Thread.sleep(random.nextInt(3600*10) * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int personNum = random.nextInt(31) + 20;
        process(personNum, true);
        System.out.println("health care automation ended");
    }

    public void init() {
        System.setProperty("webdriver.chrome.driver",diverPath);
        ChromeOptions options = new ChromeOptions();
        if(!"local".equals(activeProfile)) {
            options.addArguments("--headless", "--no-sandbox", "--disable-gpu", "--whitelisted-ips");
        }

        ChromeDriverService.Builder builder = new ChromeDriverService.Builder();
        ChromeDriverService chromeService = builder.usingDriverExecutable(new File(diverPath)).usingPort(3333).build();
        try{
            chromeService.start();
        }catch(IOException e){
            e.printStackTrace();
        }

        driver = new ChromeDriver(chromeService, options);
        driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
        //定位对象时给10s 的时间, 如果10s 内还定位不到则抛出异常
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(3, TimeUnit.SECONDS);
        driver.get(url);
    }

    @Override
    public void process(int personNum, boolean wrongSet) {
        Wrapper<HealthUsers>  wrapper = new EntityWrapper<>();
        wrapper.and("`count` < 10").orderBy("id");
        List<HealthUsers> healthUsers = healthUsersService.selectPage(new Page<>(0, personNum), wrapper).getRecords();
        for (HealthUsers healthUser : healthUsers) {
            singlePersonProcess(healthUser, wrongSet);
        }

        System.out.println("health care end");

    }

    private void singlePersonProcess(HealthUsers healthUser,boolean wrongSet){
        Map<String, Object> personInfoMap = this.getPersonInfo(healthUser);
        personInfoMap.put("wrongSet", wrongSet);
        int count = healthUser.getCount();
        if(count >= 10){
            return;
        }
        for (int i = count+1; i <= 10; i++) {
            personInfoMap.put("index", String.valueOf(i));
            atomOperation(personInfoMap);
            healthUser.setCount(i);
            healthUser.setAge(personInfoMap.get("age").toString());
            healthUser.setEducation(personInfoMap.get("education").toString());
            healthUser.setJob(personInfoMap.get("job").toString());
            healthUsersService.updateById(healthUser);
            System.out.println(healthUser.getName() + "完成第" + i + "遍数" );
        }
    }

    private void atomOperation(Map<String, Object> personInfoMap){
        boolean fineFlag = false;
        while(!fineFlag) {
            try {
                processSingle(personInfoMap);
                fineFlag = true;
            } catch (Exception e) {
                try {
                    driver.close();
                }catch (Exception ex){
                    ex.printStackTrace();
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
    }

    public void processSingle(Map<String, Object> personInfoMap) throws IOException {
        List<Health> questionBankList = healthService.selectList(new EntityWrapper<>());
        this.init();
        login(personInfoMap.get("name").toString(),
                personInfoMap.get("age").toString(),
                personInfoMap.get("sex").toString(),
                personInfoMap.get("education").toString(),
                personInfoMap.get("job").toString(),
                personInfoMap.get("orgName").toString());
        int questionCount = Integer.parseInt(driver.findElementById("__subjectCount").getText());
        List<Integer> wrongItems = new ArrayList<>();
        if((boolean)personInfoMap.get("wrongSet")){
            wrongItems = getRandomNumbers(questionCount,1 + new Random().nextInt(questionCount/3 + 1));
        }
        doTest(questionBankList, questionCount, wrongItems);
        submit(questionCount);
        this.processReport(questionCount, questionBankList, wrongItems);
        ChromeDriveUtils.screenShotLong(driver, appName, personInfoMap.get("name").toString(), personInfoMap.get("index").toString());
        driver.close();

    }

    private Map<String, Object> getPersonInfo(HealthUsers healthUser){
        Map<String, Object> map = new HashMap<>();
        Random random = new Random();
        String age = Arrays.asList("20～25岁以下", "25～30岁以下", "30～35岁以下", "35～40岁以下", "40～45岁以下").get(random.nextInt(5));
        String education = Arrays.asList("小学", "初中", "高中/职高/中专", "大专/本科").get(random.nextInt(2));
        String job = Arrays.asList("教师", "饮食服务", "商业服务", "医务人员", "公司管理").get(random.nextInt(5));
        map.put("name", healthUser.getName());
        map.put("age", StringUtils.isNotBlank(healthUser.getAge()) ? healthUser.getAge() : age);
        map.put("sex", healthUser.getSex());
        map.put("education", StringUtils.isNotBlank(healthUser.getEducation()) ? healthUser.getEducation() :education);
        map.put("job", StringUtils.isNotBlank(healthUser.getJob()) ? healthUser.getJob() :job);
        map.put("orgName", healthUser.getOrgName());
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

    public void processReport(int totalQuestionCount, List<Health> questionBankList, List<Integer> wrongItems){
        List<String> questionTitles = questionBankList.stream().map(Health::getTitle).collect(Collectors.toList());
        for (int questionIndex = 1;questionIndex <= totalQuestionCount; questionIndex++) {
            Map<String, String> questionAndSelection = this.getTitleAndAnswers(questionIndex, true);
            String title = questionAndSelection.get("title");
            String[] correctSelections = this.getCorrectSelectionsForReport(questionIndex);
            questionAndSelection.remove("title");
            List<String> correctSelectionDetails = this.getSelectionsDetails(questionAndSelection, correctSelections, true);
            List<String> selectionDetials = this.getSelectionsDetails(questionAndSelection, correctSelections, false);

            if(isMissed(questionIndex) && !wrongItems.contains(questionIndex)){
                logger.info("错题集:");
                logger.info("title: " + title);
                logger.info("selectionDetials: " + Arrays.toString(selectionDetials.toArray()));
                logger.info("correctSelectionDetails: " + Arrays.toString(correctSelectionDetails.toArray()));
            }

            if(!questionTitles.contains(title)
                    || isMissed(questionIndex) && !wrongItems.contains(questionIndex)) {
                Health health = new Health();
                health.setTitle(title);
                health.setAnswers("[\"" + correctSelectionDetails.stream().collect(Collectors.joining("\", \"")) + "\"]");
                System.out.println(health);
                healthService.insert(health);
            }
        }
    }

    private boolean isMissed(int questionIndex){
        String answers = driver.findElementById("KWait" + questionIndex).getText();
        return answers.contains("标准答案：");
    }

    private String[] getCorrectSelectionsForReport(int questionIndex){
        String answers = driver.findElementById("KWait" + questionIndex).getText();
        if(answers.contains("标准答案：")){
            answers = answers.split("您的答案：")[1].split("标准答案：")[1].trim();
        } else {
            answers = answers.split("您的答案：")[1].trim();
        }
        return answers.split("");
    }

    private List<String> getSelectionsDetails(Map<String, String> questionAndSelection, String[] correctSelections, boolean correctFlag){
        if(correctFlag) {
            return questionAndSelection.entrySet().stream()
                    .filter(e -> Arrays.asList(correctSelections).contains(e.getKey()))
                    .map(e -> e.getValue())
                    .collect(Collectors.toList());
        }else{
            return questionAndSelection.entrySet().stream()
                    .map(e -> e.getValue())
                    .collect(Collectors.toList());
        }
    }

    private Map<String, String> getTitleAndAnswers(int questionIndex, boolean isScoreReport) {
        Map<String, String> title_and_answers = new HashMap<>();
        String this_title_html = driver.findElementByXPath(
                "//li[@id='E" + questionIndex + "'][@style='display: block;']/table[1]/tbody[1]/tr[1]/td[1]/div[@class='ECnt']")
                .getAttribute("innerHTML");
        String[] this_title_and_answers = this_title_html.split("<br>");
        String this_title = this_title_and_answers[1].split("</b>")[1].replace("<b>" + questionIndex + ".</br>", "").trim();
        title_and_answers.put("title", this_title);
        if(this_title == null){
            System.out.println("this_title error");
            System.out.println(this_title_html);
        }
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
            List<String> healthCorrectSelections = health.getSelections();
            if (questionAndSelection.get("title").contains(health.getTitle())) {
                correctSelections = questionAndSelection.entrySet().stream()
                        .filter(e -> healthCorrectSelections.contains(e.getKey()))
                        .map(e -> e.getValue())
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(correctSelections)) {
                    return correctSelections;
                }
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
                    driver.findElementById("k" + selection + questionIndex).click();
                }
                driver.findElementByXPath("//img[@id='Key_Next']").click();
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
