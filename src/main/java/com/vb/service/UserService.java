package com.vb.service;

import com.vb.domain.User;

public interface UserService {
    void addUser(User user);

    User getUser(String id);
}
