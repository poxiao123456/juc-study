package com.poxiao.juc.c_011_01;

import java.util.concurrent.TimeUnit;

/**
 * @author qq
 * @date 2020/12/30
 * synchronized方法和非synchronized方法可以同时执行，因为非synchronized方法不需要抢这把锁。
 */
public class T3 {
    // 同步方法
    public synchronized void m1() {
        System.out.println(Thread.currentThread().getName() + " m1 start");
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " m1 end");
    }

    // 非同步方法
    public void m2() {
        System.out.println(Thread.currentThread().getName() + " m2 start");
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " m2 end");
    }

    public static void main(String[] args) {
        T3 t = new T3();
        new Thread(t::m1).start();
        new Thread(t::m2).start();
    }
}
