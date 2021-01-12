package com.yczuoxin.others.spring.service;

import com.yczuoxin.others.spring.bean.User;

public interface UserService {

    // 如果使用 int 会导致 aop 报错，可能是由于使用动态代理的时候，基础类型无法接收 null 值导致
    Integer save(User user);

}
