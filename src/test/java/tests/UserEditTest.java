package tests;

import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserEditTest extends BaseTestCase {

    int userId;
    String userEmail;
    String userPassword;
    @BeforeEach
    public void createUser(){
        // ARRANGE: create user
        Map<String, String> initialUserData = DataGenerator.GetUserData();

        Response initialUserResponse = ApiCoreRequests.CreateUser(initialUserData);
        userEmail = initialUserData.get("email");
        userPassword = initialUserData.get("password");
        userId = getUserId(initialUserResponse, "id");
    }

    @Test
    public void editJustCreatedUserTest() {


        // ACT: login under createdUser and update its data
        Map<String, String> updatedUserData = new HashMap<>();
        String firstNameNewValue = "updatedFirstName";
        String lastNameNewValue = "updatedLastName";
        updatedUserData.put("firstName", firstNameNewValue);
        updatedUserData.put("lastName", lastNameNewValue);

        Map<String, String> authData = ApiCoreRequests.UserLogin(userEmail, userPassword);
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
