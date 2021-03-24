/**
 * volatile 引用类型（包括数组）只能保证引用本身的可见性，不能保证内部字段的可见性
 */
package com.poxiao.juc.c_012_Volatile;

import java.util.concurrent.TimeUnit;

/**
 * running加了volatile才能有结果，不加没有结果，说明要明确自己的变量
 */
public class T02_VolatileReference1 {

    volatile boolean running = true;

    static T02_VolatileReference1 T = new T02_VolatileReference1();


    void m() {
        System.out.println("m start");
        while(running) {
			/*
			try {
				TimeUnit.MILLISECONDS.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
        }
        System.out.println("m end!");
    }

    public static void main(String[] args) {
        new Thread(T::m, "t1").start();

        //lambda表达式 new Thread(new Runnable( run() {m()}

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        T.running = false;
    }
}
