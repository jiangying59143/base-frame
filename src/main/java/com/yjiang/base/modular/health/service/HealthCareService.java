package com.yjiang.base.modular.health.service;

public interface HealthCareService {
    String diverPath = "/root/driver/chromedriver";

    void init() throws Exception;

    void process(int personNum, boolean wrongSet) throws Exception;
}
