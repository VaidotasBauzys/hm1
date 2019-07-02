package com.vb;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import spark.utils.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.fail;

public class HttpHandler {

    public static TestResponse request(String method, String path, String body) {
        try {
            URL url = new URL("http://localhost:4567" + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoOutput(true);

            if (!"".equals(body)) {
                OutputStream os = connection.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
                osw.write(body);
                osw.flush();
                osw.close();
                os.close();
            }

            connection.connect();
            String responseBody = IOUtils.toString(connection.getInputStream());
            return new TestResponse(connection.getResponseCode(), responseBody);

        } catch (IOException e) {
            e.printStackTrace();
            fail("Sending request failed: " + e.getMessage());
            return null;
        }
    }

    public static class TestResponse {

        public final String body;
        public final int status;

        public TestResponse(int status, String body) {
            this.status = status;
            this.body = body;
        }

        public Map<String, LinkedTreeMap> json() {
            return new Gson().fromJson(body, HashMap.class);
        }
    }
}
