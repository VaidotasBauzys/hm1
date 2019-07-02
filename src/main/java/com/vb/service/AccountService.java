package com.vb.service;

public interface AccountService {

    void adjustMoney(String userId, int amount);

    void transferMoney(String fromUserId, String toUserId, int amount);

}
