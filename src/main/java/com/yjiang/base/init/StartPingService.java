package com.yjiang.base.init;

import com.yjiang.base.core.util.MailUtils;
import com.yjiang.base.modular.health.service.HealthCareService;
import com.yjiang.base.modular.system.model.HealthUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartPingService implements CommandLineRunner {

    @Autowired
    @Qualifier("NutritionServiceImpl")
    HealthCareService nutritionService;

    @Autowired
    @Qualifier("HealthCareServiceImpl")
    HealthCareService healthCareService;

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 5; i++) {
            try {
//                nutritionService.singlePersonProcess(new HealthUsers(0, "王冬梅", "25～30岁以下", "女", "大专/本科","医务人员", "", 0, 0), false, false);
//                nutritionService.singlePersonProcess(new HealthUsers(0, "王加徐", "55～60岁以下", "男", "高中/职高/中专","医务人员", "", 0, 0), false, false);
//                nutritionService.singlePersonProcess(new HealthUsers(0, "王树喜", "55～60岁以下", "男", "高中/职高/中专","医务人员", "", 0, 0), false, false);
//                nutritionService.singlePersonProcess(new HealthUsers(0, "曹兴亚", "55～60岁以下", "男", "高中/职高/中专","医务人员", "", 0, 0), false, false);
//                nutritionService.singlePersonProcess(new HealthUsers(0, "王震", "45～50岁以下", "男", "高中/职高/中专","医务人员", "", 0, 0), false, false);
                nutritionService.process(1, true);
                healthCareService.process(1, true);
                break;
            } catch (Exception e) {
                e.printStackTrace();
                MailUtils.sendSimpleMail("907292671@qq.com", "healthCare " + e.getMessage(), healthCareService.getUrl());
                try {
                    Thread.sleep(600 * 1000);
                } catch (InterruptedException x) {
                    x.printStackTrace();
                }
            }
        }
    }

}
