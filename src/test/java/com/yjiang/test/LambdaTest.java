package com.yjiang.test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class LambdaTest {

    @Test
    public void test(){
        List<Policy> list = new ArrayList<>();
        list.add(new Policy("packageCd1","packageName1"));
        list.add(new Policy("packageCd2","packageName2"));
        String packageCd = list.stream().filter( c -> "packageName3".equals(c.getPackageName())).map(Policy::getPackageCd).findFirst().orElse(null);
        System.out.println(packageCd);
    }

    private class Policy{
        private String packageCd;
        private String packageName;

        public Policy(String packageCd, String packageName) {
            this.packageCd = packageCd;
            this.packageName = packageName;
        }

        public String getPackageCd() {
            return packageCd;
        }

        public void setPackageCd(String packageCd) {
            this.packageCd = packageCd;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }
    }

}
