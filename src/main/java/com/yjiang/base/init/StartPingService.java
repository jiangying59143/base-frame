package com.yjiang.base.init;

import com.yjiang.base.modular.health.service.HealthCareService;
import com.yjiang.base.modular.health.service.IXieWeiService;
import com.yjiang.base.modular.system.model.HealthUsers;
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
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            new Thread(()-> {
                try {
                    healthCareService.singlePersonProcess(new HealthUsers(1, "张三" + finalI,
                            "20～25岁以下", "男", "高中/职高/中专", "工人", "", 0, 0), false, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

}
