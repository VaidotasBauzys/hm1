package com.vb.web;

import com.google.gson.internal.LinkedTreeMap;
import com.vb.AccountTransactions;
import com.vb.HttpHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import spark.Spark;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class AccountControllerIntegrationTest {

    @BeforeClass
    public static void beforeClass() {
        AccountTransactions.main(new String[]{"4567"});
        Spark.awaitInitialization();
    }

    @AfterClass
    public static void afterClass() {
        Spark.stop();
        Spark.awaitStop();
    }

    @Test
    public void testAddingAmountWorks() throws Exception {
        String userId = "1001";
        String firstName = "Arn";
        String lastName = "Beta";
        //create user to use
        HttpHandler.TestResponse postResponse = HttpHandler.request("POST", "/users", getUserRequestBody(userId, firstName, lastName));
        assertEquals(200, postResponse.status);

        double amount = 123;
        //add amount
        postResponse = HttpHandler.request("POST", "/account/add", getAmountAddAmountRequestBody(userId, String.valueOf(amount)));
        assertEquals(200, postResponse.status);

        HttpHandler.TestResponse getResponse = HttpHandler.request("GET", "/users/" + userId, "");
        Map<String, LinkedTreeMap> json = getResponse.json();
        assertEquals(200, getResponse.status);

        // verify
        assertEquals(userId, json.get("data").get("id"));
        assertEquals(amount, ((LinkedTreeMap) json.get("data").get("account")).get("balance"));
    }

    @Test
    public void testAmountTransferWorks() throws Exception {
        String firstUserId = "56";
        String secondUserId = "57";

        //create first user to use
        HttpHandler.TestResponse postResponse = HttpHandler.request("POST", "/users", getUserRequestBody(firstUserId, "name1", "surname1"));
        assertEquals(200, postResponse.status);

        //create second user to use
        postResponse = HttpHandler.request("POST", "/users", getUserRequestBody(secondUserId, "name2", "surname2"));
        assertEquals(200, postResponse.status);

        //add amount to first user
        double amount = 123;
        postResponse = HttpHandler.request("POST", "/account/add", getAmountAddAmountRequestBody(firstUserId, String.valueOf(amount)));
        assertEquals(200, postResponse.status);

        //add amount to second user
        postResponse = HttpHandler.request("POST", "/account/add", getAmountAddAmountRequestBody(secondUserId, String.valueOf(amount)));
        assertEquals(200, postResponse.status);

        String transferAmountRequestBody = "{\n" +
                "    \"fromUserId\": \"" + firstUserId + "\", \n" +
                "    \"toUserId\": \"" + secondUserId + "\", \n" +
                "    \"amount\": \"" + 50 + "\"\n" +
                "}";
        // transfer amount
        postResponse = HttpHandler.request("POST", "/account/transfer", transferAmountRequestBody);
        assertEquals(200, postResponse.status);

        HttpHandler.TestResponse getResponse = HttpHandler.request("GET", "/users/" + firstUserId, "");
        Map<String, LinkedTreeMap> json = getResponse.json();
        assertEquals(200, getResponse.status);

        // verify first user
        assertEquals(firstUserId, json.get("data").get("id"));
        assertEquals(amount - 50, ((LinkedTreeMap) json.get("data").get("account")).get("balance"));

        getResponse = HttpHandler.request("GET", "/users/" + secondUserId, "");
        json = getResponse.json();
        assertEquals(200, getResponse.status);

        // verify second user
        assertEquals(secondUserId, json.get("data").get("id"));
        assertEquals(amount + 50, ((LinkedTreeMap) json.get("data").get("account")).get("balance"));

    }

    private String getUserRequestBody(String userId, String firstName, String lastName) {
        return "{\n" +
                "    \"id\": \"" + userId + "\", \n" +
                "    \"firstName\": \"" + firstName + "\",\n" +
                "    \"lastName\": \"" + lastName + "\"\n" +
                "}";
    }

    private String getAmountAddAmountRequestBody(String id, String amount) {
        return "{\n" +
                "    \"userId\": \"" + id + "\", \n" +
                "    \"amount\": \"" + amount + "\"\n" +
                "}";
    }


}