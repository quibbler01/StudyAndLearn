package cn.quibbler.codetop.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrintWithTwoThread {

    static int i = 0;

    public static void main(String[] args) {

        final Lock lock = new ReentrantLock();
        final Condition condition = lock.newCondition();


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(i < 10){
                    try {
                        lock.lock();
                        if (i % 2 == 0) {
                            System.out.println("i " + i++);
                        }
                    } finally {
                        lock.unlock();
                    }
                }
            }
        });


        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while(i < 10){
                    try {
                        lock.lock();
                        if (i % 2 == 1) {
                            System.out.println("j    " + i++);
                        }
                    } finally {
                        lock.unlock();
                    }
                }
            }
        });


        thread.start();
        thread1.start();
    }

}
