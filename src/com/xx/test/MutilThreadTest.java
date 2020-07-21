package com.xx.test;

/**
 * Created by xurui on 2019/12/28.
 */
public class MutilThreadTest {

    public static void main(String[] args) {
        class FirstMutilThreat extends Thread {
            @Override
            public void run() {
                System.out.println("first test!!!");
            }
        }

        FirstMutilThreat firstMutilThreat = new FirstMutilThreat();
        firstMutilThreat.start();

        class SecondMutilThreat implements Runnable{

            @Override
            public void run() {
                System.out.println("second test !!!");
            }
        }

        SecondMutilThreat secondMutilThreat = new SecondMutilThreat();
        Thread thread = new Thread(secondMutilThreat);
        thread.start();

    }
}
