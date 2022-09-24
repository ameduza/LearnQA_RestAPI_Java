import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class Lesson5 {

    @Test
    public void testExercise5() {
// В рамках этой задачи нужно создать тест, который будет делать GET-запрос на адрес https://playground.learnqa.ru/api/get_json_homework
// Полученный JSON необходимо распечатать и изучить. Мы увидим, что это данные с сообщениями и временем, когда они были написаны.
// Наша задача вывести текст второго сообщения.

        Response response = RestAssured.given().get("https://playground.learnqa.ru/api/get_json_homework");

        String secondMessage = response.jsonPath().getString("messages[1].message");
        System.out.println("Second message: " + secondMessage);
    }

    @Test
    public void testExercise6(){
// Необходимо написать тест, который создает GET-запрос на адрес: https://playground.learnqa.ru/api/long_redirect
// С этого адреса должен происходит редирект на другой адрес. Наша задача — распечатать адрес, на который редиректит указанные URL.
// Ответом должна быть ссылка на тест в вашем репозитории.

        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        String redirectUrl = response.getHeader("Location");
        System.out.println(redirectUrl);
    }
}
