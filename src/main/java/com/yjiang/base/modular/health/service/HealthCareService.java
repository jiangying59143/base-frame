package com.yjiang.base.modular.health.service;

import com.yjiang.base.modular.system.model.HealthUsers;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public interface HealthCareService {

    Integer numThreads = Runtime.getRuntime().availableProcessors() * 3;

    ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

    RemoteWebDriver init() throws Exception;

    void process(int personNum, boolean wrongSet) throws Exception;

    void singlePersonProcess(HealthUsers healthUser, boolean wrongSet, boolean needUpdate) throws Exception;

    String getUrl();
}
