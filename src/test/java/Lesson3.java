import Models.UserAgentApiResponseModel;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Lesson3 {

    @ParameterizedTest
    @MethodSource("InputStringsForEx10")
    public void testEx10(String input) {
// Ex10: Тест на короткую фразу
// В рамках этой задачи с помощью JUnit необходимо написать тест, который проверяет длину какое-то переменной типа String с помощью любого выбранного Вами метода assert.
// Если текст длиннее 15 символов, то тест должен проходить успешно. Иначе падать с ошибкой.
        int inputLength = input.length();
        Assertions.assertTrue(inputLength > 15, "Input string length is less than 15");
    }

    @Test
    public void testEx11() {
// Необходимо написать тест, который делает запрос на метод: https://playground.learnqa.ru/api/homework_cookie
// Этот метод возвращает какую-то cookie с каким-то значением. Необходимо понять что за cookie и с каким значением, и зафиксировать это поведение с помощью assert.

        String actualCookieValue = RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn()
                .getCookie("HomeWork");

        Assertions.assertEquals("hw_value", actualCookieValue, "HomeWork cookie value is not equal 'hw_value'");
    }

    @Test
    public void testEx12() {
// Необходимо написать тест, который делает запрос на метод: https://playground.learnqa.ru/api/homework_header
// Этот метод возвращает headers с каким-то значением. Необходимо понять что за headers и с каким значением, и зафиксировать это поведение с помощью assert

        String actualHeaderValue = RestAssured
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn()
                .getHeader("x-secret-homework-header");

        Assertions.assertEquals("Some secret value", actualHeaderValue, "Response header value does not equal 'Some secret value'");
    }

    @ParameterizedTest
    @MethodSource("InputDataForEx13")
    public void testEx13(UserAgentApiResponseModel request) {
        UserAgentApiResponseModel response = RestAssured
                .given()
                .header("User-Agent", request.getUser_agent())
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .as(UserAgentApiResponseModel.class);

        assertAll("All properties",
                () -> assertEquals(request.getUser_agent(), response.getUser_agent(), "User agent is not equal"),
                () -> assertEquals(request.getPlatform(), response.getPlatform(), "Platform is not equal"),
                () -> assertEquals(request.getBrowser(), response.getBrowser(), "Browser is not equal"),
                () -> assertEquals(request.getDevice(), response.getDevice(), "Device is not equal"),

                () -> assertEquals(request, response, "Failed User Agent: " + request.getUser_agent()) // just to print failed User Agent
        );
    }

    private static String[] InputStringsForEx10() {
        return new String[]{
                "", // fail
                "123456789012345", // fail
                "asdf sdt23a wr2 2334", // pass
                "12345678901234567890", // pass
                " 123456789012345", // pass
        };
    }

    private static List<UserAgentApiResponseModel> InputDataForEx13() {
        List<UserAgentApiResponseModel> list = new ArrayList<>();
        list.add(new UserAgentApiResponseModel(
                "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
                "Mobile", "No", "Android"));
        list.add(new UserAgentApiResponseModel(
                "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1",
                "Mobile", "Chrome", "iOS"));
        list.add(new UserAgentApiResponseModel(
                "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)",
                "Googlebot", "Unknown", "Unknown"));
        list.add(new UserAgentApiResponseModel(
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0",
                "Web", "Chrome", "No"));
        list.add(new UserAgentApiResponseModel(
                "Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1",
                "Mobile", "No", "iPhone"));
        return list;
    }
}