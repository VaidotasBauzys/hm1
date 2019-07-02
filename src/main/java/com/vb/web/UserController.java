package com.vb.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.vb.domain.User;
import com.vb.service.UserService;

import static spark.Spark.get;
import static spark.Spark.post;

public class UserController {

    public UserController(final UserService userService) {

        get("/users/:id", (request, response) -> {
            response.type("application/json");

            User user = userService.getUser(request.params(":id"));
            JsonElement userJson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJsonTree(user);
            JsonElement accountJson = new Gson().toJsonTree(user.getAccount().getBalance());
            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, userJson));
        });

        post("/users", (request, response) -> {
            response.type("application/json");

            User user = new Gson().fromJson(request.body(), User.class);
            userService.addUser(user);

            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS));
        });
    }
}
