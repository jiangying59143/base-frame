package com.yjiang.base.init;

import com.yjiang.base.core.util.MailUtils;
import com.yjiang.base.modular.health.service.HealthCareService;
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
