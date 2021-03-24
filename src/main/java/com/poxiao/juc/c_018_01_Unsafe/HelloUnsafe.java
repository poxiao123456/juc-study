package com.poxiao.juc.c_018_01_Unsafe;

//import sun.misc.*;

import sun.misc.Unsafe;

//Unsafe可以直接操作内容，不归jvm管理
public class HelloUnsafe {
    static class M {
        int i =0;

        private M() {}
    }

   public static void main(String[] args) throws InstantiationException {
        Unsafe unsafe = Unsafe.getUnsafe();
        M m = (M)unsafe.allocateInstance(M.class);
        m.i = 9;
        System.out.println(m.i);
    }
}


