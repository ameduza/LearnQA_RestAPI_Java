package lib;

import Models.UserResponseModel;
import io.restassured.response.Response;

import java.util.Objects;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Assertions {
    public static void AssertJsonByName(Response response, String keyName, int expectedValue) {
        Assertions.AssertResponseHasField(response, keyName);

        int actualValue = response.jsonPath().getInt(keyName);
        assertTrue(expectedValue == actualValue, "Actual JSON value is not equal to expected one");
    }

    public static void AssertJsonByName(Response response, String keyName, String expectedValue) {
        Assertions.AssertResponseHasField(response, keyName);

        String actualValue = response.jsonPath().getString(keyName);
        assertTrue(Objects.equals(expectedValue, actualValue), "Actual JSON value is not equal to expected one");
    }
    public static void AssertStatusCode(Response response, int expectedCode) {
        int actualCode = response.getStatusCode();

        assertEquals(expectedCode, actualCode, "Actual response status code is not equal expected");
    }

    public static void AssertResponseBody(Response response, String expectedBody) {
        String actualBody = response.getBody().asString();

        assertEquals(expectedBody, actualBody, "Response body is not equal expected");
    }

    public static void AssertResponseHasField(Response response, String expectedFieldName) {
        response.then().assertThat().body("$", hasKey(expectedFieldName));
    }

    public static void AssertResponseHasFields(Response response, String[] expectedFieldNames) {
        for (String expectedFieldName : expectedFieldNames) {
            Assertions.AssertResponseHasField(response, expectedFieldName);
        }
    }

    public static void AssertResponseHasNoField(Response response, String unexpectedFieldName){
        response.then().assertThat().body("$", not(hasKey(unexpectedFieldName)));
    }

    public static void AssertResponseHasNoFields(Response response, String[] unexpectedFieldNames) {
        for (String unexpectedFieldName : unexpectedFieldNames) {
            Assertions.AssertResponseHasNoField(response, unexpectedFieldName);
        }
    }

    public static void AssertUserEquals(UserResponseModel expectedUser, UserResponseModel actualUser){
        assertEquals(expectedUser, actualUser, "User is not equal");
    }
}
