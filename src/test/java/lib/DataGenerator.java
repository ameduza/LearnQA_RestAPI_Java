package lib;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class DataGenerator {
    public static Map<String, String> GetUserData() {
        String timestamp = new SimpleDateFormat("yyyyMMddHHssSSS").format(new java.util.Date());
        Map<String, String> data = new HashMap<>();

        data.put("username", "defaultUsername");
        data.put("firstName", "defaultFirstName");
        data.put("lastName", "defaultLastName");
        data.put("email", "default" + timestamp + "@example.com");
        data.put("password", "defaultPassword");

        return data;
    }

    public static Map<String, String> GetUserData(Map<String, String> nonDefaultValues) {
        Map<String, String> defaultData = GetUserData();
        Map<String, String> userData = new HashMap<>();

        String[] keys = {"username", "firstName", "lastName", "email", "password"};
        for (String key : keys) {
            if (nonDefaultValues.containsKey(key)) {
                userData.put(key, nonDefaultValues.get(key));
            } else {
                userData.put(key, defaultData.get(key));
            }
        }
        return userData;
    }
}
