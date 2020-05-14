package com.yjiang.base.modular.health.service;

public interface HealthCareService {
    void init();

    void process(int personNum, boolean wrongSet);
}
