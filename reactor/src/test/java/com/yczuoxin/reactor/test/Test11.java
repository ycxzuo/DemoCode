package com.yczuoxin.reactor.test;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class Test11 {

    @Test
    public void testScheduling() {
        Flux.range(0, 10)
                // 元数据流是在 my-elastic-x 线程执行的
                .log()
                // publish 之后数据流是在 my-parallel-x 线程执行
                // publishOn 会影响链中其后的操作符，例如
                .publishOn(Schedulers.newParallel("my-parallel"))
                .log()
                // subscribe 之后数据流是在 my-parallel-x 线程执行
                // subscribeOn 只影响源头的执行环境
                .subscribeOn(Schedulers.newElastic("my-elastic"))
                .log()
                .blockLast();
    }

}
