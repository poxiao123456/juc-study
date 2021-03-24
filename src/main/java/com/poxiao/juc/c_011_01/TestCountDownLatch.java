package com.poxiao.juc.c_011_01;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author qq
 * @date 2020/12/30
 */
public class TestCountDownLatch {
    volatile List lists = new ArrayList();

    public void add(Object o){
        lists.add(o);
    }

    public int size(){
        return lists.size();
    }

    public static void main(String[] args) {
        TestCountDownLatch c = new TestCountDownLatch();

        CountDownLatch latch = new CountDownLatch(1);//1变成0的时候门闩就开了

        new Thread(() -> {
            System.out.println("t2启动");
            if(c.size() != 5){
                try{
                    latch.await();//等待门闩放下，不需要锁定任何对象
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            System.out.println("t2结束");
        },"t2").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            System.out.println("t1启动");
            for(int i = 0;i < 10;i++){
                c.add(new Object());
                System.out.println("add "+i);

                if(c.size() == 5){
                    latch.countDown();//打开门闩，可以继续向下运行，不需要释放锁
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("t1结束");
        },"t1").start();
    }
}
