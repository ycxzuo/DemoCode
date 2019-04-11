package com.yczuoxin.concurrent.demo.basis.lock;

/**
 * 悲观锁
 * 悲观锁对数据被外界修改保持保守态度，认为数据很容易就会被其他线程修改，
 * 所以在数据被处理前先对数据进行加锁，并在整个数据处理过程中，使数据处于锁定状态
 * 悲观锁得实现往往依靠数据库提供的锁机制，及在数据库中，在读数据记录操作前给记录加排它锁
 *
 * 如果获取锁失败，则说明数据正在被其他线程修改，当前线程则等待或者抛出异常
 * 如果获取锁成功，则对记录进行操作，然后提交事务后释放排它锁
 *
 * // 1
 * 开启事务
 *
 * // 2
 * object entry = query("select * from table where id = #{id} for update", id)
 *
 * // 3
 * 组装数据
 * entry.setName(name)
 * entry.setAge(age)
 *
 * // 4
 * update(update table set name = #{name}, age = #{age} where id = #{id}, entry)
 *
 * // 5
 * 提交事务
 */
public class PessimisticLock {

}
