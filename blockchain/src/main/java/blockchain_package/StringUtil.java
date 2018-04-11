package blockchain_package;

import java.security.MessageDigest;

public class StringUtil {
    // Applies SHA256 algorithm then returns the result
    public static String applySha256(String input) {
        try {
            // Sets algorithm to SHA256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Applies SHA256 to our input
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            // Makes the hexString mutable
            StringBuffer hexString = new StringBuffer();
            // Converts hash to hexString
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) {hexString.append(0);}
                hexString.append(hex);
            }
            // Converts it to String so it can be returned
            return hexString.toString();
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }
}
