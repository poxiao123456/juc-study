/**
 * �����⣺ģ�������˻�
 * ��ҵ��д��������
 * ��ҵ�������������
 * �����в��У�
 *
 * ���ײ���������⣨dirtyRead��
 */

package com.poxiao.juc.c_008;

import java.util.concurrent.TimeUnit;

public class Account {
	String name;
	double balance;
	
	public synchronized void set(String name, double balance) {
		this.name = name;

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		
		this.balance = balance;
	}
	
	public /*synchronized*/ double getBalance(String name) {
		return this.balance;
	}

	public static void main(String[] args) {
		Account a = new Account();
		new Thread(()->a.set("zhangsan", 100.0)).start();
		
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("1:"+a.getBalance("zhangsan"));//1:0.0
		
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("2:"+a.getBalance("zhangsan"));//2:100.0
	}
}
