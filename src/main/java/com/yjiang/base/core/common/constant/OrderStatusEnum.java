package com.yjiang.base.core.common.constant;

public enum OrderStatusEnum {
    UNPAID(0), FAIL(1), PAID(2);

    private int code;
    OrderStatusEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
