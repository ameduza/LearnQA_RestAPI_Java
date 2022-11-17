package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.DataGenerator;
import lib.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

@Epic("User tests")
@Feature("Register user")
public class UserRegisterTest {
    @Test
    @Description("Should not register user without @ sign")
    public void testCreateUserWithInvalidEmail() {
        // create user with invalid email - without @
        Map<String, String> userData = new HashMap<>();
        userData.put("email", "invalid-email.com");
        userData = DataGenerator.GetUserData(userData);

        Response response = ApiCoreRequests.CreateUser(userData);

        Assertions.AssertStatusCode(response, 400);
        Assertions.AssertResponseBody(response, "Invalid email format");
    }

    @ParameterizedTest
    @MethodSource("InvalidUserData")
    public void testCreateUserWithInvalidData(Map<String, String> userData) {
        Response response = ApiCoreRequests.CreateUser(userData);
        System.out.println(response.prettyPrint());

        Assertions.AssertStatusCode(response, 400);
    }

    //As alternative we can use ValueSource:
    @ParameterizedTest
    @ValueSource(strings = {"username", "firstName", "lastName", "email", "password"})
    public void testCreateUserWithInvalidData(String paramToAvoid) {
        Map<String, String> userDataWithoutField = new HashMap<>();
        userDataWithoutField = DataGenerator.GetUserData();
        userDataWithoutField.remove(paramToAvoid);

        Response response = ApiCoreRequests.CreateUser(userDataWithoutField);
        System.out.println(response.prettyPrint());

        Assertions.AssertStatusCode(response, 400);
    }

    @Test
    public void testCreateUserWithShortName() {
        // user with username with 1 char long
        Map<String, String> userWith1CharName = new HashMap<>();
        userWith1CharName = DataGenerator.GetUserData();
        userWith1CharName.replace("username", "1");

        Response response = ApiCoreRequests.CreateUser(userWith1CharName);
        System.out.println(response.prettyPrint());

        Assertions.AssertStatusCode(response, 400);
    }

    @Test
    public void testCreateUserWithLongName() {
        // 1 user with name longer than 250 characters
        Map<String, String> userWithLongName = new HashMap<>();
        String longName = "A".repeat(256);
        userWithLongName = DataGenerator.GetUserData();
        userWithLongName.replace("username", longName);

        Response response = ApiCoreRequests.CreateUser(userWithLongName);
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
        return users;
    }
}
