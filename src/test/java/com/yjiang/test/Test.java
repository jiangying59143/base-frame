package com.yjiang.test;

public class Test extends Thread {
    //不加volatile
    private static boolean flag = false;

    public void run() {
        while (!flag) {
            try {
                //这个是sleep完之后可能从主存中拿数据
                Thread.sleep(1000);
                //这个方法有锁
                System.out.println(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new Test().start();
        Thread.sleep(2000);
        flag = true;
    }
}
