package com.yjiang.base.init;

import com.yjiang.base.core.util.MailUtils;
import com.yjiang.base.modular.health.service.HealthCareService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class StartPingService implements CommandLineRunner {

    @Resource
    HealthCareService healthCareService;

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 5; i++) {
            try {
                System.out.println("health care with high score automation started");
                healthCareService.process(1, false);
                System.out.println("health care with low score automation started");
                healthCareService.process(1, true);
                break;
            } catch (Exception e) {
                e.printStackTrace();
                MailUtils.sendSimpleMail("907292671@qq.com", "healthCare " + e.getMessage(), healthCareService.url);
                try {
                    Thread.sleep(600 * 1000);
                } catch (InterruptedException x) {
                    x.printStackTrace();
                }
            }
        }
        ;
    }

}
