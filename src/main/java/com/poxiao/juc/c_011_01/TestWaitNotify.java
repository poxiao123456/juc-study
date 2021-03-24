package com.poxiao.juc.c_011_01;

import java.util.LinkedList;
import java.util.List;

/**
 * @author qq
 * @date 2020/12/30
 */
public class TestWaitNotify<T> {
    private final LinkedList<T> list = new LinkedList<T> ();
    private static final int MAX = 10;
    private int count = 0;

    public synchronized void put(T t) {
        while (list.size() == MAX) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        list.add(t);
        count++;
        this.notifyAll();
    }

    public synchronized T get() {
        T t = null;
        while (list.size() == 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        t = list.removeFirst();
        count--;
        this.notifyAll();
        return t;
    }
    
    public static void main(String[] args){
        TestWaitNotify<String> testWaitNotify = new TestWaitNotify<>();

        //启动生产者线程
        for(int i = 0; i < 2; i++) {
            new Thread(()->
                    {for(int j = 0; j < 10; j++){
                        testWaitNotify.put(Thread.currentThread().getName()+" "+j);
            }},"生产者线程"+i).start();
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
