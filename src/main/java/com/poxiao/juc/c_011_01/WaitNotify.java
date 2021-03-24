package com.poxiao.juc.c_011_01;

/**
 * @author qq
 * @date 2020/12/30
 * 两个线程使用同一容器,使用wait()和notify()进行线程间通信的模式为生产者/消费者模式
 */
public class WaitNotify {

    public static void main(String[] args) {
        SyncStack syncStack = new SyncStack();
        // 生产者和消费者都注入了同一个同步栈,线程间的锁都锁在同步栈syncStack上
        Producer producer = new Producer(syncStack);	// 生产者
        Consumer consumer = new Consumer(syncStack);	// 消费者
        // 创建多个生产者线程和消费者线程
        new Thread(producer, "生产者1").start();	// 生产者线程1
        new Thread(producer, "生产者2").start();	// 生产者线程2
        new Thread(consumer, "消费者1").start();	// 消费者线程1
        new Thread(consumer, "消费者2").start();	// 消费者线程2
    }
}

// 产品类
class Product {
    int id;

    public Product(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Product{id=" + id + '}';
    }
}

// 同步栈,用于在生产者线程和消费者线程之间通信
class SyncStack {
    int index = 0;  // 栈顶元素上一位的下标

    Product[] products = new Product[4];

    // 向栈中送入产品,synchronized方法保证原子性
    public synchronized void push(Product product) {
        // 栈满了,停止生产
        while (index == products.length) {
            // 为防止wait时发生异常后执行程序剩余部分或栈被其它生产者线程生产满,使用while而非if检测栈状态
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 栈不满,通知消费者线程消费
        // 因为notify()方法不能指定唤醒哪个线程,若只唤醒了另一生产者线程,则会发生死锁,因此我们需要把所有线程都唤醒.
        this.notifyAll();
        products[index] = product;
        index++;
    }

    // 从栈中取出产品,synchronized保证原子性
    public synchronized Product pop() {
        // 栈空了,停止消费
        while (index == 0) {
            // 为防止wait时发生异常后执行程序剩余部分或栈被其它消费者线程消费空,使用while而非if检测栈状态
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 栈不空,通知生产者线程生产
        // 因为notify()方法不能指定唤醒哪个线程,若只唤醒了另一消费者线程,则会发生死锁,因此我们需要把所有线程都唤醒.
        this.notifyAll();
        index--;
        return products[index];
    }
}

// 生产者类
class Producer implements Runnable {
    // 生产者工作的栈
    SyncStack syncStack = null;

    public Producer(SyncStack syncStack) {
        this.syncStack = syncStack;
    }

    @Override
    public void run() {
        // 生产10个产品
        for (int i = 0; i < 10; i++) {
            Product product = new Product(i);
            syncStack.push(product);
            System.out.println(Thread.currentThread().getName()+" produce " + product);
            try {
                Thread.sleep((int)(Math.random()*200));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

// 消费者类
class Consumer implements Runnable {
    // 消费者消费的栈
    SyncStack syncStack = null;

    public Consumer(SyncStack syncStack) {
        this.syncStack = syncStack;
    }

    @Override
    public void run() {
        // 消费10个产品
        for (int i = 0; i < 10; i++) {
            Product product = syncStack.pop();
            System.out.println(Thread.currentThread().getName()+" consume " + product);
            try {
                Thread.sleep((int)(Math.random()*1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
