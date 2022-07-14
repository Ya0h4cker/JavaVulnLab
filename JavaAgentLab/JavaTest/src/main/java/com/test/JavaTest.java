package com.test;

public class JavaTest {
    public static void main(String[] args) throws Exception {
        System.out.println("main start");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int i = 0; i <10; i++ ) {
            test();
            Thread.sleep(3000);
        }
        System.out.println("main end");
    }

    public static void test() {
        System.out.println("Test");
    }
}
