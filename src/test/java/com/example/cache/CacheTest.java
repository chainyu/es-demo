package com.example.cache;

import com.example.caffeine.service.CacheService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheTest {

    private static CountDownLatch cdl = new CountDownLatch(1);

    private Set<String> values = new HashSet<>();

    @Autowired
    private CacheService cacheService;

    @Test
    public void getDataByCacheable() throws InterruptedException {

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 150; i++) {
            Thread thread = threadTask(i);
            threads.add(thread);
        }
        cdl.countDown();
        System.out.println("::::::start::::::");
        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("value size"+values.size());
        for (String value : values) {
            System.out.println("value="+value);
        }
    }

    private  Thread threadTask(int i){
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    cdl.await();
                    String key = "k2";
                    TimeUnit.SECONDS.sleep(i>=50 ? i>=100 ? i/10+20 : i/10+15 : 1);
                    System.out.println(currentThread().getId()+"/"+key+"/"+System.currentTimeMillis()%1000000L+" start......");

                    Map<String, Object> value = cacheService.findData(key);
                    System.out.println(currentThread().getId()+"/"+key+"/"+System.currentTimeMillis()%1000000L+" find end:::"+value.get(key));
                    values.add(key+"-"+value.get(key));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        thread.setName("name"+i);
        thread.start();
        return thread;
    }
}
