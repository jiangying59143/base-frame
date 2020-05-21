package com.yjiang.base.modular.health.service;

public interface HealthCareService {
    void init() throws Exception;

    void process(int personNum, boolean wrongSet) throws Exception;
}
