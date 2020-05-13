package com.yjiang.base.modular.health.service;

import java.io.IOException;

public interface HealthCareService {
    void init();

    void process() throws IOException;
}
