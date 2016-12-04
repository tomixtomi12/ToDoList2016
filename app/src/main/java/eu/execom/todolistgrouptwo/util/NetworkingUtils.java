package eu.execom.todolistgrouptwo.util;

import org.springframework.util.LinkedMultiValueMap;

/**
 * Created by Alex on 11/27/16.
 */

public class NetworkingUtils {

    public static LinkedMultiValueMap<String, String> packUserCredentials(String email, String password) {
        final LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.set("grant_type", "password");
        map.set("username", email);
        map.set("password", password);
        return map;
    }
}
