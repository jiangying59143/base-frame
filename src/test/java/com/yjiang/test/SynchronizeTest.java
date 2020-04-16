package com.yjiang.test;

public class SynchronizeTest implements Runnable{

    @Override
    public synchronized void run() {
        System.out.println(1);
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(2);
    }

    public void test(){
        System.out.println("test");
    }

    public static void main(String[] args) {
        SynchronizeTest synchronizeTest = new SynchronizeTest();
        new Thread(synchronizeTest).start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronizeTest.test();
    }
}
