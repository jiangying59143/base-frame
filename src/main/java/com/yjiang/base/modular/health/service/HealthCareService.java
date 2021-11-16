package com.yjiang.base.modular.health.service;

import com.yjiang.base.modular.system.model.HealthUsers;
import org.openqa.selenium.remote.RemoteWebDriver;

public interface HealthCareService {

    RemoteWebDriver init() throws Exception;

    void process(int personNum, boolean wrongSet) throws Exception;

    void singlePersonProcess(HealthUsers healthUser, boolean wrongSet, boolean needUpdate) throws Exception;

    String getUrl();
}
