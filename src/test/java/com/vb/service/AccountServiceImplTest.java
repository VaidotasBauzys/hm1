package com.vb.service;

import com.vb.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplTest {

    @Mock
    UserService userService;

    @Test
    public void testAdjustMoneyWorks() {
        AccountService accountService = new AccountServiceImpl(userService);
        String userId = "12588";
        User mockedUser = new User(userId, "name1", "surname1");
        when(userService.getUser(userId)).thenReturn(mockedUser);

        // take action
        int amount=580;
        accountService.adjustMoney(userId, amount);

        //verify
        assertEquals(Integer.valueOf(amount), mockedUser.getAccount().getBalance());
    }

    @Test
    public void transferMoneyWorks() {
        AccountService accountService = new AccountServiceImpl(userService);
        String userId = "12588";
        User mockedUser = new User(userId, "name1", "surname1");
        when(userService.getUser(userId)).thenReturn(mockedUser);
        accountService.adjustMoney(userId, 100);

        String userId2 = "13589";
        User mockedUser2 = new User(userId2, "name2", "surname2");
        when(userService.getUser(userId2)).thenReturn(mockedUser2);
        accountService.adjustMoney(userId2, 100);

        // take action
        accountService.transferMoney(userId, userId2, 50);

        //verify
        assertEquals(Integer.valueOf(50), mockedUser.getAccount().getBalance());
        assertEquals(Integer.valueOf(150), mockedUser2.getAccount().getBalance());
    }

    @Test(expected = IllegalArgumentException.class)
    public void transferMoneyFailsDueNotEnoughMoney() {
        AccountService accountService = new AccountServiceImpl(userService);
        String userId = "12588";
        User mockedUser = new User(userId, "name1", "surname1");
        when(userService.getUser(userId)).thenReturn(mockedUser);
        accountService.adjustMoney(userId, 100);

        String userId2 = "13589";
        User mockedUser2 = new User(userId2, "name2", "surname2");
        when(userService.getUser(userId2)).thenReturn(mockedUser2);
        accountService.adjustMoney(userId2, 100);

        // take action
        accountService.transferMoney(userId, userId2, 150);
    }
}