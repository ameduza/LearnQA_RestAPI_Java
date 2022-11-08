package tests;

import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.DataGenerator;
import lib.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;

public class UserRegisterTest {

    String loginUrl = "https://playground.learnqa.ru/api/user/login";
    String userUrl = "https://playground.learnqa.ru/api/user/user";

    @Test
    public void testCreateUserWithInvalidEmail() {
        // create user with invalid email - without @
        Map<String, String> userData = new HashMap<>();
        userData.put("email", "invalid-email.com");
        userData = DataGenerator.GetUserData(userData);

        Response response = ApiCoreRequests.CreateUser(userData);

        Assertions.AssertStatusCode(response, 400);
        Assertions.AssertResponseBody(response, "Invalid email format~");
    }

    @ParameterizedTest
    @MethodSource("InvalidUserData")
    public void testCreateUserWithInvalidData(Map<String, String> userData) {
        Response response = ApiCoreRequests.CreateUser(userData);
        System.out.println(response.prettyPrint());

        Assertions.AssertStatusCode(response, 400);

    }

    private static List<Map<String, String>> InvalidUserData() {

        List<Map<String, String>> users = new ArrayList<>();

        // 4 users without 1 required field:
        String[] userFields = new String[]{"username", "firstName", "lastName", "email", "password"};
        for (String field : userFields) {
            Map<String, String> userDataWithoutField = new HashMap<>();
            userDataWithoutField = DataGenerator.GetUserData();
            userDataWithoutField.remove(field);
            users.add(userDataWithoutField);
        }

        // 1 user with username with 1 char long
        Map<String, String> userWith1CharName = new HashMap<>();
        userWith1CharName = DataGenerator.GetUserData();
        userWith1CharName.replace("username", "1");
        users.add(userWith1CharName);

        // 1 user with name longer than 250 characters
        Map<String, String> userWithLongName = new HashMap<>();
        String longName = "A".repeat(256);
        userWithLongName = DataGenerator.GetUserData();
        userWithLongName.replace("username", longName);
        users.add(userWithLongName);

        return users;
    }
}
