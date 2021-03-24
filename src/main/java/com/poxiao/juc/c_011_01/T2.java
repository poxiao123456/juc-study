package com.poxiao.juc.c_011_01;

/**
 * @author qq
 * @date 2020/12/30
 * 不要以字符串常量作为锁定对象: 因为字符串常量池的存在,两个不同的字符串引用可能指向同一字符串对象
 */
public class T2 {

    // 两个字符串常量,作为两同步方法的锁
    String s1 = "Hello";
    String s2 = "Hello";

    // 同步m1方法以s1为锁
    void m1() {
        synchronized (s1) {
            while (true) {
                System.out.println(Thread.currentThread().getName() + ":m1 is running");
            }
        }
    }

    // 同步m2方法以s2为锁
    void m2() {
        synchronized (s2) {
            while (true) {
                System.out.println(Thread.currentThread().getName() + ":m1 is running");
            }
        }
    }

    //程序执行结果如下,我们发现两个字符串常量指向的是同一对象,且有一个线程永远得不到锁. 若我们的程序与某个库使用了同一个字符串对象作为锁,就会出现难以发现的bug.
    public static void main(String[] args) {
        T2 t = new T2();

        // 输出两个锁的哈希码
        System.out.println(t.s1.hashCode());
        System.out.println(t.s2.hashCode());

        new Thread(t::m1, "线程1").start();
        new Thread(t::m2, "线程2").start();
    }
}
