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
    public void testExercise6() {
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

    @Test
    public void testExercise7() {
//  Необходимо написать тест, который создает GET-запрос на адрес из предыдущего задания: https://playground.learnqa.ru/api/long_redirect
//  На самом деле этот URL ведет на другой, который мы должны были узнать на предыдущем занятии. Но этот другой URL тоже куда-то редиректит.
//  И так далее. Мы не знаем заранее количество всех редиректов и итоговый адрес.
//
//  Наша задача — написать цикл, которая будет создавать запросы в цикле, каждый раз читая URL для редиректа из нужного заголовка.
//  И так, пока мы не дойдем до ответа с кодом 200.

        Response response;
        int responseCode = 0;
        String url = "https://playground.learnqa.ru/api/long_redirect";

        while (responseCode != 200) {
            response = RestAssured
                    .given()
                    .redirects().follow(false)
                    .get(url)
                    .andReturn();
            responseCode = response.getStatusCode();
            url = response.getHeader("Location");

            System.out.println(responseCode + " " + url);
        }
    }
}
