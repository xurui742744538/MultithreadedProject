package com.dongnaoedu.aim;

/**
 * xulinfei
 * 创建日期：2017/12/05
 * 创建时间: 10:42
 * 用sleep模拟实际的业务操作
 */
public class BusiMock {

    public static void buisness(int sleepTime){
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
