package lib;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class ApiCoreRequests {
    static String userUrl = "https://playground.learnqa.ru/api/user/";
    static String loginUrl = "https://playground.learnqa.ru/api/user/login";

    @Step("Login with {email}")
    public static Map<String, String> UserLogin(String email, String password) {
        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("password", password);

        Response response = RestAssured.given().body(userData).post(loginUrl).andReturn();

        String header = response.getHeader("x-csrf-token");
        String cookie = response.getCookie("auth_sid");

        HashMap<String, String> authData = new HashMap<>();
        authData.put("header", header);
        authData.put("cookie", cookie);

        return authData;
    }

    @Step("Create user")
    public static Response CreateUser(Map<String, String> userData) {
        return RestAssured
                .given()
                .body(userData)
                .post(userUrl)
                .andReturn();
    }

    public static Response CreateUser() {
        Map<String, String> userData = DataGenerator.GetUserData();

        return RestAssured
                .given()
                .body(userData)
                .post(userUrl)
                .andReturn();
    }

    @Step("GET user data as NON authenticated user")
    public static Response GetUserDetails(int userId) {
        return RestAssured.given().get(userUrl + userId).andReturn();
    }

    @Step("GET user data as authenticated user")
    public static Response GetUserDetails(String authHeader, String authCookie, int userId) {
        return RestAssured
                .given()
                .header("x-csrf-token", authHeader)
                .cookie("auth_sid", authCookie)
                .get(userUrl + userId)
                .andReturn();
    }

    @Step("Update user data")
    public static Response UpdateUserDetails(String authHeader, String authCookie, int userId, Map<String, String> userUpdatedData) {
        return RestAssured
                .given()
                .header("x-csrf-token", authHeader)
                .cookie("auth_sid", authCookie)
                .body(userUpdatedData)
                .put(userUrl + userId)
                .andReturn();
    }

    @Step("Delete user")
    public static Response DeleteUser(String authHeader, String authCookie, int userId) {
        return RestAssured
                .given()
                .header("x-csrf-token", authHeader)
                .cookie("auth_sid", authCookie)
                .delete(userUrl + userId)
                .andReturn();
    }
}
