package com.vb.web;

import com.google.gson.internal.LinkedTreeMap;
import com.vb.AccountTransactions;
import com.vb.HttpHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import spark.Spark;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class UserControllerIntegrationTest {

    @BeforeClass
    public static void beforeClass() throws InterruptedException {
        AccountTransactions.main(new String[]{"4568"});
        Spark.awaitInitialization();
    }

    @AfterClass
    public static void afterClass() {
        Spark.stop();
        Spark.awaitStop();
    }

    @Test
    public void testUserCreationWorks() throws Exception {
        String userId = "1001";
        String firstName = "Arn";
        String lastName = "Beta";
        String body = "{\n" +
                "    \"id\": \"" + userId + "\", \n" +
                "    \"firstName\": \"" + firstName + "\",\n" +
                "    \"lastName\": \"" + lastName + "\"\n" +
                "}";
        HttpHandler.TestResponse userCreateResponse = HttpHandler.request("POST", "/users", body);
        assertEquals(200, userCreateResponse.status);

        // verify
        HttpHandler.TestResponse getResponse = HttpHandler.request("GET", "/users/" + userId, "");
        Map<String, LinkedTreeMap> json = getResponse.json();
        assertEquals(200, getResponse.status);
        assertEquals(userId, json.get("data").get("id"));
        assertEquals(firstName, json.get("data").get("firstName"));
        assertEquals(lastName, json.get("data").get("lastName"));
    }

}