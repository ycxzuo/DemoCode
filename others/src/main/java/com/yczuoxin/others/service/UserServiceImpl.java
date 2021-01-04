package com.yczuoxin.others.service;

import com.yczuoxin.others.annotation.TimeLog;
import com.yczuoxin.others.bean.User;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Override
    @TimeLog
    public Integer save(User user) {
        System.out.println(AopContext.currentProxy().getClass().getName());
        System.out.println(user);
        return 0;
    }
}
