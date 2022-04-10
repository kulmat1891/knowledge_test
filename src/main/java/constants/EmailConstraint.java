package constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailConstraint {
    public static final boolean emailValidator(String email) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@" +
                "(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+(?:[a-zA-Z]{2}|aero|asia|biz|cat|com|coop|edu|gov|info" +
                "|int|jobs|mil|mobi|museum|name|net|org|pro|tel|travel)$");
        boolean result = pattern.matcher(email).matches();
        return result;
    }
}
