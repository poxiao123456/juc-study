package com.poxiao.juc.c_000;

/**
 * @author qq
 * @date 2020/12/29
 * 线程之间的关系，父子？守护？互不相干？
 * 结论：线程之间没有任何关系，对于CPU而言，都是进行调度的基本单位，子线程死亡也不会影响父线程，反之同理
 */
public class T05_Thread_isolation {

    public static void main(String[] args){
        //1.Main线程是个非守护线程，不能设置成守护线程。
        System.out.println(" parent thread begin ");
        Thread.currentThread().setDaemon(true);

        //----------------------------

        //2.Main线程结束，其他线程一样可以正常运行。
        System.out.println("parent thread begin ");

        ChildThread t1 = new ChildThread("thread1");
        ChildThread t2 = new ChildThread("thread2");
        t1.start();
        t2.start();

        System.out.println("parent thread over ");

        //--------------------------------

        //Main线程结束，其他线程也可以立刻结束，当且仅当这些子线程都是守护线程。
        System.out.println("parent thread begin ");

        ChildThread t3 = new ChildThread("thread1");
        ChildThread t4 = new ChildThread("thread2");
        t3.setDaemon(true);
        t4.setDaemon(true);

        t3.start();
        t4.start();

        System.out.println("parent thread over ");
    }

}

class ChildThread extends Thread
{
    private String name = null;

    public ChildThread(String name)
    {
        this.name = name;
    }

    @Override
    public void run()
    {
        System.out.println(this.name + "--child thead begin");

        try
        {
            Thread.sleep(500);
        }
        catch (InterruptedException e)
        {
            System.out.println(e);
        }

        System.out.println(this.name + "--child thead over");
    }
}
