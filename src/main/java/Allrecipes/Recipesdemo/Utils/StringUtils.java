package Allrecipes.Recipesdemo.Utils;

import java.util.regex.Pattern;

/**
 * Utility class for string operations.
 */
public class StringUtils {

    // Define commonly used patterns
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^[a-zA-Z0-9]*$");
    public static boolean isNullOrEmpty(String str) {
        return (str == null || str.trim().isEmpty());
    }
    public static boolean isValidEmail(String email) {
        if (isNullOrEmpty(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    public static boolean isAlphanumeric(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        return ALPHANUMERIC_PATTERN.matcher(str).matches();
    }
    public static String capitalizeFirstLetter(String str) {
        if (isNullOrEmpty(str)) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
    public static String capitalizeEachWord(String str) {
        if (isNullOrEmpty(str)) {
            return str;
        }
        String[] words = str.trim().split("\\s+");
        StringBuilder capitalized = new StringBuilder();
        for (String word : words) {
            capitalized.append(capitalizeFirstLetter(word)).append(" ");
        }
        return capitalized.toString().trim();
    }
    public static String reverse(String str) {
        if (isNullOrEmpty(str)) {
            return str;
        }
        return new StringBuilder(str).reverse().toString();
    }
    public static boolean isOnlyLetters(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        return str.chars().allMatch(Character::isLetter);
    }

    public static String truncate(String str, int length) {
        if (isNullOrEmpty(str) || length < 0) {
            return str;
        }
        if (str.length() <= length) {
            return str;
        }
        return str.substring(0, length) + "...";
    }
    public static String replace(String original, String target, String replacement) {
        if (isNullOrEmpty(original) || isNullOrEmpty(target)) {
            return original;
        }
        return original.replace(target, replacement);
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        if (isNullOrEmpty(str1) || isNullOrEmpty(str2)) {
            return false;
        }
        return str1.equalsIgnoreCase(str2);
    }

    public static String toUpperCase(String str) {
        if (isNullOrEmpty(str)) {
            return str;
        }
        return str.toUpperCase();
    }

    public static String toLowerCase(String str) {
        if (isNullOrEmpty(str)) {
            return str;
        }
        return str.toLowerCase();
    }

}
