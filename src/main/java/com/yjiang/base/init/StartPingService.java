package com.yjiang.base.init;

import com.yjiang.base.modular.health.service.HealthCareService;
import com.yjiang.base.modular.health.service.IXieWeiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${test.person.number}")
    private String personNumber;

    @Override
    public void run(String... args) throws Exception {
        healthCareService.process(Integer.parseInt(personNumber), true);
        /*for (int i = 10; i < 20; i++) {
            int finalI = i;
            new Thread(()-> {
                try {
                    healthCareService.singlePersonProcess(new HealthUsers(1, "张三" + finalI,
                            "20～25岁以下", "男", "高中/职高/中专", "工人", "", 0, 0), false, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }*/
    }

}
