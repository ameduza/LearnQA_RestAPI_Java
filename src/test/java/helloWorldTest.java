import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.io.Console;

public class helloWorldTest {

    @Test
    public void testHelloFromAlex() {
        System.out.println("Hello from Alex");
    }

    @Test
    public void testGetRequest() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/get_text")
                .andReturn();

        System.out.println(response.asString());
    }

}