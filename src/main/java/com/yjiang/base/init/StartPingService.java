package com.yjiang.base.init;

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
        healthCareService.process(2, false);
    }

}
