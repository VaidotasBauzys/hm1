package com.vb;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.vb.service.AccountService;
import com.vb.service.UserService;
import com.vb.web.AccountController;
import com.vb.web.UserController;

import static spark.Service.ignite;
import static spark.Spark.port;

public class AccountTransactions {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new AccountTransactionModule());
        UserService userService = injector.getInstance(UserService.class);
        AccountService accountService = injector.getInstance(AccountService.class);
        injector.injectMembers(accountService);

        // add Rest API controllers
        new AccountController(accountService);
        new UserController(userService);
    }
}
