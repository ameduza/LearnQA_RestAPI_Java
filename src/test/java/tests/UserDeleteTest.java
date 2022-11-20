package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.Map;

@Epic("User tests")
@Feature("Delete user")
public class UserDeleteTest extends BaseTestCase {

    int userId;
    String userEmail;
    String userPassword;

    @Test
    @Description("Should not be able to delete locked user")
    public void deleteLockedUserTest() {
        // ACT: login as userId=2 and try to delete it
        Map<String, String> authData = ApiCoreRequests.UserLogin("vinkotov@example.com", "1234");
        Response deleteUserResponse= ApiCoreRequests.DeleteUser(
                authData.get("header"),
                authData.get("cookie"),
                userId);

        // ASSERT: assert delete failed
        Assertions.AssertStatusCode(deleteUserResponse, 400);
        // User still exist
        Assertions.AssertStatusCode(ApiCoreRequests.GetUserDetails(2), 200);
    }

    @Test
    @Description("Should delete just created user")
    public void deleteJustCreatedUserTest() {
        // ARRANGE: create user
        Map<String, String> initialUserData = DataGenerator.GetUserData();

        Response initialUserResponse = ApiCoreRequests.CreateUser(initialUserData);
        userEmail = initialUserData.get("email");
        userPassword = initialUserData.get("password");
        userId = getResponseJsonIntValue(initialUserResponse, "id");

        // ACT: login under createdUser and delete it
        Map<String, String> authData = ApiCoreRequests.UserLogin(userEmail, userPassword);
        Response deleteUserResponse = ApiCoreRequests.DeleteUser(
                authData.get("header"),
                authData.get("cookie"),
                userId);

        // ASSERT: assert delete succeed
        Assertions.AssertStatusCode(deleteUserResponse, 200);
        // User not found
        Assertions.AssertResponseBody(ApiCoreRequests.GetUserDetails(userId), "User not found");
    }

    @Test
    @Description("Should not be able to delete user if authorised as another one")
    public void deleteUserUnderOtherUserTest() {
        // ARRANGE: create user
        Map<String, String> initialUserData = DataGenerator.GetUserData();

        Response initialUserResponse = ApiCoreRequests.CreateUser(initialUserData);
        userEmail = initialUserData.get("email");
        userPassword = initialUserData.get("password");
        userId = getResponseJsonIntValue(initialUserResponse, "id");

        // ACT: login under one user, but delete created user
        Map<String, String> authData = ApiCoreRequests.UserLogin("vinkotov@example.com", "1234");
        Response deleteUserResponse = ApiCoreRequests.DeleteUser(
                authData.get("header"),
                authData.get("cookie"),
                userId);

        // ASSERT: delete failed
        Assertions.AssertStatusCode(deleteUserResponse, 400);
        // User still exist
        Assertions.AssertStatusCode(ApiCoreRequests.GetUserDetails(userId), 200);
    }
}
