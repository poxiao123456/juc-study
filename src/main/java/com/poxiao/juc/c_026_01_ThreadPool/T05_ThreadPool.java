/**
 * �̳߳صĸ���
 */
package com.poxiao.juc.c_026_01_ThreadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class T05_ThreadPool {
	public static void main(String[] args) throws InterruptedException {
		ExecutorService service = Executors.newFixedThreadPool(5); //execute submit
		for (int i = 0; i < 6; i++) {
			service.execute(() -> {
				try {
					TimeUnit.MILLISECONDS.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(Thread.currentThread().getName());//java.util.concurrent.ThreadPoolExecutor@723279cf[Running, pool size = 5, active threads = 5, queued tasks = 1, completed tasks = 0]
			});
		}
		System.out.println(service);
		
		service.shutdown();
		System.out.println(service.isTerminated());//false
		System.out.println(service.isShutdown());//true
		System.out.println(service);//java.util.concurrent.ThreadPoolExecutor@723279cf[Shutting down, pool size = 5, active threads = 5, queued tasks = 1, completed tasks = 0]
		
		TimeUnit.SECONDS.sleep(5);
		System.out.println(service.isTerminated());
		System.out.println(service.isShutdown());
		System.out.println(service);
	}
}
