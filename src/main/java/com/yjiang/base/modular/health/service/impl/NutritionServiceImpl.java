package com.yjiang.base.modular.health.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.yjiang.base.core.util.MailUtils;
import com.yjiang.base.modular.HealthUsers.service.IHealthUsersService;
import com.yjiang.base.modular.system.model.Health;
import com.yjiang.base.modular.system.model.HealthUsers;
import org.openqa.selenium.Alert;
import org.openqa.selenium.support.ui.Select;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service("NutritionServiceImpl")
@EnableScheduling
public class NutritionServiceImpl extends HealthCareServiceImpl {

    @Resource
    private IHealthUsersService healthUsersService;

//    @Scheduled(cron="0 21 8 * * ?")
    public void scheduleProcess(){
        System.out.println("Nutrition automation started");

        Wrapper<HealthUsers>  wrapper = new EntityWrapper<>();
        wrapper.and("`nutrition` = 1").orderBy("id");
        int count = healthUsersService.selectCount(wrapper);
        if(count > 350){
            return;
        }
        int personNum = new Random().nextInt(50) + 100;
        for (int i = 0; i < 5; i++) {
            try {
                process(personNum, true);
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

        System.out.println("Nutrition automation ended");
    }

    @Override
    public String getUrl() {
        return "http://www.jscdc.cn/KABPWeb2011/paperTest1/createPagerForSafety.action";
    }

    @Override
    public Wrapper<HealthUsers> getUsersWrapper(){
        Wrapper<HealthUsers>  wrapper = new EntityWrapper<>();
        wrapper.and("`nutrition` <= 0").orderBy("id");
        return wrapper;
    }

    public String getAppName(){
        return "nutrition";
    }

    public int getPort(){
        return 4444;
    }

    @Override
    public int getCount(HealthUsers healthUser) {
        return healthUser.getNutrition();
    }

    @Override
    public int getPersonCount() {
        return 1;
    }

    @Override
    public void update(HealthUsers healthUser, int i, Map<String, Object> personInfoMap){
        healthUser.setNutrition(i);
    }

    @Override
    public void login(String name, String age, String sex, String edu, String metier, String orgName){
        Select city = new Select(driver.findElementById("city"));
        city.selectByVisibleText("宿迁市");
        Select zone = new Select(driver.findElementById("zone"));
        zone.selectByVisibleText("沭阳县");
        Select village = new Select(driver.findElementById("village"));
        village.selectByVisibleText("东小店乡");
        driver.findElementById("ename").sendKeys(name);
        Select ageGroup = new Select(driver.findElementById("ageGroup"));
        ageGroup.selectByVisibleText(age);
        Select sexGroup = new Select(driver.findElementByName("sex"));
        sexGroup.selectByVisibleText(sex);
        Select educationStatus = new Select(driver.findElementById("educationStatus"));
        educationStatus.selectByVisibleText(edu);
        Select metierGroup = new Select(driver.findElementById("metier"));
        metierGroup.selectByVisibleText(metier);
        driver.findElementById("log_img").click();
    }

    public List<Health> getQuestionBankList(){
        return null;
    }

    public int getQuestionCount(){
        String[] answers = driver.findElementById("subject1Type").getAttribute("value").split(",");
        return answers.length;
    }


    public void doTest(List<Health> questionBankList, int questionCount, List<Integer> wrongItems, Map<String, Object> personInfoMap) {
        String[] answers = driver.findElementById("subject1Type").getAttribute("value").split(",");
        for (int i = 0; i < answers.length; i++) {
            if("null".equals(answers[i])){
                driver.findElementById("A" + i).click();
            }else{
                answerSingleQuestion(answers, i, wrongItems);
            }
        }
        try {
            screenShot(personInfoMap.get("name").toString(), "last");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void answerSingleQuestion(String[] answers, int i, List<Integer> wrongItems){
        if(wrongItems.contains(i+1)){
            if(answers[i].length() > 1) {
                answers[i] = answers[i].substring(0, answers[i].length()-1);
            }else{
                answers[i] = String.valueOf("A".equals(answers[i]) ? (char)(answers[i].charAt(0) + 1) : (char)(answers[i].charAt(0) - 1));
            }
        }
        for (int i1 = 0; i1 < answers[i].length(); i1++) {
            String subAnswer = String.valueOf(answers[i].charAt(i1));
            driver.findElementByXPath("//input[@value='" + subAnswer + i + "']").click();
        }
    }

    public void submit(int totalQuestionCount){
        driver.findElementById("Submit").click();
        Alert confirm = driver.switchTo().alert();
        confirm.accept();
        Alert confirm1 = driver.switchTo().alert();
        confirm1.accept();
    }

    public void processReport(int totalQuestionCount, List<Health> questionBankList, List<Integer> wrongItems){

    }


}
