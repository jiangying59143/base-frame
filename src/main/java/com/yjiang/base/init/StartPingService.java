package com.yjiang.base.init;

import com.yjiang.base.modular.health.service.HealthCareService;
import com.yjiang.base.modular.health.service.IXieWeiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartPingService implements CommandLineRunner {

    @Autowired
    @Qualifier("HealthCareServiceImpl")
    HealthCareService healthCareService;

    @Autowired
    @Qualifier("XieWeiServiceImpl")
    IXieWeiService xieWeiService;

    @Override
    public void run(String... args) {
        try {
            healthCareService.process(156, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
