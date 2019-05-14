package com.yczuoxin.concurrent.other.redis;

import redis.clients.jedis.Jedis;

public class RedisTest {
    public static void main(String[] args) {
        /*for (int i = 0; i < 13; i++) {
            Thread thread = new Thread(RedisTest::minus,"thread-" + i);
            thread.start();
        }*/
        Thread threadOne = new Thread(RedisTest::minus);
        Thread threadTwo = new Thread(RedisTest::minus);
        Thread threadThree = new Thread(RedisTest::minus);
        Thread threadFour = new Thread(RedisTest::minus);
        Thread threadFive = new Thread(RedisTest::minus);
        Thread threadSix = new Thread(RedisTest::minus);
        Thread threadSeven = new Thread(RedisTest::minus);
        Thread threadEight = new Thread(RedisTest::minus);
        Thread threadNine = new Thread(RedisTest::minus);

        threadOne.start();
        threadTwo.start();
        threadThree.start();
        threadFour.start();
        threadFive.start();
        threadSix.start();
        threadSeven.start();
        threadEight.start();
        threadNine.start();
    }

    private static boolean minus() {
        Jedis jedis = new Jedis("47.106.146.28");
        String redPackage = jedis.lpop("redpackage");
        if (redPackage !=  null) {
            System.out.println(Thread.currentThread().getName() + ": " + redPackage);
            return true;
        }
        System.out.println(Thread.currentThread().getName() + ": " + "没有数据了");
        return false;
    }
}
