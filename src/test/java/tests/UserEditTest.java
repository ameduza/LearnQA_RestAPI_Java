package tests;

import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserEditTest extends BaseTestCase {

    @Test
    public void editJustCreatedUserTest() {
        // ARRANGE: create user
        Map<String, String> initialUserData = DataGenerator.GetUserData();

        Response initialUserResponse = ApiCoreRequests.CreateUser(initialUserData);
        String email = initialUserData.get("email");
        String initialPassword = initialUserData.get("password");
        int userId = getUserId(initialUserResponse, "id");


        // ACT: login under createdUser and update its data
        Map<String, String> updatedUserData = new HashMap<>();
        String firstNameNewValue = "updatedFirstName";
        String lastNameNewValue = "updatedLastName";
        updatedUserData.put("firstName", firstNameNewValue);
        updatedUserData.put("lastName", lastNameNewValue);

        Map<String, String> authData = ApiCoreRequests.UserLogin(email, initialPassword);
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
    public void editUserAsUnauthorisedTest(){
        // ARRANGE: create user
        Map<String, String> initialUserData = DataGenerator.GetUserData();

        Response initialUserResponse = ApiCoreRequests.CreateUser(initialUserData);
        String email = initialUserData.get("email");
        String initialPassword = initialUserData.get("password");
        int userId = getUserId(initialUserResponse, "id");

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
    }




}
