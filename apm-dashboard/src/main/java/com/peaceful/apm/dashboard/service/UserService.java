package com.peaceful.apm.dashboard.service;

import com.peaceful.apm.alert.users.User;

import java.util.List;
import java.util.Map;

/**
 * Created by wangjun on 16/9/19.
 */
public interface UserService {


    Map<Long, String> selectAllUserGroups();


    List<User> selectUsersByGroupId(List<Long> ids);
}
