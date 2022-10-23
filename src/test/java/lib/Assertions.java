package lib;

import io.restassured.response.Response;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Assertions {
    public static void AssertJsonByName(Response response, String keyName, int expectedValue){
        response.then().body("$", hasKey(keyName));

        int actualValue = response.jsonPath().getInt(keyName);
        assertTrue(expectedValue == actualValue, "Actual JSON value is not equal to expected one");
    }

    public static void AssertStatusCode(Response response, int expectedCode){
        int actualCode = response.getStatusCode();

        assertEquals(expectedCode, actualCode, "Actual response status code is not equal expected");
    }

    public static void AssertResponseBody(Response response, String expectedBody){
        String actualBody = response.getBody().asString();

        assertEquals(expectedBody, actualBody, "Response body is not equal expected");
    }
}
