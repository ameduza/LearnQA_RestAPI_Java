package lib;

import io.restassured.http.Headers;
import io.restassured.response.Response;

import java.util.Map;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseTestCase {
    protected String getCookie(Response response, String name){
        Map<String, String> cookies = response.getCookies();

        assertTrue(cookies.containsKey(name), "Response doesn't have cookie with name " + name);
        return cookies.get(name);
    }

    protected String getHeader(Response response, String name){
        Headers headers = response.getHeaders();

        assertTrue(headers.hasHeaderWithName(name), "Response doesn't have Header with name " + name);
        return headers.getValue(name);
    }

    protected int getUserId(Response response, String key){
        response.then().assertThat().body("$", hasKey(key));

        return response.jsonPath().getInt(key);
    }
}
