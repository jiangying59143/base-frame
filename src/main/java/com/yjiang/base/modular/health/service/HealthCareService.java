package com.yjiang.base.modular.health.service;

public interface HealthCareService {

    String url = "http://www.jscdc.cn/KABP2011/business/index1.jsp";

    void init() throws Exception;

    void process(int personNum, boolean wrongSet) throws Exception;
}
