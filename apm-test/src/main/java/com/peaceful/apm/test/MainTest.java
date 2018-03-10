package com.peaceful.apm.test;

import com.peaceful.apm.test.metric.StopWatch;

import java.util.concurrent.TimeUnit;

/**
 * Created by wangjun38 on 2018/1/27.
 */
public class MainTest {

    public static void main(String[] args) throws InterruptedException {


        new Thread(
                new Runnable() {
                    public void run() {
                        StopWatch watch = APM.createStopWatch();
                        boolean flag = true;
                        while (flag) {
                            watch.start("aaa");
                            try {
                                TimeUnit.MILLISECONDS.sleep((long) (Math.random()*5));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            watch.end();
                        }
                    }
                }
        ).start();

        new Thread(
                new Runnable() {
                    public void run() {
                        StopWatch watch = APM.createStopWatch();
                        boolean flag = true;
                        while (flag) {
                            watch.start("bbb");
                            try {
                                TimeUnit.MILLISECONDS.sleep((long) (Math.random()*5));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            watch.end();
                        }
                    }
                }
        ).start();

    }
}
