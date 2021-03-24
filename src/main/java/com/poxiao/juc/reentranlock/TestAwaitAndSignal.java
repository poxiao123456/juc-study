package com.poxiao.juc.reentranlock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import	java.util.concurrent.locks.ReentrantLock;

import com.poxiao.juc.c_011_01.TestWaitNotify;

import java.util.LinkedList;

/**
 * @author qq
 * @date 2020/12/30
 */
public class TestAwaitAndSignal<T> {

    private final LinkedList<T> list = new LinkedList<T> ();
    private static final int MAX = 10;
    private int count = 0;

    private Lock lock = new ReentrantLock();
    private Condition producer = lock.newCondition();
    private Condition consumer = lock.newCondition();

    public void put(T t) {
        lock.lock();
        try {
            while (list.size() == MAX) {
                producer.await();
            }

            list.add(t);
            count++;
            consumer.signalAll();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public T get() {
        T t = null;
        lock.lock();
        try {
            while (list.size() == 0) {
                consumer.await();
            }

            t = list.removeFirst();
            count--;
            producer.signalAll();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return t;
    }

    public static void main(String[] args){
        TestAwaitAndSignal<String> testWaitNotify = new TestAwaitAndSignal<>();

        //启动生产者线程
        for(int i = 0; i < 2; i++) {
            new Thread(()->
            {for(int j = 0; j < 10; j++){
                testWaitNotify.put(Thread.currentThread().getName()+" "+j);
            }},"生产者线程"+i).start();
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //启动消费者线程
        for(int i = 0; i < 10; i++) {
            new Thread(()->
            {for(int j = 0; j < 2; j++){
                System.out.println("消费"+testWaitNotify.get());
            }},"消费者线程"+i).start();
        }
    }

}
