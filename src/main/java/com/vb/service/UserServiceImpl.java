package com.vb.service;

import com.vb.domain.User;

import java.util.HashMap;

public class UserServiceImpl implements UserService {
    private HashMap<String, User> userMap;

    public UserServiceImpl() {

        userMap = new HashMap<>();
    }

    /**
     * Created user stored in memory
     */
    @Override
    public void addUser(User user) {
        userMap.put(user.getId(), user);
    }

    /**
     * User returned from memory if existing
     */
    @Override
    public User getUser(String id) {
        return userMap.get(id);
    }

}
