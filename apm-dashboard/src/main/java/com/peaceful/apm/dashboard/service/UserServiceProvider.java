package com.peaceful.apm.dashboard.service;

import com.peaceful.boot.Application;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Service;

/**
 * Created by wangjun on 16/9/19.
 */
@Service
public class UserServiceProvider implements FactoryBean{

    @Override
    public UserService getObject() throws Exception {
        String userService = Application.getConfigContext().getString("apm.user.service.impl");
        return (UserService) Class.forName(userService).newInstance();
    }

    @Override
    public Class<?> getObjectType() {
        return UserService.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
