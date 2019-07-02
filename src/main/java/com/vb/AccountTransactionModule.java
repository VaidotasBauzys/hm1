package com.vb;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.vb.service.AccountService;
import com.vb.service.AccountServiceImpl;
import com.vb.service.UserService;
import com.vb.service.UserServiceImpl;

public class AccountTransactionModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(UserService.class).to(UserServiceImpl.class).in(Singleton.class);

        bind(AccountService.class).to(AccountServiceImpl.class);
    }
}
