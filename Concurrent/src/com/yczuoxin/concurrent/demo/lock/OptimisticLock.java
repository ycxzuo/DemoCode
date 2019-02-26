package com.yczuoxin.concurrent.demo.lock;

/**
 * 乐观锁
 * 乐观锁是相对悲观锁来说的，他认为数据在一般情况下不会造成冲突，所以在访问记录前不会加排它锁，
 * 而在进行记录更新时，才会正式对数据冲突与否进行检测。具体来说，根据 update 返回的行数让用户决定如何去做
 *
 * // 1
 * object entry = query("select * from table where id = #{id}", id)
 *
 * // 2
 * 组装数据
 * entry.setName(name)
 * entry.setAge(age)
 *
 * // 3
 * int count = update("update table set name = #{name} and age = #{age} and version = ${version} + 1
 *      where id = #{id} and version = #{version}", entry)
 *
 * // 4
 * 根据返回的影响的行号可以决定进行的动作，可以进行报错，结束或者重试操作
 *
 * 这个方式有点 CAS 的意思。如果多个线程同时进来修改，只要有一条执行成功，version 就变化了，其他线程就无法修改数据了
 */
public class OptimisticLock {
}
