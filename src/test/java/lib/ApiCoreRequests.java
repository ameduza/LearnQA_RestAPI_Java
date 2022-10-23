package lib;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.Map;

public class ApiCoreRequests {
    static String userUrl = "https://playground.learnqa.ru/api/user/";

    public static Response CreateUser(Map<String, String> userData){
        return RestAssured
                .given()
                .body(userData)
                .post(userUrl)
                .andReturn();
    }

}
