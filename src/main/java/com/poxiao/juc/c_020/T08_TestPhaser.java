package com.poxiao.juc.c_020;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class T08_TestPhaser {
    static Random r = new Random();
    static MarriagePhaser phaser = new MarriagePhaser();

    static void milliSleep(int milli) {
        try {
            TimeUnit.MILLISECONDS.sleep(milli);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        phaser.bulkRegister(5);

        for(int i=0; i<5; i++) {
            final int nameIndex = i;
            new Thread(()->{

                Person p = new Person("person " + nameIndex);
                p.arrive();
                phaser.arriveAndAwaitAdvance();

                p.eat();
                phaser.arriveAndAwaitAdvance();

                p.leave();
                phaser.arriveAndAwaitAdvance();
            }).start();
        }

    }


    static class MarriagePhaser extends Phaser {
        @Override
        protected boolean onAdvance(int phase, int registeredParties) {

            switch (phase) {
                case 0:
                    System.out.println("客人都到了");
                    return false;
                case 1:
                    System.out.println("客人都吃完了");
                    return false;
                case 2:
                    System.out.println("客人都离开了");
                    System.out.println("新郎新娘入洞房了");
                    return true;
                default:
                    return true;
            }
        }
    }


    static class Person {
        String name;

        public Person(String name) {
            this.name = name;
        }

        public void arrive() {
            milliSleep(r.nextInt(1000));
            System.out.printf("%s 到了\n", name);
        }

        public void eat() {
            milliSleep(r.nextInt(1000));
            System.out.printf("%s 吃完了!\n", name);
        }

        public void leave() {
            milliSleep(r.nextInt(1000));
            System.out.printf("%s 离开了\n", name);
        }

    }
}

