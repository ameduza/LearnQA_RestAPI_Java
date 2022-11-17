package tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.BaseTestCase;
import lib.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

@Epic("User tests")
@Feature("Get user details")
public class UserGetTest extends BaseTestCase {

    @Test
    @Description("Should return limited user data if non authenticated")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetUserAsNotAuthorised() {
        Response response = ApiCoreRequests.GetUserDetails(2);

        Assertions.AssertResponseHasField(response, "username");
        Assertions.AssertResponseHasNoFields(response, new String[]{"email", "firstName", "lastName"});
    }

    @Test
    @Description("Should return all user data if authenticated")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetUserAsSameAuthUser() {
        Map<String, String> authData = ApiCoreRequests.UserLogin("vinkotov@example.com", "1234");
        Response authorisedUserResponse = ApiCoreRequests.GetUserDetails(authData.get("header"), authData.get("cookie"), 2);

        Assertions.AssertResponseHasFields(authorisedUserResponse, new String[]{"username", "email", "firstName", "lastName"});
    }

    @Test
    @Description("Should return limited user data is authenticated as another user")
    public void testGetUserAsOtherAuthUser() {
        Map<String, String> authData = ApiCoreRequests.UserLogin("vinkotov@example.com", "1234");
        Response unauthorisedUserResponse = ApiCoreRequests.GetUserDetails(authData.get("header"), authData.get("cookie"), 1);

        Assertions.AssertResponseHasField(unauthorisedUserResponse, "username");
        Assertions.AssertResponseHasNoFields(unauthorisedUserResponse, new String[]{"email", "firstName", "lastName"});
    }
}
