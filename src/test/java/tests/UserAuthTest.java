package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

public class UserAuthTest extends BaseTestCase {

    String authCookie;
    String authHeader;
    int userId;

    @BeforeEach
    public void getAuthData() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response loginResponse = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        this.authCookie = getCookie(loginResponse, "auth_sid");
        this.authHeader = getHeader(loginResponse, "x-csrf-token");
        this.userId = getResponseJsonIntValue(loginResponse, "user_id");
    }

    @Test
    public void testAuthUser() {
        Response authResponse = RestAssured
                .given()
                .cookie("auth_sid", authCookie)
                .header("x-csrf-token", authHeader)
                .get("https://playground.learnqa.ru/api/user/auth")
                .andReturn();

        int authUserId = getResponseJsonIntValue(authResponse, "user_id");

        Assertions.AssertJsonByName(authResponse, "user_id", authUserId);
    }

    @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"})
    public void testNegativeAuthUser(String condition) {
        RequestSpecification requestSpec = RestAssured.given();
        requestSpec.baseUri("https://playground.learnqa.ru/api/user/auth");

        switch (condition) {
            case "cookie":
                requestSpec.cookie("auth_sid", authCookie);
                break;
            case "headers":
                requestSpec.header("x-csrf-token", authHeader);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + condition);
        }

        Response response = requestSpec.get();

        Assertions.AssertJsonByName(response, "user_id", 0);
    }

}
