import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

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

    private static String[] InputStringsForEx10() {
        return new String[]{
                "", // fail
                "123456789012345", // fail
                "asdf sdt23a wr2 2334", // pass
                "12345678901234567890", // pass
                " 123456789012345", // pass
        };
    }
}


