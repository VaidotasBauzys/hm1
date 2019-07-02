package com.vb.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.vb.domain.Account;

import java.lang.reflect.Type;

/**
 * Specific serializer needed to handle account balance
 */
public class AccountSerializer implements JsonSerializer<Account> {

    @Override
    public JsonElement serialize(Account src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("balance", src.getBalance());
        return jsonObject;
    }
}
