import org.junit.jupiter.api.Assertions;
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


