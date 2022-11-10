package tests;

import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class UserDeleteTest extends BaseTestCase {

    int userId;
    String userEmail;
    String userPassword;

    @Test
    public void deleteLockedUserTest() {
        // ACT: login as userId=2 and try to delete it
        Map<String, String> authData = ApiCoreRequests.UserLogin("vinkotov@example.com", "1234");
        Response deleteUserResponse= ApiCoreRequests.DeleteUser(
                authData.get("header"),
                authData.get("cookie"),
                userId);

        // ASSERT: assert delete failed
        Assertions.AssertStatusCode(deleteUserResponse, 400);
    }

    @Test
    public void deleteJustCreatedUserTest() {
        // ARRANGE: create user
        Map<String, String> initialUserData = DataGenerator.GetUserData();

        Response initialUserResponse = ApiCoreRequests.CreateUser(initialUserData);
        userEmail = initialUserData.get("email");
        userPassword = initialUserData.get("password");
        userId = getUserId(initialUserResponse, "id");

        // ACT: login under createdUser and delete it
        Map<String, String> authData = ApiCoreRequests.UserLogin(userEmail, userPassword);
        Response deleteUserResponse = ApiCoreRequests.DeleteUser(
                authData.get("header"),
                authData.get("cookie"),
                userId);

        Response getUserResponse = ApiCoreRequests.GetUserDetails(userId);

        // ASSERT: assert delete succeed, user not found
        Assertions.AssertStatusCode(deleteUserResponse, 200);
        Assertions.AssertResponseBody(getUserResponse, "User not found");
    }

    @Test
    public void deleteUserUnderOtherUserTest() {
        // ARRANGE: create user
        Map<String, String> initialUserData = DataGenerator.GetUserData();

        Response initialUserResponse = ApiCoreRequests.CreateUser(initialUserData);
        userEmail = initialUserData.get("email");
        userPassword = initialUserData.get("password");
        userId = getUserId(initialUserResponse, "id");

        // ACT: login under one user, but delete created user
        Map<String, String> authData = ApiCoreRequests.UserLogin("vinkotov@example.com", "1234");
        Response deleteUserResponse = ApiCoreRequests.DeleteUser(
                authData.get("header"),
                authData.get("cookie"),
                userId);

        // ASSERT: update failed
        Assertions.AssertStatusCode(deleteUserResponse, 400);
    }
}
