package com.vb.web;

import com.google.gson.Gson;
import com.vb.domain.AdjustAmount;
import com.vb.domain.TransferAmount;
import com.vb.service.AccountService;

import static spark.Spark.post;

public class AccountController {

    public AccountController(final AccountService accountService) {

        post("/account/add", (request, response) -> {
            response.type("application/json");

            AdjustAmount adjustAmount = new Gson().fromJson(request.body(), AdjustAmount.class);
            try {
                accountService.adjustMoney(adjustAmount.getUserId(), adjustAmount.getAmount());
            } catch (IllegalArgumentException e) {
                return new Gson().toJson(
                        new StandardResponse(StatusResponse.ERROR, new Gson()
                                .toJson("Account issue:" + e.getMessage())));

            }

            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS));
        });

        post("/account/transfer", (request, response) -> {
            response.type("application/json");

            TransferAmount transferAmount = new Gson().fromJson(request.body(), TransferAmount.class);
            try {
                accountService.transferMoney(transferAmount.getFromUserId(), transferAmount.getToUserId(), transferAmount.getAmount());
            } catch (RuntimeException e) {
                return new Gson().toJson(
                        new StandardResponse(StatusResponse.ERROR, new Gson()
                                .toJson("Account issue:" + e.getMessage())));

            }
            return new Gson().toJson(
                    new StandardResponse(StatusResponse.SUCCESS));
        });
    }
}
