package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.DataGenerator;
import lib.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserRegisterTest {

    String loginUrl = "https://playground.learnqa.ru/api/user/login";
    String userUrl = "https://playground.learnqa.ru/api/user/user";

//- Создание пользователя без указания одного из полей - с помощью @ParameterizedTest необходимо проверить,
//      что отсутствие любого параметра не дает зарегистрировать пользователя
//- Создание пользователя с очень коротким именем в один символ
//- Создание пользователя с очень длинным именем - длиннее 250 символов


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


}
