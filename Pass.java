import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pass {

    public boolean isValidPassword(User u) {
        // Password length should be at least 8 characters
        String password=u.pwd;
        if (password.length() < 8) {
            System.out.println("Less than 8 characters");
            return false;
        }
        // Password should contain at least one uppercase letter
        if (!containsUppercaseLetter(password)) {
            System.out.println("Doesn't contain UpperCase Letter");
            return false;
        }
        // Password should contain at least one lowercase letter
        if (!containsLowercaseLetter(password)) {
            System.out.println("Doesn't contain LowerCase Letter");
            return false;
        }

        // Password should contain at least one digit
        if (!containsDigit(password)) {
            System.out.println("Does't contain a Number");
            return false;
        }

        // Password should contain at least one special character (non-alphanumeric)
        if (!containsSpecialCharacter(password)) {
            System.out.println("Doesn't contain Special Character");
            return false;
        }

        // Additional custom checks can be added as needed (e.g., avoiding common patterns)

        return true;
    }

    private static boolean containsUppercaseLetter(String password) {
        return password.matches(".*[A-Z].*");
    }

    private static boolean containsLowercaseLetter(String password) {
        return password.matches(".*[a-z].*");
    }

    private static boolean containsDigit(String password) {
        return password.matches(".*\\d.*");
    }

    private static boolean containsSpecialCharacter(String password) {
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }

}
