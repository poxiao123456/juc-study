/**
 * volatile �ؼ��֣�ʹһ�������ڶ���̼߳�ɼ�
 * A B�̶߳��õ�һ��������javaĬ����A�߳��б���һ��copy���������B�߳��޸��˸ñ�������A�߳�δ��֪��
 * ʹ��volatile�ؼ��֣����������̶߳�������������޸�ֵ
 * 
 * ������Ĵ����У�running�Ǵ����ڶ��ڴ��t������
 * ���߳�t1��ʼ���е�ʱ�򣬻��runningֵ���ڴ��ж���t1�̵߳Ĺ������������й�����ֱ��ʹ�����copy��������ÿ�ζ�ȥ
 * ��ȡ���ڴ棬�����������߳��޸�running��ֵ֮��t1�̸߳�֪���������Բ���ֹͣ����
 * 
 * ʹ��volatile������ǿ�������̶߳�ȥ���ڴ��ж�ȡrunning��ֵ
 * 
 * �����Ķ���ƪ���½��и���������
 * http://www.cnblogs.com/nexiyi/p/java_memory_model_and_thread.html
 * 
 * volatile�����ܱ�֤����̹߳�ͬ�޸�running����ʱ�������Ĳ�һ�����⣬Ҳ����˵volatile�������synchronized
 * @author mashibing
 */
package com.poxiao.juc.c_012_Volatile;

import java.util.concurrent.TimeUnit;

/**
 * 我们发现,若不对running变量加以volatile修饰,则对running变量的修改不能终止子线程,说
 * 明在主线程中对running的修改对子线程不可见.
 *
 * 有趣的是,若在while死循环体中加入一些语句或sleep一段时间之后,可见性问题可能会消失,
 * 这是因为加入语句后,CPU就可能会出现空闲,并同步主内存中的内容到工作内存,但这是不确定的,因
 * 此在这种情况下还是尽量要加上volatile。
 */
public class T01_HelloVolatile {
	/*volatile*/ boolean running = true;//// 若无volatile关键字修饰,则变量running难以在每个线程之间共享,对running变量的修改自然不能终止线程

	void m() {
		System.out.println("m start");
		while(running) {
		}
		System.out.println("m end!");
	}
	
	public static void main(String[] args) {
		T01_HelloVolatile t = new T01_HelloVolatile();
		
		new Thread(t::m, "t1").start();
		//new Thread(T::m, "t1").start();

		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		t.running = false;
	}
	
}


