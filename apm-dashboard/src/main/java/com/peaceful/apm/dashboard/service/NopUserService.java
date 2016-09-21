package com.peaceful.apm.dashboard.service;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.peaceful.apm.alert.users.User;
import com.peaceful.boot.Application;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Only for test
 * <p>
 * Created by wangjun on 16/9/19.
 */
public class NopUserService extends AbstractScheduledService implements UserService {

    private Map<Long, String> groupMap = new ConcurrentHashMap<>();
    private Map<Long, List<User>> groupUserMap = new ConcurrentHashMap<>();

    public NopUserService(){
        this.startAsync();
    }

    @Override
    public Map<Long, String> selectAllUserGroups() {
        return groupMap;
    }

    @Override
    public List<User> selectUsersByGroupId(List<Long> ids) {
        List<User> users = new ArrayList<>();
        for (Long id : ids) {
            users.addAll(groupUserMap.get(id));
        }
        return users;
    }

    @Override
    protected void runOneIteration() throws Exception {
        Config config = ConfigFactory.load("user-group.conf");
        for (ConfigObject group : config.getObjectList("apm.user.group.map")) {
            Long id = Long.valueOf(group.unwrapped().get("id").toString());
            String name = group.unwrapped().get("name").toString();
            groupMap.put(id, name);

        }

        for (ConfigObject group : config.getObjectList("apm.group.users")) {
            Long id = Long.valueOf(group.unwrapped().get("id").toString());
            String mails = group.unwrapped().get("mails").toString();
            List<User> users = Lists.newArrayList();
            for (String mail : mails.split(",")) {
                User user = new User();
                user.email = mail;
                users.add(user);
            }
            groupUserMap.put(id, users);
        }
    }

    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedDelaySchedule(0, 5, TimeUnit.MINUTES);
    }
}
