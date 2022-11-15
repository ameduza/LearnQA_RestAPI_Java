package tests;

import Models.UserResponseModel;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


import java.util.HashMap;
import java.util.Map;

public class UserEditTest extends BaseTestCase {

    int userId;
    String userEmail;
    String userPassword;
    Map<String, String> authData;
    UserResponseModel User;

    @BeforeEach
    public void createUser() {
        // ARRANGE: create user
        Map<String, String> initialUserData = DataGenerator.GetUserData();

        Response initialUserResponse = ApiCoreRequests.CreateUser(initialUserData);
        userId = getResponseJsonIntValue(initialUserResponse, "id");

        authData = ApiCoreRequests.UserLogin(initialUserData.get("email"), initialUserData.get("password"));

        User = ApiCoreRequests.GetUserDetails(
                        authData.get("header"),
                        authData.get("cookie"),
                        userId)
                .getBody().as(UserResponseModel.class);

        userEmail = initialUserData.get("email");
        userPassword = initialUserData.get("password");
    }

    @Test
    public void editJustCreatedUserTest() {
        // ACT: login under createdUser and update its data
        Map<String, String> updatedUserData = new HashMap<>();
        String firstNameNewValue = "updatedFirstName";
        String lastNameNewValue = "updatedLastName";
        updatedUserData.put("firstName", firstNameNewValue);
        updatedUserData.put("lastName", lastNameNewValue);

        Map<String, String> authData = ApiCoreRequests.UserLogin(User.getEmail(), userPassword);
        ApiCoreRequests.UpdateUserDetails(
                authData.get("header"),
                authData.get("cookie"),
                userId,
                updatedUserData);

        // ASSERT: assert updated user details
        Response updatedUserResponse = ApiCoreRequests.GetUserDetails(
                authData.get("header"),
                authData.get("cookie"),
                userId);

        Assertions.AssertJsonByName(updatedUserResponse, "firstName", firstNameNewValue);
        Assertions.AssertJsonByName(updatedUserResponse, "lastName", lastNameNewValue);
    }

    @Test
    public void editUserAsUnauthorisedTest() {
        // ACT: update user data under not authorized user
        Map<String, String> updatedUserData = new HashMap<>();
        String firstNameNewValue = "updatedFirstName";
        String lastNameNewValue = "updatedLastName";
        updatedUserData.put("firstName", firstNameNewValue);
        updatedUserData.put("lastName", lastNameNewValue);

        Response updateResponse = ApiCoreRequests.UpdateUserDetails(
                "",
                "",
                userId,
                updatedUserData);

        // ASSERT: update failed
        Assertions.AssertStatusCode(updateResponse, 400);
        Assertions.AssertResponseBody(updateResponse, "Auth token not supplied");

        // user data not modified
        UserResponseModel actualUser = ApiCoreRequests.GetUserDetails(
                        authData.get("header"),
                        authData.get("cookie"),
                        userId)
                .getBody()
                .as(UserResponseModel.class);

        Assertions.AssertUserEquals(User, actualUser);
    }

    @Test
    public void editUserAsAnotherAuthorisedTest() {
        // ACT: login under one user, but edit created user data
        Map<String, String> updatedUserData = new HashMap<>();
        updatedUserData.put("firstName", "updatedFirstName");
        updatedUserData.put("lastName", "updatedLastName");

        Map<String, String> user2AuthData = ApiCoreRequests.UserLogin("vinkotov@example.com", "1234");
        Response updateResponse = ApiCoreRequests.UpdateUserDetails(
                user2AuthData.get("header"),
                user2AuthData.get("cookie"),
                User.getId(),
                updatedUserData);

        // ASSERT: update failed
        Assertions.AssertStatusCode(updateResponse, 400);

        // user data not modified
        UserResponseModel actualUser = ApiCoreRequests.GetUserDetails(
                        authData.get("header"),
                        authData.get("cookie"),
                        userId)
                .getBody()
                .as(UserResponseModel.class);

        Assertions.AssertUserEquals(User, actualUser);
    }

    @ParameterizedTest
    @CsvSource({
            "email,user-emailexample.com", // email with no @ sign
            "firstName,1" // firstName very short
    })
    public void editJustCreatedUserWithInvalidFieldDataTest(String fieldName, String fieldInvalidValue) {
        //ACT: login under createdUser, update field value to invalid one
        Map<String, String> updatedUserData = new HashMap<>();
        updatedUserData.put(fieldName, fieldInvalidValue);

        Map<String, String> authData = ApiCoreRequests.UserLogin(userEmail, userPassword);
        Response updateResponse = ApiCoreRequests.UpdateUserDetails(
                authData.get("header"),
                authData.get("cookie"),
                userId,
                updatedUserData);

        // ASSERT: update failed
        Assertions.AssertStatusCode(updateResponse, 400);

        // user data not modified
        UserResponseModel actualUser = ApiCoreRequests.GetUserDetails(
                        authData.get("header"),
                        authData.get("cookie"),
                        userId)
                .getBody()
                .as(UserResponseModel.class);

        Assertions.AssertUserEquals(User, actualUser);
    }
}
