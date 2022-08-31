package com.yjiang.base.modular.health.service;

import com.yjiang.base.modular.system.model.HealthUsers;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public interface HealthCareService {

    Integer numThreads = Runtime.getRuntime().availableProcessors() * 3;

    ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

    RemoteWebDriver init() throws Exception;

    void process(int personNum, int score, String city, String county, String village, String orgName) throws Exception;

    void singlePersonProcess(HealthUsers healthUser, int score, boolean needUpdate, String city, String county, String village, String orgName) throws Exception;

    String getUrl();
}
