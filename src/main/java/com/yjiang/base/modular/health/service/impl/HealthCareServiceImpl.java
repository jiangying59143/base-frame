package com.yjiang.base.modular.health.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yjiang.base.core.util.ChromeDriveUtils;
import com.yjiang.base.core.util.MailUtils;
import com.yjiang.base.modular.HealthUsers.service.IHealthUsersService;
import com.yjiang.base.modular.health.service.HealthCareService;
import com.yjiang.base.modular.health.service.IHealthScoreService;
import com.yjiang.base.modular.health.service.IHealthService;
import com.yjiang.base.modular.system.model.Health;
import com.yjiang.base.modular.system.model.HealthScore;
import com.yjiang.base.modular.system.model.HealthUsers;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 100次
 * 地域改变 东小店
 * 无头模式 -headless
 */
@EnableScheduling
@Service("HealthCareServiceImpl")
public class HealthCareServiceImpl implements HealthCareService {

    private static Logger logger = LoggerFactory.getLogger(HealthCareServiceImpl.class);

    @Resource
    private IHealthService healthService;

    @Resource
    private IHealthUsersService healthUsersService;

    @Resource
    private IHealthScoreService healthScoreService;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    protected String appName = "health";

//    String diverPath = "C://temp/chromedriver.exe";

    String diverPath = "D:\\root\\driver\\chromedriver91.exe";

    String url = "http://www.jscdc.cn/KABP2011/business/index1.jsp";

//    @Scheduled(cron="0 32 7 * * ?")
    public void scheduleProcess(){
        logger.debug("health care automation started");
        //等待时间,模拟任意时间 7-17
        Random random = new Random();
//        try {
//            Thread.sleep(random.nextInt(3600*10) * 1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        int personNum = random.nextInt(31) + 40;
        personNum=1;
        for (int i = 0; i < 5; i++) {
            try {
//                process(personNum, true);
                break;
            } catch (Exception e) {
                e.printStackTrace();
                MailUtils.sendSimpleMail("907292671@qq.com", "healthCare " + e.getMessage(), getUrl());
                try {
                    Thread.sleep(1800 * 1000);
                } catch (InterruptedException x) {
                    x.printStackTrace();
                }
            }
        }

        logger.debug("health care automation ended");
    }

    public String getAppName(){
        return appName;
    }

    public RemoteWebDriver init() throws Exception {
        RemoteWebDriver driver;
        try {
            System.setProperty("webdriver.chrome.driver", diverPath);
            ChromeOptions options = new ChromeOptions();
//            if (!"local".equals(activeProfile)) {
                options.addArguments("--headless", "--no-sandbox", "--disable-gpu", "--whitelisted-ips");
//            }

            ChromeDriverService.Builder builder = new ChromeDriverService.Builder();
            ChromeDriverService chromeService = builder.usingDriverExecutable(new File(diverPath)).usingPort(getPort()).build();
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
            driver.get(getUrl());
        }catch(Exception e){
            logger.error("", e);
            throw new Exception("webdriver 初始化失败，应该是网站无法访问");
        }
        return driver;
    }

    public int getPort(){
        return 3333;
    }

    public Wrapper<HealthUsers> getUsersWrapper(){
        Wrapper<HealthUsers>  wrapper = new EntityWrapper<>();
        wrapper.and("`count` < 10 || `count` is null").orderBy("id");
        return wrapper;
    }

    @Override
    public void process(int personNum, int score) throws Exception {
        Wrapper<HealthUsers>  wrapper = getUsersWrapper();
        List<HealthUsers> healthUsers = healthUsersService.selectPage(new Page<>(0, personNum), wrapper).getRecords();
        for (HealthUsers healthUser : healthUsers) {
            executorService.submit(()-> {
                try {
                    singlePersonProcess(healthUser, score, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        logger.debug("health care end");

    }

    @Override
    public String getUrl() {
        return url;
    }

    public int getPersonCount(){
        return 10;
    }

    public int getCount(HealthUsers healthUser){
        return healthUser.getCount() == null ? 0 : healthUser.getCount();
    }

    public void singlePersonProcess(HealthUsers healthUser,int score, boolean needUpdate) throws Exception {
        Map<String, Object> personInfoMap = this.getPersonInfo(healthUser);
        personInfoMap.put("score", score);
        int count = getCount(healthUser);
        if(count >= getPersonCount()){
            return;
        }
        for (int i = count+1; i <= getPersonCount(); i++) {
            personInfoMap.put("index", String.valueOf(i));
            int df = atomOperation(personInfoMap);
            if(needUpdate) {
                update(healthUser, i, personInfoMap);
                healthUsersService.updateById(healthUser);
            }
            healthScoreService.insert(new HealthScore(healthUser.getId(), i, df));
            logger.info(healthUser.getName() + "完成第" + i + "遍数, 得分:" + df );
        }
    }

    public void update(HealthUsers healthUser, int i, Map<String, Object> personInfoMap){
        healthUser.setCount(i);
        healthUser.setAge(personInfoMap.get("age").toString());
        healthUser.setEducation(personInfoMap.get("education").toString());
        healthUser.setJob(personInfoMap.get("job").toString());
    }

    private int atomOperation(Map<String, Object> personInfoMap) throws Exception {
        int df=0;
        boolean fineFlag = false;
        while(!fineFlag) {
            RemoteWebDriver driver = this.init();
            try {
                df = processSingle(driver, personInfoMap);
                fineFlag = true;
            } catch (Exception e) {
                if(!"local".equals(activeProfile)) {
                    try {
                        driver.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        fineFlag = false;
                        e.printStackTrace();
                    }
                }else {
                    e.printStackTrace();
                    fineFlag = true;
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return df;
    }

    public int getQuestionCount(RemoteWebDriver driver){
        return Integer.parseInt(driver.findElementById("__subjectCount").getText());
    }

    public int processSingle(RemoteWebDriver driver, Map<String, Object> personInfoMap) throws IOException {
        List<Health> questionBankList = getQuestionBankList();
        login(driver, personInfoMap.get("name").toString(),
                personInfoMap.get("age").toString(),
                personInfoMap.get("sex").toString(),
                personInfoMap.get("education").toString(),
                personInfoMap.get("job").toString(),
                personInfoMap.get("orgName").toString());
        int questionCount = getQuestionCount(driver);
        List<Integer> wrongItems = new ArrayList<>();
        if(personInfoMap.get("score") != null){
            int score = (int)personInfoMap.get("score");
            int randomNum = new Random().nextInt((int)(questionCount * (100-score)/100.00d + 1));
            if(randomNum > 0) {
                wrongItems = getRandomNumbers(questionCount, randomNum);
            }
        }
        doTest(driver, questionBankList, questionCount, wrongItems, personInfoMap);
        submit(driver, questionCount);
        int df = this.processReport(driver, questionCount, questionBankList, wrongItems);
        screenShot(driver,personInfoMap.get("name").toString(), personInfoMap.get("index").toString());
        driver.close();
        return df;
    }

    public void screenShot(RemoteWebDriver driver,String name, String index) throws IOException {
        ChromeDriveUtils.screenShotLong(driver, getAppName(), name, index);
    }

    public List<Health> getQuestionBankList(){
        return healthService.selectList(new EntityWrapper<>());
    }

    public Map<String, Object> getPersonInfo(HealthUsers healthUser){
        Map<String, Object> map = new HashMap<>();
        Random random = new Random();
        String age = Arrays.asList("20～25岁以下", "25～30岁以下", "30～35岁以下", "35～40岁以下", "40～45岁以下").get(random.nextInt(5));
        String education = Arrays.asList("小学", "初中", "高中/职高/中专").get(random.nextInt(3));
        String job = Arrays.asList("工人", "民工", "农民").get(random.nextInt(3));
        map.put("name", healthUser.getName());
        map.put("age", StringUtils.isNotBlank(healthUser.getAge()) ? healthUser.getAge() : age);
        map.put("sex", healthUser.getSex());
        map.put("education", StringUtils.isNotBlank(healthUser.getEducation()) ? healthUser.getEducation() :education);
        map.put("job", StringUtils.isNotBlank(healthUser.getJob()) ? healthUser.getJob() :job);
        map.put("orgName", healthUser.getOrgName() != null ? healthUser.getOrgName() : "");
        return map;
    }


    public void login(RemoteWebDriver driver,String name, String age, String sex, String edu, String metier, String orgName){
        Select city = new Select(driver.findElementById("zone3"));
        city.selectByVisibleText("宿迁市");
//        city.selectByVisibleText("南京市");
        Select zone = new Select(driver.findElementById("zone4"));
        zone.selectByVisibleText("沭阳县");
//        zone.selectByVisibleText("玄武区");
        Select village = new Select(driver.findElementById("zone5"));
        village.selectByVisibleText("东小店乡");
//        village.selectByVisibleText("不详乡镇");
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

    public void doTest(RemoteWebDriver driver,List<Health> questionBankList, int questionCount, List<Integer> wrongItems, Map<String, Object> personInfoMap){
        for (int questionIndex = 1; questionIndex <= questionCount; questionIndex++) {
            Map<String, String> questionAndSelection = this.getTitleAndAnswers(driver, questionIndex, false);
            List<String> correctSelections = this.getCorrectSelections(questionBankList, questionAndSelection);
            this.setAnswersToQuestion(driver, correctSelections, questionIndex, wrongItems);
        }
    }

    public void submit(RemoteWebDriver driver,int totalQuestionCount){
        driver.findElementByXPath("//td[@id='btnAct" + totalQuestionCount + "']/div[1]/input[1]").click();
        Alert confirm = driver.switchTo().alert();
        confirm.accept();
    }

    public int processReport(RemoteWebDriver driver,int totalQuestionCount, List<Health> questionBankList, List<Integer> wrongItems){
        int df = Integer.parseInt(driver.findElementById("df_fs").getText());
        List<String> questionTitles = questionBankList.stream().map(Health::getTitle).collect(Collectors.toList());
        for (int questionIndex = 1;questionIndex <= totalQuestionCount; questionIndex++) {
            Map<String, String> questionAndSelection = this.getTitleAndAnswers(driver, questionIndex, true);
            String title = questionAndSelection.get("title");
            String[] correctSelections = this.getCorrectSelectionsForReport(driver, questionIndex);
            questionAndSelection.remove("title");
            List<String> correctSelectionDetails = this.getSelectionsDetails(questionAndSelection, correctSelections, true);
            List<String> selectionDetials = this.getSelectionsDetails(questionAndSelection, correctSelections, false);

            if(isMissed(driver, questionIndex) && !wrongItems.contains(questionIndex)){
                logger.debug("错题集:");
                logger.debug("title: " + title);
                logger.debug("selectionDetials: " + Arrays.toString(selectionDetials.toArray()));
                logger.debug("correctSelectionDetails: " + Arrays.toString(correctSelectionDetails.toArray()));
            }

            if(!questionTitles.contains(title)
                    || isMissed(driver, questionIndex) && !wrongItems.contains(questionIndex)) {
                Health health = new Health();
                health.setTitle(title);
                health.setAnswers("[\"" + correctSelectionDetails.stream().collect(Collectors.joining("\", \"")) + "\"]");
                logger.debug(health.toString());
                healthService.insert(health);
            }
        }
        logger.debug("-----------------得分--------------------" + df);
        return df;
    }

    private boolean isMissed(RemoteWebDriver driver, int questionIndex){
        String answers = driver.findElementById("KWait" + questionIndex).getText();
        return answers.contains("标准答案：");
    }

    private String[] getCorrectSelectionsForReport(RemoteWebDriver driver, int questionIndex){
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

    public Map<String, String> getTitleAndAnswers(RemoteWebDriver driver, int questionIndex, boolean isScoreReport) {
        Map<String, String> title_and_answers = new HashMap<>();
        String this_title_html = driver.findElementByXPath(
                "//li[@id='E" + questionIndex + "'][@style='display: block;']/table[1]/tbody[1]/tr[1]/td[1]/div[@class='ECnt']")
                .getAttribute("innerHTML");
        String[] this_title_and_answers = this_title_html.split("<br>");
        String this_title = this_title_and_answers[1].split("</b>")[1].replace("<b>" + questionIndex + ".</br>", "").trim();
        title_and_answers.put("title", this_title);
        if(this_title == null){
            logger.debug("this_title error");
            logger.debug(this_title_html);
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

    public void setAnswersToQuestion(RemoteWebDriver driver, List<String> correctSelections, int questionIndex, List<Integer> wrongItems){
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
