package com.poxiao.juc.c_012_Volatile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author qq
 * @date 2020/12/30
 * volatile只能保证可见性,但不能保证原子性. volatile不能解决多个线程同时修改一个变量带来的线程安全问题.
 *
 * 运行该程序,我们发现最终变量t.count并非如我们所预计的那样为100000,而是小于100000
 * (当然,若去掉volatile修饰,最终t.count会更小).这说明volatile并不能保证对变量操作的原子性.
 */
public class TestOnlyVolatile {

    //volatile int count = 0;
    AtomicInteger count = new AtomicInteger(0);

    /*synchronized*/ void m() {
        for (int i = 0; i < 10000; i++) {
            //count++;
            count.incrementAndGet();
            //incrementAndGet()是原子方法,而count++不是原子方法
        }
    }

    /**
     * AtomicInteger count = new AtomicInteger(0);
     *
     * count.incrementAndGet();
     *
     * ps:单个Atomic操作是原子的，但是几句的组合是非原子的
     *
     * if(count.get() < 1000)
     *
     * count.incrementAndGet();
     *
     * 比如这两句之间可能会有别的线程进来改变值，这种情况还是要加锁
     */

    public static void main(String[] args) throws InterruptedException {
        // 创建一个10个线程的容器,其中每个线程都执行m()方法
        TestOnlyVolatile t = new TestOnlyVolatile();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(t::m, "t-" + i));
        }

        // 启动这10个线程并join到主线程,防止主线程先行结束
        for (Thread thread : threads) {
            try {
                thread.start();
                //thread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Thread.sleep(10000);

        System.out.println(t.count);	// 10个线程,每个线程执行10000次,结果应为100000
    }
}
