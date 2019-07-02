package com.vb.service;

import com.vb.domain.User;
import com.vb.exception.UserException;

import javax.inject.Inject;

public class AccountServiceImpl implements AccountService {

    private final UserService userService;

    @Inject
    AccountServiceImpl(UserService userService) {
        this.userService = userService;
    }

    /**
     * Add or subtract money from account
     */
    @Override
    public void adjustMoney(String userId, int amount) {
        User user = userService.getUser(userId);
        user.getAccount().adjustBy(amount);
    }

    /**
     * Transfers money from one account to another
     */
    @Override
    public void transferMoney(String fromUserId, String toUserId, int amount) {
        User fromUser = userService.getUser(fromUserId);
        User toUser = userService.getUser(toUserId);
        if (fromUser == null || toUser == null) {
            throw new UserException("one of user not found");
        }
        fromUser.getAccount().transferTo(toUser.getAccount(), amount);
    }
}
