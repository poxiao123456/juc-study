package com.poxiao.juc.c_011_01;

import java.util.concurrent.TimeUnit;

/**
 * @author qq
 * @date 2020/12/30
 * synchronized锁住的是堆中o对象的实例,而不是o对象的引用,因为synchronized是针对堆中o对象的实例上进行计数.
 */
public class T {

    Object o = new Object();

    // 该方法锁住的o对象引用没有被设为final
    void m() {
        synchronized (o) {
            while (true) {
                System.out.println(Thread.currentThread().getName() + "正在运行");
                System.out.println("o:"+o);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        T t = new T();
        new Thread(t::m, "线程1").start();

        // 在这里让程序睡一会儿,保证两个线程得到的o对象不同
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread thread2 = new Thread(t::m, "线程2");

        // 改变锁引用,使得线程2也有机会运行,否则一直都是线程1运行
        t.o = new Object();
        thread2.start();
    }
}
