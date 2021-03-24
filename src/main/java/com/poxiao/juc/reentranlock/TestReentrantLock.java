package com.poxiao.juc.reentranlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author qq
 * @date 2020/12/30
 */
public class TestReentrantLock {

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();

        // 线程1一直占用着lock锁
        new Thread(() -> {
            lock.lock();
            try {
                System.out.println("线程1启动");
                TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);  // 线程一直占用锁
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "线程1").start();

        // 线程2抢不到lock锁,若不被中断则一直被阻塞
        Thread t2 = new Thread(() -> {
            try {
                //lock.lockInterruptibly();       // 尝试获取锁,若获取不到锁则一直阻塞
                //lock.tryLock();//直接输出-线程2启动
                lock.tryLock(7,TimeUnit.SECONDS);//输出-线程2阻塞过程中被中断
                System.out.println("线程2启动");
            } catch (Exception e) {
                System.out.println("线程2阻塞过程中被中断");
            } finally {
                if (lock.isLocked()) {
                    try {
                        lock.unlock(); // 没有锁定进行unlock就会抛出IllegalMonitorStateException异常
                    } catch (Exception e) {
                        System.out.println("没有锁定就进行unlock");
                    }
                }
            }
        }, "线程2");
        t2.start();

        // 4秒后中断线程2
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t2.interrupt();//告诉t2别傻等了，抛出异常
    }
}
